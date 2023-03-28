package dongwontuna.net.coinTradingSystem

import org.knowm.xchange.Exchange
import org.knowm.xchange.ExchangeFactory
import org.knowm.xchange.instrument.Instrument
import org.knowm.xchange.service.marketdata.MarketDataService
import org.knowm.xchange.service.account.AccountService
import org.knowm.xchange.service.trade.TradeService
import scala.collection.JavaConverters._
import org.knowm.xchange.dto.marketdata.Ticker
import org.knowm.xchange.dto.marketdata.OrderBook
import org.knowm.xchange.dto.marketdata.Trades


class AnExchange(exName: Class[_ <: Exchange]) {
  val Exchange: Exchange = ExchangeFactory.INSTANCE.createExchange(exName)
  val Name = Exchange.getExchangeSpecification().getExchangeName().toUpperCase()
  val marketDataService: MarketDataService = Exchange.getMarketDataService
  val accountService: AccountService = Exchange.getAccountService
  val tradeService: TradeService = Exchange.getTradeService
  val instruments: Map[String, Instrument] = Exchange.getExchangeInstruments().asScala.toList.map(instrument => instrument.toString -> instrument).toMap
  val defaultInstrument = Exchange.getExchangeInstruments().get(0)

  def getInstrument(ticker: String) : Option[Instrument] = {
    this.instruments.get(ticker) match {
      case None => None
      case Some(value) => Some(value)
    }
  }

  def getCurrentTicker(ticker: Instrument) : Ticker  = {
    marketDataService.getTicker(ticker)
  }

  def getCurrentOrderBook(ticker: Instrument) : OrderBook = {
    marketDataService.getOrderBook(ticker)
  }

  def getRecentTrades(ticker: Instrument) : Trades = {
    marketDataService.getTrades(ticker)
  }


}
