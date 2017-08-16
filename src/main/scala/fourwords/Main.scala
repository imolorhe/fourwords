package fourwords

import com.typesafe.config._
import slick.jdbc.MySQLProfile.api._
import slick.jdbc.meta.MTable
import slick.lifted.Tag

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class Mappings(tag: Tag) extends Table[(Int, Float, Float, String)](tag, "mappings") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def pointX = column[Float]("point_x")
  def pointY = column[Float]("point_y")
  def wordMap = column[String]("word_map")

  def * = (id, pointX, pointY, wordMap)
}

object Main extends App{
  // Load the configuration
  val conf = ConfigFactory.load()
  Console println "***** FOURWORDS *****"

  val mappings = TableQuery[Mappings]
  val setup = DBIO.seq(
    mappings.schema.create
  )

  val drop = DBIO.seq(
    mappings.schema.drop
  )

  val db = Database.forConfig("db")

  // Check if the mappings table already exists, and drop it before running setup to create it
  val existingTables = Await.result(db.run(MTable.getTables("mappings")), Duration.Inf).toList
  if(existingTables.nonEmpty) Await.result(db.run(drop), Duration.Inf)

  Await.result(db.run(setup), Duration.Inf)

  val mapper = new Mapper()

  println("Store mappings:")
  val storeMappings = DBIO.seq(
    mappings ++= mapper.getMappingsDbSequence
  )
  Await.result(db.run(storeMappings), Duration.Inf)
  println("Mappings have been stored")

  println(mapper.humanFriendly(mapper.searchWordX(("man", "boy", "girl", "woman"))))
  println(mapper.humanFriendly(mapper.searchPoint((43, 93))))
}
