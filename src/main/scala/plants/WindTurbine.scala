package plants

class WindTurbine extends GeneralPlant {
  //todo
  override val maxHealth: Int = ???
  var health: Int = ???
  override val storageCapacity: Double = ???
  var occupiedStorage: Double = ???

  override def changeAngle(angle: Double): Option[String] = ???

  override def damage(): Unit = ???
}
