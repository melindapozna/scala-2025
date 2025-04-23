import plants.Plant
import application.Menu

@main
def main(): Unit = {
  Plant.run()
  println("Power plant started successfully.")
  Menu.start()
  
}

