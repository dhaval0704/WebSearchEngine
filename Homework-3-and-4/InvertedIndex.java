package ir.vsr;

import java.io.*;
import java.util.*;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import ir.utilities.*;
import ir.classifiers.*;

public class InvertedIndex {

  ServletContext context; 
  ServletResponse response;
  ServletRequest request;
  public static final int MAX_RETRIEVALS = 500;

  public Map<String, TokenInfo> tokenHash = null;

  // Contains a list of indexed documents
  public List<DocumentReference> docRefs = null;

  public File dirFile = null;

  // type of file (HTML, TEXT)
  public short docType = DocumentIterator.TYPE_TEXT;

  public boolean stem = false;
  public boolean feedback = false;

  //Directory of files To Index, Type Of Doc
  public InvertedIndex(File dirFile, short docType, boolean stem, boolean feedback) {
    this.dirFile = dirFile;
    this.docType = docType;
    this.stem = stem;
    this.feedback = feedback;
    tokenHash = new HashMap<String, TokenInfo>();
    docRefs = new ArrayList<DocumentReference>();
    indexDocuments();
  }

  public InvertedIndex(List<Example> examples) {
    tokenHash = new HashMap<String, TokenInfo>();
    docRefs = new ArrayList<DocumentReference>();
    indexDocuments(examples);
  }


  public InvertedIndex() {
	// TODO Auto-generated constructor stub
  }

  protected void indexDocuments() {
    if (!tokenHash.isEmpty() || !docRefs.isEmpty()) {
      throw new IllegalStateException("Cannot indexDocuments more than once in the same InvertedIndex");
    }
    
    DocumentIterator docIter = new DocumentIterator(dirFile, docType, stem);
    System.out.println("Indexing documents in " + dirFile);

    //process each document
    while (docIter.hasMoreDocuments()) {
      FileDocument doc = docIter.nextDocument();
      System.out.print(doc.file.getName() + ",");
      HashMapVector vector = doc.hashMapVector();
      indexDocument(doc, vector);
    }
    computeIDFandDocumentLengths();
    System.out.println("\nIndexed " + docRefs.size() + " documents with " + size() + " unique terms.");
  }

  public void indexDocuments(List<Example> examples) {
    if (!tokenHash.isEmpty() || !docRefs.isEmpty()) {
      throw new IllegalStateException("Cannot indexDocuments more than once in the same InvertedIndex");
    }

    for (Example example : examples) {
      FileDocument doc = example.getDocument();
      HashMapVector vector = example.getHashMapVector();
      indexDocument(doc, vector);
    }
    computeIDFandDocumentLengths();
    System.out.println("Indexed " + docRefs.size() + " documents with " + size() + " unique terms.");
  }

  protected void indexDocument(FileDocument doc, HashMapVector vector) {
    DocumentReference docRef = new DocumentReference(doc);
    docRefs.add(docRef);
    for (Map.Entry<String, Weight> entry : vector.entrySet()) {
      String token = entry.getKey();
      int count = (int) entry.getValue().getValue();
      indexToken(token, count, docRef);
    }
  }
  
  protected void indexToken(String token, int count, DocumentReference docRef) {
    TokenInfo tokenInfo = tokenHash.get(token);
    if (tokenInfo == null) {
      tokenInfo = new TokenInfo();
      tokenHash.put(token, tokenInfo);
    }
    tokenInfo.occList.add(new TokenOccurrence(docRef, count));
  }

  protected void computeIDFandDocumentLengths() {
    double N = docRefs.size();
    Iterator<Map.Entry<String, TokenInfo>> mapEntries = tokenHash.entrySet().iterator();
    while (mapEntries.hasNext()) {
      Map.Entry<String, TokenInfo> entry = mapEntries.next();

      TokenInfo tokenInfo = entry.getValue();
      double numDocRefs = tokenInfo.occList.size();
      double idf = Math.log(N / numDocRefs);
      if (idf == 0.0)
        mapEntries.remove();
      else {
        tokenInfo.idf = idf;
        for (TokenOccurrence occ : tokenInfo.occList) {
          occ.docRef.length = occ.docRef.length + Math.pow(idf * occ.count, 2);
        }
      }
    }
    for (DocumentReference docRef : docRefs) {
      docRef.length = Math.sqrt(docRef.length);
    }
  }

