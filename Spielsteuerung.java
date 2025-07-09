import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import java.util.ArrayList;

public class Spielsteuerung {

    private Schachbrett brett;
    private String spielerAmZug;
    private Schachfigur ausgewaehlteFigur;
    private boolean spielBeendet;
    private String endNachricht = "";
    private int[] koenigImSchachPos = null;
    
    private Schachfigur enPassantBauer;
    private ArrayList<String> zugHistorie;

    public Spielsteuerung() {
        this.brett = new Schachbrett();
        this.spielerAmZug = "weiss";
        this.ausgewaehlteFigur = null;
        this.spielBeendet = false;
        this.zugHistorie = new ArrayList<>();
    }

    public void verarbeiteKlick(int feldX, int feldY) {
        if (spielBeendet) return;

        if (ausgewaehlteFigur == null) {
            Schachfigur geklickteFigur = brett.getFigur(feldX, feldY);
            if (geklickteFigur != null && geklickteFigur.getFarbe().equals(spielerAmZug)) {
                ausgewaehlteFigur = geklickteFigur;
            }
        } else {
            if (istZugGueltig(ausgewaehlteFigur, feldX, feldY)) {
                macheZug(ausgewaehlteFigur, feldX, feldY);
                pruefeSpielstatus();
            }
            ausgewaehlteFigur = null;
        }
    }
    
    private void macheZug(Schachfigur figur, int zielX, int zielY) {
        speichereZug(figur, zielX, zielY);
        
        int startX = figur.x;
        int startY = figur.y;

        if (figur instanceof Bauer && zielX != startX && brett.getFigur(zielX, zielY) == null) {
            brett.setFigur(enPassantBauer.x, enPassantBauer.y, null);
        }
        
        brett.fuehreZugAus(figur, zielX, zielY);
        
        if (figur instanceof Koenig && Math.abs(zielX - startX) == 2) {
            Schachfigur turm = brett.getFigur((zielX > startX) ? 7 : 0, startY);
            brett.fuehreZugAus(turm, (zielX > startX) ? 5 : 3, startY);
        }
        
        enPassantBauer = (figur instanceof Bauer && Math.abs(zielY - startY) == 2) ? figur : null;

        pruefeBauernumwandlung(figur);
        spielerAmZug = spielerAmZug.equals("weiss") ? "schwarz" : "weiss";
        
        if (brett.istKoenigImSchach(spielerAmZug)) {
            Koenig bedrohterKoenig = brett.findeKoenig(spielerAmZug);
            koenigImSchachPos = new int[]{bedrohterKoenig.x, bedrohterKoenig.y};
        } else {
            koenigImSchachPos = null;
        }
    }

    private void pruefeSpielstatus() {
        if (!hatSpielerGueltigeZuege(spielerAmZug)) {
            if (koenigImSchachPos != null) {
                endNachricht = "SCHACHMATT! " + (spielerAmZug.equals("weiss") ? "Schwarz" : "Weiss") + " gewinnt!";
            } else {
                endNachricht = "PATT! Unentschieden.";
            }
            spielBeendet = true;
        }
    }
    
    private boolean istZugGueltig(Schachfigur figur, int zielX, int zielY) {
        if (figur instanceof Koenig && Math.abs(figur.x - zielX) == 2) {
            if (!istRochadeGueltig((Koenig) figur, zielX)) return false;
        } else if (figur instanceof Bauer && zielX != figur.x && brett.getFigur(zielX, zielY) == null) {
             if (enPassantBauer == null || enPassantBauer.x != zielX || enPassantBauer.y != figur.y) {
                 return false;
             }
        } else if (!figur.kannZiehen(zielX, zielY, brett.getFelder())) {
            return false;
        }
        
        int startX = figur.x;
        int startY = figur.y;
        Schachfigur geschlageneFigur = brett.getFigur(zielX, zielY);
        
        brett.getFelder()[zielX][zielY] = figur;
        brett.getFelder()[startX][startY] = null;
        figur.x = zielX;
        figur.y = zielY;
        
        boolean imSchach = brett.istKoenigImSchach(figur.getFarbe());
        
        brett.getFelder()[startX][startY] = figur;
        brett.getFelder()[zielX][zielY] = geschlageneFigur;
        figur.x = startX;
        figur.y = startY;
        
        return !imSchach;
    }
    
