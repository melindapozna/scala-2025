package application

import plants.Plant

import scala.annotation.tailrec
import scala.io.StdIn.readLine

import application.FunctorAid._
import application.FunctorInstances._
import sensors.SolarSensor
import plants.SolarPanel


// user interface for the entire plant
case object Menu {
  def start(): Unit = {
    Plant.start()
    println("Power plant started successfully.")
    mainMenu()
  }
  
  private def mainMenu(): Unit = {
    println("\n-------------- REPS Power Plant --------------\n")
    println(
      """1. Check cameras
        |2. Check storage
        |3. Check current energy output
        |4. Analyse data
        |5. Adjust operation parameters
        |0. Exit""".stripMargin)
    val choice = readLine()

    choice match
      case "0" => Plant.shutdown()
      case "1" => checkCameras()
      case "2" => checkStorage()
      case "3" => println("IMPLEMENT")        //todo implement
      case "4" => analyseSolarData() //Functor
      case "5" => plantTypeMenu()
      case _ => println("Error: invalid input")

  }
  
  // this will be refactored, so don't worry about it for now
  private def plantTypeMenu(): Unit = {
    println(
      """1. Solar Panels
        |2. Wind Turbines
        |3. Hydro Plants
        |0. Back""".stripMargin)
    val choice = readLine()

    choice match
      case "0" => mainMenu()
      case "1" => println("IMPLEMENT")        //todo implement
      case "2" => println("IMPLEMENT")        //todo implement
      case "3" => println("IMPLEMENT")        //todo implement
      case _ => println("Error: invalid input")

  }
  
  private def checkCameras(): Unit = {
    Plant.checkCameras()
    mainMenu()
  }
  
  @tailrec
  private def checkStorage(): Unit = {
    Plant.checkStorage()
    println("0. Back")
    val choice = readLine()
    choice match
      case "0" => mainMenu()
      case _ => 
        println("Invalid input")
        checkStorage()
  }

 //Analyze solar data working with the functor
private def analyseSolarData(): Unit = {
  //informing the user that the fetching and analyzing process started :)
  println("Fetching and analyzing solar panel data.....")

  //passing into the solar sensor
  val plant = new SolarPanel(id = 1) //solar panel ID
  //attach to a plant
  val sensor = new SolarSensor(plant)
  //data request to sensor
  val data = sensor.requestData(sensor.datasetId, sensor.LastMonthTime.toString, sensor.currentTime.toString)


  //Functor aid fmap
  val boostedData = fmap(data) { readings =>
    readings.map { case (timestamp, value) =>
      (timestamp, value * 1.1) //energy production boosted by 10%
    }
  }

  
  
  //results and their handling
  boostedData match {
    case Right(readings) =>
      println("\nBoosted Solar Readings:")

      readings.foreach { case (timestamp, value) =>
        println(s"$timestamp => $value kWh") //val in kWh
      }

    case Left(error) =>
      println(s"Error fetching sensor data: $error") //error statement
  }

  mainMenu() //back to menu function
}

}