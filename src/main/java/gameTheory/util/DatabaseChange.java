package gameTheory.util;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.json.simple.JSONObject;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.StringRefAddr;
import java.security.NoSuchAlgorithmException;
import java.sql.*;


/**
 * Created by NotePad on 19.03.2016.
 */
public class DatabaseChange {
    static DataSource datasource = new DataSource();
    static {
        PoolProperties p = new PoolProperties();
        p.setUrl("jdbc:mysql://localhost:3306/GameTheory");
        p.setDriverClassName("com.mysql.jdbc.Driver");
        p.setUsername("root");
        p.setPassword("");
        p.setValidationInterval(30000);
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMaxActive(100);
        p.setInitialSize(10);
        p.setMaxWait(10000);
        p.setRemoveAbandonedTimeout(60);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMinIdle(10);
        datasource.setPoolProperties(p);
    }
    public static boolean checkUser(String username, String password){
        Connection conn;
        try {
            conn = datasource.getConnection();
            PreparedStatement st = conn.prepareStatement("SELECT * FROM users WHERE username = ? ");
            st.setString(1, username);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String pass = rs.getString("password");
                String salt = Functions.createSalt(username);
                password = Functions.hashAndSalt(salt, password);
                if (password.equals(pass)) {
                    conn.close();
                    return true;
                }
            }
            conn.close();
            return false;
        }  catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean addUser(String username, String password){
        Connection conn = null;
        try {

            conn = datasource.getConnection();
            PreparedStatement pst = conn.prepareStatement("SELECT username FROM users WHERE username = ?");
            pst.setString(1,username);
            ResultSet rs = pst.executeQuery();
            if (rs.next()){
                return false;
            }
            pst = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?,?)");
            pst.setString(1, username);
            String salt = Functions.createSalt(username);
            pst.setString(2, Functions.hashAndSalt(salt, password));
            if (pst.executeUpdate() > 0){
                conn.close();
                return true;
            } else {
                conn.close();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }  catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static JSONObject getTask(int taskID){
        Connection conn;
        try {
            conn = datasource.getConnection();
            PreparedStatement pst = conn.prepareStatement("SELECT text, answer FROM tasks WHERE type = ?");
            pst.setInt(1, taskID);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                JSONObject res = new JSONObject();
                res.put("text",rs.getString("text"));
                res.put("answer",rs.getInt("answer"));
                conn.close();
                return res;
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addTry(String username, int taskID){
        Connection conn;
        try {
            conn = datasource.getConnection();
            int userID = getUserID(username);
            PreparedStatement pst = conn.prepareStatement("SELECT tried FROM statistics WHERE userID=? AND taskID=?");
            pst.setInt(1,userID);
            pst.setInt(2,taskID);
            ResultSet rs = pst.executeQuery();
            int tries;
            if (rs.next()){
                tries = rs.getInt("tried");
                pst = conn.prepareStatement("UPDATE statistics SET tried=? WHERE userID=? AND taskID=?");
                pst.setInt(1, tries+1);
                pst.setInt(2, userID);
                pst.setInt(3, taskID);
                pst.executeUpdate();
            } else {
                pst = conn.prepareStatement("INSERT INTO statistics VALUES (?,?,?,?)");
                pst.setInt(1, userID);
                pst.setInt(2, taskID);
                pst.setInt(3, 1);
                pst.setInt(4, 0);
                pst.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addSolved(String username, int taskID){
        Connection conn;
        try {
            conn = datasource.getConnection();
            int userID = getUserID(username);
            PreparedStatement pst = conn.prepareStatement("SELECT solved FROM statistics WHERE userID=? AND taskID=?");
            pst.setInt(1,userID);
            pst.setInt(2,taskID);
            ResultSet rs = pst.executeQuery();
            int solved;
            if (rs.next()){
                solved = rs.getInt("solved");
                pst = conn.prepareStatement("UPDATE statistics SET solved=? WHERE userID=? AND taskID=?");
                pst.setInt(1, solved+1);
                pst.setInt(2, userID);
                pst.setInt(3, taskID);
                pst.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getStatistics(String username){
        Connection conn;
        try {
            int userID = getUserID(username);
            StringBuilder sb = new StringBuilder("");
            conn = datasource.getConnection();
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM statistics WHERE userID=?");
            pst.setInt(1, userID);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                int task = rs.getInt("taskID");
                int tried = rs.getInt("tried");
                int solved = rs.getInt("solved");
                Record r = new Record(task, tried, solved);
                sb.append(r.toString());
            }
            if ("".equals(sb.toString())){
                sb.append("<p>Нет статистики. Попробуйте решать задачи.</p>");
            }
            return sb.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static int getUserID(String username){
        Connection conn;
        try {
            conn = datasource.getConnection();
            PreparedStatement pst = conn.prepareStatement("SELECT ID FROM users WHERE username=?");
            pst.setString(1,username);
            ResultSet rs = pst.executeQuery();
            if (rs.next()){
                return rs.getInt("ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
