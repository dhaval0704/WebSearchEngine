package hw.index;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

public class GenerateIndexFiles {

	//Index Directory Path
	String filesToIndex 		= "D:\\Index";
	//Index Data Directory Path
	String indexedDataDirectory = "D:\\Data";
	
	Indexer indexer;
	SearchTerm searchTerm;

	public static void main(String[] args) throws ParseException {
		GenerateIndexFiles driver;
		try {
			driver = new GenerateIndexFiles();
			driver.createIndex();
			driver.search("csula");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createIndex() throws IOException {
		indexer = new Indexer(filesToIndex);
		int numIndexed;
		//response time
		long startTime = System.currentTimeMillis();
		numIndexed = indexer.createIndex(indexedDataDirectory, new FileExtention());
		long endTime = System.currentTimeMillis();
		indexer.close();
		System.out.println(numIndexed + " File indexed, time taken: "
				+ (endTime - startTime) + " ms");
	}

	private void search(String searchQuery) throws IOException, ParseException {
		searchTerm = new SearchTerm(filesToIndex);
		long startTime = System.currentTimeMillis();
		TopDocs hits = searchTerm.search(searchQuery);
		long endTime = System.currentTimeMillis();

		System.out.println(hits.totalHits + " documents found. Time :"
				+ (endTime - startTime) + " ms");
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = searchTerm.getDocument(scoreDoc);
			System.out.println("File: " + doc.get(Constants.FILE_PATH));
		}
		searchTerm.close();
	}

}
