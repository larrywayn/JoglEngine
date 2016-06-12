package klassen;

import grundklassen.DrawObjekt;

/**
 * Created by larry on 20.03.2016.
 */
public class Spieler extends DrawObjekt {
    protected Physik physik;
    protected Vektor4 tmp;

    public Spieler(){
        this(new Quaternion(0.0f,0.0f,0.0f,1.0f), new Vektor4(0.0f,0.0f,-8.0f,1.0f), new Physik(0.82f,50.0f));
    }

    public Spieler(Quaternion q, Vektor4 v, Physik p){
        super();
        this.setzAusrichtung(q);
        this.setzStandort(v);
        this.physik = p;
    }

    public float[] holMatrix() {
        return this.holAusrichtung().erzeugeMatrix(this.standort);
    }

    public Physik holPhysik() {
        return this.physik;
    }

    public void bewegeVorwaerts() {
        tmp = new Vektor4(0.0f,0.0f,-1.0f,1.0f);
       // Quaternion a = ausrichtung.kopiere();
       // a.invertiere();
       ausrichtung.invertiere();
        tmp = ausrichtung.multipliziereV(tmp);
        this.standort = this.standort.addiereV4(tmp);
        ausrichtung.invertiere();
    }

    public void bewegeRueckwaerts() {
        tmp = new Vektor4(0.0f,0.0f,1.0f,1.0f);
        ausrichtung.invertiere();
        tmp = ausrichtung.multipliziereV(tmp);
        this.standort = this.standort.addiereV4(tmp);
        ausrichtung.invertiere();
    }

    public void bewegeLinks() {
        tmp = new Vektor4(-1.0f,0.0f,0.0f,1.0f);
        ausrichtung.invertiere();
        tmp = ausrichtung.multipliziereV(tmp);
        this.standort = this.standort.addiereV4(tmp);
        ausrichtung.invertiere();
    }

    public void bewegeRechts() {
        tmp = new Vektor4(1.0f,0.0f,0.0f,1.0f);
        ausrichtung.invertiere();
        tmp = ausrichtung.multipliziereV(tmp);
        this.standort = this.standort.addiereV4(tmp);
        ausrichtung.invertiere();
    }
}