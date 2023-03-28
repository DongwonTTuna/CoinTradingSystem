package dongwontuna.net.coinTradingSystem.CLI

import dongwontuna.net.coinTradingSystem.ORDER
import dongwontuna.net.coinTradingSystem.AnExchange
import scala.io.StdIn
import org.knowm.xchange.instrument.Instrument

object StringFormat {

  var exClass: AnExchange = _
  var exName: String = _

  def initialize(exClass: AnExchange): Unit = {
      this.exClass = exClass
      this.exName = exClass.Name
  }

  def makeMenuString(menuText: String, thirdText: String = ""): String = {
    s"=--- ${exName.toLowerCase.capitalize} $menuText".padTo(72, "-").mkString("") + "=" + thirdText
  }
  
  def makeOrderString(orders: List[ORDER]): String = {
    if (orders.isEmpty) {
      return """|||                                                                       |
                |||                                  NONE                                 |
                |||                                                                       |
                |=-----------------------------------------------------------------------=""".stripMargin
    }

    val formattedOrders = orders.zipWithIndex.map { case (order, index) =>
      val orderType = order.orderType match {
        case 0 => "Buy"
        case 1 => "Sell"
        case 2 => "TakeP"
        case 3 => "LossC"
      }

      s"||${padMiddle(index.toString, 5)}|${padMiddle(order.ticker, 10)}|${padMiddle(orderType, 8)}|${padLeft(order.triggerPrice.toString, 16)}|${padLeft(order.targetPrice.toString, 15)}|${padLeft(order.amount.toString, 12)}|"
    }

    formattedOrders.mkString("\n=-----------------------------------------------------------------------=\n") + "\n=-----------------------------------------------------------------------="
  }

  def searchInstruments(): Instrument = {

      val symbols = exClass.instruments

      def mkString(text: String = ""): String = {
        if !text.isBlank then {
              symbols.keys
                  .filter(_.startsWith(text))
                  .grouped(5)
                  .map(_.mkString(" "))
                  .mkString("\n")
        } else {
          symbols.keys.grouped(5).map(_.mkString(" ")).mkString("\n")
        }
      }

      var symbolsString = mkString()
      
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
              | Symbol : """.stripMargin)
        
         val selectedSymbol = StdIn.readLine

        exClass.instruments.get(selectedSymbol) match {
          case None => symbolsString = mkString(selectedSymbol)
          case Some(value) => return value
        } 
      }
      throw new Exception("Could not return the Instrument while searchInstruments")
  }

  def padLeft(s: String, num: Int): String = {
    s.reverse.padTo(num, ' ').reverse
  }
  def padMiddle(s: String, num: Int): String = {
    val diff = num - s.length
    if diff % 2 == 0 then (" " * (diff / 2)) + s + (" " * (diff / 2))
    else " " * ((diff - 1) / 2) + s + " " * ((diff - 1) / 2 + 1)
  }


}
