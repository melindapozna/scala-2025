package dataAnalysis

object DataAnalysis {
  // calculate mean, median, range, midrange
  // error handling needs to be implemented
  // this whole file is not yet used, but
  // the user will be able to select a range, then this range is read from the corresponding file, and the data analysis 
  // is calculated on that

  def mean(data: List[Double]): Double = {
    val sum = data.foldLeft(0.0)(_ + _)
    val length = data.length
    sum / length
  }


  def median(data: List[Double]): Double = {
    val sortedData = data.sortWith(_ < _)
    val length = data.length

    // get median if the list has an even number of elements
    if (length % 2 == 0) {

      // checks if the indices are correct, returns median if yes, throws IndexOutOfBoundsException if not
      val first = sortedData.lift(length / 2).getOrElse(0.0)
      val second = sortedData.lift((length / 2) + 1).getOrElse(0.0)
      return (first + second) / 2

    }

    // get median if the list has an odd number of elements
    sortedData.lift(length / 2 + 1).getOrElse(0.0)
  }


  def mode(data: List[Double]): Double = {
    val frequencies = data.groupBy(x => x)
    frequencies.reduce((a, b) =>
      if (a._2.length < b._2.length) b else a)._1
  }


  def range(data: List[Double]): Double = {
    val sortedData = data.sortWith(_ < _)
    val last = sortedData.head
    val first = sortedData.last
    first - last
  }


  def midrange(data: List[Double]): Double = {
    val sortedData = data.sortWith(_ < _)
    val last = sortedData.head
    val first = sortedData.last
    (last + first) / 2
  }

}
