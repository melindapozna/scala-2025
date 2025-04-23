package sensors

import sttp.client4.Response
import sttp.client4.quick.*
import io.circe.*
import io.circe.parser.*
import java.time.LocalDateTime


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

  def requestData(datasetId: Int, startTime: String, endTime: String): Either[String, Map[String, Double]] = {
    val response: Response[Either[String, String]] = basicRequest
      .get(uri"https://data.fingrid.fi/api/datasets/${datasetId}/data?startTime=${startTime}&endTime=${endTime}&format=json&pageSize=20000")
      .header("x-api-key", apiKey)
      .send()

    response.body match {
      case Right(response) =>
        val parsedJson = parse(response).getOrElse(Json.Null)
        val timestamps = parsedJson.findAllByKey("endTime").map(_.toString)
        val values = parsedJson.findAllByKey("value").map(_.toString.toDouble)
        Right(timestamps.zip(values).toMap)
      case Left(err) =>
        val parsedJson = parse(err).getOrElse(Json.Null)
        val errorMessage = parsedJson.findAllByKey("message")
        Left(errorMessage.toString)
    }
  }
  
  def getLatest: Either[String, Map[String, Double]]
  def writeToFile(dataRequesterFunction: Either[String, Map[String, Double]]): Either[String, String]
  def readFromFile: Either[String, List[Double]]
  def getCurrentEnergy: Double
  def getStorageOccupancy: Double
  def getHealth: Int
}
