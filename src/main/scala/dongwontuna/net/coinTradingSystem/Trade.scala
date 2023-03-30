package dongwontuna.net.coinTradingSystem

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import dongwontuna.net.coinTradingSystem.types.EXCHANGE._
import dongwontuna.net.coinTradingSystem.types.DECIMAL_TYPE._
import dongwontuna.net.coinTradingSystem.types.EXCHANGE
import org.knowm.xchange.instrument.Instrument
import org.knowm.xchange.dto.trade.MarketOrder
import org.knowm.xchange.dto.Order.OrderType
import org.knowm.xchange.dto.trade.LimitOrder
import scala.util.Try
import scala.util.Failure
import scala.util.Success

object Trade {
  
    var orders: List[ORDER] = _
    
    def updateOrders(): Unit = {
        this.orders = sqlManager.getAllOrders()
    }

    def getExclass(order: ORDER) : AnExchange = {
        val exType = EXCHANGE.withName(order.exchangeName)
        Main.Exchanges.get(exType).get
    }

    def getCurrentPrice(order: ORDER): BigDecimal = {
        val exType = EXCHANGE.withName(order.exchangeName)
        val exClass = Main.Exchanges.get(exType).get
        
        val tickerInstrument = exClass.getInstrument(order.ticker) match {
            case None => throw new Exception("Invalid ticker type: ")
            case Some(value) =>  value
        }

        exClass.getCurrentTicker(tickerInstrument).getLast()
    }


    def buyMarket(order: ORDER) : Boolean = {
        val exClass = getExclass(order)
        val instrument = exClass.getInstrument(order.ticker).get
        val newOrder : MarketOrder = MarketOrder(OrderType.BID,java.math.BigDecimal(order.amount.toString),instrument)
        Try(exClass.tradeService.placeMarketOrder(newOrder)) match {
            case Failure(exception) => print(exception.getMessage); return false
            case Success(value) => println(value); return true
        }
    }

    def sellMarket(order: ORDER) : Boolean = {
        val exClass = getExclass(order)
        val instrument = exClass.getInstrument(order.ticker).get
        val newOrder : MarketOrder = MarketOrder(OrderType.ASK,java.math.BigDecimal(order.amount.toString),instrument)
        Try(exClass.tradeService.placeMarketOrder(newOrder)) match {
            case Failure(exception) => print(exception.getMessage); return false
            case Success(value) => println(value); return true
        }
    }
    
    def buyLimit(order: ORDER) : Boolean = {
        val exClass = getExclass(order)
        val instrument = exClass.getInstrument(order.ticker).get
        val newOrder : LimitOrder = LimitOrder(OrderType.BID, java.math.BigDecimal(order.amount.toString),instrument,null,null,java.math.BigDecimal(order.triggerPrice.toString))
        Try(exClass.tradeService.placeLimitOrder(newOrder)) match {
            case Failure(exception) => print(exception.getMessage); return false
            case Success(value) => println(value); return true
        }
    }  

    def sellLimit(order: ORDER) : Boolean = {
        val exClass = getExclass(order)
        val instrument = exClass.getInstrument(order.ticker).get
        val newOrder : LimitOrder = LimitOrder(OrderType.ASK, java.math.BigDecimal(order.amount.toString),instrument,null,null,java.math.BigDecimal(order.triggerPrice.toString))
        Try(exClass.tradeService.placeLimitOrder(newOrder)) match {
            case Failure(exception) => print(exception.getMessage); return false
            case Success(value) => println(value); return true
        }
    }

    def takeProfit(order : ORDER) : Unit = {
        
        val currentPrice : BigDecimal = getCurrentPrice(order)

        def checkTargetPrice() : Unit = {
            if order.targetPrice.compare(currentPrice) < 1
            then sqlManager.upsertOrder(order.copy(targetPrice = 0))
        }

        def checkTriggerPrice() : Unit = {
            if order.targetPrice.compare(currentPrice) > 0
            then {
                val value :Boolean = order.ismarket match {
                    case true => sellMarket(order)
                    case false => sellLimit(order)
                }
                if value == true then sqlManager.deleteOrder(order)
            }
        }

        if order.targetPrice.compare(0) == 0
        then checkTriggerPrice()
        else checkTargetPrice()
    }

    def lossCut(order : ORDER) : Unit = { 
        val currentPrice : BigDecimal = getCurrentPrice(order)
        if order.triggerPrice.compare(currentPrice) > 0
        then {
            val value : Boolean = order.ismarket match {
                case true => sellMarket(order)
                case false => sellLimit(order)
            }
            if value == true then sqlManager.deleteOrder(order)
        }
    }


    def buy(order:ORDER) : Unit = {
        val currentPrice : BigDecimal = getCurrentPrice(order)
        if order.triggerPrice.compare(currentPrice) > -1
        then {
            val value : Boolean = order.ismarket match {
                case true => buyMarket(order)
                case false => buyLimit(order)
            }
            if value == true then sqlManager.deleteOrder(order)
        }
    }
    
    def sell(order:ORDER) : Unit = {
        val currentPrice : BigDecimal = getCurrentPrice(order)
        if order.triggerPrice.compare(currentPrice) < 1
        then {
            val value : Boolean = order.ismarket match {
                case true => sellMarket(order)
                case false => sellLimit(order)
            }
            if value == true then sqlManager.deleteOrder(order)
        }
    }


    def tradeTasks() : Unit = {
        for order <- this.orders do {
            order.orderType match {
                case 0 => buy(order)
                case 1 => sell(order)
                case 2 => takeProfit(order)
                case 3 => lossCut(order)
                case _ => throw new Exception("Invalid order type while run the tradeTasks()")
            }
        }
    }

    

    def startUp(): Future[Unit] = Future{
        while true do {
            updateOrders()
            tradeTasks()
            Thread.sleep(5000)
        }
    }
}
