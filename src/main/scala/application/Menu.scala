package application

import plants.Plant

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import scala.annotation.tailrec
import scala.io.StdIn.readLine

// user interface for the entire plant
case object Menu {
  def start(): Unit = {
    Plant.start()
    println("Power plant started successfully.")
    mainMenu()
  }

  @tailrec
  private def mainMenu(): Unit = {
    println("\n-------------- REPS Power Plant --------------\n")
    println(
      """1. Check cameras
        |2. Check storage
        |3. Check and adjust current energy output
        |4. Analyse data
        |0. Exit""".stripMargin)
    val choice = readLine()

    choice match
      case "0" => Plant.shutdown()
      case "1" => checkCameras()
      case "2" => checkStorage()
      case "3" => checkCurrentEnergy()
      case "4" => startDataAnalysis()
      case _ =>
        println("Error: invalid input")
        mainMenu()
  }
  
  private def checkCameras(): Unit = {
    Plant.checkCameras()
    mainMenu()
  }
  
  @tailrec
  private def checkStorage(): Unit = {
    Plant.checkStorage()
    println(
      """1. Empty solar panel storage
        |2. Empty wind turbine storage
        |3. Empty hydro plant storage
        |0. Back""".stripMargin)
    val choice = readLine()
    choice match
      case "0" => mainMenu()
      case "1" =>
        printResult(Plant.emptyStorage("solar"))
        mainMenu()
      case "2" =>
        printResult(Plant.emptyStorage("wind"))
        mainMenu()
      case "3" =>
        printResult(Plant.emptyStorage("hydro"))
        mainMenu()
      case _ =>
        println("Invalid input")
        checkStorage()
  }

  @tailrec
  private def checkCurrentEnergy(): Unit = {
    Plant.checkCurrentEnergy()
    println(
      """1. Adjust solar panel angles
        |2. Adjust wind turbine angles
        |3. Adjust hydro turbine resistance
        |0. Back""".stripMargin)
    val choice = readLine()
    choice match
      case "0" => mainMenu()
      case "1" =>
        printResult(Plant.adjustParams("solar"))
        mainMenu()
      case "2" =>
        printResult(Plant.adjustParams("wind"))
        mainMenu()
      case "3" =>
        printResult(Plant.adjustParams("hydro"))
        mainMenu()
      case _ =>
        println("Invalid input")
        checkCurrentEnergy()
  }

  private def startDataAnalysis(): Unit = {
    val format = new SimpleDateFormat("yyyy-MM-dd HH:mm")
    val now = LocalDateTime.now()
    val earliestDate = now.minusMonths(1)
    val formattedEarliestDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(earliestDate)
    println("Enter the desired range in 'yyyy-MM-dd HH:mm' format.")
    println(s"Start date can be at most 1 month prior to the current time ($formattedEarliestDate).")
    val startDate = readLine("Start date:")
    try
      format.parse(startDate)
    catch
      case e: Exception =>
        println(s"Incorrect date format. Please enter the date as 'yyyy-MM-dd HH:mm. E.g.: $formattedEarliestDate")
        startDataAnalysis()

    val endDate = readLine("End date:")
    try
      format.parse(endDate)
    catch
      case e: Exception =>
        println(s"Incorrect date format. Please enter the date as 'yyyy-MM-dd HH:mm. E.g.: $formattedEarliestDate")
        startDataAnalysis()

    Plant.analyzeData(startDate, endDate)
    readLine("Press any key to continue:")
    mainMenu()
  }

  // prints the results of plant methods in a pretty way
  private def printResult(funcResult: Either[String, String]): Unit = {
    funcResult match
      case Left(funcResult) => println(s"Error: $funcResult")
      case Right(funcResult) => println(funcResult)
  }
}
