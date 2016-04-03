package gameTheory.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by NotePad on 13.03.2016.
 */
public class Functions {
    public static JSONObject stringToJson(String data) throws ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(data.trim());
    }

    static String createSalt(String str) {
        int mn = Math.min(str.length(), 10);
        int key = 0;
        for (int i = 0; i < mn; ++i){
            key += str.charAt(i);
        }
        int len = str.length();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                if (i < len)
                    buf.append(str.charAt(i));
                else
                    buf.append((char) (33 + key % 94));
            } else
                buf.append((char) (33 + (key) % 31));
            key += len;
        }
        return buf.toString();
    }
    static String hashAndSalt(String salt, String password) throws NoSuchAlgorithmException {
        MessageDigest mDigest = null;
        mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest((salt+password).getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
