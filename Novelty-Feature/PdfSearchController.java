package hw.ui.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.queryParser.ParseException;

/**
 * Servlet implementation class PdfSearch
 */
@WebServlet("/PdfSearch")
public class PdfSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
      PdfSearch tester;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PdfSearch() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.sendRedirect("pdfsearch.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
        String searchTerm = request.getParameter("searchTerm");
		
		try {
			List<String> i=hw.lucene.index.PdfSearch.test(searchTerm,"C://Users//Dhaval//Desktop//SearchEngineLUCN//resource");
			
			request.setAttribute("results", i);
			request.getRequestDispatcher("/pdfsearch.jsp").forward(request, response);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
