package core.refmap.mongodriver

/**
  * Created by hoki on 7/16/2017.
  */
import org.mongodb.scala._

import scala.collection.JavaConverters._
import core.refmap.mongodriver.Helpers._
import core.refmap.mongodriver.RefMapImpl.mongoClient
// import org.mongodb.scala.model.Filters._
import org.mongodb.scala.bson.BsonValue
import dao._
import core.refmap.CaseClassMap._
import org.json4s._
import org.json4s.jackson.JsonMethods._
//import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.{read, write}
import java.text.SimpleDateFormat

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

trait RefMapImpl {

  implicit val formats = org.json4s.DefaultFormats



    /*
  def getResultSet(k: String)
          (implicit tag:TypeTag[R], tag2:ClassTag[R]) : Seq[R] = {

    val tableName = getTableName[K](k)
    val keyJson = write(k)

    val resultsJson = getResultSet(keyJson, tableName)
//    val.

    //    (r.map(fromMap[R](_).asInstanceOf[R]))
    r.map(row => fromMap[R](row).asInstanceOf[R])
  }

  def getValue[K<:Product,R<:Product](k:K)
           (implicit tag:TypeTag[R], tag2:ClassTag[R]) : Option[R] = {

    val query = createSQL[K,R](k)

    println("Query: " + query)
    val r = DB autoCommit  { implicit session =>
      SQL(query)                // don't worry, prevents SQL injection
        .map(rs => rs.toMap())  // extracts values from rich java.sql.ResultSet
        .first().apply()
    }

    r.map(row => fromMap[R](row).asInstanceOf[R])
  }
*/

  val database: MongoDatabase;
  def getResultSet(k:String, tableName:String) : Seq[String] = {

    val filter: Document = Document(k)
    println("filter :" + filter.toJson())

    val collection: MongoCollection[Document] = database.getCollection(tableName)
    val docs:Seq[Document] = collection.find(filter).results()
    docs.map(doc=>doc.toJson())
  }

  def getValue(k:String, tableName:String) : String= {

    val filter: Document = Document(k)
    println("filter :" + filter.toJson())

    val collection: MongoCollection[Document] = database.getCollection(tableName)
    val f:SingleObservable[Document] = collection.find(filter).first()
    f.headResult().toJson()
  }

}


object RefMapImpl extends RefMapImpl {

  val url = "mongodb://localhost:27017"
  val mongoClient: MongoClient = MongoClient(url)

  val database: MongoDatabase = mongoClient.getDatabase("mydb");
}