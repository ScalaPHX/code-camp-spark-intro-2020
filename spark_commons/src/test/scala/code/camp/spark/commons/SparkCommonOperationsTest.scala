/*
 * Copyright 2020 acme.com
 */


package code.camp.spark.commons

import org.scalatest.{BeforeAndAfter, FunSuite}


class SparkCommonOperationsTest extends FunSuite with BeforeAndAfter {

  test("test getSparkSession") {

    object UnitTestSparkJob extends SparkCommonOperations {}

    val spark = UnitTestSparkJob.getSparkSession("unittest")
    assert(spark != null)
    println("Done!")
  }

}
