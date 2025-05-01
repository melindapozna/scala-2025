package plants

import application.IdProvider
import sensors.*
import dataAnalysis.DataAnalysis

import java.util.Timer
import java.util.TimerTask
import scala.collection.immutable.HashMap
import scala.io.StdIn.readLine


// initializes the REPS power plant
case object Plant {
  
  // initializing solar panels with corresponding sensors
  // the id provider provides ids incrementally
  // you need to pass the plant instance as a parameter to the sensor
  private val solar1 = new SolarPanel(IdProvider.setNextId())
  private val solar1Sensor = new SolarSensor(solar1)
  
  private val solar2 = new SolarPanel(IdProvider.setNextId())
  private val solar2Sensor = new SolarSensor(solar2)
  
  // a list of solar panels and sensors, for easier handling
  private val solarPanels = List(solar1, solar2)
  private val solarSensors = List(solar1Sensor, solar2Sensor)

  /*private val wind1 = new WindTurbine(IdProvider.setNextId())
  private val wind1Sensor = new WindSensor(wind1)

  private val windTurbines = List[WindTurbine](wind1)
  private val windSensors = List[WindSensor](wind1Sensor)

  private val hydroPlants = List[HydroPlant]()
  private val hydroSensors = List[HydroSensor]()
*/
  // starting a timer
  private val timer = new Timer()

  private def getPlantId(plant: GeneralPlant): Int = {
    plant.id
  }

  private def toInt(s: String): Option[Int] = {
    try {
      Some(Integer.parseInt(s.trim))
    } catch {
      case e: Exception => None
    }
  }
  
  // prints every object's storage capacity in %
  def checkStorage(): Unit = {
    val solarStorages = solarSensors.foldLeft("")((currStr, sensor) =>
      currStr.concat(f"Solar panel #${sensor.plantId}: ${sensor.getStorageOccupancy}%.2f%%\n"))
    println(solarStorages)
  }
  
  def checkCameras(): Unit = {
    println("Checking cameras...")
    Thread.sleep(3000)
  }

  def emptyStorage(plantType: String): Either[String, String] = {
    plantType match
      case "solar" =>
        solarPanels.foreach(_.clearStorage())
        Right("Solar panel storages cleared successfully.")
      case "wind" =>
        //windTurbines.foreach(_.clearStorage())
        Right("Solar panel storages cleared successfully.")
      case "hydro" =>
        //hydroPlants.foreach(_.clearStorage())
        Right("Solar panel storages cleared successfully.")
      case _ =>
        Left("Invalid plant type when trying to clear storage.")
  }

  def checkCurrentEnergy(): Unit = {
    val solarEnergies = solarSensors.foldLeft("")((currStr, sensor) =>
      currStr.concat(f"Solar panel #${sensor.plantId}: ${sensor.getCurrentEnergy}%.2f MWh/h\n"))
    println(solarEnergies)
  }

  def adjustParams(plantType: String): Either[String, String] = {
    plantType match
      case "solar" =>
        println("New solar panel angle (integer, 0-180): ")
        val angle = readLine()
        toInt(angle) match
          case Some(angle) =>
            if 0 to 180 contains angle then
              solarPanels.foreach(_.changeAngle(angle))
              Right("Solar panel angles set successfully.")
            else
              Left("Invalid input.")
          case None =>
            Left("Invalid input. Solar panel angles have not been set.")
            
      case "wind" =>
        println("New wind turbine angle (integer, 0-90): ")
        val angle = readLine()
        toInt(angle) match
          case Some(angle) =>
            //windTurbines.foreach(_.changeAngle(angle))
            Right("Wind turbine angles set successfully.")
          case None =>
            Left("Invalid input. Wind turbine angles have not been set.")

      case "hydro" =>
        println("New hydro plant resistance (integer, 0-10): ")
        val angle = readLine()
        toInt(angle) match
          case Some(angle) =>
            //hydroPlants.foreach(_.changeResistance(angle))
            Right("Hydro plant resistances set successfully.")
          case None =>
            Left("Invalid input. Hydro plant resistances have not been set.")
      case _ => Left("Invalid plant type when trying to adjust plant parameters.")
  }

  def analyzeData(startDate: String, endDate: String): Unit = {
    val solarReadings = solarSensors.map(
      _.readFromFile(startDate, endDate) match
        case Right(readings) => readings
        case Left(error) => Nil
    )
    val resultsWithSensors = solarReadings.map(dataAnalysisHelper).zip(solarSensors)
    prettyPrintDataAnalysis(resultsWithSensors, "Solar Panel")

  }

  private def dataAnalysisHelper(readings: List[Double]): HashMap[String, Double] = {
    val mean = DataAnalysis.mean(readings)
    val median = DataAnalysis.median(readings)
    val mode = DataAnalysis.mode(readings)
    val range = DataAnalysis.range(readings)
    val midrange = DataAnalysis.midrange(readings)
    HashMap(
      "Mean" -> mean,
      "Median" -> median,
      "Mode" -> mode,
      "Range" -> range,
      "Midrange" -> midrange
    )
  }

  private def prettyPrintDataAnalysis(readingsWithSensor: List[(HashMap[String, Double], GeneralSensor)], plantType: String): Unit = {
    readingsWithSensor.foreach(tuple =>
      val results = tuple._1
      println(s"\n$plantType ${tuple._2.plantId}.:")
      results.foreach(pair =>
      println(f"${pair._1}: ${pair._2}%.2f"))
    )
  }

  // every 15 minutes (the API is updated that frequently), it gets the new reading
  def start(): Unit = {
    val getNewSensorData = new TimerTask {
      def run(): Unit = {
        solarSensors.foreach(_.getLatest)
      }
    }
    timer.schedule(getNewSensorData, 1000, 900000)
  }
  
  // cancels the timer
  def shutdown(): Unit = {
    timer.cancel()
    timer.purge()
  }


}
