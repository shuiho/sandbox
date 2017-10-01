package core.refmap.scalikejdbc

import java.text.SimpleDateFormat
import scalikejdbc._
import dao._
import core.refmap.CaseClassMap._

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

/**
  * Created by hoki on 6/23/2017.
  */
class RefMapImpl {

  def createSQL[K<:Product, R<:Product](k:K)
    (implicit tag:TypeTag[R], tag2:ClassTag[R]):String = {

    var selectClause: String =""
    var whereClause: String =""
    val keyClassName = k.getClass.getSimpleName
    val tableName = key_table_pairs.m(keyClassName)

    val values = typeOf[R].members.collect {
      case m: MethodSymbol
        if m.isCaseAccessor => m.name
    }

    for (x<-values) {
      if (selectClause.length > 0) {
        selectClause = selectClause + " ,"
      }

      selectClause = selectClause + x.toString()
    }

    val keys:Map[String, Any] = toMap(k)
    if (keys.isEmpty == false) {
      keys.foreach {
        case (k, v) => {
          if (whereClause.length > 0) {
            whereClause = whereClause + " AND "
          } else {
            whereClause = " WHERE "
          }

          whereClause = whereClause + {
            v match {
              case x: String => s" $k = '$x'"
              case x: Long => s" $k = $x"
              case x: Int => s" $k = $x"
              case x: java.sql.Date => s" $k = '$x.toString().substring(0.9)'"
            }
          }
        }
      }
    }

    "SELECT " + selectClause + " FROM " + tableName + " " + whereClause
  }


  def getResultSet[K<:Product,R<:Product](k:K)
     (implicit tag:TypeTag[R], tag2:ClassTag[R]) : Seq[R] = {

    val keys:Map[String, Any] = toMap(k)
    val query = createSQL[K,R](k)

    println("Query: " + query)
    val r = DB autoCommit  { implicit session =>
      SQL(query)                // don't worry, prevents SQL injection
        .map(rs => rs.toMap())  // extracts values from rich java.sql.ResultSet
        .list.apply()
    }

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
}

object RefMapImpl extends RefMapImpl {
  val url = "jdbc:sqlite:C:/DEV/test.db"
  val driver = "org.sqlite.JDBC"
  // initialize JDBC driver & connection pool
  Class.forName(driver)
  ConnectionPool.singleton(url, null, null)
}
