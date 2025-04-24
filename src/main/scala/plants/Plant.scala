package plants

import application.IdProvider
import sensors.SolarSensor

import java.util.Timer
import java.util.TimerTask

case object Plant {
  private val solar1 = new SolarPanel(IdProvider.setNextId())
  private val solar1Sensor = new SolarSensor(solar1)
  
  private val solar2 = new SolarPanel(IdProvider.setNextId())
  private val solar2Sensor = new SolarSensor(solar2)

  private val solarPanels = List(solar1, solar2)
  private val solarSensors = List(solar1Sensor, solar2Sensor)

  private val timer = new Timer()

  private def getPlantId(plant: GeneralPlant): Int = {
    plant.id
  }

  def checkStorage(): Unit = {
    val solarStorages = solarSensors.foldLeft("")((currStr, sensor) =>
      currStr.concat(f"Solar panel #${sensor.plantId}: ${sensor.getStorageOccupancy}%.2f%%\n"))
    println(solarStorages)
  }

  def checkCameras(): Unit = {
    println("Checking cameras...")
    Thread.sleep(3000)
  }

  def start(): Unit = {
    val getNewSensorData = new TimerTask {
      def run(): Unit = {
        solarSensors.foreach(_.getLatest)
      }
    }
    timer.schedule(getNewSensorData, 1000, 900000)
  }

  def shutdown(): Unit = {
    timer.cancel()
    timer.purge()
  }


}
