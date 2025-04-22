package plants

class WindTurbine(instanceId: Int) extends GeneralPlant {
  //todo
  override val id: Int = instanceId
  override val maxHealth: Int = ???
  var health: Int = ???
  override val storageCapacity: Double = ???
  var occupiedStorage: Double = ???

  override def changeAngle(angle: Double): Option[String] = ???

  override def damage(): Unit = ???
}
