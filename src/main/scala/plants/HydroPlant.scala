package plants

class HydroPlant(instanceId: Int) extends GeneralPlant {
  //todo
  override val id: Int = instanceId
  override val maxHealth: Int = ???
  var health: Int = ???
  override val storageCapacity: Double = ???
  var occupiedStorage: Double = ???
  var currentEnergy: Double = ???

  def changeResistance(angle: Double): Either[String, Double] = ???

  override def clearStorage(): Unit = ???

  override def damage(): Unit = ???

  override def generateEnergy(reading: Double): Unit = ???

  override def calculateTakenStorage(readings: List[Double]): Unit = ???

  override def updateStorage(reading: Double): Unit = ???
}
