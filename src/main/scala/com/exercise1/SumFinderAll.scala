package com.exercise1

import SumFinder.getPositionsToResult

object SumFinderAll extends App {
  val sumResult: Int = args.toList(0).toInt
  val listValues: Array[Int] = args.toList(1).split(",").map(x => x.toInt)

  val results: Array[(Int, Int)] = getPositionsToResult(sumResult, listValues)

  for (result <- results) {
    println(s"The positions of the given array that sum $sumResult are (${result._1}, ${result._2})")
  }
}
