package plants

import application.IdProvider
import sensors.SolarSensor

import java.util.Timer
import java.util.TimerTask


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
  
  // starting a timer
  private val timer = new Timer()

  private def getPlantId(plant: GeneralPlant): Int = {
    plant.id
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
