package sensors

import sttp.client4.Response
import sttp.client4.quick.*
import io.circe.*
import io.circe.parser.*
import java.io.*

class SolarSensor(plantInstanceId: Int) extends GeneralSensor {
  // Constructor
  override val datasetId = 247
  override val plantId: Int = plantInstanceId

  writeToFile(requestData(datasetId, LastMonthTime.toString, currentTime.toString))
  
  //todo implement these

  override def getLatest: Either[String, Map[String, Double]] = {
    val response: Response[Either[String, String]] = basicRequest
      .get(uri"https://data.fingrid.fi/api/datasets/${datasetId}/data/latest")
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


  override def writeToFile(dataRequesterFunction: Either[String, Map[String, Double]]): Either[String, String] = {
    val data = dataRequesterFunction
    data match
      case Left(data) => Left("Error while receiving data")
      case Right(data) =>
        val fileWriter = new FileWriter(new File(s"data/solar/solar-$plantId.csv"))
        data.foreach(pair =>
          fileWriter.append(s"${pair._1};${pair._2}\n")
        )
        fileWriter.close()
        Right("done")
  }

  override def readFromFile: Either[String, List[Double]] = ???

  override def getCurrentEnergy: Double = ???

  override def getStorageOccupancy: Double = ???

  override def getHealth: Int = ???

}
