/**
 * A class inherited from Command to describe, parse and execute the exec command for pipe. 
 * Defines the API of exec command from pipe. 
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer; 
import java.util.HashMap; 

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap; 
public class PipecmdExecCommand extends Command{
	String path, id, in, out;
	ArrayList<String> args; 
	
	//constructor
	public PipecmdExecCommand() {
		this.path = null; 
		this.id = null;
		this.in = null; 
		this.out = null; 
		this.args = null; 
	}
	
	/**
	 * Getter and setter of each field in PipeExecCommand.
	 */
	public void setID(String id) { this.id = id; }
	public String getID() { return id; }
	
	public void setPath(String path) { this.path = path; }
	public String getPath() { return path; }
	
	public void setArgs(ArrayList<String> args){ this.args = args; }
	public ArrayList<String> getArgs(){ return args; }
	
	public void setIn(String in) { this.in = in; }
	public String getIn() { return in; }
	
	public void setOut(String out) { this.out = out; }
	public String getOut() { return out; }
	
	/**
	 * Override describe method to show the Command that the execCommand is executing. 
	 */
	@Override
	public String describe() {
		String s = path; 
		if(this.args.isEmpty()) { return s; }
		
		for(int i = 0; i < this.args.size(); i++) {
			s += " " + args.get(i); 
		}
		return s; 
	}
	
	/**
	 * Override parse method.
	 * Capture the id, path, args, in and out from element of batch file. 
	 * Throw ProcessException if missing id or path information in the element of batch file. 
	 */
	@Override
	public void parse(Element element) throws ProcessException { 
		System.out.println("Parsing exec");
		
		//retrieve id from element file of batch.
		String id = element.getAttribute("id"); 
		if(id == null || id.isEmpty()) {
			throw new ProcessException("Missing ID in exec command"); 
		}
		setID(id); 
		
		//retrieve path from element file of batch.
		String path = element.getAttribute("path"); 
		if(path == null || path.isEmpty()) {
			throw new ProcessException("Missing Path in exec command"); 
		}
		setPath(path); 
		
		//check whether there is any unsupported command in batch. If yes, throw an exception. 
		NamedNodeMap attributes = element.getAttributes(); 
		for(int i = 0; i < attributes.getLength(); i++) {
			String s = attributes.item(i).getNodeName(); 
			if(Arrays.binarySearch(Batch.supportCommands, s) < 0) {
				throw new ProcessException("Not supported commands when parsing " + this.id + ": " + s); 
			}
		}
		
		//retrieve arguments from element file of batch. 
		ArrayList<String> args = new ArrayList<String>(); 
		String arg = element.getAttribute("args"); 
		if(!(arg == null || arg.length() == 0)) {
			StringTokenizer st = new StringTokenizer(arg); 
			while(st.hasMoreTokens()){
				args.add(st.nextToken()); 
			}
		}
		setArgs(args); 
		
		//retrieve input source from element file of batch. 
		String in = element.getAttribute("in"); 
		if(!(in == null || in.isEmpty())) {
			setIn(in); 
		}
		
		//retrieve output destination from element file of batch. 
		String out = element.getAttribute("out"); 
		if(!(out == null || out.isEmpty())) {
			setOut(out); 
		}
	}
	
	/**
	 * Execute the command given all commands included in the batch is provided. 
	 * @param commands All commands that are included in the batch. 
	 */
	public void execute(HashMap<String, Command> commands) throws Exception{
		System.out.println("Command: " + describe()); 
		System.out.println(getPath() + " Deferring Execution");
		return; 
	}
}
