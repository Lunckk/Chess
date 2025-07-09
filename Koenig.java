public class Koenig extends Schachfigur {
    public Koenig(int startX, int startY, String farbe) { super(startX, startY, farbe); }
    
    @Override 
    public void setzeSymbol() { 
        symbol = farbe.equals("weiss") ? '\u2654' : '\u265A'; 
    }
    
    @Override 
    public boolean kannZiehen(int zielX, int zielY, Schachfigur[][] brett) {
        Schachfigur zielFigur = brett[zielX][zielY];
        int dX = Math.abs(x - zielX);
        int dY = Math.abs(y - zielY);
        
        if (dX <= 1 && dY <= 1) {
            return zielFigur == null || !zielFigur.getFarbe().equals(this.farbe);
        }
        
        if (!hatSichBewegt && dY == 0 && dX == 2) {
            return true;
        }
        
        return false;
    }
}