package APIconnection

import sttp.client4.Response
import sttp.client4.quick.*
import io.circe._, io.circe.parser._

object APIconnection extends App {

  private val apiKey = {
    val fileName = "apikey.txt"
    val bufferedSource = scala.io.Source.fromFile(fileName)
    val apiKey= bufferedSource.getLines().mkString.strip
    bufferedSource.close()
    apiKey
  }

  //245 - wind
  //247 - solar

  def requestData(datasetId: Int, startTime: String, endTime: String): Either[String, List[Double]] = {
    val response: Response[Either[String, String]] = basicRequest
      .get(uri"https://data.fingrid.fi/api/datasets/${datasetId}/data?startTime=${startTime}&endTime=${endTime}&format=json&pageSize=100")
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

  def getLatest(datasetId: Int): Either[String, List[Double]] = {
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



  //test request
  //println(requestData(245, "2025-04-20T10:00:00", "2025-04-20T12:00:00"))
}
