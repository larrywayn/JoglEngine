package klassen.geometrie;

import com.jogamp.opengl.GL4;
import grundklassen.Geometrie;
import klassen.GeometrieManager;
import main.LarryEngineKern;

public class Ebene extends Geometrie {
    public Ebene(GeometrieManager mM) {
        super(mM);
        GL4 gl = this.mM.holGL();
        this.verticesPlain = new float[]{
-0.000000f, 1.000000f, 1.000000f,
 0.000000f, -1.000000f, 1.000000f,
-0.000000f, 1.000000f, -1.000000f,
 0.000000f, -1.000000f, -1.000000f
    };
        this.uvsPlain = new float[]{
 0.9999f, 0.0001f,
 0.0001f, 0.9999f,
 0.0001f, 0.0001f,
 0.9999f, 0.9999f};
          this.normalenPlain = new float[]{
              -1.0000f, 0.0000f, 1.0000f,
              1.0000f, 0.0000f, 1.0000f,
              -1.0000f, 0.0000f, -1.0000f,
              1.0000f, 0.0000f, -1.0000f
          };
        this.indiciesPlain = new int[]{1, 2, 0, 1, 3, 2};
        int[] vboHandles = new int[4];
        gl.glGenBuffers(4, vboHandles, 0);
        this.vertices = vboHandles[0];
        this.uvs = vboHandles[1];
        this.normalen = vboHandles[2];
        this.indicies = vboHandles[3];
        vboHandles = null;

        this.erzeugeBuffer(this.verticesPlain);
        LarryEngineKern.createBuffer(gl, this.vertices, this.erzeugeBuffer(this.verticesPlain));
        LarryEngineKern.createBuffer(gl, this.uvs, this.erzeugeBuffer(this.uvsPlain));   
        LarryEngineKern.createBuffer(gl, this.normalen, this.erzeugeBuffer(this.normalenPlain));
        LarryEngineKern.createBuffer(gl, this.indicies, this.erzeugeBuffer(this.indiciesPlain));
    }
}