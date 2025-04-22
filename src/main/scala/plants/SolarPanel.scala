package plants

import scala.util.Random

class SolarPanel(instanceId: Int) extends GeneralPlant {
  //initialize it with real values
  override val id: Int = instanceId
  override val maxHealth: Int = 100
  var health: Int = Random.between(50, 101) //initializes the instance with random health
  override val storageCapacity: Double = ???
  var occupiedStorage: Double = ???

  override def changeAngle(angle: Double): Option[String] = ???

  override def damage(): Unit = {
    health -= Random.nextInt(10)
  }
}
