import org.json.*;

public class Baum {
    private Element root;

    public Baum() {

    }

    private int tiefe(Element node) {
        if (node == null) return 0;

        if (node.smallerDepth > node.biggerDepth) return node.smallerDepth + 1;
        return node.biggerDepth + 1;
    }

    private void aktualisiereTiefen(Element node) {
        node.smallerDepth = tiefe(node.smaller);
        node.biggerDepth = tiefe(node.bigger);
    }

    private Element internAdd(Element node, int index) {
        if (node == null) {
            node = new Element(index);
        } else {
            if (index < node.index) node.smaller = internAdd(node.smaller, index);
            if (index > node.index) node.bigger = internAdd(node.bigger, index);
        }
        aktualisiereTiefen(node);
        return node;
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
        aktualisiereTiefen(node);
        return node;
    }

    private JSONObject internOutput(Element node, JSONArray array) {
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
        json.put("smdepth", node.smallerDepth);
        json.put("bidepth", node.biggerDepth);
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

    public boolean contains(int index) {
        return internContains(root, index);
    }
}
