package plants

import scala.util.Random

class SolarPanel(instanceId: Int) extends GeneralPlant {
  override val id: Int = instanceId
  override val maxHealth: Int = 100
  var health: Int = Random.between(50, 101) //initializes the instance with random health
  override val storageCapacity: Double = 99_999_999.0
  var occupiedStorage: Double = 10.0
  private var panelAngle: Double = 90.0

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
}
