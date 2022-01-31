package com.exercise2

import org.apache.log4j._
import org.apache.spark.sql.SparkSession


object AvgTenMinutesJob extends App {
  val inputPath: String = args.toList(0)
  val outputPath: String = args.toList(1)

  Logger.getLogger("org").setLevel(Level.ERROR)

  val spark: SparkSession = SparkSession.builder
    .appName("AvgTenMinutesJob")
    .master("local[*]")
    .getOrCreate()

  spark.conf.set("spark.sql.session.timeZone", "UTC") // This is used to keep an standard timezone, also used in events timestamp

  val avgTenMinutesETL = new AvgTenMinutesETL(spark)
  avgTenMinutesETL.execute(inputPath, outputPath)

  spark.stop()
}
