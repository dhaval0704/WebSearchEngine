package hw.ui.controller;

import hw.lucene.index.Constants;
import hw.lucene.index.Searcher;
import hw.lucene.index.TestBoolean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;

@WebServlet("/ANDController")
public class ANDController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	String indexDir = "P:\\MS_CS\\W_16\\CS_454\\workspace_454\\SearchEngineLUCN\\IndexedFiles\\";
	String dataDir = "P:\\MS_CS\\W_16\\CS_454\\workspace_454\\SearchEngineLUCN\\filesToIndex\\";
	Searcher searcher; 
	ANDController tester;
	
	List<SearchResults> searchResults;
	int counter = 1;
	
    public ANDController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("booleanAnd.jsp");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String searchTerm = request.getParameter("searchTerm");
		String searchTerm2 = request.getParameter("searchTerm2");
		
		try {
			tester = new ANDController();
			List<SearchResults> results = tester.searchUsingBooleanQuery(searchTerm, searchTerm2);
			request.setAttribute("results", results);
			request.getRequestDispatcher("/booleanAnd.jsp").forward(request, response);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public List<SearchResults> searchUsingBooleanQuery(String searchQuery1,
		      String searchQuery2)throws IOException, ParseException{
		
		searchResults = new ArrayList<SearchResults>();
		      searcher = new Searcher(indexDir);
		      long startTime = System.currentTimeMillis();
		      Term term1 = new Term(Constants.CONTENTS, searchQuery1);
		      Query query1 = new TermQuery(term1);
		      Term term2 = new Term(Constants.CONTENTS, searchQuery2);
		      Query query2 = new PrefixQuery(term2);
		      BooleanQuery query = new BooleanQuery();
		      query.add(query1,BooleanClause.Occur.MUST);
		      query.add(query2,BooleanClause.Occur.MUST);
		      TopDocs hits = searcher.search(query);
		      long endTime = System.currentTimeMillis();
		      for(ScoreDoc scoreDoc : hits.scoreDocs) {
		         Document doc = searcher.getDocument(scoreDoc);
		         searchResults.add(new SearchResults(counter++, scoreDoc.score, doc.get(Constants.FILE_PATH), doc.get(Constants.FILE_NAME)));
		      }
		      searcher.close();
		      return searchResults;
		   }

}
