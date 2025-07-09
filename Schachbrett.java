public class Schachbrett {

    private Schachfigur[][] felder;

    public Schachbrett() {
        felder = new Schachfigur[8][8];
        aufstellung();
    }
    
    private void aufstellung() {
        setFigur(0, 0, new Turm(0, 0, "schwarz")); setFigur(1, 0, new Springer(1, 0, "schwarz"));
        setFigur(2, 0, new Laeufer(2, 0, "schwarz")); setFigur(3, 0, new Dame(3, 0, "schwarz"));
        setFigur(4, 0, new Koenig(4, 0, "schwarz")); setFigur(5, 0, new Laeufer(5, 0, "schwarz"));
        setFigur(6, 0, new Springer(6, 0, "schwarz")); setFigur(7, 0, new Turm(7, 0, "schwarz"));
        for (int i = 0; i < 8; i++) { setFigur(i, 1, new Bauer(i, 1, "schwarz")); }

        setFigur(0, 7, new Turm(0, 7, "weiss")); setFigur(1, 7, new Springer(1, 7, "weiss"));
        setFigur(2, 7, new Laeufer(2, 7, "weiss")); setFigur(3, 7, new Dame(3, 7, "weiss"));
        setFigur(4, 7, new Koenig(4, 7, "weiss")); setFigur(5, 7, new Laeufer(5, 7, "weiss"));
        setFigur(6, 7, new Springer(6, 7, "weiss")); setFigur(7, 7, new Turm(7, 7, "weiss"));
        for (int i = 0; i < 8; i++) { setFigur(i, 6, new Bauer(i, 6, "weiss")); }
    }

    public Schachfigur getFigur(int x, int y) {
        if (x >= 0 && x < 8 && y >= 0 && y < 8) {
            return felder[x][y];
        }
        return null;
    }
    
    public Schachfigur[][] getFelder() { return felder; }
    
    public void fuehreZugAus(Schachfigur figur, int zielX, int zielY) {
        felder[figur.x][figur.y] = null;
        felder[zielX][zielY] = figur;
        figur.bewegen(zielX, zielY);
    }
    
    public void setFigur(int x, int y, Schachfigur figur) {
        felder[x][y] = figur;
    }
    
    public Koenig findeKoenig(String farbe) {
        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                Schachfigur figur = felder[x][y];
                if (figur instanceof Koenig && figur.getFarbe().equals(farbe)) {
                    return (Koenig) figur;
                }
            }
        }
        return null;
    }
    
    public boolean istKoenigImSchach(String koenigFarbe) {
        Koenig koenig = findeKoenig(koenigFarbe);
        if (koenig == null) return false;
        
        String gegnerFarbe = koenigFarbe.equals("weiss") ? "schwarz" : "weiss";
        return istFeldBedroht(koenig.x, koenig.y, gegnerFarbe);
    }
    
    public boolean istFeldBedroht(int feldX, int feldY, String angreiferFarbe) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Schachfigur figur = felder[x][y];
                if (figur != null && figur.getFarbe().equals(angreiferFarbe) && figur.kannZiehen(feldX, feldY, felder)) {
                    return true;
                }
            }
        }
        return false;
    }
}