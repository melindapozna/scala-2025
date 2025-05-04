package sensors

import sttp.client4.Response
import sttp.client4.quick.*
import io.circe.*
import io.circe.parser.*

import java.nio.file.Paths
import plants.{GeneralPlant, HydroPlant}

import java.io.{File, FileWriter}
import scala.io.Source

class HydroSensor(plantInstance: HydroPlant) extends GeneralSensor {
  override val plant: GeneralPlant = plantInstance
  override val plantId: Int = plant.id
  override val datasetId: Int = 191 
  var currentReading: (String, Double) = ("", 0.0)
  
  writeToFile(requestData(datasetId, LastMonthTime.toString, currentTime.toString))
  plant.generateEnergy(currentReading._2)

  override def getLatest: Either[String, List[(String, Double)]] = {
    val response: Response[Either[String, String]] = basicRequest
      .get(uri"https://data.fingrid.fi/api/datasets/${datasetId}/data/latest") // Ensure this endpoint is for hydro data
      .header("x-api-key", apiKey)
      .send()

    response.body match {
      case Right(response) =>
        val parsedJson = parse(response).getOrElse(Json.Null)
        val timestamps = parsedJson.findAllByKey("endTime").flatMap(_.asString)
        val values = parsedJson.findAllByKey("value").map(_.toString.toDouble)
        val readings = timestamps.zip(values).reverse
        if (readings.nonEmpty && currentReading != readings.last) {
          currentReading = readings.last
          val fileWriter = new FileWriter(new File(s"data/hydro/hydro-$plantId.csv"), true)
          fileWriter.append(s"${currentReading._1};${currentReading._2}\n")
          fileWriter.close()
          plant.generateEnergy(currentReading._2)
          plant.updateStorage(currentReading._2)
        }
        Right(readings)
      case Left(err) =>
        val parsedJson = parse(err).getOrElse(Json.Null)
        val errorMessage = parsedJson.findAllByKey("message")
        Left(errorMessage.mkString(", "))
    } 
  }

  override def writeToFile(dataRequesterFunction: Either[String, List[(String, Double)]]): Either[String, String] = {
    val data = dataRequesterFunction
    data match {
      case Left(data) => Left("Error while receiving data")
      case Right(data) =>
        val filepath = s"data/hydro/hydro-$plantId.csv"
        val fileWriter = new FileWriter(new File(filepath))
        data.foreach { case (timestamp, value) =>
          fileWriter.append(s"$timestamp;$value\n")
        }
        fileWriter.close()
        Right("Data successfully written to file.")
    }
  }

  override def readFromFile(startDate: String, endDate: String): Either[String, List[(String, Double)]] = {
    try
      val fileName = s"hydro-$plantId.csv"
      val currentPath = Paths.get(System.getProperty("user.dir"))
      val filePath = Paths.get(currentPath.toString, "data", "hydro", fileName).toString
      val fileSource = Source.fromFile(filePath)
      val lines = fileSource.getLines()
      val pairs = lines.flatMap(parseLine).toList
      fileSource.close()

      (userInputToDateTime(startDate), userInputToDateTime(endDate)) match
        case (Some(start), Some(end))  =>
          val goodPairs = pairs.filter(pair => pair._1.after(start) && pair._1.before(end))
          val formattedPairs = goodPairs.map(pair => (pair._1.toString, pair._2))
          //println(goodValues)
          Right(formattedPairs)
        case _ =>
          Left("Invalid user input.")
    catch
      case e: Exception => Left("Error while reading files.")
  }
}
