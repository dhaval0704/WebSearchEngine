package se.presentation.controller;

import ir.vsr.InvertedIndex;
import ir.vsr.PageRankInvertedIndex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/SearchTerm")
public class SearchTerm extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public SearchTerm() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("searchterm.jsp");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		String searchTerm = request.getParameter("searchTerm");
		
		PageRankInvertedIndex pageRankInvertedIndex = new PageRankInvertedIndex();
		InvertedIndex index = new InvertedIndex();
		
		String[] args = new String[4];
		
		args[0] = "-html";
		args[1] = "-weight";
		args[2] = "0";
		args[3] = "P:\\MS_CS\\W_16\\CS_454\\workspace_454\\Hw3Hw4_CS454\\students-page";
		
		pageRankInvertedIndex.main(args, searchTerm);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
        List<String> arrayList = new ArrayList<String>();
        for (String line : Files.readAllLines(Paths.get("P:\\MS_CS\\W_16\\CS_454\\workspace_454\\Hw3Hw4_CS454\\searchResults.txt"))) {
            for (String part : line.split("\\s+")) {
                arrayList.add(part);
            }
        }
		
  		getServletContext().setAttribute("searchValue", arrayList);
		response.sendRedirect("searchterm.jsp");
		
	}

}
