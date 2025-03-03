package examples

import it.unibo.scafi.incarnations.BasicAbstractIncarnation

// Define the ScaFi environment
object IdIncarnation extends BasicAbstractIncarnation

// Import everything from the incarnation
import examples.IdIncarnation._

object printId extends App {
  // Define an aggregate program that just returns its own ID
  class SimpleIDProgram extends AggregateProgram {
    override def main(): ID = mid()
  }

  val program = new SimpleIDProgram()

  // Create two devices with IDs 0 and 1
  val devices = Seq(0, 1)
  var state: Map[ID, EXPORT] = Map.empty

  // Run the program for each device
  for (d <- devices) {
    val ctx = factory.context(
      selfId = d,      
      exports = state, 
      lsens = Map.empty, 
      nbsens = Map.empty
    )

    // Execute the aggregate program
    val export = program.round(ctx)

    // Store the result
    state += d -> export

    // Print the output
    println(s"Device $d → ID: ${export.root()}")
  }
}


