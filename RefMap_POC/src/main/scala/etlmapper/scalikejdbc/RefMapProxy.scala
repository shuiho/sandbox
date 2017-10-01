package etlmapper.scalikejdbc

import dao._
import core.refmap.scalikejdbc._
import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

/**
  * Created by hoki on 6/20/2017.
  */
object RefMapProxy {

  def getValue[K<:Product, R<:Product](k:K, default:R)
        (implicit tag:TypeTag[R], tag2:ClassTag[R]):R = {
    // val map = new MessageRefMapImpl
    RefMapImpl.getValue[K, R](k).getOrElse(default)
  }

  def getResultSet[K<:Product,R<:Product](k:K)
     (implicit tag:TypeTag[R], tag2:ClassTag[R]): Seq[R]  = {
    // val map = new MessageRefMapImpl
    RefMapImpl.getResultSet[K, R](k)
  }
}
