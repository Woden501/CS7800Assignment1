package snedeker.lucene.practice.com;

import snedeker.lucene.practice.com.services.IndexerService;

public class Application {

	public static void main(String[] args) {
		IndexerService indexerService = new IndexerService();
		
		try {
			indexerService.run();
		} catch (Exception e) {
			System.out.println("IndexerService failed to complete: " + e);
		}
	}
}
