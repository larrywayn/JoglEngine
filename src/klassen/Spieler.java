package klassen;

import grundklassen.DrawObjekt;

/**
 * Created by larry on 20.03.2016.
 */
public class Spieler extends Objekt3D {

    protected Physik physik;
    protected Vektor4 tmp;
    protected float faktorX;
    protected float faktorY;
    protected float ausdauer;

    public Spieler() {
        this(new Quaternion(0.0f, 0.0f, 0.0f, 1.0f), new Vektor4(0.0f, 0.0f, 0.0f, 1.0f), new Physik(0.82f, 50.0f));
        this.faktorX = 0.5f;
        this.faktorY = 0.3f;
        this.ausdauer = 100f;
        Vektor4 spielerVn = (this.holStandort().kopiere());
        Vektor4 vor2 = new Vektor4(0f, 8f, 0f, 1);
        spielerVn = spielerVn.addiereV4(vor2);
        this.setzStandort(spielerVn);

    }

    public Spieler(Quaternion q, Vektor4 v, Physik p) {
        super();
        this.setzAusrichtung(q);
        this.setzStandort(v);
        this.physik = p;
        this.ausdauer = 100f;
        this.faktorX = 0.5f;
        this.faktorY = 0.3f;
    }

    /*  @Override
    public void setzAusrichtung(Quaternion q){
            q.ausgabe();
        super.setzAusrichtung(q);
    }*/
    public float[] holMatrix() {
        return this.holAusrichtung().erzeugeMatrix(this.standort);
    }

    public Physik holPhysik() {
        return this.physik;
    }

    public void setzAusdauer(float ausdauer) {
        this.ausdauer = ausdauer;
    }

    public float holAusdauer() {
        return this.ausdauer;
    }

    public void bewegeVorwaerts() {
        tmp = new Vektor4(0.0f, 0.0f, -this.faktorX, 1.0f);
        //Quaternion a = ausrichtung.kopiere();
        //a.invertiere();
        ausrichtung.invertiere();
        tmp = ausrichtung.multipliziereV(tmp);
        this.standort = this.standort.addiereV4(tmp);
        ausrichtung.invertiere();
    }

    public void bewegeRueckwaerts() {
        tmp = new Vektor4(0.0f, 0.0f, this.faktorX, 1.0f);
        ausrichtung.invertiere();
        tmp = ausrichtung.multipliziereV(tmp);
        this.standort = this.standort.addiereV4(tmp);
        ausrichtung.invertiere();
    }

    public void bewegeLinks() {
        tmp = new Vektor4(-this.faktorY, 0.0f, 0.0f, 1.0f);
        ausrichtung.invertiere();
        tmp = ausrichtung.multipliziereV(tmp);
        this.standort = this.standort.addiereV4(tmp);
        ausrichtung.invertiere();
    }

    public void bewegeRechts() {
        tmp = new Vektor4(this.faktorY, 0.0f, 0.0f, 1.0f);
        ausrichtung.invertiere();
        tmp = ausrichtung.multipliziereV(tmp);
        this.standort = this.standort.addiereV4(tmp);
        ausrichtung.invertiere();
    }

    public void bewegeNormal(boolean beschleunigt) {
        this.ausdauer = (this.ausdauer >= 100) ? 100f : (!beschleunigt && this.ausdauer >= 50) ? this.ausdauer + 0.3f : (!beschleunigt && this.ausdauer >= 0) ? this.ausdauer + 0.1f : this.ausdauer;
        this.faktorX = 0.5f;
        this.faktorY = 0.3f;
    }

    public void bewegeSchneller() {
        this.ausdauer = (0f > this.ausdauer) ? 0f : this.ausdauer - 0.2f;
        if (this.ausdauer > 50f) {
            this.faktorX = 7.5f;
            this.faktorY = 1.6f;
        } else if (this.ausdauer > 20f) {
            this.faktorX = 4.5f;
            this.faktorY = 0.8f;
        } else if (this.ausdauer > 5f) {
            this.faktorX = 2.5f;
            this.faktorY = 0.5f;
        }
    }
}
