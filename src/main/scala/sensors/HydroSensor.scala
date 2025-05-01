package sensors

import plants.{GeneralPlant, HydroPlant}

class HydroSensor(plantInstance: HydroPlant) extends GeneralSensor {
  override val plant: GeneralPlant = ???
  override val plantId: Int = ???
  override val datasetId: Int = ???
  var currentReading: (String, Double) = ???

  override def getLatest: Either[String, List[(String, Double)]] = ???

  override def writeToFile(dataRequesterFunction: Either[String, List[(String, Double)]]): Either[String, String] = ???

  override def readFromFile(startDate: String, endDate: String): Either[String, List[Double]] = ???

  override def getCurrentEnergy: Double = ???

  override def getStorageOccupancy: Double = ???

  override def getHealth: Int = ???
}
