package dongwontuna.net.coinTradingSystem

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import dongwontuna.net.coinTradingSystem.types.EXCHANGE._
import dongwontuna.net.coinTradingSystem.types.DECIMAL_TYPE._
import dongwontuna.net.coinTradingSystem.types.EXCHANGE

object Trade {
  
    var orders: List[ORDER] = _
    
    def updateOrders(): Unit = {
        this.orders = sqlManager.getAllOrders()
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



    def takeProfit(order : ORDER) : Option[ORDER] = {
        var tempOrder = order.copy()

        def checkTriggerPrice() : Unit = {

        }

        def checkTargetPrice() : Unit = {
            
        }


    }

    def lossCut(order : ORDER) : Option[ORDER] = { 
        def checkTriggerPrice() : Unit = {

        }

        def checkTargetPrice() : Unit = {
            
        }
        var tempOrder = order.copy()

    }


    def buy(order:ORDER) : Option[ORDER] = {
        var tempOrder = order.copy()

        

    }
    
    def sell(order:ORDER) : Option[ORDER] = {
        var tempOrder = order.copy()

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
            Thread.sleep(1000)
        }
    }
}
