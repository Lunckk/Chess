import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;

public abstract class Schachfigur {
    protected int x, y;
    protected String farbe;
    protected boolean hatSichBewegt;
    protected char symbol;

    public Schachfigur(int startX, int startY, String pFarbe) {
        this.x = startX;
        this.y = startY;
        this.farbe = pFarbe;
        this.hatSichBewegt = false;
        setzeSymbol();
    }

    public abstract void setzeSymbol();
    public abstract boolean kannZiehen(int zielX, int zielY, Schachfigur[][] brett);
    
    public void zeichne(Graphics g, int x, int y, int groesse) {
        g.setFont(new Font("SansSerif", Font.BOLD, groesse - 15));
        
        g.setColor(this.farbe.equals("weiss") ? Color.BLACK : Color.WHITE);
        String s = Character.toString(symbol);
        int stringBreite = g.getFontMetrics().stringWidth(s);
        int stringHoehe = g.getFontMetrics().getAscent();
        int posX = x * groesse + (groesse - stringBreite) / 2;
        int posY = y * groesse + groesse - (groesse - stringHoehe) / 2 - 8;
        g.drawString(s, posX + 1, posY + 1);
        
        g.setColor(this.farbe.equals("weiss") ? Color.WHITE : Color.BLACK);
        g.drawString(s, posX, posY);
    }
    
    public void bewegen(int neuX, int neuY) {
        this.hatSichBewegt = true;
        this.x = neuX;
        this.y = neuY;
    }
    
    public String getFarbe() { return farbe; }
}