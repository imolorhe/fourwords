package fourwords

import com.typesafe.config._
import slick.jdbc.MySQLProfile.api._
import slick.jdbc.meta.MTable
import slick.lifted.Tag

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class Configs(tag: Tag) extends Table[(Int, String, String)](tag, "configs") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def configKey = column[String]("config_key")
  def configValue = column[String]("config_value")

  def * = (id, configKey, configValue)
}

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
  val configs = TableQuery[Configs]
  val setup = DBIO.seq(
    (configs.schema ++ mappings.schema).create
  )

  val dropMappings = DBIO.seq(
    mappings.schema.drop
  )
  val dropConfigs = DBIO.seq(
    configs.schema.drop
  )

  val db = Database.forConfig("db")

  // Check if the mappings table already exists, and drop it before running setup to create it
  val existingMappingTables = Await.result(db.run(MTable.getTables("mappings")), Duration.Inf).toList
  if(existingMappingTables.nonEmpty) Await.result(db.run(dropMappings), Duration.Inf)

  // Check if the configs table already exists, and drop it before running setup to create it
  val existingConfigTables = Await.result(db.run(MTable.getTables("configs")), Duration.Inf).toList
  if(existingConfigTables.nonEmpty) Await.result(db.run(dropConfigs), Duration.Inf)

  Await.result(db.run(setup), Duration.Inf)

  // Set the mapping config
//  val mappingConfig = Map[String, Double](
//    "origin_x" -> 0,
//    "origin_y" -> 0,
//    "region_width" -> 100.0,
//    "region_height" -> 100,
//    "unit_width" -> 5,
//    "unit_height" -> 5
//  )

  // Setting parameters based on Lagos' location
  val startX = 2.71199
  val endX = 4.34997
  val startY = -6.70552
  val endY = -6.37569
  val unitWidth = 0.001
  val unitHeight = 0.001
  val mappingConfig = Map[String, Double](
    "origin_x" -> startX,
    "origin_y" -> startY,
    "region_width" -> (endX - startX).abs,
    "region_height" -> (endY - startY).abs,
    "unit_width" -> unitWidth,
    "unit_height" -> unitHeight
  )
  val mapper = new Mapper(
    x = mappingConfig("origin_x"),
    y = mappingConfig("origin_y"),
    w = mappingConfig("region_width"),
    h = mappingConfig("region_height"),
    uw = mappingConfig("unit_width"),
    uh = mappingConfig("unit_height")
  )

  println("Store configs:")
  val storeConfigs = DBIO.seq(
    configs ++= mappingConfig.toSeq.map(v => (0, v._1, v._2.toString))
  )
  Await.result(db.run(storeConfigs), Duration.Inf)
  println("Configs stored.")

  println("Store mappings:")
  val storeMappings = DBIO.seq(
    mappings ++= mapper.getMappingsDbSequence
  )
  Await.result(db.run(storeMappings), Duration.Inf)
  println("Mappings have been stored")

  println(mapper.humanFriendly(mapper.searchWordX(("man", "boy", "girl", "woman"))))
  println(mapper.humanFriendly(mapper.searchPoint((43, 93))))
}
