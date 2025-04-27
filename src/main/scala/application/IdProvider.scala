package application

case object IdProvider {
  var nextId: Int = 0
  
  //returns an int that is 1 bigger than the previous one
  def setNextId(): Int = {
    nextId += 1
    nextId
  }
}
