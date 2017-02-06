public class BatchProcessor {
	public static void main(String[] args) {
		try {
			String filename = args[0]; 
			BatchParser bp = new BatchParser(); 
			Batch batch = bp.buildBatch(filename); 
			batch.execute(); 
			System.out.println("Program terminated. ");
		}
		catch (Exception e) {
			System.out.println(e.getMessage()); 
			e.printStackTrace(); 
		}
	}
}
