package fourwords

object WordsGenerator {
  val mapWords = Array(
    "boy",
    "girl",
    "man",
    "woman",
    "male",
    "female",
    "twin",
    "dog",
    "cat"
  )
  var matchedWords = Set()

//  println(mapWords.length)

//  mapWords.foreach(println(_))
//println(getRandomIndex)
  def getWords: (String, String, String, String) = {
    (mapWords(getRandomIndex), mapWords(getRandomIndex), mapWords(getRandomIndex), mapWords(getRandomIndex))
  }

  def getRandomIndex = (Math.random() * mapWords.length).toInt
}
