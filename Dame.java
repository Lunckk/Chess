public class Dame extends Schachfigur {
    public Dame(int startX, int startY, String farbe) { super(startX, startY, farbe); }
    @Override public void setzeSymbol() { symbol = farbe.equals("weiss") ? '\u2655' : '\u265B'; }
    @Override public boolean kannZiehen(int zielX, int zielY, Schachfigur[][] brett) {
        if (Math.abs(x-zielX) != Math.abs(y-zielY) && x != zielX && y != zielY) return false;
        int xR = Integer.signum(zielX - x), yR = Integer.signum(zielY - y);
        int curX = x + xR, curY = y + yR;
        while (curX != zielX || curY != zielY) {
            if (brett[curX][curY] != null) return false;
            curX += xR; curY += yR;
        }
        return brett[zielX][zielY] == null || !brett[zielX][zielY].getFarbe().equals(farbe);
    }
}