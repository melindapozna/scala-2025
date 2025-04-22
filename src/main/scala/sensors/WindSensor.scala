package sensors

class WindSensor(plantInstanceId: Int) extends GeneralSensor {

  override val datasetId: Int = 245
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
