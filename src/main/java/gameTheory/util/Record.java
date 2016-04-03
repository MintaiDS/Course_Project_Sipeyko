package gameTheory.util;

/**
 * Created by NotePad on 03.04.2016.
 */
public class Record {
    private int theme;
    private int tried;
    private int solved;

    public Record(int theme, int tried, int solved) {
        this.theme = theme;
        this.tried = tried;
        this.solved = solved;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("");
        res.append("<p>Тема №" + theme + ":<br>");
        res.append("Решено: " + solved + "\t");
        res.append("Всего попыток: " + tried + "</p>");
        return res.toString();
    }
}
