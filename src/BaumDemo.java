
/**
 * Klasse BaumDemo
 * <p>
 * Roland Stiebel
 * version 1.0
 */

//Import

import basis.*;

import java.awt.*;
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

    private void zeichneBaum() {
        leinwand.loescheAlles();
        Baum.zeichne(stift, baum.externZeichnen(), leinwand.breite(), leinwand.hoehe());
    }

    @Override
    public void bearbeiteKnopfDruck(Knopf k) {
        if (k == ende) {
            this.gibFrei();
        } else if (k == knopf1) {
            baum.add(zahlenfeld.ganzZahl());
            zeichneBaum();
        } else if (k == knopf2) {
            System.out.println(baum.print());
            text1.setzeText(baum.print());
        } else if (k == knopf3) {
            System.out.println("Anzahl der Ebenen:" + baum.ebenen());
            text1.setzeText("Anzahl der Ebenen: " + baum.ebenen());
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
            text1.setzeText(anzahl + " Zufallszahlen eingefügt, " + baum.ebenen() + " Ebenen");
            zeichneBaum();
        }
    }

}
