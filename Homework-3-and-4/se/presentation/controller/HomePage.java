package se.presentation.controller;

import ir.webutils.PageRankSiteSpider;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/HomePage")
public class HomePage extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public HomePage() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("homepage.jsp");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		String url = request.getParameter("url");
		
		PageRankSiteSpider siteSpider = new PageRankSiteSpider();
		
		String[] args = new String[6];
			
		args[0] = "-u";
		args[1] = url;
		args[2] = "-d";
		args[3] = "P:\\MS_CS\\W_16\\CS_454\\workspace_454\\Hw3Hw4_CS454\\students-page";
		args[4] = "-c";
		args[5] = "50";
		
		siteSpider.main(args);
		
		request.setAttribute("finished", "Indexing Complete!");
		request.getRequestDispatcher("homepage.jsp").forward(request, response);
	}

}
