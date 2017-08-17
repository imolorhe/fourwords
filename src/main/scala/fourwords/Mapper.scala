package fourwords

import scala.collection.mutable

/**
  * Maps the provided area and unit area to generated fourwords
  * @param x the origin x
  * @param y the origin y
  * @param w the width of the entire region
  * @param h the height of the entire region
  * @param uw the unit width of a mapping
  * @param uh the unit height of a mapping
  */
class Mapper(x: Double = 0, y: Double = 0, w: Double = 100, h: Double = 100, uw: Double = 5, uh: Double = 5) {
  val num_rows: Long = (h / uh).round
  val num_cols: Long = (w / uw).round

  // Stores the mappings
  var map: mutable.HashMap[(String, String, String, String), (Double, Double)] = new mutable.HashMap[(String, String, String, String), (Double, Double)]()

  // i specifies the current row (y-axis)
  for(i <- 0 until num_rows.toInt){
    // j specifies the current column (x-axis)
    for(j <- 0 until num_cols.toInt){
      var curEntry = (WordsGenerator.getWordX, (x + (uw * j), y + (uh * i)))
      map += curEntry
    }
  }

  println(s"Total mappings required: ${num_rows * num_cols}")
  println(s"Total mappings provided: ${map.size}")
  println(s"Total mappings possible: ${WordsGenerator.maxWordX}")

  def searchWordX(wordX: (String, String, String, String)): Option[((String, String, String, String), (Double, Double))] = map.find(_._1 == wordX)

  def searchPoint(point: (Double, Double)): Option[((String, String, String, String), (Double, Double))] = {
    // Subtract the modulo to get the point which was used in mapping
    // Round down to the nearest mapped point
    val curPoint = (point._1 - point._1 % uw, point._2 - point._2 % uh)
    map.find(_._2 == curPoint)
  }

  def humanFriendly(mapItem: Option[((String, String, String, String), (Double, Double))]): String = {
    mapItem match {
      case Some(s) => s._1.productIterator.mkString(".")
      case None => ""
    }
  }

  def getMappingsDbSequence: Seq[(Int, Float, Float, String)] = map.map(v => {
    (0, v._2._1.toFloat, v._2._2.toFloat, humanFriendly(Option(v)))
  }).toSeq

  def getMappings: mutable.HashMap[(String, String, String, String), (Double, Double)] = map
}
