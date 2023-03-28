package dongwontuna.net.coinTradingSystem.CLI

import dongwontuna.net.coinTradingSystem.AnExchange
import scala.io.StdIn

object MarketService {

  var exClass: AnExchange = _
  var exName: String = _
    
  def initialize(exClass: AnExchange): Unit = {
        this.exClass = exClass
        this.exName = exClass.Name
  }

  def getInformation(): Unit = {

  }

  def getExchangeInformation(): Unit = {

  }

  def getOrderBookInformation() : Unit ={
    var currentSymbol: String = "NONE"
    while true do {
      print(s"""${StringFormat.makeMenuString(" Order Book Menu")}
            |
            |  currentSymbol = $currentSymbol
            | 
            |  Symbol : """.stripMargin)

      val input = StdIn.readLine()

      val openBook = exClass.getCurrentOrderBook(input) match {
        case None => clearTerminal(); currentSymbol = StringFormat.searchInstruments()
        case Some(value) => value
      }




    }

    
    
  }
  def getRecentTrades() : Unit = {
    val exName = exClass.Exchange.getExchangeSpecification().getExchangeName().toUpperCase()
    while true do {
      
    }
  }
}
