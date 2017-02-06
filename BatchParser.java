import java.io.File; 
import java.io.FileInputStream; 

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory; 

import org.w3c.dom.Document; 
import org.w3c.dom.Element; 
import org.w3c.dom.Node; 
import org.w3c.dom.NodeList; 
public class BatchParser {
	public Command buildCommand(Element element) throws ProcessException{
		String cmdName = element.getNodeName(); 
		//System.out.println("Element name = " + cmdName);
		
		Command  cmd; 
		if(cmdName == null) {
			throw new ProcessException("Unable to parse command from " + element.getTextContent()); 
		}  
		else if ("filename".equalsIgnoreCase(cmdName)) {
			cmd = new filenameCommand(); 
		} else if("exec".equalsIgnoreCase(cmdName)) {
			cmd = new execCommand(); 
		} else if("pipecmd".equals(cmdName)) {
			cmd = new PipecmdCommand(); 
		}else {
			throw new ProcessException("Unknown command " + cmdName + " from: " + element.getBaseURI()); 
		}
		
		cmd.parse(element);
		return cmd; 
	}
	
	public Batch buildBatch(String filename) throws Exception{
		System.out.println("Openning " + filename); 
		File f = new File(filename); 
			
		FileInputStream fis = new FileInputStream(f); 
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance(); 
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder(); 
			
		Document doc = dBuilder.parse(fis); 
			
		Element pnode = doc.getDocumentElement(); 
		NodeList nodes = pnode.getChildNodes(); 
		
		Batch batch = new Batch(); 
		for(int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i); 
			if(node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node; 
				Command command = buildCommand(element); 
				batch.addCommand(command.getID(), command);
			}
		}
		return batch; 
	}
}
