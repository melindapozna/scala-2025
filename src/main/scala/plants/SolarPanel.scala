package plants

import scala.util.Random

class SolarPanel(instanceId: Int) extends GeneralPlant {
  override val id: Int = instanceId
  override val maxHealth: Int = 100
  var health: Int = Random.between(50, 101) //initializes the instance with random health
  override val storageCapacity: Double = Random.between(800_000.0, 1_000_000.0)
  var occupiedStorage: Double = 0.0
  private var panelAngle: Double = 90.0
  var currentEnergy: Double = 0.0

  override def changeAngle(angle: Double): Either[String, Double] = {
    0 to 181 contains angle match
      case true =>
        panelAngle = angle
        Right(panelAngle)
      case false => Left("Error: invalid angle")
  }

  override def damage(): Unit = {
    health -= Random.nextInt(10)
  }

  override def generateEnergy(reading: Double): Unit = {
    currentEnergy = reading * panelAngle * Random.nextDouble() * 0.1
  }

  override def calculateTakenStorage(readings: List[Double]): Unit = {
    occupiedStorage = readings.foldLeft(0.0)((x, y) => x + y)
  }
  
  override def updateStorage(reading: Double): Unit = {
    occupiedStorage += reading
  }
}
