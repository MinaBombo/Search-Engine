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

@WebServlet(urlPatterns = "/search_result")
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
        try (PrintWriter printWriter = resp.getWriter()){
            if(browserDocumentList == null){
                printWriter.println("No more results!");
                return;
            }
            for(BrowserDocument browserDocument:browserDocumentList){
                printWriter.println("<a href='"+browserDocument.getUrl()+"'>"+browserDocument.getUrl()+"</a><br>");
                printWriter.println(browserDocument.getDescription()+"<br><br>");
            }
            printWriter.println(
                    "<script>\n" +
                    "    function next(){\n" +
                    "        location.href='search_result?keywords=" + req.getParameter("keywords") + "&page=" + String.valueOf(pageNumber+1) +"';" +
                    "    }\n" +
                    "</script>" +
                    "<div align=\"center\">\n" +
                    "    <form action=\"javascript:next();\">\n" +
                    "        <input type=\"submit\" value=\"Next\">\n" +
                    "    </form>\n" +
                    "</div>");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}

