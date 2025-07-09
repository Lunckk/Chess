public class Springer extends Schachfigur {
    public Springer(int startX, int startY, String farbe) { super(startX, startY, farbe); }
    @Override public void setzeSymbol() { symbol = farbe.equals("weiss") ? '\u2658' : '\u265E'; }
    @Override public boolean kannZiehen(int zielX, int zielY, Schachfigur[][] brett) {
        int dX = Math.abs(x - zielX), dY = Math.abs(y - zielY);
        if (!((dX == 1 && dY == 2) || (dX == 2 && dY == 1))) return false;
        return brett[zielX][zielY] == null || !brett[zielX][zielY].getFarbe().equals(farbe);
    }
}