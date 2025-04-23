package sensors

import sttp.client4.Response
import sttp.client4.quick.*
import io.circe.*
import io.circe.parser.*

class SolarSensor(plantInstanceId: Int) extends GeneralSensor {
  // Constructor
  override val datasetId = 247
  override val plantId: Int = plantInstanceId

  requestData(datasetId, LastMonthTime.toString, currentTime.toString)
  
  //todo implement these

  override def getLatest: Either[String, List[Double]] = {
    val response: Response[Either[String, String]] = basicRequest
      .get(uri"https://data.fingrid.fi/api/datasets/${datasetId}/data/latest")
      .header("x-api-key", apiKey)
      .send()

    response.body match {
      case Right(response) =>
        val parsedJson = parse(response).getOrElse(Json.Null)
        val values = parsedJson.findAllByKey("value")
        Right(values.map(item => item.toString.toDouble))
      case Left(err) =>
        val parsedJson = parse(err).getOrElse(Json.Null)
        val errorMessage = parsedJson.findAllByKey("message")
        Left(errorMessage.toString)
    }
  }


  override def writeToFile(): Unit = ???

  override def readFromFile: Either[String, List[Double]] = ???

  override def getCurrentEnergy: Double = ???

  override def getStorageOccupancy: Double = ???

  override def getHealth: Int = ???

}
