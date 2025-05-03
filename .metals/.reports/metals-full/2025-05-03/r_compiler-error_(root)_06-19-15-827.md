file:///C:/Users/Avaneep/Documents/GitHub/scala-2025/src/main/scala/plants/Plant.scala
### java.lang.AssertionError: assertion failed: position not set for runtimeChecked(<empty>) # -1 of class dotty.tools.dotc.ast.Trees$Apply in C:/Users/Avaneep/Documents/GitHub/scala-2025/src/main/scala/plants/Plant.scala

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 6009
uri: file:///C:/Users/Avaneep/Documents/GitHub/scala-2025/src/main/scala/plants/Plant.scala
text:
```scala
package plants

import application.IdProvider
import sensors.*
import dataAnalysis.DataAnalysis

import java.util.Timer
import java.util.TimerTask
import scala.collection.immutable.HashMap
import scala.io.StdIn.readLine


// initializes the REPS power plant
case object Plant {
  
  // initializing solar panels with corresponding sensors
  // the id provider provides ids incrementally
  // you need to pass the plant instance as a parameter to the sensor
  private val solar1 = new SolarPanel(IdProvider.setNextId())
  private val solar1Sensor = new SolarSensor(solar1)
  
  private val solar2 = new SolarPanel(IdProvider.setNextId())
  private val solar2Sensor = new SolarSensor(solar2)
  
  // a list of solar panels and sensors, for easier handling
  private val solarPanels = List(solar1, solar2)
  private val solarSensors = List(solar1Sensor, solar2Sensor)

  
  private val wind1 = new WindTurbine(IdProvider.setNextId())
  private val wind1Sensor = new WindSensor(wind1)

  private val wind2 = new WindTurbine(IdProvider.setNextId())
  private val wind2Sensor = new WindSensor(wind2)

  private val windTurbines = List(wind1, wind2)
  private val windSensors = List(wind1Sensor, wind2Sensor)

  
  private val hydro1 = new HydroPlant(IdProvider.setNextId())
  private val hydro1Sensor = new HydroSensor(hydro1)

  private val hydroSensors = List(hydro1Sensor)
  private val hydroPlants = List(hydro1)

  // starting a timer
  private val timer = new Timer()

  private def getPlantId(plant: GeneralPlant): Int = {
    plant.id
  }

  private def toInt(s: String): Option[Int] = {
    try {
      Some(Integer.parseInt(s.trim))
    } catch {
      case e: Exception => None
    }
  }
  
  // prints every object's storage capacity in %
  def checkStorage(): Unit = {
    val solarStorages = solarSensors.foldLeft("")((currStr, sensor) =>
      currStr.concat(f"Solar panel #${sensor.plantId}: ${sensor.getStorageOccupancy}%.2f%%\n"))
    println(solarStorages)
    println("Note: Clearing storage is advised above 85% occupancy!\n")
  }
  
  def checkCameras(): Unit = {
    println("Checking cameras...")
    Thread.sleep(3000)
  }

  def emptyStorage(plantType: String): Either[String, String] = {
    plantType match
      case "solar" =>
        solarPanels.foreach(_.clearStorage())
        Right("Solar panel storages cleared successfully.")
      case "wind" =>
        //windTurbines.foreach(_.clearStorage())
        Right("Solar panel storages cleared successfully.")
      case "hydro" =>
        hydroPlants.foreach(_.clearStorage())
        Right("Solar panel storages cleared successfully.")
      case _ =>
        Left("Invalid plant type when trying to clear storage.")
  }

  def checkCurrentEnergy(): Unit = {
    val solarEnergies = solarSensors.foldLeft("")((currStr, sensor) =>
      currStr.concat(f"Solar panel #${sensor.plantId}: ${sensor.getCurrentEnergy}%.2f MWh/h\n"))
    println(solarEnergies)
    
    val windEnergies = windSensors.foldLeft("")((currStr, sensor) =>
      currStr.concat(f"Wind turbine #${sensor.plantId}: ${sensor.getCurrentEnergy}%.2f MWh/h\n"))
    println(windEnergies)
    

    val hydroEnergies = hydroSensors.foldLeft("")((currStr, sensor) =>
      currStr.concat(f"Hydro plant #${sensor.plantId}: ${sensor.getCurrentEnergy}%.2f MWh/h\n"))
    println(hydroEnergies)
  }

  def adjustParams(plantType: String): Either[String, String] = {
    plantType match
      case "solar" =>
        println("New solar panel angle (integer, 0-180): ")
        val angle = readLine()
        toInt(angle) match
          case Some(angle) =>
            if 0 to 180 contains angle then
              solarPanels.foreach(_.changeAngle(angle))
              Right("Solar panel angles set successfully.")
            else
              Left("Invalid input.")
          case None =>
            Left("Invalid input. Solar panel angles have not been set.")
            
      case "wind" =>
        println("New wind turbine gear (integer, 1-10): ")
        val gear = readLine()
        toInt(gear) match
          case Some(gear) =>
            windTurbines.foreach(_.changeGear(gear))
            Right(s"Wind turbine gear set to ${gear} successfully.")
          case None =>
            Left("Invalid input. Wind turbine gear has not been set.")

      case "hydro" =>
        println("New hydro plant resistance percentage (integer, 0-10): ")
        val percent = readLine()
        toInt(percent) match
          case Some(percent) =>
            hydroPlants.foreach(_.changeResistance(percent))
            Right(s"Hydro plant resistances set to ${percent}% successfully.")
          case None =>
            Left("Invalid input. Hydro plant resistances have not been set.")
      case _ => Left("Invalid plant type when trying to adjust plant parameters.")
  }

  def checkHealth(): Unit = {
    val solarHealths = solarSensors.foldLeft("")((currStr, sensor) =>
      currStr.concat(f"Solar panel #${sensor.plantId}: ${sensor.getHealth}%.2f%%\n"))
    println(solarHealths)

    val windHealths = windSensors.foldLeft("")((currStr, sensor) =>
      currStr.concat(f"Wind turbine #${sensor.plantId}: ${sensor.getHealth}%.2f%%\n"))
    println(windHealths)

    val hydroHealths = hydroSensors.foldLeft("")((currStr, sensor) =>
      currStr.concat(f"Hydro plant #${sensor.plantId}: ${sensor.getHealth}%.2f%%\n"))
    println(hydroHealths)

    println("Note: Repairing is strongly advised under 10% health!\n")
  }

  def repair(plantType: String): Either[String, String] = {
    plantType match
      case "solar" =>
        solarPanels.foreach(_.solarRepair())
        Right("Solar panels successfully repaired.")
      case "wind" =>
        windTurbines.foreach(_.windRepair())
        Right("Wind turbines successfully repaired.")
      case "hydro" =>
        hydroPlants.foreach(_.h@@repair())
        Right("Hydro plants successfully repaired.")
      case _ =>
        Left("Error while trying to repair solar panels.")
  }

  def analyzeData(startDate: String, endDate: String): Unit = {
    val solarReadings = solarSensors.map(sensor =>
      sensor.readFromFile(startDate, endDate) match
        case Right(readings) =>
          val sorted = readings.sortBy(_._2)
          println(s"Solar Panel ${sensor.plantId}. - all readings, sorted by value:")
          sorted.foreach(pair =>
            println(s"${pair._2} MWh/h (${pair._1})")
          )
          readings.map(_._2)
        case Left(error) => Nil
    )
    val resultsWithSensors = solarReadings.map(dataAnalysisHelper).zip(solarSensors)
    prettyPrintDataAnalysis(resultsWithSensors, "Solar Panel")

  }

  private def dataAnalysisHelper(readings: List[Double]): HashMap[String, Double] = {
    val mean = DataAnalysis.mean(readings)
    val median = DataAnalysis.median(readings)
    val mode = DataAnalysis.mode(readings)
    val range = DataAnalysis.range(readings)
    val midrange = DataAnalysis.midrange(readings)
    HashMap(
      "Mean" -> mean,
      "Median" -> median,
      "Mode" -> mode,
      "Range" -> range,
      "Midrange" -> midrange
    )
  }

  private def prettyPrintDataAnalysis(readingsWithSensor: List[(HashMap[String, Double], GeneralSensor)], plantType: String): Unit = {
    readingsWithSensor.foreach(tuple =>
      val results = tuple._1
      println(s"\n$plantType ${tuple._2.plantId}.:")
      results.foreach(pair =>
      println(f"${pair._1}: ${pair._2}%.2f"))
    )
  }

  // every 15 minutes (the API is updated that frequently), it gets the new reading
  def start(): Unit = {
    val getNewSensorData = new TimerTask {
      def run(): Unit = {
        solarSensors.foreach(_.getLatest)
      }
    }
    timer.schedule(getNewSensorData, 1000, 900000)
  }
  
  // cancels the timer
  def shutdown(): Unit = {
    timer.cancel()
    timer.purge()
  }
}

```



