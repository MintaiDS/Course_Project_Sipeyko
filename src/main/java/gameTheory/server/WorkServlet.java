package gameTheory.server;

import gameTheory.util.DatabaseChange;
import gameTheory.util.TaskGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Locale;

@WebServlet(urlPatterns = {"/index"})
public class WorkServlet extends HttpServlet {

    private static final String url = "jdbc:mysql://localhost:3306/student";
    private static final String user = "MintaiDS";
    private static final String password = "mysql2016";

    private static Connection con;
    private static PreparedStatement stmt;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            con = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int task = Integer.parseInt(req.getParameter("task"));
        resp.setContentType("text/html;charset=Windows-1251");
        HttpSession session = req.getSession();
        String username = session.getAttribute("username").toString();
        System.out.println(username);
        PrintWriter pw = resp.getWriter();
        switch (task){
            case 1:
                DatabaseChange.addTry(username, task);
                pw.print(TaskGenerator.task1());
                pw.flush();
                pw.close();
                break;
            case 2:
                DatabaseChange.addTry(username, task);
                pw.print(TaskGenerator.task2());
                pw.flush();
                pw.close();
                break;
            case -1:
                pw.print(DatabaseChange.getStatistics(username));
                pw.flush();
                pw.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String username = session.getAttribute("username").toString();
        BufferedReader br = req.getReader();
        int task = Integer.parseInt(br.readLine());
        DatabaseChange.addSolved(username, task);
        resp.setStatus(204);
    }

}
