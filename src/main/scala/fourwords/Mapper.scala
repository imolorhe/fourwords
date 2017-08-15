package fourwords

import scala.collection.mutable

/**
  * Maps the provided area and unit area to generated fourwords
  *
  * @param x
  * @param y
  * @param w
  * @param h
  */
class Mapper(x: Int = 0, y: Int = 0, w: Int = 100, h: Int = 100, uw: Int = 5, uh: Int = 5) {
  val num_rows: Int = h / uh
  val num_cols: Int = w / uw

  var map: mutable.HashMap[(String, String, String, String), (Int, Int)] = new mutable.HashMap[(String, String, String, String), (Int, Int)]()

  // i specifies the current row (y-axis)
  for(i <- 0 until num_rows){
    // j specifies the current column (x-axis)
    for(j <- 0 until num_cols){
      var curEntry = (WordsGenerator.getWordX, (uw * j, uh * i))
      map += curEntry
    }
  }

  println(s"Total mappings required: ${num_rows * num_cols}")
  println(s"Total mappings provided: ${map.size}")
  println(s"Total mappings possible: ${WordsGenerator.maxWordX}")

  def searchWordX(wordX: (String, String, String, String)): Option[((String, String, String, String), (Int, Int))] = map.find(_._1 == wordX)

  def searchPoint(point: (Int, Int)): Option[((String, String, String, String), (Int, Int))] = {
    // Subtract the modulo to get the point which was used in mapping
    // Round down to the nearest mapped point
    val curPoint = (point._1 - point._1 % uw, point._2 - point._2 % uh)
    map.find(_._2 == curPoint)
  }

  def humanFriendly(mapItem: Option[((String, String, String, String), (Int, Int))]): String = {
    mapItem match {
      case Some(s) => s._1.productIterator.mkString(".")
      case None => ""
    }
  }
}
