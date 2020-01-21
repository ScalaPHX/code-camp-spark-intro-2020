/*
 * Copyright 2020 acme.com
 */

package code.camp.spark.jobs.word_count

import org.rogach.scallop.{ScallopConf, ScallopOption}

class CliArgs(arguments: Seq[String]) extends ScallopConf(arguments) {

  val input: ScallopOption[String] = opt[String](required = true)
  val output: ScallopOption[String] = opt[String](required = false)
  val debug: ScallopOption[String] = opt[String](required = false)
  val numOfPartitions: ScallopOption[Int] = opt[Int](required = false)

  verify()
}