  public void print() {
    for (Map.Entry<String, TokenInfo> entry : tokenHash.entrySet()) {
      String token = entry.getKey();
      System.out.println(token + " (IDF=" + entry.getValue().idf + ") occurs in:");
      for (TokenOccurrence occ : entry.getValue().occList) {
        System.out.println("   " + occ.docRef.file.getName() + " " + occ.count +
            " times; |D|=" + occ.docRef.length);
      }
    }
  }

  public int size() {
    return tokenHash.size();
  }

  public void clear() {
    docRefs.clear();
    tokenHash.clear();
  }

  public Retrieval[] retrieve(String input) {
    return retrieve(new TextStringDocument(input, stem));
  }

  public Retrieval[] retrieve(Document doc) {
    return retrieve(doc.hashMapVector());
  }

  public Retrieval[] retrieve(HashMapVector vector) {
    Map<DocumentReference, DoubleValue> retrievalHash =
        new HashMap<DocumentReference, DoubleValue>();
    double queryLength = 0.0;
    for (Map.Entry<String, Weight> entry : vector.entrySet()) {
      String token = entry.getKey();
      double count = entry.getValue().getValue();
      queryLength = queryLength + incorporateToken(token, count, retrievalHash);
    }
    queryLength = Math.sqrt(queryLength);
    Retrieval[] retrievals = new Retrieval[retrievalHash.size()];
    int retrievalCount = 0;
    for (Map.Entry<DocumentReference, DoubleValue> entry : retrievalHash.entrySet()) {
      DocumentReference docRef = entry.getKey();
      double score = entry.getValue().value;
      retrievals[retrievalCount++] = getRetrieval(queryLength, docRef, score);
    }
    Arrays.sort(retrievals);
    return retrievals;
  }

  protected Retrieval getRetrieval(double queryLength, DocumentReference docRef, double score) {
    score = score / (queryLength * docRef.length);
    return new Retrieval(docRef, score);
  }

  public double incorporateToken(String token, double count,
                                 Map<DocumentReference, DoubleValue> retrievalHash) {
    TokenInfo tokenInfo = tokenHash.get(token);
    if (tokenInfo == null) return 0.0;
    double weight = tokenInfo.idf * count;
    for (TokenOccurrence occ : tokenInfo.occList) {
      DoubleValue val = retrievalHash.get(occ.docRef);
      if (val == null) {
        val = new DoubleValue(0.0);
        retrievalHash.put(occ.docRef, val);
      }
      val.value = val.value + weight * tokenInfo.idf * occ.count;
    }
    // Return the square of the weight of this token in the query
    return weight * weight;
  }

  String userSearch = "";
  public void getUserSearchQuery(String search) {
	  userSearch = search;
  }
  
  public void processQueries() {

    System.out.println("Now able to process queries. When done, enter an empty query to exit.");

    do {
    	String query = userSearch;
      // If query is empty then exit the loop
      if (query.equals(""))
        break;
      // Get the ranked retrievals for this query string and present them
      System.out.println(query);
      HashMapVector queryVector = (new TextStringDocument(query, stem)).hashMapVector();
      Retrieval[] retrievals = retrieve(queryVector);
      showRetrievals(retrievals);
      break;
   //   presentRetrievals(queryVector, retrievals);
    }
    while (true);
  }

