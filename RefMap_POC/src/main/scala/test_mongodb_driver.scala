import scala.collection.JavaConverters._
import org.mongodb.scala._
import core.refmap.mongodriver.Helpers._
import org.json4s._
import org.json4s.jackson.JsonMethods._
import java.text.SimpleDateFormat

import dao.BankManager._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.bson._
import org.mongodb.scala.bson.collection.mutable.Document
import core.refmap.mongodriver._
import org.json4s.jackson.Serialization.write



object test_mongodb_driver extends App {

  implicit val formats = org.json4s.DefaultFormats
  // Use a Connection String
  val mongoClient: MongoClient = MongoClient("mongodb://localhost:27017")

  mongoClient.listDatabaseNames().printResults("database name list: ")

  val database: MongoDatabase = mongoClient.getDatabase("mydb");
  val collection: MongoCollection[Document] = database.getCollection("bankmanager")
  val f: SingleObservable[Document] = collection.find().first()
  f.printHeadResult("First bankmanager records: ")

  val all: Observable[Document] = collection.find()
  all.printResults("All bankmanager records: ")

  println("All bankmanager records (own - json): ")
  val filterMap = Map("snapshot_date" -> BsonString("2017-06-30"), "name" -> BsonString("Dave"))
  val filter = Document(filterMap)
  //  val docs:Seq[Document] = collection.find(and (equal("snapshot_date", "2017-06-30"), equal("name", "Dave"))).results()
  val docs: Seq[Document] = collection.find(filter).results()
  docs.foreach(doc => println(doc.toJson()))

  println("List bank manager case classes")
  val mgrs = docs.foreach(doc => println(parse(doc.toJson()).extract[BankManagerValue]))

  // Insert a new record
  val bankManager = write(BankManagerValue("2017-09-10",889,"write1","668","123 Bay"))
  val doc = Document(bankManager)
  collection.insertOne(doc).results()
  println("BankManagerQueryByID(889) - inserted")

//  val filterMap2 = Map("id" -> BsonInt32(889))
  val filterMap2 = Map("transit" -> BsonString("668"))
  val filter2 = Document(filterMap2)

  val f2: SingleObservable[Document] = collection.find(filter2).first()
  val doc2 = f2.headResult()
  println("BankManagerQueryByID(889) - retrieve using driver :" + doc2.toString())
//  docs2.foreach(doc => println(doc.toJson()))

  mongoClient.close()

  // with core.refmap.mongodriver
  val queryByIDJson = write(BankManagerQueryByID(889))
  val bmResult1 = RefMapImpl.getValue(queryByIDJson, "bankmanager")
  println("BankManagerQueryByID(889)) :" + bmResult1)
}
