package Servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

@WebServlet(urlPatterns = {"/home"})
public class Home extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            File file = new File(classLoader.getResource("/home.html").getFile());
            try (Scanner scanner = new Scanner(file)) {
                try(PrintWriter printWriter = resp.getWriter()){
                    while (scanner.hasNextLine()) {
                        printWriter.write(scanner.nextLine());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }
}