  public void presentRetrievals(HashMapVector queryVector, Retrieval[] retrievals) {
    if (showRetrievals(retrievals)) {
      Feedback fdback = null;
      if (feedback)
        fdback = new Feedback(queryVector, retrievals, this);
      int currentPosition = MAX_RETRIEVALS;
      int showNumber = 0;
      do {
        String command = UserInput.prompt("\n Enter command:  ");
        if (command.equals(""))
          break;
        if (command.equals("m")) {
          printRetrievals(retrievals, currentPosition);
          currentPosition = currentPosition + MAX_RETRIEVALS;
          continue;
        }
        if (command.equals("r") && feedback) {
          if (fdback.isEmpty()) {
            System.out.println("Need to first view some documents and provide feedback.");
            continue;
          }
          System.out.println("Positive docs: " + fdback.goodDocRefs +
              "\nNegative docs: " + fdback.badDocRefs);
          System.out.println("Executing New Expanded and Reweighted Query: ");
          queryVector = fdback.newQuery();
          retrievals = retrieve(queryVector);
          fdback.retrievals = retrievals;
          if (showRetrievals(retrievals))
            continue;
          else
            break;
        }
        try {
          showNumber = Integer.parseInt(command);
        }
        catch (NumberFormatException e) {
          // If not a number, it is an unknown command
          System.out.println("Unknown command.");
          System.out.println("Enter `m' to see more, a number to show the nth document, nothing to exit.");
          if (feedback && !fdback.isEmpty())
            System.out.println("Enter `r' to use any feedback given to `redo' with a revised query.");
          continue;
        }
        if (showNumber > 0 && showNumber <= retrievals.length) {
          System.out.println("Showing document " + showNumber + " in the " + Browser.BROWSER_NAME + " window.");
          Browser.display(retrievals[showNumber - 1].docRef.file);
          if (feedback && !fdback.haveFeedback(showNumber))
            fdback.getFeedback(showNumber);
        } else {
          System.out.println("No such document number: " + showNumber);
        }
      }
      while (true);
    }
  }

  public boolean showRetrievals(Retrieval[] retrievals) {
    if (retrievals.length == 0) {
      System.out.println("\nNo matching documents found.");
      return false;
    } else {
      System.out.println("\nTop " + MAX_RETRIEVALS + " matching Documents from most to least relevant:");
      printRetrievals(retrievals, 0);
      System.out.println("\nEnter `m' to see more, a number to show the nth document, nothing to exit.");
      if (feedback)
        System.out.println("Enter `r' to use any relevance feedback given to `redo' with a revised query.");
      return true;
    }
  }

  //Create Text File and PRint to console
  public void printRetrievals(Retrieval[] retrievals, int start) {
    System.out.println("");
    if (start >= retrievals.length)
      System.out.println("No more retrievals.");
    
    File file = new File("P:\\MS_CS\\W_16\\CS_454\\workspace_454\\Hw3Hw4_CS454\\searchResults.txt");
    FileWriter fw = null;
	try {
		fw = new FileWriter(file.getAbsoluteFile());
	} catch (IOException e1) {
		e1.printStackTrace();
	}
	BufferedWriter bw = new BufferedWriter(fw);
	
    for (int i = start; i < Math.min(retrievals.length, start + MAX_RETRIEVALS); i++) {
      
		try {
			bw.write((i+1) + " " + retrievals[i].docRef.file.getName() + " " + retrievals[i].score);
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}        
		
      System.out.println(MoreString.padTo((i + 1) + ". ", 4) +
          MoreString.padTo(retrievals[i].docRef.file.getName(), 20) +
          " Score: " +
          MoreMath.roundTo(retrievals[i].score, 5));
    }
    
	try {
		bw.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
	System.out.println("Done");
   
  }

  public static void main(String[] args) {
    String dirName = args[args.length - 1];
    short docType = DocumentIterator.TYPE_TEXT;
    boolean stem = false, feedback = false;
    for (int i = 0; i < args.length - 1; i++) {
      String flag = args[i];
      if (flag.equals("-html"))
        // Create HTMLFileDocuments to filter HTML tags
        docType = DocumentIterator.TYPE_HTML;
      else if (flag.equals("-stem"))
        stem = true;
      else if (flag.equals("-feedback"))
        // Use relevance feedback
        feedback = true;
      else {
        throw new IllegalArgumentException("Unknown flag: "+ flag);
      }
    }
    
    InvertedIndex index = new InvertedIndex(new File(dirName), docType, stem, feedback);
    // index.print();
    index.processQueries();
  }


}

   