#### Error stacktrace:

```
scala.runtime.Scala3RunTime$.assertFailed(Scala3RunTime.scala:8)
	dotty.tools.dotc.typer.Typer$.assertPositioned(Typer.scala:76)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3680)
	dotty.tools.dotc.typer.Applications.extMethodApply(Applications.scala:2685)
	dotty.tools.dotc.typer.Applications.extMethodApply$(Applications.scala:465)
	dotty.tools.dotc.typer.Typer.extMethodApply(Typer.scala:151)
	dotty.tools.dotc.typer.Applications.tryApplyingExtensionMethod(Applications.scala:2730)
	dotty.tools.dotc.typer.Applications.tryApplyingExtensionMethod$(Applications.scala:465)
	dotty.tools.dotc.typer.Typer.tryApplyingExtensionMethod(Typer.scala:151)
	dotty.tools.dotc.interactive.Completion$Completer.tryApplyingReceiverToExtension$1(Completion.scala:561)
	dotty.tools.dotc.interactive.Completion$Completer.$anonfun$23(Completion.scala:604)
	scala.collection.immutable.List.flatMap(List.scala:294)
	scala.collection.immutable.List.flatMap(List.scala:79)
	dotty.tools.dotc.interactive.Completion$Completer.extensionCompletions(Completion.scala:601)
	dotty.tools.dotc.interactive.Completion$Completer.selectionCompletions(Completion.scala:449)
	dotty.tools.dotc.interactive.Completion$.computeCompletions(Completion.scala:221)
	dotty.tools.dotc.interactive.Completion$.rawCompletions(Completion.scala:80)
	dotty.tools.pc.completions.Completions.enrichedCompilerCompletions(Completions.scala:114)
	dotty.tools.pc.completions.Completions.completions(Completions.scala:136)
	dotty.tools.pc.completions.CompletionProvider.completions(CompletionProvider.scala:139)
	dotty.tools.pc.ScalaPresentationCompiler.complete$$anonfun$1(ScalaPresentationCompiler.scala:150)
```
#### Short summary: 

java.lang.AssertionError: assertion failed: position not set for runtimeChecked(<empty>) # -1 of class dotty.tools.dotc.ast.Trees$Apply in C:/Users/Avaneep/Documents/GitHub/scala-2025/src/main/scala/plants/Plant.scala