package dongwontuna.net.coinTradingSystem

import scala.concurrent.ExecutionContext.Implicits.global
import slick.jdbc.SQLiteProfile.api._
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import slick.ast.ColumnOption.PrimaryKey
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import java.io.IOException
import concurrent.duration.DurationInt
import slick.lifted.ProvenShape
import java.time.LocalDate
import java.time.format.DateTimeFormatter

case class API(exchangeName: String, apiKey: String, secretKey: String)
class APIKEYs(tag: Tag) extends Table[API](tag, "APIKEY") {
  def exchangeName: Rep[String] = column[String]("EXCHANGE", O.PrimaryKey)
  def apiKey: Rep[String] = column[String]("API_KEY")
  def secretKey: Rep[String] = column[String]("SECRET_KEY")

  def * : ProvenShape[API] = (exchangeName, apiKey, secretKey).<>(
    (API.apply _).tupled,
    (api: API) => Some((api.exchangeName, api.apiKey, api.secretKey))
  )

}
case class DAILYDATA(
    date: Int,
    totalBalance: BigDecimal,
    tradeNum: Int,
    dailyPNL: BigDecimal
)
class DAILYDATAs(tag: Tag) extends Table[DAILYDATA](tag, "DAILYDATA") {
  def date: Rep[Int] = column[Int]("DATE", O.PrimaryKey)
  def totalBalance: Rep[BigDecimal] = column[BigDecimal]("TOTAL_BALANCE")
  def tradeNum: Rep[Int] = column[Int]("TRADE_NUM")
  def dailyPNL: Rep[BigDecimal] = column[BigDecimal]("TODAY_PNL")

  def * : ProvenShape[DAILYDATA] = (date, totalBalance, tradeNum, dailyPNL).<>(
    (DAILYDATA.apply _).tupled,
    (dailyData: DAILYDATA) =>
      Some(
        (
          dailyData.date,
          dailyData.totalBalance,
          dailyData.tradeNum,
          dailyData.dailyPNL
        )
      )
  )
}

case class ORDER(
    orderUUID: String,
    exchangeName: String,
    ticker: String,
    orderType: Int,
    triggerPrice: BigDecimal,
    targetPrice: BigDecimal,
    amount: BigDecimal,
    ismarket: Boolean
)
class ORDERs(tag: Tag)
    extends Table[
      ORDER
    ](tag, "ORDERS") {
  def orderUUID: Rep[String] = column[String]("ORDER_UUID", O.PrimaryKey, O.Unique)
  def exchangeName: Rep[String] = column[String]("EXCHANGE")
  def ticker: Rep[String] = column[String]("TICKER")
  def orderType: Rep[Int] = column[Int]("ORDER_TYPE")
  def triggerPrice: Rep[BigDecimal] = column[BigDecimal]("TRIGGER_PRICE")
  def targetPrice: Rep[BigDecimal] = column[BigDecimal]("TARGET_PRICE")
  def amount: Rep[BigDecimal] = column[BigDecimal]("AMOUNT")
  def ismarket: Rep[Boolean] = column[Boolean]("ISMARKET")

  def * : ProvenShape[ORDER] = (
    orderUUID,
    exchangeName,
    ticker,
    orderType,
    triggerPrice,
    targetPrice,
    amount,
    ismarket
  ).<>(
    (ORDER.apply _).tupled,
    (order: ORDER) =>
      Some(
        (
          order.orderUUID,
          order.exchangeName,
          order.ticker,
          order.orderType,
          order.triggerPrice,
          order.targetPrice,
          order.amount,
          order.ismarket
        )
      )
  )
}

// Declare the TableQuerys
val apiKeysTable = TableQuery(new APIKEYs(_))
val orderTable = TableQuery(new ORDERs(_))
val dailyDataTable = TableQuery(new DAILYDATAs(_))

object sqlManager {

  createFileIfNotExists()
  val db = Database.forURL(
    url = s"jdbc:sqlite:database.db",
    driver = "org.sqlite.JDBC"
  )

  def getAPIKEY(exName: String = "BINANCE"): Option[API] = {
    val sqlCommand = apiKeysTable.filter(_.exchangeName === exName).result
    Await.result(db.run(sqlCommand), 1.minute).headOption
  }

  def upsertAPIKEY(api: API): Boolean = {
    val sqlCommand = apiKeysTable.insertOrUpdate(api)
    Await.result(db.run(sqlCommand), 1.minute) > 0
  }

  def getOrders(exchangeName: String): Map[String,ORDER] = {
    val sqlCommand = orderTable.filter(_.exchangeName === exchangeName).result
    val result = Await.result(db.run(sqlCommand), 1.minute)
    result.zipWithIndex.map((item,index) => (index.toString -> item)).toMap
  }

  def getAllOrders(): List[ORDER] = {
    Await.result(db.run(orderTable.result),1.minute).toList
  }

  def upsertOrder(order: ORDER): Boolean = {
    val sqlCommand = orderTable.insertOrUpdate(order)
    Await.result(db.run(sqlCommand), 1.minute) > 0
  }

  def deleteOrder(order: ORDER): Boolean = {
    val sqlCommand = orderTable.filter(_.orderUUID === order.orderUUID).delete
    Await.result(db.run(sqlCommand), 1.minute) > 0
  }

  def getDailyData(date: LocalDate): DAILYDATA = {
    val dateString = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
    val sqlCommand = dailyDataTable.filter(_.date === dateString.toInt).result
    Await.result(db.run(sqlCommand), 1.minute).headOption match {
      case None => DAILYDATA(dateString.toInt, 0, 0, 0)
      case Some(value) => value
    }
  }

  def upsertDailyData(data: DAILYDATA): Boolean = {
    val sqlCommand = dailyDataTable.insertOrUpdate(data)
    Await.result(db.run(sqlCommand), 1.minute) > 0
  }

  def createFileIfNotExists(): Unit = {
    var targetDestination = new File("./database.db")
    var sourceDestination = getClass.getClassLoader.getResourceAsStream("data.db");
    if !targetDestination.exists() then {
      Files.copy(sourceDestination, Paths.get("database.db"))
    }
  }
}
