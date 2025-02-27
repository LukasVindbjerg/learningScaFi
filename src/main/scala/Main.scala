package examples

import it.unibo.scafi.incarnations.BasicAbstractIncarnation

// Define an incarnation (like `hello-scafi`)
object MyIncarnation extends BasicAbstractIncarnation

// Import everything from the incarnation
import examples.MyIncarnation._

object Main extends App {
  // Import standard sensor names
  val sensorsNames = new StandardSensorNames {}
  import sensorsNames._

  // Define a simple aggregate program
  class NeighborIDProgram extends AggregateProgram {
    override def main(): Set[ID] = foldhood(Set(mid()))(_ ++ _)(nbr { Set(mid()) })
  }

  val program = new NeighborIDProgram()

  // Define a simplified system with two nodes
  case class DeviceState(
      self: ID,
      exports: Map[ID, EXPORT],
      localSensors: Map[CNAME, Any],
      nbrSensors: Map[CNAME, Map[ID, Any]]
  )

  val devices = Seq(0, 1)
  var state: Map[ID, DeviceState] = devices.map { d =>
    d -> DeviceState(
      self = d,
      exports = Map.empty[ID, EXPORT],
      localSensors = Map.empty[CNAME, Any], // No sensors needed
      nbrSensors = Map(
        NBR_RANGE -> Map(devices.find(_ != d).get -> 1.0)
      )
    )
  }.toMap

  // Simulate execution over multiple rounds
  val scheduling = devices ++ devices ++ devices // Run 3 rounds
  for (d <- scheduling) {
    val ctx = factory.context(
      selfId = d,
      exports = state(d).exports,
      lsens = state(d).localSensors,
      nbsens = state(d).nbrSensors
    )

    println(s"RUN: DEVICE $d\n\tCONTEXT: ${state(d)}")
    val export = program.round(ctx)
    state += d -> state(d).copy(exports = state(d).exports + (d -> export)) // Update state

    state(d)
      .nbrSensors(NBR_RANGE)
      .keySet
      .foreach(nbr => state += nbr -> state(nbr).copy(exports = state(nbr).exports + (d -> export)))

    println(s"\tEXPORT: $export\n\tOUTPUT: ${export.root()}\n--------------")
  }
}

