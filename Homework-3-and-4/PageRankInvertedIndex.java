package ir.vsr;

import java.io.*;
import java.util.*;


public class PageRankInvertedIndex extends InvertedIndex {
	private double pageRankWeight;
	private Map<String, Double> rank;

	public PageRankInvertedIndex(File dirFile, short docType, boolean stem,
			boolean feedback, double weight) {
		super(dirFile, docType, stem, feedback);
		pageRankWeight = weight;
		pageRankRead();
	}
	
	public PageRankInvertedIndex() {
	}

	protected Retrieval getRetrieval(double queryLength,
			DocumentReference docRef, double score) {
		score = score / (queryLength * docRef.length);	
		score += pageRankWeight * rank.get(docRef.toString());
		return new Retrieval(docRef, score);
	}

	private void pageRankRead() {
		rank = new HashMap<String, Double>();
		String line;

		try {
			FileReader fstream = new FileReader(dirFile + "/pageRanks");
			BufferedReader in = new BufferedReader(fstream);

			while ((line = in.readLine()) != null) {
				String[] elems = line.split(" ");
				String indexName = elems[0];
				Double rankValue = Double.parseDouble(elems[1]);
				System.out.println(indexName);
				System.out.println(rankValue);
				rank.put(indexName, rankValue);
			}
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

	public static void main(String[] args, String term) {
		
		for (int i = 0; i < args.length; i++) {
			System.out.println("ARGS: "+ i + " "+args[i]);
		}
		
		String dirName = args[args.length - 1];
		short docType = DocumentIterator.TYPE_TEXT;
		double weight = 0;
		boolean stem = false, feedback = false, readWeight = false;
		for (int i = 0; i < args.length - 1; i++) {
			String flag = args[i];
			if (flag.equals("-html"))
				docType = DocumentIterator.TYPE_HTML;
			else if (flag.equals("-stem"))
				stem = true;
			else if (flag.equals("-feedback"))
				feedback = true;
			else if (flag.equals("-weight"))
				readWeight = true;
			else if (readWeight) {
				weight = Double.parseDouble(flag);
				readWeight = false;
			} else {
				throw new IllegalArgumentException("Unknown flag: " + flag);
			}
		}

		PageRankInvertedIndex index = new PageRankInvertedIndex(new File(
				dirName), docType, stem, feedback, weight);
		index.getUserSearchQuery(term);
		index.processQueries();
		
	}
}
