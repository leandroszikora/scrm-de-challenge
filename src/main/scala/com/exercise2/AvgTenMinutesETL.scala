package com.exercise2

import org.apache.spark.sql.functions.{avg, col, date_trunc, desc, from_unixtime, minute, unix_timestamp}
import org.apache.spark.sql.types.{StringType, StructType, TimestampType}
import org.apache.spark.sql.{DataFrame, SparkSession}

class AvgTenMinutesETL(val spark: SparkSession) {

  val eventsSchema: StructType = new StructType()
    .add("time", TimestampType)
    .add("action", StringType)

  /**
   * Reads CSV file in the path given as parameter
   * @param path csv file path
   * @return spark dataframe with the content of csv file
   */
  def readEventsCsvFile(path: String): DataFrame = {
    spark.read
      .schema(eventsSchema)
      .option("header", "true")
      .csv(path)
  }

  /**
   * Transforms dataframe given as parameter adding a new column and grouping by it to obtain count of actions
   * @param df spark dataframe with columns time_minute and action
   * @return spark dataframe with three columns: time_minute, Open, Close
   */
  def transformAggDfMinute(df: DataFrame): DataFrame = {
    df.withColumn("time_minute", date_trunc("minute", col("time")))
      .groupBy("time_minute", "action").count()
      .groupBy("time_minute").pivot("action").sum("count")
  }

  /**
   * Transforms dataframe given as parameter adding a column to identify the ten minutes id
   * @param df spark dataframe with colum time_minute
   * @return spark dataframe wit column ten_minutes_id as integer
   */
  def transformAddTenMinutesId(df: DataFrame): DataFrame = {
    df.withColumn("minutes_to_ten", minute(col("time_minute")))
      .withColumn("minutes_to_ten", col("minutes_to_ten") % 10)
      .withColumn("ten_minutes_id", unix_timestamp(col("time_minute")))
      .withColumn("ten_minutes_id", col("ten_minutes_id") - (col("minutes_to_ten") * 60))
  }

  /**
   * Transforms dataframe given as parameter grouping by ten_minutes_id column and taking the average count of each action
   * @param df spark dataframe with columns: ten_minutes_id, Open, Close
   * @return spark dataframe with columns time_ten_minutes, avg_close_action, avg_open_action
   */
  def transformAggDfTenMinutes(df: DataFrame): DataFrame = {
    df.groupBy("ten_minutes_id")
      .agg(
        avg("Close").as("avg_close_action"),
        avg("Open").as("avg_open_action")
      )
      .select(
        from_unixtime(col("ten_minutes_id")).as("time_ten_minutes"),
        col("avg_close_action"),
        col("avg_open_action")
      )
  }

  /**
   * Sorts the spark dataframe by avg_open_action
   * @param df spark dataframe with column avg_open_action
   * @return spark dataframe ordered by avg_open_action desc
   */
  def transformOrderByOpenAction(df: DataFrame): DataFrame = {
    df.orderBy(desc("avg_open_action"))
  }

  /**
   * Saves the results into two different paths /events_ten_minutes_aggregated and /exercise_two_result
   * @param path output path to save the results
   * @param df spark dataframe to save
   */
  def saveEventsCsvFile(path: String, df: DataFrame): Unit = {
    df.coalesce(1)
      .write
      .mode("overwrite")
      .option("header", "true")
      .csv(path + "events_ten_minutes_aggregated")

    df.limit(10)
      .coalesce(1)
      .write
      .mode("overwrite")
      .option("header", "true")
      .csv(path + "exercise_two_result")
  }

  /**
   * Contains the ETL execution logic divided into steps
   * @param inputPath path used to read seed file
   * @param outputPath path used to save the results
   */
  def execute(inputPath: String, outputPath: String): Unit = {
    // Step 1: read csv file into Spark DataFrame
    val dfSeed: DataFrame = readEventsCsvFile(inputPath)

    // Step 2: aggregate df to one minute and count each type of events
    val dfOneMinuteAgg: DataFrame = transformAggDfMinute(dfSeed)

    // Step 3: add ten_minute_id as unix timestamp to group by later
    val dfWithTenMinuteId: DataFrame = transformAddTenMinutesId(dfOneMinuteAgg)

    // Step 4: aggregate df to ten minutes and take the average of each type of events
    val dfTenMinutesAgg: DataFrame = transformAggDfTenMinutes(dfWithTenMinuteId)

    // Step 5: order df by open events average desc
    val dfOrderedByOpenEvents: DataFrame = transformOrderByOpenAction(dfTenMinutesAgg)

    // Step 6: save the results
    saveEventsCsvFile(outputPath, dfOrderedByOpenEvents)
  }

}
