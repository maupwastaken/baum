
/**
 * Klasse BaumDemo
 * <p>
 * Roland Stiebel
 * version 1.0
 */

//Import

import basis.*;
import org.json.*;

import java.util.Random;

public class BaumDemo extends Fenster implements KnopfLauscher {

    //Deklaration
    private Knopf ende, knopf1, knopf2, knopf3, knopf4, knopf5, knopf6;
    private BeschriftungsFeld label1;
    private ZahlenFeld zahlenfeld;
    private TextFeld text1;
    private Leinwand leinwand;
    private Stift stift;
    private Baum baum;
    private Random zufall;

    // Konstruktor

    public BaumDemo() {
        this.initGui();
    }

    public void initGui() {
        this.setzeGroesse(600, 540);
        this.setzeTitel("BaumDemo");
        ende = new Knopf("Ende", 474, 500, 106, 30);
        ende.setzeKnopfLauscher(this);
        label1 = new BeschriftungsFeld("Zahl:", 10, 10, 50, 30);
        zahlenfeld = new ZahlenFeld(60, 10, 100, 30);
        zahlenfeld.setzeZahl(0);
        text1 = new TextFeld(170, 10, 420, 30);
        text1.setzeText("noch leer");
        leinwand = new Leinwand(10, 50, 580, 400);
        leinwand.setzeHintergrundFarbe(Farbe.WEISS);
        stift = new Stift(leinwand);
        knopf1 = new Knopf("Einfügen", 10, 460, 106, 30);
        knopf1.setzeKnopfLauscher(this);
        knopf2 = new Knopf("Ausgeben", 126, 460, 106, 30);
        knopf2.setzeKnopfLauscher(this);
        knopf3 = new Knopf("Ebenen", 242, 460, 106, 30);
        knopf3.setzeKnopfLauscher(this);
        knopf4 = new Knopf("Suchen", 358, 460, 106, 30);
        knopf4.setzeKnopfLauscher(this);
        knopf5 = new Knopf("JSON", 474, 460, 106, 30);
        knopf5.setzeKnopfLauscher(this);
        knopf6 = new Knopf("Zufällig", 10, 500, 106, 30);
        knopf6.setzeKnopfLauscher(this);

        baum = new Baum();
        zufall = new Random();
    }

    private void zufallsBaum(int anzahl) {
        baum = new Baum();

        int eingefuegt = 0;
        while (eingefuegt < anzahl) {
            int zahl = zufall.nextInt(99) + 1;
            if (!baum.contains(zahl)) {
                baum.add(zahl);
                eingefuegt++;
            }
        }
    }

    private static int ebenen(JSONObject node) {
        if (node == null) return 0;

        int links = ebenen(node.optJSONObject("smaller"));
        int rechts = ebenen(node.optJSONObject("bigger"));

        if (links > rechts) {
            return links + 1;
        } else {
            return rechts + 1;
        }
    }

    private static String ausgabe(JSONObject node) {
        if (node == null) return "";

        String line = "";
        line = line + ausgabe(node.optJSONObject("smaller"));
        line = line + " " + node.getInt("self") + " ";
        line = line + ausgabe(node.optJSONObject("bigger"));

        return line;
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

    private void zeichne(JSONObject json, double breite, double hoehe) {
        if (json == null) return;

        int ebenen = ebenen(json);
        double dy = hoehe / ebenen;
        double radius = Math.min(15, dy / 3);

        internZeichne(json, breite / 2, dy / 2, breite / 4, dy, radius);
    }

    private void zeichneBaum() {
        leinwand.loescheAlles();
        zeichne(baum.externZeichnen(), leinwand.breite(), leinwand.hoehe());
    }

    @Override
    public void bearbeiteKnopfDruck(Knopf k) {
        if (k == ende) {
            this.gibFrei();
        } else if (k == knopf1) {
            baum.add(zahlenfeld.ganzZahl());
            zeichneBaum();
        } else if (k == knopf2) {
            String ausgabe = ausgabe(baum.externZeichnen());
            System.out.println(ausgabe);
            text1.setzeText(ausgabe);
        } else if (k == knopf3) {
            int ebenen = ebenen(baum.externZeichnen());
            System.out.println("Anzahl der Ebenen:" + ebenen);
            text1.setzeText("Anzahl der Ebenen: " + ebenen);
        } else if (k == knopf4) {
            int zahl = zahlenfeld.ganzZahl();
            if (baum.contains(zahl)) {
                text1.setzeText(zahl + " ist im Baum enthalten");
            } else {
                text1.setzeText(zahl + " ist nicht im Baum enthalten");
            }
        } else if (k == knopf5) {
            System.out.println(baum.externZeichnen());
        } else if (k == knopf6) {
            int anzahl = zahlenfeld.ganzZahl();
            if (anzahl < 1 || anzahl > 31) anzahl = 10;

            zufallsBaum(anzahl);
            text1.setzeText(anzahl + " Zufallszahlen eingefügt, " + ebenen(baum.externZeichnen()) + " Ebenen");
            zeichneBaum();
        }
    }

}
