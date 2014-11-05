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
		val (commandName, commandArguments) = (args(0), args.slice(1, args.length - 1))	
		try {
			commands(commandName)(commandArguments)	
		} catch {
			case e: NoSuchElementException => {
					println("Command \"" + commandName + "\" does not exist, available are: \"" + commands.keys.mkString("\", \"") + "\".")
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
		println("list about machine command")	
	}
}

Vamach.main(args)