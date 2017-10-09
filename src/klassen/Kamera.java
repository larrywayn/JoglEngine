package klassen;

import main.LarryEngineKern;

/**
 * Created by larry on 13.03.2016.
 */
public class Kamera {

    private Quaternion ausrichtung;
    private Vektor4 standort;
    private float[] perspektive;
    private float[] orthogonal;
    private float aspekt;

    public Kamera(float aspekt) {
        this(aspekt, new Vektor4(0.0f, 0.0f, 0.0f, 1.0f), new Quaternion(0.0f, 0.0f, 0.0f, 1.0f));
    }

    public Kamera(float aspekt, Vektor4 standort, Quaternion ausrichtung) {
        this.ausrichtung = ausrichtung;
        this.standort = standort;
        this.aspekt = aspekt;
        // alpha°*pi/180°
        float radians = (float) ((Configuration.holSichtfeld() * Math.PI) / 180.0f);
        this.perspektive = this.erzeugePerspektive(radians, Configuration.holNear(), Configuration.holFar());
        this.orthogonal = this.erzeugeOrthogonal(Configuration.holFensterBreite(), Configuration.holFensterHoehe(), Configuration.holNear(), Configuration.holFar());
    }

    public void setzAspekt(float aspekt) {
        this.aspekt = aspekt;
        float radians = (float) ((Configuration.holSichtfeld() * Math.PI) / 180.0f);
        this.perspektive = this.erzeugePerspektive(radians, Configuration.holNear(), Configuration.holFar());
        this.orthogonal = this.erzeugeOrthogonal(Configuration.holFensterBreite(), Configuration.holFensterHoehe(), Configuration.holNear(), Configuration.holFar());
    }

    protected float[] erzeugePerspektive(float fieldOfViewInRadians, float near, float far) {
        float f = 1.0f / (float) Math.tan(fieldOfViewInRadians / 2.0f);
        return new float[]{
            f / this.aspekt, 0.0f, 0.0f, 0.0f,
            0.0f, f, 0.0f, 0.0f,
            0.0f, 0.0f, (near + far) / (near - far), (2.0f * far * near) / (near - far),
            0.0f, 0.0f, -1.0f, 0.0f};
    }

    protected float[] erzeugeOrthogonal(int viewport_width, int viewport_height, float near, float far) {
        //  near = 10.0f;
        // far = -10.0f;
        System.out.println("Aspekt: " + this.aspekt);
        float right = this.aspekt;//(viewport_width);
        float left = -this.aspekt;
        float top = 1.0f;//(viewport_height);
        float bottom = -1.0f;
        return new float[]{
            2.0f / (right - left), 0.0f, 0.0f, (-((right + left) / (right - left))),
            0.0f, 2.0f / (top - bottom), 0.0f, (-((top + bottom) / (top - bottom))),
            0.0f, 0.0f, -2.0f / (far - near), (-((far + near) / (far - near))),
            0.0f, 0.0f, 0.0f, 1.0f};

        /*
        return new float[]{
        2.0f / (right - left),   0.0f, 0.0f, 0.0f,
             0.0f, 1.0f / (top - bottom), 0.0f, 0.0f,
             0.0f,0.0f,-2.0f / (far - near),0.0f,
             (-(right + left) / (right - left)),(-(top + bottom) / (top - bottom)),(-(far + near) / (far - near)), 1.0f
        };*/
    }

    public Vektor4 holStandort() {
        return this.standort;
    }

    public Quaternion holAusrichtung() {
        return this.ausrichtung;
    }

    public float[] holPerspektive() {
        return this.perspektive;
    }

    public float[] holOrthogonal() {
        return this.orthogonal;
    }

    public float[] holMatrix() {
        Quaternion invertView = null;
        Vektor4 invertStandort = null;
        try {
            invertView = this.holAusrichtung().kopiere();
            invertStandort = this.holStandort().kopiere();
            invertStandort.invertiere();
            return invertView.erzeugeMatrix(invertStandort);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.ausrichtung.erzeugeMatrix(this.standort);
    }

    public void setzAusrichtung(Quaternion ausrichtung) {
        this.ausrichtung = ausrichtung;
    }

    public void setzStandort(Vektor4 standort) {
        this.standort = standort;
    }
}
