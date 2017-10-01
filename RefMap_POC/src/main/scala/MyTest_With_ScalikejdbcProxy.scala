import etlmapper.scalikejdbc.RefMapProxy
import dao._

object MyTest_With_ScalikejdbcProxy extends App {

  // Run the test query and print the results:
  println("\nSelecting message for ID = 1:")
  val k1 = Message.MessageQueryByID(1)
  val v1 = RefMapProxy.getValue[Message.MessageQueryByID,Message.MessageValue](k1,Message.defaultValue)
  println(v1)

  println("\nSelecting only messages from Dave:")
  val k2 = Message.MessageQueryBySender("Dave")
  val v2 = RefMapProxy.getValue[Message.MessageQueryBySender,Message.MessageValue](k2,Message.defaultValue)
  println(v2)

  println("\nSelecting only messages from all senders:")
  val k3 = Message.MessageQueryAll()
  val v3 = RefMapProxy.getResultSet[Message.MessageQueryAll,Message.MessageValue](k3)
  v3 foreach {println}

  println("\nSelecting bank manager for ID = 1:")
  val k4 = BankManager.BankManagerQueryByID(1)
  val v4 = RefMapProxy.getValue[BankManager.BankManagerQueryByID,BankManager.BankManagerValue](k4,BankManager.defaultValue)
  println(v4)

  println("\nSelecting only bank manager with name = Dave:")
  val k5 = BankManager.BankManagerQueryByName("Dave")
  val v5 = RefMapProxy.getValue[BankManager.BankManagerQueryByName,BankManager.BankManagerValue](k5,BankManager.defaultValue)
  println(v5)

  println("\nSelecting all bank manager:")
  val k6 = BankManager.BankManagerQueryAll()
  val v6 = RefMapProxy.getResultSet[BankManager.BankManagerQueryAll,BankManager.BankManagerValue](k6)
  v6 foreach {println}
}
