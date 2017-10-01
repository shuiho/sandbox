import org.json4s._
import org.json4s.jackson.JsonMethods._
//import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.{read, write}
import java.text.SimpleDateFormat


/**
  * Created by hoki on 7/8/2017.
  */
case class Person(name: String, age: Int)
case class Child(name: String, age: Int, birthdate: Option[java.util.Date])
case class Mapper(l:List[Map[String, Any]])

// case class Address(street: String, city: String)
// case class Person(name: String, address: Address, children: List[Child])



object testJasonSerialization extends App {

  // implicit val formats = Serialization.formats(NoTypeHints)
  implicit val formats = org.json4s.DefaultFormats


  val c1 = write(Child("Mary", 5, Some(new SimpleDateFormat("yyyy-MM-dd").parse("2017-07-08"))))
  println("json - child:" + c1)

  val p1 = write(Person("joe",15))
  println("json - person:" + p1)

  //val child = read[Child](c1)
  //println ("Child" + child)

  // val cMap = read[Map[String, Any]](c1)
  val cMap = parse(c1)
  println ("Child Json object:" + cMap)
  val cc = cMap.values.asInstanceOf[Map[String, Any]]
  println ("Child Map[String, Any]:" + cc)

  val c = cMap.extract[Child]
  println ("Child case class:" + c)


  //val person = read[Person](p1)
  //println ("Person" + person)

  // val pMap = read[Map[String, Any]](p1)
  val pMap = parse(p1)
  println ("Person Json object:" + pMap)

  val pp = pMap.values.asInstanceOf[Map[String, Any]]
  println ("Person Map[String, Any]:" + pp)

  val p = cMap.extract[Person]
  println ("Person class class:" + p)


}
