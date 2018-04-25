package Servlets;

import BusinessModel.BrowserDocument;
import DynamicRanker.DynamicRanker;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.NoSuchElementException;

@WebServlet(urlPatterns = "/search_result.html")
public class SearchResult extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        DynamicRanker dynamicRanker = new DynamicRanker();
        final String keywords[] = req.getParameter("keywords").split(" ");
        final Integer pageNumber = Integer.parseInt(req.getParameter("page"));
        try{
            dynamicRanker.rank(keywords);
        }
        catch (NoSuchElementException exception){
            resp.getWriter().println("No Search Results found for you query");
            return;
        }
        List<BrowserDocument> browserDocumentList = dynamicRanker.getBrowserDocuments(pageNumber);
        // TODO: show urls and parts of documents
        // TODO: handle url redirection
        try (PrintWriter printWriter = resp.getWriter()){
            for(BrowserDocument browserDocument:browserDocumentList){
                printWriter.println(browserDocument.getUrl());
                printWriter.println(browserDocument.getDescription());
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

