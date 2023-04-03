package dongwontuna.net.coinTradingSystem.CLI

import dongwontuna.net.coinTradingSystem.AnExchange
import scala.io.StdIn
import org.knowm.xchange.dto.account.Wallet
import scala.jdk.CollectionConverters._
import scala.collection.JavaConverters.asScala

object AccountService {

  var exClass: AnExchange = _
  var exName: String = _
  
        
    def initialize(exClass: AnExchange): Unit = {
        this.exClass = exClass
        this.exName = exClass.Name
    }

    def getAccountBalance(): Unit = {
        clearTerminal()
        val walletData : Wallet = exClass.accountService.getAccountInfo().getWallet()
        val balanceHeadString = "\n| Currency | Avilable Balance | Frozen balance | Loaned Balance | Withdrawing Balance | Depositing Balance |"

        val balanceString = walletData.balances().asScala.toList.map(item => s"||${StringFormat.padMiddle(item.getCurrency().toString(),9)}|${StringFormat.padLeft(item.getAvailable().toString,18)}|${StringFormat.padLeft(item.getFrozen().toString,16)}|${StringFormat.padLeft(item.getLoaned().toString,16)}|${StringFormat.padLeft(item.getWithdrawing().toString,21)}|${StringFormat.padLeft(item.getDepositing().toString,20)}|").mkString("\n")
        println(s"""${StringFormat.makeMenuString("Wallet Balance",balanceHeadString)}
        |
        $balanceString
        |""".stripMargin)
        println("\n\nPress the Enter key to Exit")
        StdIn.readLine()
    }
}
