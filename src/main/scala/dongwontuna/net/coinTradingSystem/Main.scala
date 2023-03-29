package dongwontuna.net.coinTradingSystem

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.Success
import scala.util.Failure
import org.knowm.xchange.binance.BinanceExchange
import org.knowm.xchange.gateio.GateioExchange
import org.knowm.xchange.huobi.HuobiExchange
import org.knowm.xchange.kucoin.KucoinExchange

import scala.collection.mutable.ListBuffer
import org.knowm.xchange.Exchange
import org.knowm.xchange.instrument.Instrument

import java.util.Date
import java.io.IOException
import scala.io._
import dongwontuna.net.coinTradingSystem.sqlManager
import concurrent.duration.DurationInt
import dongwontuna.net.coinTradingSystem.CLI._
import dongwontuna.net.coinTradingSystem.types.EXCHANGE._

import dongwontuna.net.coinTradingSystem.AnExchange

import dongwontuna.net.coinTradingSystem.types.EXCHANGE
object Main {

  val Exchanges: Map[EXCHANGE, AnExchange] = initialize()

  def main(args: Array[String]): Unit = {
    //Trade.startUp()
    CLIMenu.mainPage()
  }

  def runWithScalaFX(): Unit = {
    Trade.startUp()
  }

  def initialize(): Map[EXCHANGE, AnExchange] = {

    def newAnExchange(className: Class[_ <: Exchange]): Future[AnExchange] =
      Future {
        new AnExchange(className)
      }

    val exchangeNames: Map[EXCHANGE, Future[AnExchange]] = Map(
      BINANCE -> newAnExchange(classOf[BinanceExchange]),
      GATEIO -> newAnExchange(classOf[GateioExchange]),
      HUOBI -> newAnExchange(classOf[HuobiExchange]),
      KUCOIN -> newAnExchange(classOf[KucoinExchange])
    )

    val exchanges =
      Future.sequence(exchangeNames.values.toList).map { anExchanges =>
        exchangeNames.keys.zip(anExchanges).toMap
      }

    Await.result(exchanges, 1.minute)
  }
}
