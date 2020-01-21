/*
 * Copyright 2020 acme.com
 */

package code.camp.spark.commons

import org.apache.spark.sql.SparkSession

trait SparkCommonOperations {

  def getSparkSession(appName: String = "Spark",
                      configure: Option[(SparkSession.Builder) => SparkSession.Builder] = None): SparkSession = {

    var sparkSessionBuilder = SparkSession
      .builder
      .master("local[8]")
      .config("spark.driver.allowMultipleContexts", "true")
      .appName(appName)

    if (configure.isDefined) {
      sparkSessionBuilder = configure.get(sparkSessionBuilder)
    }

    sparkSessionBuilder.getOrCreate()
  }
}
