import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.IOException;
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.File; 

import org.w3c.dom.Element; 
import org.w3c.dom.Node; 
import org.w3c.dom.NodeList; 

public class PipecmdCommand extends Command{
	private ArrayList<Command> commandList; //commands to store all exec commands in pipe; 
	private String id; //id related to the pipe.
	
	//Constructor
	public PipecmdCommand() {
		this.commandList = new ArrayList<Command>(); 
		this.id = null; 
	}
	
	//Getter and Setter; 
	public String getID() { return this.id; }
	public void setID(String id) { this.id = id; }
	
	//Get the command at ith position of command list. 
	public Command getCommand(int i) { return commandList.get(i); }
	public void addCommand(Command cmd) { commandList.add(cmd); }
	
	//Helper class to create a pipe. 
	public class Piper implements Runnable {
		private InputStream input; 
		private OutputStream output; 
		
		public Piper(InputStream input, OutputStream output) {
			this.input = input; 
			this.output = output; 
		}
		@Override
		public void run() {
			try {
				int achar; 
				while((achar = input.read()) != -1) {
					output.write(achar);
				}
				output.close(); 
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e); 
			}
		}
	}
	
	/**
	 * Override describe method to show the Command that the execCommand is executing. 
	 */
	@Override
	public String describe() {
		return "Command: " + this.id; 
	}
	
	/**
	 * Override parse method.
	 * Capture the exec command involved in pipe. 
	 * Throw ProcessException if missing id or path information in the element of batch file. 
	 */
	@Override
	public void parse(Element element) throws ProcessException{
		System.out.println("Parsing pipecmd");
		NodeList nodes = element.getChildNodes(); 
		for(int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i); 
			if(node.getNodeType() == Node.ELEMENT_NODE) {
				Element subElement = (Element) node; 
				Command cmd; 
				if("exec".equalsIgnoreCase(subElement.getNodeName())) {
					cmd = new PipecmdExecCommand(); 
				} else {
					throw new ProcessException(subElement.getNodeName() + " not supported."); 
				}
				cmd.parse(subElement); 
				commandList.add(cmd); 
			}
		}
	}
	
	/**
	 * Execute the command given all commands included in the batch is provided. 
	 * @param commands All commands that are included in the batch. 
	 */
	public void execute(HashMap<String, Command> commands) throws Exception{
		PipecmdExecCommand start = (PipecmdExecCommand) commandList.get(0); 
		PipecmdExecCommand last = (PipecmdExecCommand) commandList.get(1); 
		
		List<String> fullCommand1 = new ArrayList<String>(); //commands of process 1
		fullCommand1.add(start.getPath()); //Add path into list.
		
		if(!(start.args == null || start.args.isEmpty())) {
			fullCommand1.addAll(start.args); //Add arguments into list.
		}
		
		List<String> fullCommand2 = new ArrayList<String>(); //commands of process2. 
		fullCommand2.add(last.getPath()); 
		
		if(!(last.args == null || last.args.isEmpty())) {
			fullCommand2.addAll(last.args); 
		}
		
		String in = null; 
		if(!(start.in == null || start.in.isEmpty())) {
			Command sIn; 
			if((sIn = commands.get(start.in)) != null && sIn instanceof filenameCommand) {
				in = ((filenameCommand) sIn).getPath(); 
			}
		}
		
		String out = null; 
		if(!(last.out == null || last.out.isEmpty())) {
			Command sOut; 
			if((sOut = commands.get(last.out)) != null && sOut instanceof filenameCommand) {
				out = ((filenameCommand) sOut).getPath(); 
			}
		}
		
		if(in == null) {
			throw new ProcessException("Missing input source."); 
		}
		
		if(out == null) {
			throw new ProcessException("Missing output source."); 
		}
		
		start.execute(commands);
		ProcessBuilder psBuilder1 = new ProcessBuilder(); 
		psBuilder1.command(fullCommand1); 
		
		last.execute(commands);
		ProcessBuilder psBuilder2 = new ProcessBuilder(); 
		psBuilder2.command(fullCommand2); 
		
		psBuilder1.redirectInput(new File(in)); 
		psBuilder2.redirectOutput(new File(out)); 
		
		Process ps1 = psBuilder1.start(); 
		Process ps2 = psBuilder2.start(); 
		
		Piper pipe = new Piper(ps1.getInputStream(), ps2.getOutputStream()); 
		new Thread(pipe).start(); 
		
		System.out.println("Waiting for cmd1 to exit"); 
		ps1.waitFor(); 
		System.out.println("cmd1 has exited");
		System.out.println("");
		
		System.out.println("Waiting for cmd2 to exit");
		ps2.waitFor(); 
		System.out.println("cmd2 has exited");
	}
}
