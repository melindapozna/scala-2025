package plants

class HydroPlant(instanceId: Int) extends GeneralPlant {
  //todo
  override val id: Int = instanceId
  override val maxHealth: Int = ???
  var health: Int = ???
  override val storageCapacity: Double = ???
  var occupiedStorage: Double = ???

  override def changeAngle(angle: Double): Either[String, Double] = ???

  override def clearStorage(): Unit = ???

  override def damage(): Unit = ???

 
}
