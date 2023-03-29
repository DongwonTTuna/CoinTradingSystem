package dongwontuna.net.coinTradingSystem.CLI

import scala.concurrent.ExecutionContext.Implicits.global
import dongwontuna.net.coinTradingSystem.AnExchange
import scala.io.StdIn
import org.knowm.xchange.instrument.Instrument
import org.knowm.xchange.dto.marketdata.OrderBook
import scala.collection.JavaConverters.AsScala
import scala.jdk.CollectionConverters._
import scala.util.control.Breaks._
import scala.concurrent.Future
import scala.collection.mutable.ListBuffer
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
    val description =exClass.Exchange.getExchangeSpecification().getExchangeDescription()
    
    println(s"""${StringFormat.makeMenuString("Exchange Information")}
            |
            |
            |  Exchange Name: ${exName.toLowerCase().capitalize}
            |  Description: $description
            |
            """.stripMargin)

    StdIn.readLine()
    clearTerminal()
  }

  def getOrderBookInformation() : Unit ={
    var currentSymbol: Instrument = StringFormat.searchInstruments()
    var status: Boolean = true
    def userAgreed(): Future[Unit] = Future {
      def calculateCumulativeVolume(list: List[BigDecimal]):List[BigDecimal] = {
        var num = BigDecimal(0)
        list.map(item => {
            num = num.`+`(item)
            num
        })
      }

      def printOrderBookAsChart(bidsPrice : List[BigDecimal], bidsAmount : List[BigDecimal], asksPrice : List[BigDecimal],asksAmount : List[BigDecimal]): Unit = {

        val prices = bidsPrice ++ asksPrice
        val amounts = bidsAmount ++ asksAmount

        val minY = bidsAmount.min
        val maxY = bidsAmount.max
        val middleY = maxY.`+`(minY)./(2).setScale(15, BigDecimal.RoundingMode.HALF_UP)
        val minX = bidsPrice.min
        val maxX = bidsPrice.max
        val middleX = maxX.`+`(minX)./(2).setScale(15, BigDecimal.RoundingMode.HALF_UP)
        val XtotalSize = prices.length 
        val YtotalSize = 40
        val oneTickSize = (maxY.`-`(minY))./(40).setScale(15, BigDecimal.RoundingMode.HALF_UP)

        val showString = "*"

        var tempPrintList= ListBuffer.empty[List[String]]
        var tempXList = ListBuffer.empty[String]

        for y <- 0 until XtotalSize do {
          val currentPrice = prices(y)
          val currentAmount = amounts(y)
          
          var timesToInsert = currentAmount./(oneTickSize).toInt

          for y <- 0 until YtotalSize do {
            if timesToInsert > 0 then {tempXList+=showString; timesToInsert-=1} else tempXList+=" "
          }
          tempPrintList += tempXList.toList
          tempXList = ListBuffer.empty[String]
        }

        val printList = tempPrintList.toList
        
        // TODO: 뎁스에 가격 및 볼륨 추가하기

        for (y <- (YtotalSize-1) to 0 by -1) {
          var tempString = ""
          for (x <- 0 until XtotalSize) {
            tempString += s"${printList(x)(y)}"
          }
            println(tempString)
        }

        val XyieldValue = XtotalSize - minX.toDouble.toString.length - middleX.toDouble.toString.length  - maxX.toDouble.toString.length + 3
        println((for x <- 0 until XyieldValue yield if x == 0 then minX.toDouble else if x == XyieldValue/2 - 4 then middleX.toDouble else if x == XyieldValue-1 then maxX.toDouble else " ").mkString(""))
        println("\n\n\n\n\n Press enter to Exit")
      }
      
      while status do {
        clearTerminal()
        val orderbook: OrderBook = exClass.getCurrentOrderBook(currentSymbol)
        var bids =  orderbook.getBids().asScala.toList
        var asks = orderbook.getAsks().asScala.toList

        var bidsPrice = bids.map(item => BigDecimal(item.getLimitPrice()))
        var bidsAmount = bids.map(item => BigDecimal(item.getRemainingAmount())).reverse
        
        var asksPrice = asks.map(item => BigDecimal(item.getLimitPrice()))
        var asksAmount = asks.map(item => BigDecimal(item.getRemainingAmount()))
        
        var cumulativeBids = calculateCumulativeVolume(bidsAmount).reverse
        var cumulativeAsks = calculateCumulativeVolume(asksAmount)
        printOrderBookAsChart(bidsPrice,cumulativeBids,asksPrice,cumulativeAsks)

        Thread.sleep(500)
        }
    }
    
    while true do {
        print(s"""${StringFormat.makeMenuString(" Order Book Menu")}
              |
              |  Would you like to retrieve data for the following symbol?
              |
              |  currentSymbol = $currentSymbol
              | 
              |  Y or N : """.stripMargin)

        val input = StdIn.readLine

        input match {
          case y: String if y.toLowerCase() == "y" => {
                                                        userAgreed()
                                                        StdIn.readLine()
                                                        
                                                        return {status = false; clearTerminal(); }
                                                      }
          case n: String if n.toLowerCase() == "n" => return
          case _ => clearTerminal(); println(valueNoExistString)
        }
      }
  }
  def getRecentTrades() : Unit = {
    val exName = exClass.Exchange.getExchangeSpecification().getExchangeName().toUpperCase()
    while true do {
      
    }
  }
}