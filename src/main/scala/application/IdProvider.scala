package application

case object IdProvider {
  var nextId: Int = 0
  
  def setNextId(): Int = {
    nextId += 1
    nextId
  }
}
