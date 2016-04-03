package gameTheory.server;

/**
 * Created by NotePad on 13.03.2016.
 */

import gameTheory.util.DatabaseChange;
import gameTheory.util.Functions;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader br = req.getReader();
        String data = br.readLine();
        try {
            JSONObject json = Functions.stringToJson(data);
            String user =json.get("username").toString();
            String password = json.get("password").toString();
            if (DatabaseChange.checkUser(user, password)) {
                HttpSession session = req.getSession();
                session.setAttribute("username", user);
                PrintWriter out = resp.getWriter();
                out.print(req.getContextPath() + "/index.html");
                out.flush();
                out.close();
            } else {
                resp.setStatus(403);
                PrintWriter out = resp.getWriter();
                out.print("Wrong username or password.");
                out.flush();
                out.close();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
