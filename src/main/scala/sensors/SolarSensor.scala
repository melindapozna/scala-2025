package sensors

class SolarSensor(plantInstanceId: Int) extends GeneralSensor {
  // Constructor
  override val datasetId = 247
  override val plantId: Int = plantInstanceId

  requestData(datasetId, LastMonthTime.toString, currentTime.toString)
  
  //todo implement these
  override def getLatest: Either[String, List[Double]] = ???

  override def writeToFile(): Unit = ???

  override def readFromFile: Either[String, List[Double]] = ???

  override def getCurrentEnergy: Double = ???

  override def getStorageOccupancy: Double = ???

  override def getHealth: Int = ???

}
