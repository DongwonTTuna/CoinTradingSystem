package dongwontuna.net.coinTradingSystem.CLI

import dongwontuna.net.coinTradingSystem.sqlManager
import scala.io.StdIn
import dongwontuna.net.coinTradingSystem.ORDER
import java.util.UUID
import dongwontuna.net.coinTradingSystem.Main
import org.knowm.xchange.dto.marketdata.Ticker
import org.knowm.xchange.instrument.Instrument
import dongwontuna.net.coinTradingSystem.AnExchange
import dongwontuna.net.coinTradingSystem.types.EXCHANGE._
import dongwontuna.net.coinTradingSystem.types.DECIMAL_TYPE._
import scala.util.Try
import scala.util.Failure
import scala.util.Success

object TradeService {

  var exClass: AnExchange = _
  var exName: String = _
    
  def initialize(exClass: AnExchange): Unit = {
    this.exClass = exClass
    this.exName = exClass.Name
  }


  def handleOrder(
      order: ORDER,
      mode: String
  ): Option[ORDER] = {
    var tempOrder: ORDER = order

    def editSymbol(): Unit = {
        var selectedSymbol: String = StringFormat.searchInstruments().toString
        return tempOrder = tempOrder.copy(ticker = selectedSymbol)
    }
    def editType(): Unit = {
      while true do {
        print(s"""Type of the order
              |
              | 0 : Buy
              | 1 : Sell
              | 2 : Take Profit
              | 3 : Loss Cut
              | 
              | Type : """.stripMargin)
        val selectedNum = StdIn.readLine
        selectedNum match {
            case "0"|"1"|"2"|"3" => return tempOrder = tempOrder.copy(orderType = selectedNum.toInt)
            case _ => clearTerminal(); println(valueNoExistString)
        }
      }
    }
    
    def editOrderType() : Unit = {
      while true do {
        print(s"""Type of the comfirmation
              |
              | 0 : Limit
              | 1 : Market
              |
              | Order Type : """.stripMargin)
        val selectedNum = StdIn.readLine
        selectedNum match {
          case "0" => return tempOrder = tempOrder.copy(ismarket = false)
          case "1" => return tempOrder = tempOrder.copy(ismarket = true)
          case _ => println(valueNoExistString)
        }
      }

    }

    def editPrice(typeOfPrice: DECIMAL_TYPE): Unit = {
      while true do {
        val currentPrice: String = exClass.getInstrument(tempOrder.ticker) match {
          case None => "NONE"
          case Some(value) => exClass.getCurrentTicker(value).getLast().toString()
        }

        val stringText: String = typeOfPrice match {
          case TARGET_PRICE  => "Target Price"
          case TRIGGER_PRICE => "Trigger Price"
          case AMOUNT => "Amount"
        }

        print(s"""$stringText
              | 
              ${if typeOfPrice != AMOUNT then s"| currentPrice : ${currentPrice}" else ""}
              |
              | $stringText : """.stripMargin)

        val input = StdIn.readLine()
        
        val result = Try(BigDecimal(input)) match {
          case Success(value) => value
          case Failure(value: NumberFormatException) => value
          case Failure(exception) => throw exception
        }
      
        result match {
          case value: BigDecimal => typeOfPrice match {
                                      case TARGET_PRICE => return tempOrder = tempOrder.copy(targetPrice = value)
                                      case TRIGGER_PRICE => return tempOrder = tempOrder.copy(triggerPrice = value)
                                      case AMOUNT => return tempOrder = tempOrder.copy(amount = value)
                                    }
          case _ => clearTerminal(); println(valueNoExistString)
        }
        
        
      }
    }

    while true do {
      clearTerminal()
      var menuString =
        StringFormat.makeMenuString(mode, orderString)
      print(s"""$menuString
      ${StringFormat.makeOrderString(List(tempOrder))}
      |
      | 1. Edit Symbol
      | 2. Edit orderType
      | 3. Edit triggerPrice
      | 4. Edit targetPrice
      | 5. Edit amount
      | 6. Edit order Confirmation Type
      |
      | 7. Save and Exit
      | 8. Discard all and Exit
      |
      | Number : """.stripMargin)


      var userInputed = StdIn.readLine()

      userInputed match
        case "1" => clearTerminal(); editSymbol()
        case "2" => clearTerminal(); editType()
        case "3" => clearTerminal(); editPrice(TRIGGER_PRICE)
        case "4" => clearTerminal(); editPrice(TARGET_PRICE)
        case "5" => clearTerminal(); editPrice(AMOUNT)
        case "6" => clearTerminal(); editOrderType()
        case "7" => clearTerminal(); return Some(tempOrder)
        case "8" => clearTerminal(); return None
        case _   => clearTerminal(); println(valueNoExistString)
    }
    None
  }

  def addOrder(): Unit = {
    val newOrder = handleOrder(
      new ORDER(
        UUID.randomUUID().toString(),
        exClass.Name,
        "NONE",
        0,
        BigDecimal("0"),
        BigDecimal("0"),
        BigDecimal("0"),
        false
      ),
      "New Order"
    )

    val result = newOrder match {
      case None        => return println("you cancelled the order")
      case Some(value) => value
    }

    if sqlManager.upsertOrder(result) then println("created the order")
    else println("cannot create the order")
  }

  def getCurrentOrder(): Unit = {
    val orders: Map[String, ORDER] = sqlManager.getOrders(exName)
    val menuString = StringFormat.makeMenuString(
      "Open Orders",
      orderString
    )
    clearTerminal()
    println(s"""$menuString
               ${StringFormat.makeOrderString(orders.values.toList)}
                """.stripMargin)
  }

  def editOrder(): Unit = {

    val orders: Map[String, ORDER] = sqlManager.getOrders(exName)
    var menuString: String = StringFormat.makeMenuString("Edit Order", orderString)
   
    print(s"""$menuString
          ${StringFormat.makeOrderString(orders.values.toList)}
          |
          |Which Order do you want to edit? : """.stripMargin)

    val orderNum = StdIn.readLine()
    val selectedOrder: ORDER = orders.get(orderNum) match {
      case None        => return println("You inputed the invalid order Number")
      case Some(value) => value
    }

    val result = handleOrder(selectedOrder, "Edit Order") match {
      case None        => return println("You cancelled the order")
      case Some(value) => value
    }

    if sqlManager.upsertOrder(result) then println("The order has been updated")
    else println("Cannot update the order")
    
  }

  def deleteOrder(): Unit = {
    while true do {
      val orders: Map[String, ORDER] = sqlManager.getOrders(exName)
      var menuString: String = StringFormat.makeMenuString("Delete Order", orderString)
      
      print(s"""$menuString
            ${StringFormat.makeOrderString(orders.values.toList)}
            |
            |
            |Which Order do you want to delete? : """.stripMargin)

      val selectedOrder: Option[ORDER] = orders.get(StdIn.readLine())

      selectedOrder match {
        case None => clearTerminal(); println("Invalid number inputed")
        case Some(value) => {
                             if sqlManager.deleteOrder(value)
                             then {clearTerminal(); return println("\n\nSuccessfully deleted")}
                             else { clearTerminal(); return println("\n\nFailed to delete") }
                            }
      }
    }
  }
}
