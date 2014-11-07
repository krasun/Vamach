object Machine {
  val UnknownStatus = "unknown"

  def fromString(machineAsString: String): Machine = {
      val Array(alias, path, status) = machineAsString.split("""\s+""")
      new Machine(alias, path, status)  
  }

  def toString(machine: Machine): String = machine.alias + "\t" + machine.path + "\t" + machine.status
}

class Machine(val alias: String, val path: String, val status: String) {
  override def hashCode = path.hashCode
  
  override def equals(other: Any) = other match { 
    case that: Machine => this.path == that.path 
    case _ => false 
  }

  override def toString(): String = Machine.toString(this)
}

object MachineManager {
  var fileName = "machines"

  def loadMachines(): List[Machine] = {
    val source = scala.io.Source.fromFile(fileName)
    val lines = source.getLines.toList
    source.close()
  
    lines.map(Machine.fromString _)
  }

  def addMachine(alias: String, path: String): Boolean = {
    val machine = new Machine(alias, path, Machine.UnknownStatus)
    val machines = loadMachines()
    if (! machines.contains(machine)) {
        val writer = new java.io.FileWriter(fileName, true) 
        writer.write("\n" + machine) 
        writer.close()

        return true
    } else {
        return false
    }
  } 
}

object Vamach {
  /** List of available commands. */
  val commands = Map(
    "add" -> machineAddCommand _,
    "remove" -> machineRemoveCommand _, 
    "status" -> machineStatusCommand _,
    "sync-status" -> machineSyncStatusCommand _, 
    "list" -> machineListCommand _
  )

  /** Runs specified command.
    *
    * @param args First argument is command name, another arguments are command arguments 
    */
  def main(args: Array[String]) {
    try {
      val (commandName, commandArguments) = (args(0), args.slice(1, args.length)) 
      commands(commandName)(commandArguments) 
    } catch {
      case e: java.lang.ArrayIndexOutOfBoundsException => {
        println("Please, specify command, available are: \"" + commands.keys.mkString("\", \"") + "\".")
      }
      case e: NoSuchElementException => {
        println("Command \"" + args(0) + "\" does not exist, available are: \"" + commands.keys.mkString("\", \"") + "\".")
      }     
    }
  }

  /** Adds virtual machine to database.
   *
   * @param args Command arguments
   */
  def machineAddCommand(args: Array[String]): Unit =  {
    try {
      if (MachineManager.addMachine(args(0), args(1))) {
        println("Machine was successfully added.")  
      } else {
        println("Machine was not added.")
      }
    } catch {
      case e: java.lang.ArrayIndexOutOfBoundsException => {
        println("Please, specify machine alias and path!")
      }
    }
  }

  /** Removes virtual machine to database.
   *
   * @param args Command arguments
   */
  def machineRemoveCommand(args: Array[String]): Unit = {
    println("remove machine command")
  }

  /** Shows status of virtual machines.
   *
   * @param args Command arguments
   */
  def machineStatusCommand(args: Array[String]): Unit = {
    println("status about machine command") 
  }

  /** Synchronizes statuses with database. 
   *
   * @param args Command arguments
   */
  def machineSyncStatusCommand(args: Array[String]): Unit = {
    println("status about machine command") 
  }

  /** Shows list of registered virtual machines.
   *
   * @param args Command arguments
   */
  def machineListCommand(args: Array[String]): Unit = {
    val machines = MachineManager.loadMachines()
    
    if (machines.length > 0) {
      println("List of machines:")
      println("-----")
    }
    for (machine <- machines) {
      println("Alias: \"" + machine.alias + "\"")
      println("Path: \"" + machine.path + "\"")
      println("Status: \"" + machine.status + "\"")
      println("-----")
    } 
  }
}

Vamach.main(args)