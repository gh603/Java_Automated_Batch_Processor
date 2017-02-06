/**
 * exec command inherited from abstract Command. 
 */
import java.util.List; 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer; 
import java.util.HashMap; 
import java.io.File; 

import org.w3c.dom.Element; 
import org.w3c.dom.NamedNodeMap; 

public class execCommand extends Command {
	String id, path, in, out;
	ArrayList<String> args; 
	
	/**
	 * Constructor
	 */
	public execCommand() {
		this.id = null; 
		this.path = null; 
		this.args = null; 
		this.in = null; 
		this.out = null; 
	}
	
	/**
	 * Getter and setter of each field in execCommand.
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
		
		List<String> fullCommand = new ArrayList<String>(); //List contains all command included in element of batch file
		fullCommand.add(this.getPath()); //Add path into list.
		
		if(!(this.args == null || this.args.isEmpty())) {
			fullCommand.addAll(this.args); //Add arguments into list.
		}
		
		String in = null; 
		if(!(this.in == null || this.in.isEmpty())) {
			Command sIn; 
			if((sIn = commands.get(this.in)) != null && sIn instanceof filenameCommand) {
				in = ((filenameCommand) sIn).getPath(); 
			}
		}
		
		String out = null; 
		if(!(this.out == null || this.out.isEmpty())) {
			Command sOut; 
			if((sOut = commands.get(this.out)) != null && sOut instanceof filenameCommand) {
				out = ((filenameCommand) sOut).getPath(); 
			}
		}
		//test whether full command is constructed successfully. 
		StringBuilder sb = new StringBuilder(); 
		sb.append("\""); 
		for(String s : fullCommand) {
			sb.append(s + " "); 
		}
		sb.append("\" Deferring Execution"); 
		System.out.println(sb.toString()); 
		
		//Build process. 
		ProcessBuilder builder = new ProcessBuilder(); //Build process
		builder.command(fullCommand); //Specify commands in the process
		
		if(in != null) {
			File inFile = new File(in); 
			builder.redirectInput(inFile); 						
		}

		if(out != null) {
			File outFile = new File(out); 
			builder.redirectOutput(outFile); //Direct output to specified file by batch			
		}

		Process process = builder.start(); //Start process
		process.waitFor(); 
		System.out.println("Batch \"" + describe() + "\" terminated! ");
	}
}
