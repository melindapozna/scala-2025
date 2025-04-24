package plants

class WindTurbine(instanceId: Int) extends GeneralPlant {
  //todo
  override val id: Int = instanceId
  override val maxHealth: Int = ???
  var health: Int = ???
  override val storageCapacity: Double = ???
  var occupiedStorage: Double = ???
  var currentEnergy: Double = ???
  
  override def changeAngle(angle: Double): Either[String, Double] = ???

  override def damage(): Unit = ???
  
  override def generateEnergy(reading: Double): Unit = ???

  override def calculateTakenStorage(readings: List[Double]): Unit = ???

  override def updateStorage(reading: Double): Unit = ???
}