    private boolean istRochadeGueltig(Koenig koenig, int zielX) {
        if (koenig.hatSichBewegt || brett.istKoenigImSchach(koenig.getFarbe())) return false;
        
        int startY = koenig.y;
        int turmX = (zielX > koenig.x) ? 7 : 0;
        Schachfigur turm = brett.getFigur(turmX, startY);
        if (turm == null || !(turm instanceof Turm) || turm.hatSichBewegt) return false;

        int schritt = (zielX > koenig.x) ? 1 : -1;
        for (int i = koenig.x + schritt; i != turmX; i += schritt) {
            if (brett.getFigur(i, startY) != null) return false;
        }

        String gegner = koenig.getFarbe().equals("weiss") ? "schwarz" : "weiss";
        for (int i = 0; i <= 2; i++) {
            if (brett.istFeldBedroht(koenig.x + schritt * i, startY, gegner)) return false;
        }
        return true;
    }

    private boolean hatSpielerGueltigeZuege(String spielerFarbe) {
        for (int x = 0; x < 8; x++) for (int y = 0; y < 8; y++) {
            Schachfigur figur = brett.getFigur(x, y);
            if (figur != null && figur.getFarbe().equals(spielerFarbe)) {
                for (int zx = 0; zx < 8; zx++) for (int zy = 0; zy < 8; zy++) {
                    if (istZugGueltig(figur, zx, zy)) return true;
                }
            }
        }
        return false;
    }

    private void pruefeBauernumwandlung(Schachfigur figur) {
        if (figur instanceof Bauer && (figur.y == 0 || figur.y == 7)) {
            String[] optionen = {"Dame", "Turm", "Läufer", "Springer"};
            int wahl = JOptionPane.showOptionDialog(null, "Wähle eine Figur für die Umwandlung:",
                    "Bauernumwandlung", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, optionen, optionen[0]);

            Schachfigur neueFigur;
            switch(wahl) {
                case 1: neueFigur = new Turm(figur.x, figur.y, figur.getFarbe()); break;
                case 2: neueFigur = new Laeufer(figur.x, figur.y, figur.getFarbe()); break;
                case 3: neueFigur = new Springer(figur.x, figur.y, figur.getFarbe()); break;
                default: neueFigur = new Dame(figur.x, figur.y, figur.getFarbe()); break;
            }
            brett.setFigur(figur.x, figur.y, neueFigur);
        }
    }
    
    private void speichereZug(Schachfigur figur, int zielX, int zielY) {
        String von = (char)('a' + figur.x) + "" + (8 - figur.y);
        String nach = (char)('a' + zielX) + "" + (8 - zielY);
        int zugNummer = zugHistorie.size() / 2 + 1;
        String farbe = figur.getFarbe().equals("weiss") ? "Weiss" : "Schwarz";
        
        zugHistorie.add(zugNummer + ". " + farbe + ": " + von + " -> " + nach);
    }

    public void zeigeZugHistorie() {
        JDialog historieDialog = new JDialog();
        historieDialog.setTitle("Zug-Historie");
        historieDialog.setSize(300, 400);
        historieDialog.setLocationByPlatform(true);
        historieDialog.setModal(true);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 14));
        
        StringBuilder sb = new StringBuilder("=== Alle Züge ===\n\n");
        for(String zug : zugHistorie) {
            sb.append(zug).append("\n");
        }
        textArea.setText(sb.toString());

        JScrollPane scrollPane = new JScrollPane(textArea);
        historieDialog.add(scrollPane);
        historieDialog.setVisible(true);
    }
    
    public Schachbrett getSchachbrett() { return brett; }
    public boolean istSpielBeendet() { return spielBeendet; }
    public String getEndNachricht() { return endNachricht; }
    public int[] getKoenigImSchachPos() { return koenigImSchachPos; }
    
    public ArrayList<int[]> getMoeglicheZuege() {
        if (ausgewaehlteFigur == null) return null;
        
        ArrayList<int[]> zuege = new ArrayList<>();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (istZugGueltig(ausgewaehlteFigur, x, y)) {
                    zuege.add(new int[]{x, y});
                }
            }
        }
        return zuege;
    }
}