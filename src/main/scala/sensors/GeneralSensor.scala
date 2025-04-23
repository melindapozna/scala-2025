package sensors

import sttp.client4.Response
import sttp.client4.quick.*
import io.circe.*
import io.circe.parser.*

import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


trait GeneralSensor() {
  val plantId: Int
  val datasetId: Int      // 245: wind, 247: solar
  val currentTime: LocalDateTime = LocalDateTime.now()
  val LastMonthTime: LocalDateTime = currentTime.minusMonths(1)
  val apiKey: String = {
    val fileName = "apikey.txt"
    val bufferedSource = scala.io.Source.fromFile(fileName)
    val apiKey = bufferedSource.getLines().mkString.strip
    bufferedSource.close()
    apiKey
  }

  def requestData(datasetId: Int, startTime: String, endTime: String): Either[String, List[(String, Double)]] = {
    val response: Response[Either[String, String]] = basicRequest
      .get(uri"https://data.fingrid.fi/api/datasets/${datasetId}/data?startTime=${startTime}&endTime=${endTime}&format=json&pageSize=20000")
      .header("x-api-key", apiKey)
      .send()
    //println(response)

    response.body match {
      case Right(response) =>
        val parsedJson = parse(response).getOrElse(Json.Null)
        val timestamps = parsedJson.findAllByKey("endTime").flatMap(_.asString)
        val values = parsedJson.findAllByKey("value").map(_.toString.toDouble)
        //println(timestamps.zip(values).toMap)
        Right(timestamps.zip(values))
      case Left(err) =>
        val parsedJson = parse(err).getOrElse(Json.Null)
        val errorMessage = parsedJson.findAllByKey("message")
        Left(errorMessage.toString)
    }
  }
  
  def getLatest: Either[String, List[(String, Double)]]
  def writeToFile(dataRequesterFunction: Either[String, List[(String, Double)]]): Either[String, String]
  def readFromFile: Either[String, List[Double]]
  def getCurrentEnergy: Double
  def getStorageOccupancy: Double
  def getHealth: Int
}
