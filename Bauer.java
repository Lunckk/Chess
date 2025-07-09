public class Bauer extends Schachfigur {
    public Bauer(int startX, int startY, String farbe) { super(startX, startY, farbe); }
    
    @Override 
    public void setzeSymbol() { 
        symbol = farbe.equals("weiss") ? '\u2659' : '\u265F'; 
    }
    
    @Override 
    public boolean kannZiehen(int zielX, int zielY, Schachfigur[][] brett) {
        Schachfigur zielFigur = brett[zielX][zielY];
        int richtung = this.farbe.equals("weiss") ? -1 : 1;
        
        if (x == zielX && zielFigur == null && y + richtung == zielY) return true;
        
        if (!hatSichBewegt && x == zielX && zielFigur == null && brett[x][y + richtung] == null && y + 2 * richtung == zielY) return true;
        
        if (Math.abs(x - zielX) == 1 && y + richtung == zielY && zielFigur != null && !zielFigur.getFarbe().equals(farbe)) return true;
        
        if (Math.abs(x - zielX) == 1 && y + richtung == zielY && zielFigur == null) return true;
    
        return false;
    }
}