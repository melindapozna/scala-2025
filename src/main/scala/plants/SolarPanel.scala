package plants

import scala.util.Random

class SolarPanel(instanceId: Int) extends GeneralPlant {
  override val id: Int = instanceId
  
  // these attributes can be any numbers basically, up to your imagination and what makes sense based on the received data
  // a bit of randomness is introduced for more realistic looking results
  override val maxHealth: Int = 100
  var health: Int = Random.between(50, 101) //initializes the instance with random health
  override val storageCapacity: Double = Random.between(800_000.0, 1_000_000.0)
  var occupiedStorage: Double = 0.0
  private var panelAngle: Int = 90
  var currentEnergy: Double = 0.0

  
  // the user can set a new angle that changes how much energy is produced
  def changeAngle(angle: Int): Either[String, Int] = {
    0 to 180 contains angle match
      case true =>
        panelAngle = angle
        Right(panelAngle)
      case false => Left("Error: invalid angle")
  }

  // everytime energy is generated, some random damage is taken
  override def damage(): Unit = {
    health -= Random.nextInt(10)
  }

  override def generateEnergy(reading: Double): Unit = {
    currentEnergy = reading * panelAngle * Random.nextDouble() * 0.1
    damage()
  }
  
  
  
  override def calculateTakenStorage(readings: List[Double]): Unit = {
    occupiedStorage = readings.foldLeft(0.0)((x, y) => x + y)
  }
  
  override def updateStorage(reading: Double): Unit = {
    occupiedStorage += reading
  }
}
