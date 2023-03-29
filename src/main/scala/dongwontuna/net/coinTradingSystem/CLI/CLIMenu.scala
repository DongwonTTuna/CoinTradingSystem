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
import concurrent.duration.DurationInt
import java.util.UUID

import dongwontuna.net.coinTradingSystem.types.EXCHANGE




object CLIMenu {
  var exClass: AnExchange = _
  var exName: String = _

  def mainPage(): Unit = {
    while true do {
      clearTerminal()
      print("""
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
      |
      |Please enter the Default Exchange
      |
      |  1. Binance
      |  2. GateIO
      |  3. Huobi
      |  4. Kucoin
      |
      |  5. Exit
      |
      |  Number : """.stripMargin)

      val num = StdIn.readLine()

      num match {
        case "1" => launchExchange(Main.Exchanges.get(BINANCE).get)
        case "2" => launchExchange(Main.Exchanges.get(GATEIO).get)
        case "3" => launchExchange(Main.Exchanges.get(HUOBI).get)
        case "4" => launchExchange(Main.Exchanges.get(KUCOIN).get)
        case "5" => return;
        case _   => println(valueNoExistString)
      }
    }
  }

  def launchExchange(exClass: AnExchange): Unit = {

    this.exClass = exClass
    this.exName = exClass.Name
    TradeService.initialize(exClass)
    MarketService.initialize(exClass)
    AccountService.initialize(exClass)
    StringFormat.initialize(exClass)

    def setupAPIKey(): API = {
      clearTerminal()
      println(s"Setup the API Key of ${exClass.Name} Exchange")
      print("API KEY : ")
      val apiKey = StdIn.readLine()
      print("SECRET KEY :")
      val secretKey = StdIn.readLine()
      sqlManager.upsertAPIKEY(new API(exClass.Name, apiKey, secretKey))
      sqlManager.getAPIKEY(exClass.Name).headOption match {
        case None => throw new Exception("Cannot resolve catching the API key")
        case Some(value) => value
      }
    }
    
    var resultAPI: API = sqlManager.getAPIKEY(exName) match {
      case None => setupAPIKey()
      case Some(value) => value
    }

    while true do {
      val menuString =
        StringFormat.makeMenuString("Menu")
      clearTerminal()
      print(s"""
      |$menuString
      |
      |  1. Trade Services
      |  2. Market Services
      |  3. Account Services
      |
      |
      |  4. Resetup the API key of ${exClass.Name} Exchange
      |  5. Return to Main Menu
      |
      |  Number : """.stripMargin)
      val userInputed = StdIn.readLine()
      userInputed match
        case "1" => clearTerminal(); tradeServiceFunc()
        case "2" => clearTerminal(); marketServiceFunc()
        case "3" => clearTerminal(); accountServiceFunc()
        case "4" => clearTerminal(); setupAPIKey();
        case "5" => return;
        case _ => clearTerminal(); println("\nUnexpected value inputed")
    }
  }

  def tradeServiceFunc(): Unit = {
    var menuString = StringFormat.makeMenuString("Trade Service Menu")
    while true do {
      print(s"""
      |$menuString
      |
      |  1. View Open Orders
      |  2. Edit the Order
      |  3. Make a New Order
      |  4. Delete the Order
      |  
      |  5. Return to Main Menu
      |
      |  Number : """.stripMargin)
      val userInputed = StdIn.readLine()
      clearTerminal()
      userInputed match
        case "1" => clearTerminal(); TradeService.getCurrentOrder()
        case "2" => clearTerminal(); TradeService.editOrder()
        case "3" => clearTerminal(); TradeService.addOrder()
        case "4" => clearTerminal(); TradeService.deleteOrder()
        case "5" => return;
        case _ => clearTerminal(); println(valueNoExistString)
    }
  }
  def marketServiceFunc(): Unit = {
    val menuString = StringFormat.makeMenuString("Market Service Menu")
    while true do {
      print(s"""$menuString
      |
      |  1. Get the Coin Information
      |  2. Get the Exchange Information
      |  3. Show the Order Books
      |  4. Show the Recent Trades
      |
      |  5. Return to Main Menu
      |  
      |  Number : """.stripMargin)

      val selectedNum = StdIn.readLine()

      selectedNum match {
        case "1" => clearTerminal(); MarketService.getInformation()
        case "2" => clearTerminal(); MarketService.getExchangeInformation()
        case "3" => clearTerminal(); MarketService.getOrderBookInformation()
        case "4" => clearTerminal(); MarketService.getRecentTrades()
        case "5" => return
        case _   => clearTerminal();println(valueNoExistString)
      }
    }
  }
  def accountServiceFunc(): Unit = {
    val menuString = StringFormat.makeMenuString("Account Service Menu")
    while true do {
      print(s"""$menuString
      |
      |  1. Get the Account Balance
      |  2. Get the 
      |  3. Get the Order Book Information
      |
      |  4. Return to Main Menu
      |  
      |  Number : """.stripMargin)

      val selectedNum = StdIn.readLine()

      selectedNum match {
        case "1" => clearTerminal();MarketService.getInformation()
        case "2" => clearTerminal();MarketService.getExchangeInformation()
        case "3" => clearTerminal();MarketService.getOrderBookInformation()
        case "4" => return
        case _   => clearTerminal();println(valueNoExistString)
      }
    }

  }

}
