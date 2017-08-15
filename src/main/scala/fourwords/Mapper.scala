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
      var curEntry = (WordsGenerator.getWords, (uw * j, uh * i))
      map += curEntry
    }
  }

  println("Total mappings required: ", num_rows * num_cols)
  println("Total mappings provided: ", map.size)

  def searchWords(words: (String, String, String, String)) = map.find(_._1 == words)

  def searchPoint(point: (Int, Int)) = {
//    Subtract the modulo to get the point which was used in mapping
    val curPoint = (point._1 - point._1 % uw, point._2 - point._2 % uh)
    map.find(_._2 == curPoint)
  }

//  println(map)
}
