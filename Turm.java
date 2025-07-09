public class Turm extends Schachfigur {
    public Turm(int startX, int startY, String farbe) { super(startX, startY, farbe); }
    @Override public void setzeSymbol() { symbol = farbe.equals("weiss") ? '\u2656' : '\u265C'; }
    @Override public boolean kannZiehen(int zielX, int zielY, Schachfigur[][] brett) {
        if (x != zielX && y != zielY) return false;
        int xR = Integer.signum(zielX - x), yR = Integer.signum(zielY - y);
        int curX = x + xR, curY = y + yR;
        while (curX != zielX || curY != zielY) {
            if (brett[curX][curY] != null) return false;
            curX += xR; curY += yR;
        }
        return brett[zielX][zielY] == null || !brett[zielX][zielY].getFarbe().equals(farbe);
    }
}