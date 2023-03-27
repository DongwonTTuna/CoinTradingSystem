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
object TradeService {

  def handleOrder(
      anExchange: AnExchange,
      order: ORDER,
      mode: String
  ): Option[ORDER] = {
    var tempOrder: ORDER = order

    def editSymbol(): Unit = {
      val symbols = anExchange.instruments
      var symbolsString =
        symbols.keys.grouped(5).map(_.mkString(" ")).mkString("\n")
      while true do {
        clearTerminal()
        print(s"""Symbol List
      | 
      | $symbolsString
      |
      |
      | You can search for symbols with enter the start of the symbol
      |
      |
      |
      | Please enter the Symbol : """.stripMargin)
        val selectedSymbol = StdIn.readLine
        try {
          symbols.get(selectedSymbol) match {
            case None => throw new Exception("Symbol not found")
            case Some(value) =>
              return tempOrder = tempOrder.copy(ticker = selectedSymbol)
          }
        } catch {
          case e: Exception => {
            symbolsString = symbols.keys
              .filter(_.startsWith(selectedSymbol))
              .grouped(5)
              .map(_.mkString(" "))
              .mkString("\n")
          }
        }
      }
    }
    def editOrderType(): Unit = {
      while true do {
        print(s"""Order Type
      | 0 : Buy
      | 1 : Sell
      | 2 : Take Profit
      | 3 : Loss Cut
      | 
      | Please enter the order type : """.stripMargin)
        try {
          val selectedNum = StdIn.readInt
          return tempOrder = tempOrder.copy(orderType = selectedNum)
        } catch {
          case e: Exception => println("Invalid Input")
        }
      }

    }

    def editPrice(typeOfPrice: DECIMAL_TYPE): Unit = {
      while true do {
        val currentPrice: String = anExchange.getCurrentTicker(tempOrder.ticker) match {
          case None => "NONE"
          case Some(value) => value.getLast().toString()
        }

        val stringText = typeOfPrice match {
          case TARGET_PRICE  => "Target Price"
          case TRIGGER_PRICE => "Trigger Price"
          case AMOUNT => "Amount"
        }

        print(s"""$stringText
              | 
              ${if typeOfPrice != AMOUNT then s"| currentPrice : ${currentPrice}" else ""}
              |
              | Please enter the $stringText : """.stripMargin)
        
        val selectedNum = StdIn.readLine()

        try {
          typeOfPrice match {
            case TARGET_PRICE => return tempOrder = tempOrder.copy(targetPrice = BigDecimal(selectedNum))
            case TRIGGER_PRICE => return tempOrder = tempOrder.copy(triggerPrice = BigDecimal(selectedNum))
            case AMOUNT => return tempOrder = tempOrder.copy(amount = BigDecimal(selectedNum))
          }
        } catch {
          case e: Exception => println("Invalid Input")
        }
      }
    }

    while true do {
      clearTerminal()
      var menuString =
        StringFormat.makeMenuString(tempOrder.exchangeName, mode, orderString)
      println(s"""$menuString
      ${StringFormat.makeOrderString(List(tempOrder))}
      |
      | 1. Edit Symbol
      | 2. Edit orderType
      | 3. Edit triggerPrice
      | 4. Edit targetPrice
      | 5. Edit amount
      |
      | 6. Save and Exit
      | 7. Discard all and Exit
      """.stripMargin)

      var userInputed = StdIn.readLine()

      userInputed match
        case "1" => clearTerminal(); editSymbol()
        case "2" => clearTerminal(); editOrderType()
        case "3" => clearTerminal(); editPrice(TRIGGER_PRICE)
        case "4" => clearTerminal(); editPrice(TARGET_PRICE)
        case "5" => clearTerminal(); editPrice(AMOUNT)
        case "6" => clearTerminal(); return Some(tempOrder)
        case "7" => clearTerminal(); return None
        case _   => clearTerminal(); println(numberNoExistString)
    }
    None
  }

  def addOrder(anExchange: AnExchange, exName: String): Unit = {
    val newOrder = handleOrder(
      anExchange,
      new ORDER(
        UUID.randomUUID().toString(),
        exName,
        "NONE",
        0,
        BigDecimal("0"),
        BigDecimal("0"),
        BigDecimal("0")
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

  def getCurrentOrder(exName: String): Unit = {
    val orders: Map[String, ORDER] = sqlManager.getOrders(exName)
    val menuString = StringFormat.makeMenuString(
      exName,
      "Open Orders",
      orderString
    )
    clearTerminal()
    println(s"""$menuString
               ${StringFormat.makeOrderString(orders.values.toList)}
                """.stripMargin)
  }

  def editOrder(anExchange: AnExchange, exName: String): Unit = {

    val orders: Map[String, ORDER] = sqlManager.getOrders(exName)
    var menuString: String = StringFormat.makeMenuString(exName, "Edit Order", orderString)
   
    print(s"""$menuString
          ${StringFormat.makeOrderString(orders.values.toList)}
          |
          |Which Order do you want to edit? : """.stripMargin)

    val orderNum = StdIn.readLine()
    val selectedOrder: ORDER = orders.get(orderNum) match {
      case None        => return println("You inputed the invalid order Number")
      case Some(value) => value
    }

    val result = handleOrder(anExchange, selectedOrder, "Edit Order") match {
      case None        => return println("You cancelled the order")
      case Some(value) => value
    }

    if sqlManager.upsertOrder(result) then println("The order has been updated")
    else println("Cannot update the order")

  }

  def deleteOrder(exName: String): Unit = {
    while true do {
      val orders: Map[String, ORDER] = sqlManager.getOrders(exName)
      var menuString: String = StringFormat.makeMenuString(exName, "Delete Order", orderString)
      
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
