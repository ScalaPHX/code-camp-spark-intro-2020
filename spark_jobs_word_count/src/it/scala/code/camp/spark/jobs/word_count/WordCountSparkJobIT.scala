/*
 * Copyright 2020 acme.com
 */

package code.camp.spark.jobs.word_count

import org.scalatest.{BeforeAndAfter, FunSuite}

class WordCountSparkJobIT  extends FunSuite with BeforeAndAfter {

  test("test") {
      // from https://www.gutenberg.org/files/98/98-0.txt
      val input = "/Users/mansari/development/git/Scala.CodeCamp.Spark.Demo/data/books/a_tale_of_two_cities.txt"
      val output = "/Users/mansari/development/git/Scala.CodeCamp.Spark.Demo/data/output/tables/a_tale_of_two_cities_word_counts.parquet"
      val numOfPartitions = "8"
      val debug = "true"

      val args = Array(
        "--input",
        input,
        "--output",
        output,
        "--num-of-partitions",
        numOfPartitions,
        "--debug",
        debug
      )

      WordCountSparkJob.main(args)
  }

  test("test explore insights") {
    val output = "/Users/mansari/development/git/Scala.CodeCamp.Spark.Demo/data/output/tables/a_tale_of_two_cities_word_counts.parquet"
    val spark = WordCountSparkJob.getSparkSession("unittst")
    val df = spark.read.parquet(output)
    df.createOrReplaceTempView("word_count_tbl")

    val topWordCountsDf = df.sqlContext.sql(
      """
        |select * from word_count_tbl order by counts desc limit 25
        |""".stripMargin)
    topWordCountsDf.show(truncate = false)

    val c = topWordCountsDf.collect()(0).getString(0)
    print(c)
  }
}
