/*
 * Copyright 2020 acme.com
 */

package code.camp.spark.jobs.word_count

import code.camp.spark.commons.{SparkCommonOperations, StringUdfs}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.apache.spark.sql.functions._
import org.slf4j.{Logger, LoggerFactory}

trait WordCountOOperations {

  def runSparkJob(spark:SparkSession,
                  input:String,
                  output: Option[String],
                  numPartitions:Int,
                  debug:Boolean=false): Unit ={
    val rdd:RDD[String] = spark.sparkContext.textFile(input).repartition(numPartitions)
    val wordCountDf = computeWordCounts(spark, rdd, debug=debug)
    if (output.isDefined) {
      wordCountDf.write.mode(SaveMode.Overwrite).parquet(output.get)
    }
  }

  def computeWordCounts(spark:SparkSession, input:RDD[String], debug:Boolean=false): DataFrame = {
    import spark.implicits._

    // scrub the text
    val bookTextDf = input.toDF("text")
      .withColumn("text", lower(col("text")))
      .withColumn("text", StringUdfs.retainAlphaNumUdf()(col("text")))

    if (debug) {
      bookTextDf.printSchema()
      bookTextDf.show(truncate = false)
    }

    // convert to rdd and use flatMap to count words
    val rdd = bookTextDf.rdd.map(_.getString(0))
    val words = rdd
      .flatMap(line => line.split(" "))

    // Transform into word and count.
    val counts = words
      .filter(!_.equals("")) // lets remove empty space
      .map(word => (word, 1))
      .reduceByKey{case (x, y) => x + y}

    // transform to DataFrame
    val wordCountDf = counts.toDF("word", "counts")
    if (debug) {
      wordCountDf.printSchema()
      wordCountDf.show(truncate = false)
    }

    // filter out stop words using DataFrames
    val stopWordsFile = this.getClass.getResource("/stop_words.txt").getFile // from classpath
    val stopWordsDf = spark.read.csv(stopWordsFile).withColumnRenamed("_c0", "word")
    if (debug) {
      stopWordsDf.printSchema()
      stopWordsDf.show(truncate = false)
    }
    val wordCountFinalDf = wordCountDf.join(broadcast(stopWordsDf), Seq("word"), "left_anti")
    if (debug) {
      wordCountFinalDf.printSchema()
      wordCountFinalDf.show(truncate = false)
    }

    wordCountFinalDf

  }
}

object WordCountSparkJob extends App with SparkCommonOperations with WordCountOOperations{

  val LOGGER: Logger = LoggerFactory.getLogger(this.getClass)
  val jobName = this.getClass.getName

  val cliArgs = new CliArgs(args)
  val input = cliArgs.input()
  val output = cliArgs.output.toOption
  val numOfPartitions = cliArgs.numOfPartitions()
  val debug = cliArgs.debug.getOrElse("false").toBoolean

  val spark = getSparkSession("wordCountApp")

  runSparkJob(
    spark,
    input,
    output,
    numOfPartitions,
    debug
  )
}
