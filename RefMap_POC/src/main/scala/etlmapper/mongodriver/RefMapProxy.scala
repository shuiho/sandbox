package etlmapper.mongodriver

import dao._
import core.refmap.mongodriver._
import dao.BankManager.BankManagerValue

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization.{read, write}

/**
  * Created by hoki on 6/20/2017.
  */
object RefMapProxy {

  implicit val formats = org.json4s.DefaultFormats

  def getTableName[K<:AnyRef](k:K):String = {
    val keyClassName = k.getClass.getSimpleName
    key_table_pairs.m(keyClassName)
  }

  def getValue[K<:AnyRef, R<:AnyRef](k:K, default:R)
        (implicit tag:TypeTag[R], tag2:ClassTag[R]):R = {
    // val map = new MessageRefMapImpl

    val tableName = getTableName[K](k)
    val keyJson = write(k)
    val valJson = RefMapImpl.getValue(keyJson, tableName)

    val valJsonObj = parse(valJson)
    valJsonObj.extract[R]
  }

  def getResultSet[K<:AnyRef,R<:AnyRef](k:K)
     (implicit tag:TypeTag[R], tag2:ClassTag[R]): Seq[R]  = {
    // val map = new MessageRefMapImpl

    val tableName = getTableName[K](k)
    val keyJson = write(k)
    val rsJson = RefMapImpl.getResultSet(keyJson, tableName)

    rsJson.map{x => val o = parse(x); o.extract[R]}
  }
}
