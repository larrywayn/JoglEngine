package klassen.geometrie;

import com.jogamp.opengl.GL4;
import grundklassen.Geometrie;
import klassen.GeometrieManager;
import main.LarryEngineKern;

public class Quad extends Geometrie {
    public Quad(GeometrieManager mM) {
        super(mM);
        GL4 gl = this.mM.holGL();
        this.verticesPlain = new float[]{
            -1.00f, 1.00f, 0.0f,
            -1.00f, -1.00f, 0.0f,
            1.00f, -1.00f, 0.0f,
            1.00f, 1.00f, 0.0f
        };
        this.uvsPlain = new float[]{
            0.0f, 1.0f,
            0f, 0.0f,
            1f, 0.0f,
            1.0f, 1.0f};
        this.normalenPlain = new float[]{
            -1.0000f, 0.0000f, 1.0000f,
            1.0000f, 0.0000f, 1.0000f,
            -1.0000f, 0.0000f, -1.0000f,
            1.0000f, 0.0000f, -1.0000f
        };
        this.indiciesPlain = new int[]{
            0, 1, 2,
            0, 2, 3};
        int[] vboHandles = new int[4];
        gl.glGenBuffers(4, vboHandles, 0);
        this.vertices = vboHandles[0];
        this.uvs = vboHandles[1];
        this.normalen = vboHandles[2];
        this.indicies = vboHandles[3];
        vboHandles = null;
        LarryEngineKern.createBuffer(gl, this.vertices, this.erzeugeBuffer(this.verticesPlain));
        LarryEngineKern.createBuffer(gl, this.uvs, this.erzeugeBuffer(this.uvsPlain));
        LarryEngineKern.createBuffer(gl, this.normalen, this.erzeugeBuffer(this.normalenPlain));
        LarryEngineKern.createBuffer(gl, this.indicies, this.erzeugeBuffer(this.indiciesPlain));
    }
}
