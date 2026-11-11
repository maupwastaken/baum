import basis.*;
import org.json.*;

public class Baum {
    private Element root;

    public Baum() {

    }

    private Element internAdd(Element node, int index) {
        if (node == null) {
            node = new Element(index);
        } else {
            if (index < node.index) node.smaller = internAdd(node.smaller, index);
            if (index > node.index) node.bigger = internAdd(node.bigger, index);
        }
        return node;
    }

    private String internPrint(Element node) {
        if (node == null) return "";

        String line = "";
        line = line + internPrint(node.smaller);
        line = line + " " + node.index + " ";
        line = line + internPrint(node.bigger);

        return line;
    }

    private boolean internContains(Element node, int index) {
        if (node == null) return false;

        if (index < node.index) return internContains(node.smaller, index);
        if (index > node.index) return internContains(node.bigger, index);
        return true;
    }

    private int internKleinstes(Element node) {
        if (node.smaller == null) return node.index;
        return internKleinstes(node.smaller);
    }

    private Element internDelete(Element node, int index) {
        if (node == null) return null;

        if (index < node.index) {
            node.smaller = internDelete(node.smaller, index);
        } else if (index > node.index) {
            node.bigger = internDelete(node.bigger, index);
        } else {
            if (node.smaller == null) return node.bigger;
            if (node.bigger == null) return node.smaller;

            node.index = internKleinstes(node.bigger);
            node.bigger = internDelete(node.bigger, node.index);
        }
        return node;
    }

    private static void internZeichne(Stift stift, JSONObject node, double x, double y, double dx, double dy, double radius) {
        if (node == null) return;

        JSONObject smaller = node.optJSONObject("smaller");
        JSONObject bigger = node.optJSONObject("bigger");

        stift.setzeFuellMuster(0);
        stift.setzeFarbe(Farbe.DUNKELGRAU);
        if (smaller != null) stift.linie(x, y, x - dx, y + dy);
        if (bigger != null) stift.linie(x, y, x + dx, y + dy);

        internZeichne(stift, smaller, x - dx, y + dy, dx / 2, dy, radius);
        internZeichne(stift, bigger, x + dx, y + dy, dx / 2, dy, radius);

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

    private static int internEbenen(JSONObject node) {
        if (node == null) return 0;

        int links = internEbenen(node.optJSONObject("smaller"));
        int rechts = internEbenen(node.optJSONObject("bigger"));

        if (links > rechts) {
            return links + 1;
        } else {
            return rechts + 1;
        }
    }

    public static void zeichne(Stift stift, JSONObject baum, double breite, double hoehe) {
        if (baum == null) return;

        int ebenen = internEbenen(baum);
        double dy = hoehe / ebenen;
        double radius = Math.min(15, dy / 3);

        internZeichne(stift, baum, breite / 2, dy / 2, breite / 4, dy, radius);
    }

    public JSONObject internOutput(Element node, JSONArray array) {
        JSONObject json = new JSONObject();
        JSONArray array2 = new JSONArray(array);
        JSONArray array3 = new JSONArray(array);

        if (node == null) return null;

        if (node.smaller != null) {
            array2.put("left");
            json.put("smaller", internOutput(node.smaller, array2));
        }
        if (node.bigger != null) {
            array3.put("right");
            json.put("bigger", internOutput(node.bigger, array3));
        }

        json.put("self", node.index);
        json.put("steps", array);

        return json;
    }

    public JSONObject externZeichnen() {
        JSONArray array = new JSONArray();

        return internOutput(root, array);
    }

    public void add(int index) {
        root = internAdd(root, index);
    }

    public void delete(int index) {
        root = internDelete(root, index);
    }

    public String print() {
        return internPrint(root);
    }

    public boolean contains(int index) {
        return internContains(root, index);
    }

    private int internEbenen(Element node) {
        if (node == null) {
            return 0;
        }

        int links = internEbenen(node.smaller);
        int rechts = internEbenen(node.bigger);

        if (links > rechts) {
            return links + 1;
        } else {
            return rechts + 1;
        }
    }

    public int ebenen() {
        return internEbenen(root);
    }
}
