package dongwontuna.net.coinTradingSystem.CLI

import dongwontuna.net.coinTradingSystem.ORDER

object StringFormat {
  def makeMenuString(exName: String, menuText: String, thirdText: String = ""): String = {
    s"=--- ${exName.toLowerCase.capitalize} $menuText".padTo(72, "-").mkString("") + "=" + thirdText
  }
  
  def makeOrderString(orders: List[ORDER]): String = {
    if (orders.isEmpty) {
      return """|||                                                                       |
                |||                                  NONE                                 |
                |||                                                                       |
                |=-----------------------------------------------------------------------=""".stripMargin
    }

    val formattedOrders = orders.zipWithIndex.map { case (order, index) =>
      val orderType = order.orderType match {
        case 0 => "Buy"
        case 1 => "Sell"
        case 2 => "TakeP"
        case 3 => "LossC"
      }

      s"||${padMiddle(index.toString, 5)}|${padMiddle(order.ticker, 10)}|${padMiddle(orderType, 8)}|${padLeft(order.triggerPrice.toString, 16)}|${padLeft(order.targetPrice.toString, 15)}|${padLeft(order.amount.toString, 12)}|"
    }

    formattedOrders.mkString("\n=-----------------------------------------------------------------------=\n") + "\n=-----------------------------------------------------------------------="
  }

  def padLeft(s: String, num: Int): String = {
    s.reverse.padTo(num, ' ').reverse
  }
  def padMiddle(s: String, num: Int): String = {
    val diff = num - s.length
    if diff % 2 == 0 then (" " * (diff / 2)) + s + (" " * (diff / 2))
    else " " * ((diff - 1) / 2) + s + " " * ((diff - 1) / 2 + 1)
  }
}
