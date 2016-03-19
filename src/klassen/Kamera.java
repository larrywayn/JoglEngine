package klassen;

import main.LarryEngineKern;

/**
 * Created by larry on 13.03.2016.
 */
public class Kamera {
    private Quaternion ausrichtung;
    private Vektor4 standort;
    private float[] perspektive;
    private float aspekt;
    public Kamera(float aspekt){
        this(aspekt, new Vektor4(0.0f, 0.0f, 0.0f, 0.0f),  new Quaternion(0.0f, 0.0f, 0.0f, 0.0f));
    }
    public Kamera(float aspekt, Vektor4 standort, Quaternion ausrichtung){
        this.ausrichtung = ausrichtung;
        this.standort = standort;
        this.aspekt = aspekt;
        this.perspektive = erzeugePerspektive(Configuration.holSichtfeld(), Configuration.holNear(),Configuration.holFar());
    }
    protected float[] erzeugePerspektive(float fieldOfViewInRadians, float near, float far) {
        float f = 1.0f/(float)Math.tan(fieldOfViewInRadians/2.0f);
        return new float[]{
                f / this.aspekt, 0.0f, 0.0f, 0.0f,
                0.0f, f, 0.0f, 0.0f,
                0.0f, 0.0f, (near + far) /(near-far), (2.0f*far*near)/(near-far),
                0.0f, 0.0f, -1.0f, 0.0f};
    };

    public Vektor4 holStandort(){
        return this.standort;
    }
    public Quaternion holAusrichtung(){
        return this.ausrichtung;
    }
    public float[] holPerspektive(){
        return this.perspektive;
    }

    public float[] holMatrix() {
        Quaternion invertView = null;
        Vektor4 invertStandort = null;
        try {
            invertView = LarryEngineKern.kopiereObject(this.holAusrichtung());
            invertView.konjugante();
            invertStandort = LarryEngineKern.kopiereObject(this.holStandort());
          //  invertStandort.invertiere();
        } catch (Exception e) {
            e.printStackTrace();
        }
    //    invertView.invertiere();
       // Vektor4 invertStandort = this.mapper.convertValue(this.holStandort(), Vektor4.class);

       // return invertView.erzeugeMatrix(this.standort);
        return this.holAusrichtung().erzeugeMatrix(this.standort);
    }
}
