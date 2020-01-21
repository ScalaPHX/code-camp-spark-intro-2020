/*
 * Copyright 2020 acme.com
 */


package code.camp.spark.jobs.word_count

import code.camp.spark.commons.SparkCommonOperations
import org.apache.spark.rdd.RDD
import org.scalatest.{BeforeAndAfter, FunSuite}

class WordCountSparkJobTest extends FunSuite with BeforeAndAfter with SparkCommonOperations {

  test ("test") {
    val spark = getSparkSession("unittest")
    val rdd:RDD[String] = spark.sparkContext.parallelize(
      Seq(
        "hello world",
        "it's a small world"
      )
    )

    val wordCountsDf = WordCountSparkJob.computeWordCounts(
      spark,
      rdd,
      debug = true
    )
    wordCountsDf.printSchema()
    wordCountsDf.show(truncate = false)

    assert(3 == wordCountsDf.count())
    assert(1 == wordCountsDf.filter("word = 'hello' ").select("counts").collect()(0).getInt(0))
    assert(1 == wordCountsDf.filter("word = 'small' ").select("counts").collect()(0).getInt(0))
    assert(2 == wordCountsDf.filter("word = 'world' ").select("counts").collect()(0).getInt(0))
  }
}
