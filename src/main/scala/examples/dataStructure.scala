package examples

import it.unibo.scafi.incarnations.BasicAbstractIncarnation

// Define the ScaFi environment
object DSIncarnation extends BasicAbstractIncarnation

// Import everything from the incarnation
import examples.DSIncarnation._

object DataStructure extends App {
  // Define an aggregate program that just returns its own ID
  class SimpleIDProgram extends AggregateProgram {
    override def main(): ID = mid()
  }

  val program = new SimpleIDProgram()
  
  println(program)

  val devices = Seq(0, 3)
  val myList = List(0, 3)

  println(devices)
  println(myList)
  
}
