package dongwontuna.net.coinTradingSystem.CLI

import scala.io.StdIn
import dongwontuna.net.coinTradingSystem.sqlManager
import dongwontuna.net.coinTradingSystem.API
import dongwontuna.net.coinTradingSystem.AnExchange
import javax.ws.rs.NotFoundException
import java.text.NumberFormat
import dongwontuna.net.coinTradingSystem.sqlManager.getOrders
import dongwontuna.net.coinTradingSystem.Main
import dongwontuna.net.coinTradingSystem.ORDER
import dongwontuna.net.coinTradingSystem.types.EXCHANGE._
import dongwontuna.net.coinTradingSystem.CLI._
import java.util.UUID

import dongwontuna.net.coinTradingSystem.types.EXCHANGE
def clearTerminal() = print("\u001b[2J\u001b[H")
val numberNoExistString = "\n\nPlease enter the right Number."

val orderString = s"""
        || Num |  Symbol  |  Type  |  triggerPrice  |  targetPrice  |   amount   |
        |=-----------------------------------------------------------------------="""

object CLIMenu {
  def mainPage(): Unit = {
    while true do {
      clearTerminal()
      println("""
      |  _____      _       
      | /  __ \    (_)      
      ||  /  \/ __  _ _ __  
      || |    / _ \| | '_  \
      ||  \_/\ (_) | | | | |
      | \____/\___/|_|_| |_|
      |                     
      |                     
      |
      |      _____               _ _               _____           _                 
      |     |_   _|             | (_)             /  ___|         | |                
      |       | |  _ __ __ _  __| |_ _ __   __ _  \ `--. _   _ ___| |_ ___ _ __ ___  
      |       | | | '__/ _` |/ _` | | '_ \ / _` |  `--. \ | | / __| __/ _ \ '_ ` _ \ 
      |       | | | | | (_| | (_| | | | | | (_| | /\__/ / |_| \__ \ ||  __/ | | | | |
      |       \_/ |_|  \__,_|\__,_|_|_| |_|\__, | \____/ \__, |___/\__\___|_| |_| |_|
      |                                     __/ |         __/ |                      
      |                                     |___/        |___/                       
      |
      """.stripMargin)
      println("""
      |Please enter the Default Exchange
      |
      |  1. Binance
      |  2. GateIO
      |  3. Huobi
      |  4. Kucoin
      |
      |  5. Exit
      |
      |""".stripMargin)
      
      print("Number : ")

      val num = StdIn.readLine()

      num match {
        case "1" => launchExchange(Main.Exchanges.get(BINANCE).get)
        case "2" => launchExchange(Main.Exchanges.get(GATEIO).get)
        case "3" => launchExchange(Main.Exchanges.get(HUOBI).get)
        case "4" => launchExchange(Main.Exchanges.get(KUCOIN).get)
        case "5" => return;
        case _   => println(numberNoExistString)
      }
    }
  }

  def launchExchange(exClass: AnExchange): Unit = {

    val exName: String = exClass.Exchange.getExchangeSpecification.getExchangeName.toUpperCase
    var resultAPI: API = sqlManager.getAPIKEY(exName)

    def setupAPIKey(): Unit = {
      println(s"Setup the API Key of $exName Exchange")
      print("API KEY : ")
      val apiKey = StdIn.readLine()
      print("SECRET KEY :")
      val secretKey = StdIn.readLine()
      sqlManager.upsertAPIKEY(new API(exName, apiKey, secretKey))
      resultAPI = sqlManager.getAPIKEY(exName)
    }

    if resultAPI.exchangeName == "NONE" then {
      setupAPIKey()
      resultAPI = sqlManager.getAPIKEY(exName)
    }

    while true do {
      val menuString =
        StringFormat.makeMenuString(exName.toLowerCase.capitalize, "Menu")
      clearTerminal()
      println(s"""
      |$menuString
      |
      |  1. Trade Services
      |  2. Market Services
      |  3. Account Services
      |
      |
      |  4. Resetup the API key of $exName Exchange
      |  5. Return to Main Menu
      """.stripMargin)
      print("Number : ")
      val userInputed = StdIn.readLine()
      userInputed match
        case "1" => clearTerminal(); tradeService(exClass, exName)
        case "2" => clearTerminal(); marketService(exClass, exName)
        case "3" => clearTerminal(); accountService(exClass, exName)
        case "4" => clearTerminal(); setupAPIKey();
        case "5" => return;
        case _ => clearTerminal(); println("\nUnexpected value inputed")
    }
  }

  def tradeService(exClass: AnExchange, exName: String): Unit = {
    var menuString = StringFormat.makeMenuString(exName, "Trade Service Menu")
    while true do {
      println(s"""
      |$menuString
      |
      |  1. View Open Orders
      |  2. Edit the Order
      |  3. Make a New Order
      |  4. Delete the Order
      |  
      |  5. Return to Main Menu
      """.stripMargin)
      print("Number : ")
      val userInputed = StdIn.readLine()
      clearTerminal()
      userInputed match
        case "1" => clearTerminal(); TradeService.getCurrentOrder(exName)
        case "2" => clearTerminal(); TradeService.editOrder(exClass,exName)
        case "3" => clearTerminal(); TradeService.addOrder(exClass,exName)
        case "4" => clearTerminal(); TradeService.deleteOrder(exName)
        case "5" => return;
        case _ => clearTerminal(); println(numberNoExistString)
    }
  }
  def marketService(exClass: AnExchange, exName: String): Unit = {
    val menuString = StringFormat.makeMenuString(exName, "Market Service Menu")
    while true do {
      println(s"""$menuString
      |
      |  1. Get the Coin Information
      |  2. Get the Exchange Information
      |  3. Get the Order Book Information
      |
      |  4. Return to Main Menu
      |  
      """.stripMargin)

      val selectedNum = StdIn.readLine()

      selectedNum match {
        case "1" => clearTerminal(); MarketService.getInformation()
        case "2" => clearTerminal();MarketService.getExchangeInformation()
        case "3" => clearTerminal();MarketService.getOrderBookInformation()
        case "4" => return
        case _   => clearTerminal();println(numberNoExistString)
      }
    }
  }
  def accountService(exClass: AnExchange, exName: String): Unit = {
    val menuString = StringFormat.makeMenuString(exName, "Account Service Menu")
    while true do {
      println(s"""$menuString
      |
      |  1. Get the Account Balance
      |  2. Get the 
      |  3. Get the Order Book Information
      |
      |  4. Return to Main Menu
      |  
      """.stripMargin)

      val selectedNum = StdIn.readLine()

      selectedNum match {
        case "1" => clearTerminal();MarketService.getInformation()
        case "2" => clearTerminal();MarketService.getExchangeInformation()
        case "3" => clearTerminal();MarketService.getOrderBookInformation()
        case "4" => return
        case _   => clearTerminal();println(numberNoExistString)
      }
    }

  }
}
