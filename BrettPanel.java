import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class BrettPanel extends JPanel {

    public static final int FELD_GROESSE = 80;
    private Spielsteuerung steuerung;
    private SchachGUI gui;

    public BrettPanel(Spielsteuerung steuerung, SchachGUI gui) {
        this.steuerung = steuerung;
        this.gui = gui;
        this.setPreferredSize(new Dimension(8 * FELD_GROESSE, 8 * FELD_GROESSE));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (steuerung.istSpielBeendet()) {
                    return;
                }
                
                int feldX = e.getX() / FELD_GROESSE;
                int feldY = e.getY() / FELD_GROESSE;
                
                steuerung.verarbeiteKlick(feldX, feldY);
                
                repaint();

                if (steuerung.istSpielBeendet()) {
                    JOptionPane.showMessageDialog(null, steuerung.getEndNachricht());
                    steuerung.zeigeZugHistorie();
                    frageNachNeustart();
                }
            }
        });
    }

    private void frageNachNeustart() {
        int wahl = JOptionPane.showConfirmDialog(
            null,
            "Möchtest du eine neue Runde spielen?",
            "Spiel beendet",
            JOptionPane.YES_NO_OPTION
        );

        if (wahl == JOptionPane.YES_OPTION) {
            gui.starteNeuesSpiel();
        } else {
            System.exit(0);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if ((x + y) % 2 == 0) {
                    g.setColor(new Color(240, 217, 181));
                } else {
                    g.setColor(new Color(181, 136, 99));
                }
                g.fillRect(x * FELD_GROESSE, y * FELD_GROESSE, FELD_GROESSE, FELD_GROESSE);
            }
        }
        
        int[] schachPos = steuerung.getKoenigImSchachPos();
        if (schachPos != null) {
            g.setColor(new Color(255, 0, 0, 120));
            g.fillRect(schachPos[0] * FELD_GROESSE, schachPos[1] * FELD_GROESSE, FELD_GROESSE, FELD_GROESSE);
        }
        
        ArrayList<int[]> markierungen = steuerung.getMoeglicheZuege();
        if (markierungen != null) {
            g.setColor(new Color(20, 80, 160, 100));
            for (int[] pos : markierungen) {
                g.fillOval(pos[0] * FELD_GROESSE + FELD_GROESSE/4, 
                           pos[1] * FELD_GROESSE + FELD_GROESSE/4, 
                           FELD_GROESSE/2, FELD_GROESSE/2);
            }
        }

        Schachfigur[][] brett = steuerung.getSchachbrett().getFelder();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (brett[x][y] != null) {
                    brett[x][y].zeichne(g, x, y, FELD_GROESSE);
                }
            }
        }
    }
}