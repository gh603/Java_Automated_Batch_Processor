/**
 * Data structure to hold all commands info included in a batch xml file
 * Two fields are provided: a hashmap commands, and an array list commandList. 
 * In hashmap commands, each command is paired with its id from xml file. 
 * User can retrieve a command with the id of this command using getCommand() function. 
 * Command can also be added into the list of command, commandList, using addCommand function. 
 */
import java.util.HashMap; 
import java.util.ArrayList;

public class Batch {
	public final static String[] supportCommands = {"args", "id", "in", "out", "path"}; 
	HashMap<String, Command> commands; 
	ArrayList<Command> commandList; 
	int size; 
	
	//Constructor. 
	public Batch() {
		commands = new HashMap<String, Command>(); 
		commandList = new ArrayList<Command>(); 
	}
	
	/**
	 * Method to get command that is labeled by s(id in batch). 
	 * @param s id in batch
	 * @return command that are labeled by s. 
	 */
	public Command getCommand(String id) {
		return commands.get(id); 
	}
	
	/**
	 * Return the command at position i in command list. 
	 * @param i, position of command in command list.
	 * @return the command at position i in command list. 
	 */
	public Command getCommand(int i) {
		return commandList.get(i); 
	}
	
	/**
	 * Add a command in the array list of commands in sequential order. 
	 * @param c command to add. 
	 * @param id id to label the command. 
	 */
	public void addCommand(String id, Command c) {
		commandList.add(c); 
		commands.put(id, c); 
		size++; 
	}
	
	/**
	 * Return the number of commands in batch. 
	 * @return the number of commands in batch. 
	 */
	public int size() {
		return this.size; 
	}
	
	public void execute() throws Exception{
		for(int i = 0; i < size(); i++) {
			Command cmd = getCommand(i); 
			cmd.execute(commands);
		}
	}
}