package snedeker.lucene.practice.com.services;

import java.io.IOException;
import java.util.Scanner;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class IndexerService {
	
	private StandardAnalyzer analyzer;
	Directory index;
	
	public IndexerService() {
		analyzer = new StandardAnalyzer();
		index = new RAMDirectory();
	}
	
	public void run() throws IOException, ParseException {
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		
		String alice = loadText();
		
		IndexWriter w = new IndexWriter(index, config);
		addDoc(w, alice, "123456789");
		addDoc(w, "Lucene In Action", "193398817");
		addDoc(w, "Lucene for Dummies", "55320055Z");
		addDoc(w, "Managing Gigabytes","55063554A");
		addDoc(w, "The Art of Computer Science", "9900333X");
		w.close();
		
		String queryString = "alice";
		
		Query q = new QueryParser("title", analyzer).parse(queryString);
		
		int hitsPerPage = 10;
		IndexReader reader = DirectoryReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopDocs docs = searcher.search(q, hitsPerPage);
		ScoreDoc[] hits = docs.scoreDocs;
		
		System.out.println("Found " + hits.length + "hits");
		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			System.out.println((i + 1) + ". " + d.get("isbn") + "\t" + d.get("title"));
		}
	}
	
	private String loadText() {
		String text = "";
		try (Scanner scanner = new Scanner(IndexerService.class.getResourceAsStream("/snedeker/lucene/practice/com/resources/AliceInWonderland.txt"), "UTF-8").useDelimiter("\\A"))
		{
			text = scanner.next();
		}

		return text;
	}
	
	private void addDoc(IndexWriter w, String title, String isbn) throws IOException
	{
		Document doc = new Document();
		doc.add(new TextField("title", title, Field.Store.YES));
		doc.add(new StringField("isbn", isbn, Field.Store.YES));
		w.addDocument(doc);
	}
}
