package gameTheory.server;

import gameTheory.util.DatabaseChange;
import gameTheory.util.Functions;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by NotePad on 19.03.2016.
 */
@WebServlet(urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader br = req.getReader();
        String data = br.readLine();
        try {
            JSONObject json = Functions.stringToJson(data);
            String user =json.get("username").toString();
            String password = json.get("password").toString();
            if (DatabaseChange.addUser(user, password)) {
                resp.setStatus(200);
                PrintWriter out = resp.getWriter();
                out.print("Registration successful!");
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
