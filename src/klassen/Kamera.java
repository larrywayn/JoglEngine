package klassen;

/**
 * Created by larry on 13.03.2016.
 */
public class Kamera {
    private Quaternion ausrichtung;
    private Vektor4 standort;
    private float[] perspektive;
    private float aspekt;
    public Kamera(float aspekt){
        this(aspekt, new Vektor4(0, 0, 950, 1),  new Quaternion(0, 0, 0, 1));
    }
    public Kamera(float aspekt, Vektor4 standort, Quaternion ausrichtung){
        this.ausrichtung = new Quaternion(0, 0, 0, 1);
        this.standort = new Vektor4(0, 0, 950, 1);
        this.aspekt = aspekt;
        this.perspektive = erzeugePerspektive(Configuration.holSichtfeld(), this.aspekt, Configuration.holNear(),Configuration.holFar());
    }
    protected float[] erzeugePerspektive(float fieldOfViewInRadians, float aspect, float near, float far) {
        float f = (float)Math.tan(Math.PI * 0.5f - 0.5f * fieldOfViewInRadians);
        float rangeInv = 1.0f / (near - far);
        return new float[]{
                f / aspect, 0.0f, 0.0f, 0.0f,
                0.0f, f, 0.0f, 0.0f,
                0.0f, 0.0f, (near + far) * rangeInv, -1.0f,
                0.0f, 0.0f, near * far * rangeInv * 2.0f, 0.0f
        };
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
}
