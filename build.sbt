name := "fourwords"

version := "0.1"

scalaVersion := "2.12.3"

val databaseDependency = "mysql" % "mysql-connector-java" % "6.0.6"

libraryDependencies += "com.typesafe.slick" %% "slick" % "3.2.1"
libraryDependencies += "com.typesafe" % "config" % "1.3.1"
libraryDependencies += databaseDependency

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.1",
  "org.slf4j" % "slf4j-nop" % "1.6.4"
)