/**
 * An abstract class to describe, execute command.
 * Defines the API of command from an XML document.
 * Once an instance of an element of XML document is provided, the element can be parsed from a variety of input sources.
 * With a hashmap of all commands in the XML document provided, the command can be executed. 
 */
import java.util.HashMap;

import org.w3c.dom.Element;

public abstract class Command {
	/**
	 * Return a string that is used to print a message to the console when the Command is executed.
	 * @return
	 */
	public String describe() { return null; }
	/**
	 * Execute the command given all commands included in the batch is provided. 
	 * @param commands All commands that are included in the batch. 
	 */
	public void execute(HashMap<String, Command> commands) throws Exception { return; }
	
	/**
	 * Parse the given element from batch from a variety of sources. 
	 * @param element Element read from batch of XML file. 
	 * @throws ProcessException
	 */
	public void parse(Element element) throws ProcessException { return; } 
	
	/**
	 * Return the id that labels the command.
	 * @return the id that labels the command
	 */
	public String getID() { return null; }
}
