/*
 * Copyright 2020 acme.com
 */

package code.camp.spark.commons

import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.udf


object StringUdfs {

  def replaceReservedCharUdf(): UserDefinedFunction = {
    val urlCleaner = (s: String) => {
      if (s == null){
        null
      }  else {
        s.replace("\t", " ").replace("\n", "").replace("\r", "")
      }
    }
    val udfFunc = udf(urlCleaner)
    udfFunc
  }

  def retainAlphaNumUdf(): UserDefinedFunction = {
    val urlCleaner = (s: String) => {
      if (s == null){
        null
      }  else {
        s.replaceAll("[^A-Za-z0-9 ]", "")
      }
    }
    val udfFunc = udf(urlCleaner)
    udfFunc
  }
}
