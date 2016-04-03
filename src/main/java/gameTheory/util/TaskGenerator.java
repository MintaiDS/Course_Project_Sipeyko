package gameTheory.util;

import org.json.simple.JSONObject;

import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * Created by NotePad on 20.03.2016.
 */
public class TaskGenerator {

    public static JSONObject task1(){
        Random rand = new Random();
        int n = rand.nextInt(6)+3;
        int m = rand.nextInt(6)+3;
        int [][] matrix = generateMatrix(n, m);
        int type = rand.nextInt(5);
        int ans = 0;
        StringBuilder text = new StringBuilder("<p>Найти оптимальную стратегию ");
        switch (type){
            case 0:
                text.append("по критерию Лапласа.\n");
                ans = solveLaplass(matrix, n, m);
                break;
            case 1:
                text.append("по критерию Вальда.\n");
                ans = solveVald(matrix, n, m);
                break;
            case 2:
                text.append("по критерию крайнего оптимизма.\n");
                ans = solveOptim(matrix, n, m);
                break;
            case 3:
                text.append("по критерию Гурвица с показателем ");
                Double a = 0.0;
                while (a == 0.0) {
                    a = rand.nextDouble();
                }
                text.append(String.format("%.2f",a));
                text.append("\n");
                ans = solveGurvic(matrix, n, m, a);
                break;
            case 4:
                text.append("по критерию Сэвиджа.\n");
                ans = solveSavage(matrix, n, m);
                break;
        }
        text.append(printMatrix(matrix, n, m));
        text.append("</p>");
        JSONObject res = new JSONObject();
        res.put("text", text.toString());
        res.put("answer", ans);
        return res;
    }
    public static JSONObject task2(){
        Random rand = new Random();
        int n = rand.nextInt(6)+3;
        int m = rand.nextInt(6)+3;
        int [][] matrix = generateMatrix(n, m);
        StringBuilder text = new StringBuilder("<p>Найти цену игры с матрицей выигрышей:\n");
        text.append(printMatrix(matrix, n, m));
        text.append("\n");
        text.append("Если решения в чистых стратегиях нет, напишите \"НЕТ\".</p>");
        String ans = minMax(matrix, n, m);
        JSONObject res = new JSONObject();
        res.put("text", text.toString());
        res.put("answer", ans);
        return res;
    }

    private static int[][] generateMatrix(int n, int m){
        Random rand = new Random();
        int[][] res = new int[n][m];
        for (int i = 0; i < n; ++i){
            for (int j = 0; j < m; ++j){
                res[i][j] = rand.nextInt(n*m)-n*m/2;
            }
        }
        return res;
    }
    private static String printMatrix(int[][] matrix, int n, int m){
        StringBuilder text = new StringBuilder("<table>");
        for (int i = 0; i < n; ++i){
            text.append("<tr>");
            for (int j = 0; j < m; ++j){
                text.append("<td>"+ matrix[i][j] + "</td>");
            }
            text.append("</tr>");
        }
        text.append("</table>");
        return text.toString();
    }

    private static int solveLaplass(int[][] matrix, int n, int m){
        int res = 0;
        for (int j = 0; j < m; ++j){
            res += matrix[0][j];
        }
        int ind = 1;
        int buf;
        for (int i = 1; i < n; ++i){
            buf = 0;
            for (int j = 0; j < m; ++j){
                buf += matrix[i][j];
            }
            if (buf > res){
                res = buf;
                ind = i+1;
            }
        }
        return ind;
    }

    private static int solveVald(int[][] matrix, int n, int m){
        int res = matrix[0][0];
        for (int j = 1; j < m; ++j){
            res = Math.min(res, matrix[0][j]);
        }
        int ind = 1;
        int buf;
        for (int i = 1; i < n; ++i){
            buf = matrix[i][0];
            for (int j = 1; j < m; ++j){
                buf = Math.min(buf, matrix[i][j]);
            }
            if (buf > res){
                res = buf;
                ind = i+1;
            }
        }
        return ind;
    }

    private static int solveOptim(int[][] matrix, int n, int m){
        int res = matrix[0][0];
        for (int j = 1; j < m; ++j){
            res = Math.max(res, matrix[0][j]);
        }
        int ind = 1;
        int buf;
        for (int i = 1; i < n; ++i){
            buf = matrix[i][0];
            for (int j = 1; j < m; ++j){
                buf = Math.max(buf, matrix[i][j]);
            }
            if (buf > res){
                res = buf;
                ind = i+1;
            }
        }
        return ind;
    }
    private static int solveGurvic(int[][] matrix, int n, int m, double a){
        int resmin = matrix[0][0];
        int resmax = matrix[0][0];
        for (int j = 1; j < m; ++j){
            resmin = Math.min(resmin, matrix[0][j]);
            resmax = Math.max(resmax, matrix[0][j]);
        }
        double res = a*resmin+(1.-a)*resmax;
        int ind = 1;
        int bufmin;
        int bufmax;
        double buf;
        for (int i = 1; i < n; ++i){
            bufmin = matrix[i][0];
            bufmax = matrix[i][0];
            for (int j = 1; j < m; ++j) {
                bufmin = Math.min(bufmin, matrix[i][j]);
                bufmax = Math.max(bufmax, matrix[i][j]);
            }
            buf = a*bufmin+(1.-a)*bufmax;
            if (buf > res){
                res = buf;
                ind = i+1;
            }
        }
        return ind;
    }

    private static int solveSavage(int[][] matrix, int n, int m){
        int resmax = matrix[0][0];
        for (int i = 0; i < n; ++i){
            resmax = Math.max(resmax, matrix[i][0]);
        }
        int res = resmax - matrix[0][0];
        for (int j = 1; j < m; ++j){
            resmax = matrix[0][j];
            for (int i = 0; i < n; ++i){
                resmax = Math.max(resmax, matrix[i][j]);
            }
            res = Math.max(res, resmax - matrix[0][j]);
        }
        int ind = 1;
        int bufmax;
        int buf;
        for (int k = 1; k < n; ++k){
            bufmax = matrix[0][0];
            for (int i = 0; i < n; ++i){
                bufmax = Math.max(bufmax, matrix[i][0]);
            }
            buf = bufmax - matrix[k][0];
            for (int j = 1; j < m; ++j){
                bufmax = matrix[k][j];
                for (int i = 0; i < n; ++i){
                    bufmax = Math.max(bufmax, matrix[i][j]);
                }
                buf = Math.max(buf, resmax - matrix[k][j]);
            }
            if (buf < res){
                res = buf;
                ind = k+1;
            }
        }
        return ind;
    }

    static String minMax(int [][] matrix, int n, int m){
        int minmax = matrix[0][0];
        for (int i = 1; i < n; ++i)
            minmax = Math.max(minmax, matrix[i][0]);
        int maxmin = matrix[0][0];
        for (int j = 1; j < m; ++j)
            maxmin = Math.min(maxmin, matrix[0][j]);
        int bufmin;
        int bufmax;
        for (int i = 1; i < n; ++i){
            bufmin = matrix[i][0];
            for (int j = 1; j < m; ++j){
                bufmin = Math.min(bufmin, matrix[i][j]);
            }
            if (bufmin > maxmin){
                maxmin = bufmin;
            }
        }
        for (int j = 0; j < m; ++j){
            bufmax = matrix[0][j];
            for (int i = 1; i < n; ++i){
                bufmax = Math.max(bufmax, matrix[i][j]);
            }
            if (bufmax < minmax){
                minmax = bufmax;
            }
        }
        if (maxmin == minmax){
            return String.valueOf(minmax);
        }
        return "НЕТ";
    }
}
