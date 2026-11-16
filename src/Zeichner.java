import basis.*;
import org.json.*;

public class Zeichner {
    private Leinwand leinwand;
    private Stift stift;

    public Zeichner(Leinwand leinwand) {
        this.leinwand = leinwand;
        this.stift = new Stift(leinwand);
    }

    public static int ebenen(JSONObject node) {
        if (node == null) return 0;

        int links = ebenen(node.optJSONObject("smaller"));
        int rechts = ebenen(node.optJSONObject("bigger"));

        if (links > rechts) {
            return links + 1;
        } else {
            return rechts + 1;
        }
    }

    private void internZeichne(JSONObject node, double x, double y, double dx, double dy, double radius) {
        if (node == null) return;

        JSONObject smaller = node.optJSONObject("smaller");
        JSONObject bigger = node.optJSONObject("bigger");

        stift.setzeFuellMuster(0);
        stift.setzeFarbe(Farbe.DUNKELGRAU);
        if (smaller != null) stift.linie(x, y, x - dx, y + dy);
        if (bigger != null) stift.linie(x, y, x + dx, y + dy);

        internZeichne(smaller, x - dx, y + dy, dx / 2, dy, radius);
        internZeichne(bigger, x + dx, y + dy, dx / 2, dy, radius);

        stift.setzeFuellMuster(1);
        stift.setzeFarbe(Farbe.GELB);
        stift.kreis(x, y, radius);
        stift.setzeFuellMuster(0);
        stift.setzeFarbe(Farbe.SCHWARZ);
        stift.kreis(x, y, radius);

        String text = "" + node.getInt("self");
        stift.bewegeBis(x - stift.textBreite(text) / 2.0, y + stift.textHoehe() / 3.0);
        stift.schreibeText(text);
    }

    public void zeichne(JSONObject json) {
        leinwand.loescheAlles();

        if (json == null) return;

        double breite = leinwand.breite();
        double hoehe = leinwand.hoehe();

        int ebenen = ebenen(json);
        double dy = hoehe / ebenen;
        double radius = Math.min(15, dy / 3);

        internZeichne(json, breite / 2, dy / 2, breite / 4, dy, radius);
    }
}
