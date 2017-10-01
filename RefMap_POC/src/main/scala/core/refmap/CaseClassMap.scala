package core.refmap

import scala.reflect._
import scala.reflect.runtime.universe._


case class Test(t: String, ot: Option[String])

package object CaseClassMap {
//  def fromMap[T: TypeTag: ClassTag](m: Map[String,_]) = {
  def fromMap[T](m: Map[String,_])
    (implicit tag:TypeTag[T], tag2:ClassTag[T]):T= {

    val rm = runtimeMirror(classTag[T].runtimeClass.getClassLoader)
    val classTest = typeOf[T].typeSymbol.asClass
    val classMirror = rm.reflectClass(classTest)
    val constructor = typeOf[T].decl(termNames.CONSTRUCTOR).asMethod
    val constructorMirror = classMirror.reflectConstructor(constructor)

    val constructorArgs = constructor.paramLists.flatten.map( (param: Symbol) => {
      val paramName = param.name.toString
      if(param.typeSignature <:< typeOf[Option[Any]])
        m.get(paramName)
      else
        m.get(paramName).getOrElse(throw new IllegalArgumentException("Map is missing required parameter named " + paramName))
    })

    val c = constructorMirror(constructorArgs:_*)
    c.asInstanceOf[T]
  }

  // def getCCParams(cc: Product) = {
  // val values = cc.productIterator
  // cc.getClass.getDeclaredFields.map( _.getName -> values.next ).toMap
  // }
  //

  def toMap(cc: Product) = {
    val values = cc.productIterator
    val fields = cc.getClass.getDeclaredFields
    fields.map( _.getName -> values.next)
//      .filter(_._2.getClass != classOf[scala.None$])
      .toMap
  }
}
