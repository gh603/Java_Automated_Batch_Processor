# Java_Automated_Batch_Processor
	*This project is to build a batch processor that automated the parsing and executing batch files that contains a number of command. 
	*A batch file contains one or more commands that are executed sequentially.
	*Each command executed by the batch processor will be executed as a process and communicate using files or pipes. 

# Application Structure
	*BatchProcessor: 
		This is the main class which drives both the parsing of the batch file into commands and the execution of those commands. 
	*Batch: 
		This class maintains the N Commands that were parsed from the given batch file. 
	*BatchParser: 
		This class builds an instance of Batch containing the N Commands parsed from the XML document provided in the batch file. The parser is responsible for visiting each of the XML elements in the given XML document and generating the correct Command subclass from the element. Note that the actual parsing of the element should be delegated to the correct Command subclass. 
	*Commands Classes: 
		Command is an abstract class that defines three abstract methods: describe() used to print a message to the console when the Command is executed. parse() should parse and extract the information contained in the given XML Element. execute() should execute the command
	*filenameCommand, PipecmdCommand, PipecmdExecCommand and execCommand
		Four classes that inherit from Comman classes to implement each of the four types of batch command that our batch processor can execute. 
	*ProcessException:
		The Exception type generated in the process of batch execution. 

# Sample batch file and sample input data
	*batch 1 - 5: sample batch file
	*numberdata.txt and randomwords.txt: sample input data file