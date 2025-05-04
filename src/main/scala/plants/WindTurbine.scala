package plants

import scala.util.Random

class WindTurbine(instanceId: Int) extends GeneralPlant {
  override val id: Int = instanceId
  override val maxHealth: Double = 100
  var health: Double = Random.between(50, 101)
  override val storageCapacity: Double = Random.between(8_000_000.0, 10_000_000.0)
  var occupiedStorage: Double = 0.0
  var currentEnergy: Double = 0.0
  private var turbineGear: Int = 6
  
  // the user can set a new gear that changes the amount of force needed it to turbine, like a bike gear
  // the higher the gear, the more energy is produced, but also the more damage is taken
  // the gear can be set between 1 and 10, where 1 is the lowest and 10 is the highest
  def changeGear(gear: Int): Either[String, Int] = {
    1 to 10 contains gear match
      case true =>
        turbineGear = gear
        Right(turbineGear)
      case false => Left("Error: invalid gear")
  }

  // the seemingly random numbers in the following methods are to make the results more "random" / "lifelike"
  override def damage(): Unit = {
    var newHealth = health
    newHealth -= Random.nextInt(10) * (turbineGear / 10.0) // damage is proportional to the gear
    if (newHealth >= 0) {
      health = newHealth
    }
  }
  
  override def generateEnergy(reading: Double): Unit = {
    currentEnergy = reading * turbineGear * Random.nextDouble() * 0.15
    damage()
  }

  override def calculateTakenStorage(readings: List[Double]): Unit = {
    occupiedStorage = readings.foldLeft(0.0)((x, y) => x + (y * 0.99))
  }

  override def updateStorage(reading: Double): Unit = {
    occupiedStorage += (reading * 0.99)
  }
}
