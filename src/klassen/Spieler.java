package klassen;

/**
 * Created by larry on 20.03.2016.
 */
public class Spieler {
    Quaternion ausrichtung;
    Vektor4 standort;
    Physik physik;
    Vektor4 tmp;

    public Spieler(){
        this(new Quaternion(0.0f,0.0f,0.0f,1.0f), new Vektor4(0.0f,0.0f,0.0f,1.0f), new Physik(0.82f,50.0f));
    }

    public Spieler(Quaternion q, Vektor4 v, Physik p){
        this.ausrichtung = q;
        this.standort = v;
        this.physik = p;
    }

    public float[] holMatrix() {
       // this.standort.ausgabe();
        return this.holAusrichtung().erzeugeMatrix(this.standort);
    }

    public Physik holPhysik() {
        return this.physik;
    }
    public Vektor4 holStandort(){
        return this.standort;
    }
    public Quaternion holAusrichtung(){
        return this.ausrichtung;
    }

    public void bewegeVorwaerts() {
        tmp = new Vektor4(0.0f,0.0f,1.0f,1.0f);
        tmp = ausrichtung.multipliziereV(tmp);
        this.standort = this.standort.addiereV4(tmp);
    }

    public void bewegeRueckwaerts() {
        tmp = new Vektor4(0.0f,0.0f,-1.0f,1.0f);
        tmp = ausrichtung.multipliziereV(tmp);
        this.standort = this.standort.addiereV4(tmp);
    }

    public void bewegeLinks() {
        tmp = new Vektor4(1.0f,0.0f,0.0f,1.0f);
        tmp = ausrichtung.multipliziereV(tmp);
        this.standort = this.standort.addiereV4(tmp);
    }

    public void bewegeRechts() {
        tmp = new Vektor4(-1.0f,0.0f,0.0f,1.0f);
        tmp = ausrichtung.multipliziereV(tmp);
        this.standort = this.standort.addiereV4(tmp);
    }
}