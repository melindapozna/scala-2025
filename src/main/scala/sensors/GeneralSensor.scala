package sensors

import sttp.client4.Response
import sttp.client4.quick.*
import io.circe.*
import io.circe.parser.*
import plants.GeneralPlant

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date

// all sensors inherit this trait
trait GeneralSensor() {
  val plant: GeneralPlant
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
  var currentReading: (String, Double)
  
  // requests data from a time period from Fingrid API
  // also calls generateEnergy and calculateTakenStorage for its plant instance
  // returns the readings as a list of tuples (timestamp, value)
  def requestData(datasetId: Int, startTime: String, endTime: String): Either[String, List[(String, Double)]] = {
    //wait needed due to API request limit
    Thread.sleep(2000)
    val response: Response[Either[String, String]] = basicRequest
      .get(uri"https://data.fingrid.fi/api/datasets/${datasetId}/data?startTime=${startTime}&endTime=${endTime}&format=json&pageSize=20000")
      .header("x-api-key", apiKey)
      .send()

    response.body match {
      case Right(response) =>
        val parsedJson = parse(response).getOrElse(Json.Null)
        val timestamps = parsedJson.findAllByKey("endTime").flatMap(_.asString)
        val values = parsedJson.findAllByKey("value").map(_.toString.toDouble)
        val readings = timestamps.zip(values).reverse
        currentReading = readings.last
        plant.generateEnergy(currentReading._2)
        plant.calculateTakenStorage(values)
        Right(readings)
      case Left(err) =>
        val parsedJson = parse(err).getOrElse(Json.Null)
        val errorMessage = parsedJson.findAllByKey("message")
        Left(errorMessage.toString)
    }
  }
  
  def parseLine(line: String): Option[(Date, Double)] = {
    val splitLine = line.split(";")
    splitLine match
      case Array(time, num) =>
        val dFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        Some((dFormat.parse(time), num.toDouble))
      case _ => None
  }

  def userInputToDateTime(userInput: String): Option[Date] = {
    val dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm")
    try
      Some(dFormat.parse(userInput))
    catch
      case e: Exception => None
  }
  
  // gets the latest reading from Fingrid (after every 15 minutes)
  // appends the latest reading to the corresponding file
  def getLatest: Either[String, List[(String, Double)]]
  
  // writes the initially received readings to a file
  def writeToFile(dataRequesterFunction: Either[String, List[(String, Double)]]): Either[String, String]
  
  // not implemented yet,
  // should read a specific time period from the file e.g. 20-27th April
  def readFromFile(startDate: String, endDate: String): Either[String, List[Double]]
  
  // returns the current energy that the plant instance is generating (user + plant communicating through sensors)
  def getCurrentEnergy: Double
  
  // returns a % of taken storage
  def getStorageOccupancy: Double
  
  // returns the current health of the plant
  def getHealth: Int
}
