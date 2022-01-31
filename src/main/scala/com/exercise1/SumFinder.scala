package com.exercise1

object SumFinder extends App {
  // Here I could use a argument parser to run the script like this:
  // scala main.scala.com.SumFinder -n 24 -l 1,2,33,23,25,-1,22
  val sumResult: Int = args.toList(0).toInt
  val listValues: Array[Int] = args.toList(1).split(",").map(x => x.toInt)

  def getPositionsToResult(resultToGet: Int, values: Array[Int]): Array[(Int, Int)] = {
    val valuesWithIx: Array[(Int, Int)] = values.map(x => (values.indexOf(x), x)) // [(0, 1), (1, 2), (2, 33), (3, 23), ...]
    valuesWithIx.map(x => (x._1, x._2, valuesWithIx.map(y => (y._1, x._2 + y._2)))) // [(0, 1, [(0, 2), (1, 3), (2, 34), (3, 24)]), (1, 2, [...]), ...]
      .map(x => (x._1, x._2, x._3.filter(y => y._2 == resultToGet & y._1 != x._1))) // [(0, 1, [(3, 24)]), (1, 2, [...]), ...]
      .filter(x => !x._3.isEmpty) // [(0, 1, [(3, 24)]), ...]
      .map(x => (x._1, x._3.toList.head._1)) // [(0, 3), ...]
  }

  val result: (Int, Int) = getPositionsToResult(sumResult, listValues).toList.head
  println(s"The positions of the given array that sum $sumResult are (${result._1}, ${result._2})")
}

