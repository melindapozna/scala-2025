package APIconnection

import sttp.client4.Response
import sttp.client4.quick.*

object APIconnection extends App{
  //send api request

  //247 - solar

  def requestData(datasetId: Int, startTime: String, endTime: String): Unit = {

    val fileName = "apikey.txt"
    val bufferedSource = scala.io.Source.fromFile(fileName)
    val apiKey= bufferedSource.getLines().mkString.strip
    bufferedSource.close()
    println(apiKey)

    val response: Response[Either[String, String]] = basicRequest
      .get(uri"https://data.fingrid.fi/api/datasets/${datasetId}/data?startTime=${startTime}&endTime=${endTime}&format=json&pageSize=100")
      .header("x-api-key", apiKey)
      .send()

    val parsedResponse = response.body match {
      case Right(response) => println(response)
      case Left(err) => println(s"Invalid request : $err")
    }
  }
  //test request
  //requestData(247, "2025-04-20T10:00:00", "2025-04-20T12:00:00")
}
