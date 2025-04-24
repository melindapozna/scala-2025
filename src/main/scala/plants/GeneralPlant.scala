package plants

trait GeneralPlant {
  //define traits that apply to all types of plants here
  //e.g. ID, "health", changeAngle
  val id: Int
  val maxHealth: Int
  var health: Int
  val storageCapacity: Double
  var occupiedStorage: Double
  var currentEnergy: Double

  //sets health to the max health of the plant type
  def repair(): Unit = {
    health = maxHealth
  }

  //changes occupiedStorage to 0
  def clearStorage(): Unit = {
    occupiedStorage = 0.0
  }

  //returns error msg if angle is out of bounds, returns new angle OK
  def changeAngle(angle: Double): Either[String, Double]

  def damage(): Unit //reduces health
  
  def generateEnergy(reading: Double): Unit

  def calculateTakenStorage(readings: List[Double]): Unit
}
