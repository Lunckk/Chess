import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Font;

public class SchachGUI {

    private JFrame fenster;
    private BrettPanel brettPanel;

    public SchachGUI() {
        zeigeStartmenue();
        
        fenster = new JFrame("Schachspiel von Lukas, Aditi und Frederic");
        fenster.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        fenster.setResizable(false);
        
        starteNeuesSpiel();

        fenster.pack();
        fenster.setLocationRelativeTo(null);
        fenster.setVisible(true);
    }

    public void starteNeuesSpiel() {
        if (brettPanel != null) {
            fenster.remove(brettPanel);
        }

        Spielsteuerung neueSteuerung = new Spielsteuerung();
        brettPanel = new BrettPanel(neueSteuerung, this);
        fenster.add(brettPanel);

        fenster.revalidate();
        fenster.repaint();
    }

    private void zeigeStartmenue() {
        JDialog startDialog = new JDialog();
        startDialog.setTitle("Willkommen!");
        startDialog.setModal(true);
        startDialog.setSize(400, 250);
        startDialog.setLocationRelativeTo(null);
        startDialog.setLayout(new BorderLayout(10, 10));

        JLabel titelLabel = new JLabel("Schach", SwingConstants.CENTER);
        titelLabel.setFont(new Font("Serif", Font.BOLD, 48));
        startDialog.add(titelLabel, BorderLayout.NORTH);

        JLabel autorenLabel = new JLabel("Ein Projekt von Lukas, Aditi und Frederic", SwingConstants.CENTER);
        autorenLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        startDialog.add(autorenLabel, BorderLayout.CENTER);

        JButton startButton = new JButton("Spiel starten");
        startButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        startButton.addActionListener(e -> startDialog.dispose()); 
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        startDialog.add(buttonPanel, BorderLayout.SOUTH);

        startDialog.setVisible(true);
    }
}