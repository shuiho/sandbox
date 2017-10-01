name := "RefMap_POC"

version := "3.2"

// scalaVersion := "2.12.1"
scalaVersion := "2.11.6"

val scalatraVersion = "2.3.0"
// val scalatraVersion = "2.5.1"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-unchecked",
  "-feature",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-Ywarn-dead-code",
  "-Xlint",
  "-Xfatal-warnings"
)

resolvers ++= Seq(
  "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/")

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick"           % "3.2.0",
  "com.h2database"      % "h2"              % "1.4.185",
  "ch.qos.logback"    %  "logback-classic"   % "1.1.3",
  "org.scalatra" %% "scalatra" % scalatraVersion,
  "org.scalatra" %% "scalatra-json" % scalatraVersion,
  "org.scalatra" %% "scalatra-commands" % scalatraVersion,
  "org.json4s"   %% "json4s-jackson" % "3.2.9",
  "org.eclipse.jetty" %  "jetty-webapp"      % "9.2.10.v20150310",
  "com.typesafe.akka" %% "akka-actor" % "2.3.4",
  "org.scalikejdbc" %% "scalikejdbc"       % "3.0.0"
)

libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "2.1.0"

libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.8.10.1"

initialCommands in console := """
  |import slick.jdbc.H2Profile.api._
  |import Example._
  |import scala.concurrent.duration._
  |import scala.concurrent.Await
  |import scala.concurrent.ExecutionContext.Implicits.global
  |val db = Database.forConfig("chapter01")
  |def exec[T](program: DBIO[T]): T = Await.result(db.run(program), 2 seconds)
  |exec(messages.schema.create)
  |exec(messages ++= freshTestData)
""".trim.stripMargin


