package fourwords

object Main extends App{

  Console println "***** FOURWORDS *****"

  val mapper = new Mapper()

  println(mapper.humanFriendly(mapper.searchWordX(("man", "boy", "girl", "woman"))))
  println(mapper.humanFriendly(mapper.searchPoint((43, 93))))
}
