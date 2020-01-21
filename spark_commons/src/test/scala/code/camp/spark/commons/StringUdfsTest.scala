/*
 * Copyright 2020 acme.com
 */


package code.camp.spark.commons

import org.apache.spark.sql.functions._
import org.scalatest.{BeforeAndAfter, FunSuite}

class StringUdfsTest extends FunSuite with BeforeAndAfter with SparkCommonOperations {

  test("test udf retainAlphaNumUdf") {
    val spark = getSparkSession("unittest")
    import spark.implicits._

    val df = Seq(("John", "!!!red\t")).toDF("name", "color")
    df.printSchema()
    df.show(10)

    val udfFun = StringUdfs.retainAlphaNumUdf()
    val df2 = df.withColumn("color", udfFun(col("color")))
    val color = df2.collect()(0).getAs[String]("color")
    assert("red" == color)
  }

}
