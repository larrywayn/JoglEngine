package grundklassen;

import klassen.Quaternion;
import klassen.ShaderBlock;
import klassen.Vektor4;

/**
 * Created by larry on 01.04.2016.
 */
public abstract class DrawObjekt {

    protected ShaderBlock shader;
    protected Geometrie mesh;
    protected Quaternion ausrichtung;
    protected Vektor4 standort;

    public DrawObjekt() {
        ausrichtung = new Quaternion();
        standort = new Vektor4();
    }

    public ShaderBlock holShader() {
        return shader;
    }

    public void setzShader(ShaderBlock shader) {
        this.shader = shader;
    }

    public Geometrie holMesh() {
        return mesh;
    }

    public void setzMesh(Geometrie mesh) {
        this.mesh = mesh;
    }

    public Quaternion holAusrichtung() {
        return ausrichtung;
    }

    public void setzAusrichtung(Quaternion ausrichtung) {
        this.ausrichtung = ausrichtung;
    }

    public Vektor4 holStandort() {
        return standort;
    }

    public void setzStandort(Vektor4 standort) {
        this.standort = standort;
    }

}
