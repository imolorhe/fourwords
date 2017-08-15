package fourwords

object WordsGenerator {
  val defaultMapWords = Array(
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

  var mapWords: Array[String] = defaultMapWords.clone

  val wordsFromFile: Array[String] = io.Source.fromInputStream(getClass.getClassLoader.getResourceAsStream("words.txt")).getLines.toArray

  // If there are words from the file, then use those instead
  if(!wordsFromFile.isEmpty){
    mapWords = wordsFromFile.clone
  }

//  Contains the matched wordX pairs
  var matchedWords: Set[(String, String, String, String)] = Set()

  def getWordX: (String, String, String, String) = {
    val wordX = (mapWords(getRandomIndex), mapWords(getRandomIndex), mapWords(getRandomIndex), mapWords(getRandomIndex))

    if (!matchedWords.contains(wordX)) {
      matchedWords += wordX
      wordX
    } else {
      getWordX
    }
  }

  /**
    * Returns a random word index
    * @return
    */
  private def getRandomIndex: Int = (Math.random() * mapWords.length).toInt

  def maxWordX: Long = factorial(mapWords.length - 4)

  private def factorial(n: Int): Long = {
    var _n = n
    var result: Long = 1
    while(_n > 0) {
      result *= _n
      _n = _n - 1
    }
    result
  }

}
