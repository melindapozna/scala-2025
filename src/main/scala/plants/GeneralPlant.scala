package plants

trait GeneralPlant {
  //define traits that apply to all types of plants here
  //e.g. ID, "health", changeAngle
  val id: Int
  val maxHealth: Int
  var health: Int
  val storageCapacity: Double
  var occupiedStorage: Double

  //sets health to the max health of the plant type
  def repair(): Unit = {
    health = maxHealth
  }

  //changes occupiedStorage to 0
  def clearStorage(): Unit = {
    occupiedStorage = 0
  }

  def changeAngle(angle: Double): Option[String] //returns error msg if angle is out of bounds, returns None if angle OK

  def damage(): Unit //reduces health
}
