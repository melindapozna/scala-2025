package plants

class WindTurbine(instanceId: Int) extends GeneralPlant {
  //todo
  override val id: Int = instanceId
  override val maxHealth: Double = ???
  var health: Double = ???
  override val storageCapacity: Double = ???
  var occupiedStorage: Double = ???
  var currentEnergy: Double = ???
  
  def changeAngle(angle: Double): Either[String, Double] = ???

  override def damage(): Unit = ???
  
  override def generateEnergy(reading: Double): Unit = ???

  override def calculateTakenStorage(readings: List[Double]): Unit = ???

  override def updateStorage(reading: Double): Unit = ???
}
