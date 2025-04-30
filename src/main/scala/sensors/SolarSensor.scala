package sensors

import sttp.client4.Response
import sttp.client4.quick.*
import io.circe.*
import io.circe.parser.*

import java.nio.file.Paths
import plants.SolarPanel

import java.io.{File, FileWriter}
import scala.io.Source


class SolarSensor(plantInstance: SolarPanel) extends GeneralSensor {
  // Constructor
  override val datasetId = 247
  override val plant: SolarPanel = plantInstance
  override val plantId: Int = plant.id
  var currentReading: (String, Double) = ("", 0.0)

  writeToFile(requestData(datasetId, LastMonthTime.toString, currentTime.toString))
  readFromFile("", "")
  plant.generateEnergy(currentReading._2)

  override def getLatest: Either[String, List[(String, Double)]] = {
    val response: Response[Either[String, String]] = basicRequest
      .get(uri"https://data.fingrid.fi/api/datasets/${datasetId}/data/latest")
      .header("x-api-key", apiKey)
      .send()

    response.body match {
      case Right(response) =>
        val parsedJson = parse(response).getOrElse(Json.Null)
        val timestamps = parsedJson.findAllByKey("endTime").flatMap(_.asString)
        val values = parsedJson.findAllByKey("value").map(_.toString.toDouble)
        val readings = timestamps.zip(values).reverse
        if (currentReading != readings.last) {
          currentReading = readings.last
          val fileWriter = new FileWriter(new File(s"data/solar/solar-$plantId.csv"), true)
          fileWriter.append(s"${currentReading._1};${currentReading._2}\n")
          fileWriter.close()
          plant.generateEnergy(currentReading._2)
          plant.updateStorage(currentReading._2)
        }
        Right(readings)
      case Left(err) =>
        val parsedJson = parse(err).getOrElse(Json.Null)
        val errorMessage = parsedJson.findAllByKey("message")
        Left(errorMessage.toString)
    }
  }


  override def writeToFile(dataRequesterFunction: Either[String, List[(String, Double)]]): Either[String, String] = {
    val data = dataRequesterFunction
    data match
      case Left(data) => Left("Error while receiving data")
      case Right(data) =>
        val filepath = s"data/solar/solar-$plantId.csv"
        val fileWriter = new FileWriter(new File(filepath))
        data.foreach(pair =>
          fileWriter.append(s"${pair._1};${pair._2}\n")
        )
        fileWriter.close()
        Right(s"Data written to $filepath")
  }

  override def readFromFile(startDate: String, endDate: String): Either[String, List[(String, Double)]] = {
    try
      val fileName = s"solar-$plantId.csv"
      val currentPath = Paths.get(System.getProperty("user.dir"))
      val filePath = Paths.get(currentPath.toString, "data", "solar", fileName).toString
      val fileSource = Source.fromFile(filePath)
      val lines = fileSource.getLines()
      val pairs = lines.flatMap(parseLine).toList
      fileSource.close()
      Right(pairs)
    catch
      case e: Exception => Left("Error while reading files.")



  }

  override def getCurrentEnergy: Double = plant.currentEnergy

  override def getStorageOccupancy: Double = plant.occupiedStorage / plant.storageCapacity * 100

  override def getHealth: Int = plant.health

}
