package snedeker.goetz.assignment1;

import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;

import snedeker.goetz.assignment1.services.IndexerService;
import snedeker.goetz.assignment1.services.QueryService;

public class Application {

	public static void main(String[] args) {
		
		try {
			IndexerService indexerService = new IndexerService();
			indexerService.run();
		} catch (Exception e) {
			System.out.println("IndexerService failed to complete: " + e);
		}
		
		QueryService queryService;
		try {
			queryService = new QueryService();
			while (true)
				queryService.run();
		} catch (IOException | ParseException e) {
			System.out.println("QueryService failed to complete: " + e);
		}
	}
}
