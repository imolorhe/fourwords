package fourwords

object Main extends App{

  Console println "***** FOURWORDS *****"

//  println(WordsGenerator.getWords)

  val mapper = new Mapper()

  println(mapper.searchWords(("man", "boy", "girl", "woman")))
  println(mapper.searchPoint((0, 93)))
}
