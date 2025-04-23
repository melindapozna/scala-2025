package sensors

import sttp.client4.Response
import sttp.client4.quick.*
import io.circe.*
import io.circe.parser.*

class WindSensor(plantInstanceId: Int) extends GeneralSensor {

  override val datasetId: Int = 245
  override val plantId: Int = plantInstanceId
  
  requestData(datasetId, LastMonthTime.toString, currentTime.toString)

  //todo implement these

  override def getLatest: Either[String, List[(String, Double)]] = {
    val response: Response[Either[String, String]] = basicRequest
      .get(uri"https://data.fingrid.fi/api/datasets/${datasetId}/data/latest")
      .header("x-api-key", apiKey)
      .send()

    response.body match {
      case Right(response) =>
        val parsedJson = parse(response).getOrElse(Json.Null)
        val timestamps = parsedJson.findAllByKey("endTime").map(_.toString)
        val values = parsedJson.findAllByKey("value").map(_.toString.toDouble)
        Right(timestamps.zip(values))
      case Left(err) =>
        val parsedJson = parse(err).getOrElse(Json.Null)
        val errorMessage = parsedJson.findAllByKey("message")
        Left(errorMessage.toString)
    }
  }
  
  override def writeToFile(dataRequesterFunction: Either[String, List[(String, Double)]]): Either[String, String] = ???

  override def readFromFile: Either[String, List[Double]] = ???

  override def getCurrentEnergy: Double = ???

  override def getStorageOccupancy: Double = ???

  override def getHealth: Int = ???
}
