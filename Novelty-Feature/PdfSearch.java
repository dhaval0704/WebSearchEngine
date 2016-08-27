package hw.lucene.index;

import org.apache.lucene.queryParser.ParseException;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PdfSearch {
	// location where the index will be stored.
	String dataDir = "C:\\Users\\Dhaval\\Desktop\\SearchEngineLUCN\\resource\\";
	private static final String INDEX_DIR = "resource/index";
	private static final int DEFAULT_RESULT_SIZE = 100;

	public static void main(String[] args) throws IOException, ParseException {

		File pdfFile = new File("resource/FTF_2016-03-13_1457926592904.pdf");
		IndexItem pdfIndexItem = index(pdfFile);

		// creating an instance of the indexer class and indexing the items
		Indexer indexer = new Indexer(INDEX_DIR);
		indexer.index(pdfIndexItem);
		indexer.close();

		// creating an instance of the Searcher class to the query the index
		SearcherTest searcher = new SearcherTest(INDEX_DIR);
		int result = searcher.findByContent("Tax", DEFAULT_RESULT_SIZE);
		print(result);
		searcher.close();
	}

	public static List<String> test(String term,String dataDirPath ) throws IOException, ParseException {
		//File pdfFile = new File("C://Users//Dhaval//Desktop//SearchEngineLUCN//resource//FTF_2016-03-13_1457926592904.pdf");
		
		File[] files = new File(dataDirPath).listFiles();

		for (File file : files) {
			if (!file.isDirectory() && !file.isHidden() && file.exists()
					&& file.canRead()) {
				
				IndexItem pdfIndexItem = index(file);

				// creating an instance of the indexer class and indexing the items
				Indexer indexer = new Indexer(INDEX_DIR);
				indexer.index(pdfIndexItem);
				
				indexer.close();

			} else {
				//test(term,file.getCanonicalPath());
			}
		}

		// creating an instance of the Searcher class to the query the index
		SearcherTest searcher = new SearcherTest(INDEX_DIR);
		//int result = searcher.findByContent(term, DEFAULT_RESULT_SIZE);
		List<String> result = searcher.findByContentArray(term, DEFAULT_RESULT_SIZE);
		//print(result);
		searcher.close();
		return result;
	}

	// Extract text from PDF document
	public static IndexItem index(File file) throws IOException {
		PDDocument doc = PDDocument.load(file);
		String content = new PDFTextStripper().getText(doc);
		doc.close();
		return new IndexItem((long) file.getName().hashCode(), file.getName(),
				content);
	}

	// Print the results
	private static void print(int result) {
		if (result == 1)
			System.out.println("Yeah We found this word in document");
		else
			System.out
					.println("The document does not contain the search keyword");

	}
}