package plants

trait GeneralPlant {
  // defines traits that apply to all types of plants here
  // e.g. ID, "health", changeAngle
  val id: Int
  val maxHealth: Int
  var health: Int
  val storageCapacity: Double
  var occupiedStorage: Double
  var currentEnergy: Double

  // sets health to the max health of the plant type
  // e.g. if health is below some %, the user can repair the plant
  def repair(): Unit = {
    health = maxHealth
  }

  // changes occupiedStorage to 0
  def clearStorage(): Unit = {
    occupiedStorage = 0.0
  }

  def damage(): Unit //reduces health
  
  // gets the last reading (that is stored as a variable called currentReading in the sensors)
  // and based on some freely chosen formula, it calculates a value
  // and updates the currentEnergy variable
  def generateEnergy(reading: Double): Unit

  // gets all the readings from the initial API request, and sums all the values
  def calculateTakenStorage(readings: List[Double]): Unit
  
  // adds the latest current reading to the storage, when calling getLatest() in the sensor
  def updateStorage(reading: Double): Unit
}
