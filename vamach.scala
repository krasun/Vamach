/** Machine companion object. */
object Machine {
  
  /** Status is unknown if Machine has not been synchronized yet. */
  val UnknownStatus = "unknown"

  /** Parses string and creates Machine instance.
   *  
   * @param machineAsString Tab-separated machine parameters: alias and path
   */
  def fromString(machineAsString: String): Machine = {
      val Array(alias, path, status) = machineAsString.split("""\s+""")
      new Machine(alias, path, status)  
  }

  /** Converts Machine instance to string.
   *
   * @param Machine Machine instance
   */
  def toString(machine: Machine): String = machine.alias + "\t" + machine.path + "\t" + machine.status
}

/** Descibes Vagrant virtual machine. */
class Machine(val alias: String, val path: String, val status: String) {
  
  override def hashCode = path.hashCode
  
  override def equals(other: Any) = other match { 
    case that: Machine => this.path == that.path 
    case _ => false 
  }

  override def toString(): String = Machine.toString(this)
}

/** Manages machines. */
object MachineManager {
  
  /** Path to file with database of registered machines. */
  var fileName = "machines"

  /** Load machines. */
  def loadMachines(): List[Machine] = {
    val source = scala.io.Source.fromFile(fileName)
    val lines = source.getLines.toList
    source.close()
  
    lines.map(Machine.fromString _)
  }

  /** Adds machine to database. */
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

  /** Removes machine by alias from database. */
  def detachMachine(alias: String): Unit = {
    val updatedMachines = loadMachines().filter(m => m.alias != alias)

    val writer = new java.io.FileWriter(fileName, false)
    for (m <- updatedMachines) {
      writer.write("\n" + m) 
    }
    writer.close()
  }
}

/** Manager of local Vagrant machines. */
object Vamach {

  /** List of available commands. */
  val commands = Map(1
    "add" -> machineAddCommand _,
    "detach" -> machineDetachCommand _, 
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

  /** Removes virtual machine from database.
   *
   * @param args Command arguments
   */
  def machineDetachCommand(args: Array[String]): Unit = {
    try {
        MachineManager.detachMachine(args(0))
        println("Machine was successfully detached.")
    } catch {
      case e: java.lang.ArrayIndexOutOfBoundsException => {
        println("Please, specify machine alias!")
      }
    }
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