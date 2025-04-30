package plants

import application.IdProvider
import sensors.SolarSensor

import java.util.Timer
import java.util.TimerTask
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

  private val windTurbines = List[WindTurbine]()
  private val hydroPlants = List[HydroPlant]()

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
        windTurbines.foreach(_.clearStorage())
        Right("Solar panel storages cleared successfully.")
      case "hydro" =>
        hydroPlants.foreach(_.clearStorage())
        Right("Solar panel storages cleared successfully.")
      case _ =>
        Left("Invalid plant type when trying to clear storage.")
  }

  def checkCurrentEnergy(): Unit = {
    val solarEnergies = solarSensors.foldLeft("")((currStr, sensor) =>
      currStr.concat(f"Solar panel #${sensor.plantId}: ${sensor.getCurrentEnergy}%.2f%%\n"))
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
            windTurbines.foreach(_.changeAngle(angle))
            Right("Wind turbine angles set successfully.")
          case None =>
            Left("Invalid input. Wind turbine angles have not been set.")

      case "hydro" =>
        println("New hydro plant resistance (integer, 0-10): ")
        val angle = readLine()
        toInt(angle) match
          case Some(angle) =>
            hydroPlants.foreach(_.changeResistance(angle))
            Right("Hydro plant resistances set successfully.")
          case None =>
            Left("Invalid input. Hydro plant resistances have not been set.")
      case _ => Left("Invalid plant type when trying to adjust plant parameters.")
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
