package Servlets;

import BusinessModel.BrowserDocument;
import DynamicRanker.DynamicRanker;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/search_result.html")
public class SearchResult extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        DynamicRanker dynamicRanker = new DynamicRanker();
        final String keywords[] = req.getParameter("keywords").split(" ");
        List<BrowserDocument> browserDocumentList = dynamicRanker.rank(keywords);
        // TODO: show urls and parts of documents
        // TODO: handle url redirection
    }
}
