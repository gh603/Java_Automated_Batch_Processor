/**
 * A class to describe, execute filename command.
 * Defines the API of filename command from an XML document.
 * Once an instance of an element of XML document is provided, the element can be parsed from a variety of input sources.
 * With a hashmap of all commands in the XML document provided, the command can be executed. 
 */
import java.util.Arrays;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap; 

public class filenameCommand extends Command{
	private String path;//path of file name command; 
	private String id; //id of file name command; 
	
	//Constructor receiving no argument. 
	public filenameCommand() {
		this.path = null; 
		this.id = null; 
	}
	
	//Getter to get id of file name command. 
	public String getID() {
		return id; 
	}
	
	//Setter to set id of file name command. 
	public void setID(String id) {
		this.id = id; 
	}
	
	//Getter to get path of file name command. 
	public String getPath() {
		return path; 
	}
	
	//Setter to set path of file name command. 
	public void setPath(String path) {
		this.path = path; 
	}
	
	
	/**
	 * Override describe() method to show the path of file name command. 
	 */
	@Override
	public String describe() {
		return "File command on file: " + path; 
	}
	
	/**
	 * Override execute() method.
	 */
	@Override
	public void execute(HashMap<String, Command> commands) {
		System.out.println(describe()); 
	}
	
	/**
	 * Override parse() method.
	 * Capture the id and path of given element.
	 * Throw ProcessException if missing id or path information.
	 */
	public void parse(Element element) throws ProcessException{
		System.out.println("Parsing filename");
		
		String id = element.getAttribute("id"); 
		if(id == null || id.isEmpty()) {
			throw new ProcessException("Missing ID in file command"); 
		}
		setID(id); //set id field as the id in batch
		
		String path = element.getAttribute("path"); 
		if(path == null || path.isEmpty()) {
			throw new ProcessException("Missing path in file command"); 
		}
		setPath(path); //set path as the actual file name in batch. 
		
		//check whether there is any unsupported command in batch. If yes, throw an exception. 
		NamedNodeMap attributes = element.getAttributes(); 
		for(int i = 0; i < attributes.getLength(); i++) {
			String s = attributes.item(i).getNodeName(); 
			if(Arrays.binarySearch(Batch.supportCommands, s) < 0) {
				throw new ProcessException("Not supported commands when parsing " + this.id + ": " + s); 
			}
		}
	}
}
