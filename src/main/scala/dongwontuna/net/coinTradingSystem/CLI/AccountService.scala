package dongwontuna.net.coinTradingSystem.CLI

import dongwontuna.net.coinTradingSystem.AnExchange

object AccountService {

  var exClass: AnExchange = _
  var exName: String = _
        
    def initialize(exClass: AnExchange): Unit = {
        this.exClass = exClass
        this.exName = exClass.Name
    }

    def getAccountBalance(): Unit = {
        
    }
}
