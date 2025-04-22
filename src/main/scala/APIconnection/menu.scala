package APIconnection

import scala.io.StdIn.readLine

object menu {
  private def mainMenu(): Unit = {
    println("\n-------------- REPS Power Plant --------------\n")
    println(
      """1. Check cameras
        |2. Check storage
        |3. Check current energy output
        |4. Analyse data
        |5. Adjust operation parameters
        |0. Exit""".stripMargin)
    val choice = readLine()

    choice match
      case "0" => System.exit(0)
      case "1" => checkCameras()
      case "2" => println("IMPLEMENT")        //todo implement
      case "3" => println("IMPLEMENT")        //todo implement
      case "4" => println("IMPLEMENT")        //todo implement
      case "5" => plantTypeMenu()
      case _ => println("Error: invalid input")

  }

  private def plantTypeMenu(): Unit = {
    println(
      """1. Solar Panels
        |2. Wind Turbines
        |3. Hydro Plants
        |0. Back""".stripMargin)
    val choice = readLine()

    choice match
      case "0" => mainMenu()
      case "1" => println("IMPLEMENT")        //todo implement
      case "2" => println("IMPLEMENT")        //todo implement
      case "3" => println("IMPLEMENT")        //todo implement
      case _ => println("Error: invalid input")

  }


  private def checkCameras(): Unit = {
    println("Checking cameras...")
    Thread.sleep(3000)
    mainMenu()
  }


}
