package plants

import scala.util.Random

class HydroPlant(instanceId: Int) extends GeneralPlant {
  //todo
  override val id: Int = instanceId
  override val maxHealth: Double = 100
  var health: Double = Random.between(50, 101) //initializes the instance with random health
  override val storageCapacity: Double = Random.between(80_000_000.0, 100_000_000.0)
  var occupiedStorage: Double = 0.0
  private var resistance: Int = 45
  var currentEnergy: Double = 0.0

  // Resistance is a percentage value that can be set between 0 and 100
  // The higher the resistance, the less flow of water and the less energy is generated
  def changeResistance(percent: Int): Either[String, Int] = {
    0 to 100 contains percent match
      case true =>
        resistance = percent
        Right(resistance)
      case false => Left("Error: invalid resistance")
  }

  override def damage(): Unit = {
    var newHealth = health
    newHealth -= Random.nextInt(10)
    if (newHealth >= 0) {
      health = newHealth
    }
  }

  override def generateEnergy(reading: Double): Unit = {
    currentEnergy = reading * resistance * Random.nextDouble() * 0.1
    damage()
  }

  override def calculateTakenStorage(readings: List[Double]): Unit = {
    occupiedStorage = readings.foldLeft(0.0)((x, y) => x + y)
  }

  override def updateStorage(reading: Double): Unit = {
    occupiedStorage += reading
  }
  // Added repair and clearStorage methods to the HydroPlant class
  def hydroRepair(): Unit = {
    health = maxHealth
  }

  override def clearStorage(): Unit = {
    occupiedStorage = 0.0
  }
}
