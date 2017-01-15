package klassen.geometrie;

import com.jogamp.opengl.GL4;
import grundklassen.Geometrie;
import klassen.GeometrieManager;
import main.LarryEngineKern;

public class Cone extends Geometrie {
    public Cone(GeometrieManager mM) {
        super(mM);
        GL4 gl = this.mM.holGL();
        this.verticesPlain = new float[]{
            -0.877324f, -0.378460f, 0.377652f, -0.877324f, 0.378085f, 0.377652f, 1.122676f, -0.000188f, -0.000620f, -0.877324f, -0.378460f, 0.377652f, -0.877324f, 0.378085f, 0.377652f, -0.877324f, 0.378085f, -0.378893f, -0.877324f, -0.378460f, -0.378893f, -0.877324f, 0.378085f, 0.377652f, -0.877324f, 0.378085f, -0.378893f, 1.122676f, -0.000188f, -0.000620f, -0.877324f, -0.378460f, 0.377652f, -0.877324f, -0.378460f, -0.378893f, 1.122676f, -0.000188f, -0.000620f, -0.877324f, 0.378085f, -0.378893f, -0.877324f, -0.378460f, -0.378893f, 1.122676f, -0.000188f, -0.000620f};
        this.uvsPlain = new float[]{
            0.4999f, 0.5002f,
            0.4999f, 0.9999f,
            0.0002f, 0.9999f,
            0.5001f, 0.0001f,
            0.9999f, 0.4999f,
            0.9999f, 0.0001f,
            0.5001f, 0.4999f,
            0.0001f, 0.4998f,
            0.0001f, 0.0001f,
            0.4998f, 0.0001f,
            0.4998f, 0.5001f,
            0.0001f, 0.5001f,
            0.0001f, 0.9998f,
            0.4999f, 0.4999f,
            0.4999f, 0.0002f,
            0.0002f, 0.4999f
        };
        this.normalenPlain = new float[]{
            0.1858f, 0.0000f, 0.9826f,
            -1.0000f, 0.0000f, 0.0000f,
            0.1858f, 0.9826f, 0.0000f,
            0.1858f, -0.9826f, 0.0000f,
            0.1858f, 0.0000f, -0.9826f
        };
        this.indiciesPlain = new int[]{/*f 3/1/1 2/2/1 1/3/1
f 6/4/2 4/5/2 5/6/2
f 6/4/2 7/7/2 4/5/2
f 10/8/3 9/9/3 8/10/3
f 11/11/4 12/12/4 13/13/4
f 
    14/14/5 16/15/5 15/16/5*/};
        int[] vboHandles = new int[4];

    gl.glGenBuffers (
    4, vboHandles, 0);
         
    this.vertices  = vboHandles[0];
     
    this.uvs  = vboHandles[1];
     
    this.normalen  = vboHandles[2];
     
    this.indicies  = vboHandles[3];

    LarryEngineKern.createBuffer (gl,  

    this.vertices, this.erzeugeBuffer(this.verticesPlain));
    LarryEngineKern.createBuffer (gl,  

    this.uvs, this.erzeugeBuffer(this.uvsPlain));
    LarryEngineKern.createBuffer (gl,  

    this.normalen, this.erzeugeBuffer(this.normalenPlain));
    LarryEngineKern.createBuffer (gl,  

this.indicies, this.erzeugeBuffer(this.indiciesPlain));
    }
}