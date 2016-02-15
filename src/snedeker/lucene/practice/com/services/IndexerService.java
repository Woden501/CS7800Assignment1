package snedeker.lucene.practice.com.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import snedeker.lucene.practice.com.models.EntryDocument;

public class IndexerService {

	private StandardAnalyzer analyzer;
	Directory index;

	public IndexerService() throws IOException {
		File indexDirectory = new File("index");
		
		analyzer = new StandardAnalyzer();
		index = FSDirectory.open(indexDirectory.toPath());
	}

	public void run() throws IOException {
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		config.setOpenMode(OpenMode.CREATE_OR_APPEND);

		ArrayList<EntryDocument> texts = loadTexts();

		IndexWriter w = new IndexWriter(index, config);
		for (EntryDocument entry : texts) {
			addDoc(w, entry);
		}
		w.close();
	}

	private ArrayList<EntryDocument> loadTexts() throws IOException {
		ArrayList<EntryDocument> texts = new ArrayList<>();

		File folder = new File(IndexerService.class.getResource("/snedeker/lucene/practice/com/resources").getPath());
		File[] files = folder.listFiles();

		for (File file : files) {
			String text = FileUtils.readFileToString(file);

			// Split into docs
			String[] splitDocs = text.split(".I");

			for (String doc : splitDocs) {
				EntryDocument newEntry = getEntry(doc);

				if (newEntry != null)
					texts.add(newEntry);
			}
		}

		return texts;
	}

	private EntryDocument getEntry(String doc) {
		EntryDocument entry = new EntryDocument();

		if (!doc.equals("")) {
			// Get ID
			String[] docId = doc.split(".T");
			entry.setDocId(StringUtils.trim(docId[0]));

			// get title
			String[] title = docId[1].split(".A");
			entry.setTitle(StringUtils.trim(title[0]));

			// get author
			String[] author = title[1].split(".B");
			entry.setAuthor(StringUtils.trim(author[0]));

			// get additional
			String[] additional = author[1].split(".W");
			entry.setAdditional(StringUtils.trim(additional[0]));

			// get contents
			entry.setContents(StringUtils.trim(additional[1]));

			return entry;
		}

		return null;
	}

	private void addDoc(IndexWriter w, EntryDocument entry) throws IOException {
		Document doc = new Document();
		doc.add(new TextField("docId", entry.getDocId(), Field.Store.YES));
		doc.add(new TextField("title", entry.getTitle(), Field.Store.YES));
		doc.add(new TextField("content", entry.getContents(), Field.Store.YES));
		doc.add(new TextField("author", entry.getAuthor(), Field.Store.YES));
		doc.add(new TextField("additional", entry.getAdditional(), Field.Store.YES));
		w.addDocument(doc);
	}
}