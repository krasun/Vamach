class Machine(p: String, s: String) {
  val path: String = p
  val status: String = s
}

object MachineManager {
  var fileName = "machines"

  /** **/
  def loadMachines(): List[Machine] = {
    val source = scala.io.Source.fromFile(fileName)
    val lines = source.getLines.toList
    source.close()
    
    lines.map(l => {
      val Array(path, status) = l.split("""\s+""")
      new Machine(path, status)  
    })
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
      val (commandName, commandArguments) = (args(0), args.slice(1, args.length - 1)) 
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
    println("add machine command")
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
      println("-----")
    }
    for (machine <- machines) {
      println("Path: \"" + machine.path + "\"")
      println("Status: \"" + machine.status + "\"")
      println("-----")
    } 
  }
}

Vamach.main(args)