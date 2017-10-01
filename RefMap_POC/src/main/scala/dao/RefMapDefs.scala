package dao

import java.sql.Timestamp

/**
  * Created by hoki on 6/20/2017.
  */
  // Message case classes
object Message {

  final case class MessageQueryByID(id: Long)

  final case class MessageQueryBySender(sender: String)

  final case class MessageQueryAll()

  final case class MessageValue(sender: String, content: String, id: Long)

  val defaultValue = MessageValue("-99","-99",-99)

}

object BankManager {

  // Bank Manager case class
  final case class BankManagerQueryByID(id: Long)

  final case class BankManagerQueryByName(name: String)

  final case class BankManagerQueryAll()

  final case class BankManagerValue(snapshot_date:String,id: Long, name: String, transit: String, bankaddress: String)

  val defaultValue = BankManagerValue("9999-12-31", -99, "-99", "-99", "-99")

}

object key_table_pairs {
  val m = Map (
    "BankManagerQueryByID" -> "bankmanager",
    "BankManagerQueryByName" -> "bankmanager",
    "BankManagerQueryAll" -> "bankmanager",
    "MessageQueryByID" -> "message",
    "MessageQueryBySender" -> "message",
    "MessageQueryAll" -> "message"
  )
}