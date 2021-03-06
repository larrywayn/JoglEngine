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
 0.000000f, -1.640264f, -1.640264f,
 0.320000f, -1.640264f, -1.608747f,
 0.627702f, -1.640264f, -1.515407f,
 0.911282f, -1.640264f, -1.363830f,
 1.159842f, -1.640264f, -1.159842f,
 1.363830f, -1.640264f, -0.911282f,
 1.515407f, -1.640264f, -0.627702f,
 1.608747f, -1.640264f, -0.320000f,
 1.640264f, -1.640264f, -0.000000f,
 0.000000f, 1.640264f, 0.000000f,
 1.608747f, -1.640264f, 0.320000f,
 1.515407f, -1.640264f, 0.627702f,
 1.363830f, -1.640264f, 0.911282f,
 1.159842f, -1.640264f, 1.159842f,
 0.911282f, -1.640264f, 1.363830f,
 0.627702f, -1.640264f, 1.515407f,
 0.319999f, -1.640264f, 1.608747f,
 -0.000001f, -1.640264f, 1.640264f,
 -0.320000f, -1.640264f, 1.608747f,
 -0.627703f, -1.640264f, 1.515406f,
 -0.911283f, -1.640264f, 1.363829f,
 -1.159843f, -1.640264f, 1.159841f,
 -1.363831f, -1.640264f, 0.911281f,
 -1.515407f, -1.640264f, 0.627701f,
 -1.608747f, -1.640264f, 0.319998f,
 -1.640264f, -1.640264f, -0.000002f,
 -1.608747f, -1.640264f, -0.320001f,
 -1.515406f, -1.640264f, -0.627704f,
 -1.363829f, -1.640264f, -0.911284f,
 -1.159841f, -1.640264f, -1.159844f,
 -0.911280f, -1.640264f, -1.363831f,
 -0.627700f, -1.640264f, -1.515408f,
 -0.319997f, -1.640264f, -1.608748f};
        this.uvsPlain = new float[]{
 0.5000f, 0.0000f,
 0.5000f, 1.0000f,
 0.5975f, 0.0000f,
 0.6913f, 0.0000f,
 0.7778f, 0.0000f,
 0.8536f, 0.0000f,
 0.7778f, 0.0000f,
 0.6913f, 0.0000f,
 0.5975f, 0.0000f,
 0.5000f, 0.0000f,
 0.4025f, 0.0000f,
 0.3087f, 0.0000f,
 0.2222f, 0.0000f,
 0.1464f, 0.0000f,
 0.8536f, 0.0000f,
 0.7778f, 0.0000f,
 0.6913f, 0.0000f,
 0.5975f, 0.0000f,
 0.5000f, 0.0000f,
 0.4025f, 0.0000f,
 0.3087f, 0.0000f,
 0.2222f, 0.0000f,
 0.1464f, 0.0000f,
 0.2222f, 0.0000f,
 0.3087f, 0.0000f,
 0.4025f, 0.0000f,
 0.5000f, 0.0000f,
 0.5975f, 0.0000f,
 0.6913f, 0.0000f,
 0.7778f, 0.0000f,
 0.8536f, 0.0000f,
 0.1464f, 0.0000f,
 0.2222f, 0.0000f,
 0.3087f, 0.0000f,
 0.4025f, 0.0000f,
 0.5975f, 0.0096f,
 0.0096f, 0.4025f,
 0.9904f, 0.5975f,
 0.4025f, 0.9904f,
 0.5000f, 1.0000f,
 0.5975f, 0.9904f,
 0.6913f, 0.9619f,
 0.7778f, 0.9157f,
 0.8536f, 0.8536f,
 0.9157f, 0.7778f,
 0.9619f, 0.6913f,
 1.0000f, 0.5000f,
 0.9904f, 0.4025f,
 0.9619f, 0.3087f,
 0.9157f, 0.2222f,
 0.8536f, 0.1464f,
 0.7778f, 0.0843f,
 0.6913f, 0.0381f,
 0.4025f, 0.0096f,
 0.3087f, 0.0381f,
 0.2222f, 0.0843f,
 0.1464f, 0.1464f,
 0.0843f, 0.2222f,
 0.0381f, 0.3087f,
 0.0000f, 0.5000f,
 0.0096f, 0.5975f,
 0.0381f, 0.6913f,
 0.0843f, 0.7778f,
 0.1464f, 0.8536f,
 0.2222f, 0.9157f,
 0.3087f, 0.9619f
        };
        this.normalenPlain = new float[]{
 0.0878f, 0.4455f, -0.8910f,
 0.2599f, 0.4455f, -0.8567f,
 0.4220f, 0.4455f, -0.7896f,
 0.5680f, 0.4455f, -0.6921f,
 0.6921f, 0.4455f, -0.5680f,
 0.7896f, 0.4455f, -0.4220f,
 0.8567f, 0.4455f, -0.2599f,
 0.8910f, 0.4455f, -0.0878f,
 0.8910f, 0.4455f, 0.0878f,
 0.8567f, 0.4455f, 0.2599f,
 0.7896f, 0.4455f, 0.4220f,
 0.6921f, 0.4455f, 0.5680f,
 0.5680f, 0.4455f, 0.6921f,
 0.4220f, 0.4455f, 0.7896f,
 0.2599f, 0.4455f, 0.8567f,
 0.0878f, 0.4455f, 0.8910f,
 -0.0878f, 0.4455f, 0.8910f,
 -0.2599f, 0.4455f, 0.8567f,
 -0.4220f, 0.4455f, 0.7896f,
 -0.5680f, 0.4455f, 0.6921f,
 -0.6921f, 0.4455f, 0.5680f,
 -0.7896f, 0.4455f, 0.4220f,
 -0.8567f, 0.4455f, 0.2599f,
 -0.8910f, 0.4455f, 0.0878f,
 -0.8910f, 0.4455f, -0.0878f,
 -0.8567f, 0.4455f, -0.2599f,
 -0.7896f, 0.4455f, -0.4220f,
 -0.6921f, 0.4455f, -0.5680f,
 -0.5680f, 0.4455f, -0.6921f,
 -0.4220f, 0.4455f, -0.7896f,
 -0.2599f, 0.4455f, -0.8567f,
 -0.0878f, 0.4455f, -0.8910f,
 0.0000f, -1.0000f, 0.0000f
        };
        this.indiciesPlain = new int[]{
            0, 9, 1, 1, 9, 2, 2, 9, 3, 3, 9, 4, 4, 9, 5, 5, 9, 6, 6, 9, 7, 7, 9, 8, 8, 9, 10, 10, 9, 11, 11, 9, 12, 12, 9, 13, 13, 9, 14, 14, 9, 15, 15, 9, 16, 16, 9, 17, 17, 9, 18, 18, 9, 19, 19, 9, 20, 20, 9, 21, 21, 9, 22, 22, 9, 23, 23, 9, 24, 24, 9, 25, 25, 9, 26, 26, 9, 27, 27, 9, 28, 28, 9, 29, 29, 9, 30, 30, 9, 31, 31, 9, 32, 32, 9, 0, 16, 24, 7, 32, 0, 1, 1, 2, 3, 3, 4, 5, 5, 6, 3, 7, 8, 10, 10, 11, 7, 12, 13, 16, 14, 15, 16, 16, 17, 18, 18, 19, 20, 20, 21, 22, 22, 23, 24, 24, 25, 26, 26, 27, 28, 28, 29, 32, 30, 31, 32, 32, 1, 7, 3, 6, 7, 7, 11, 12, 13, 14, 16, 16, 18, 24, 20, 22, 24, 24, 26, 32, 29, 30, 32, 1, 3, 7, 7, 12, 16, 18, 20, 24, 26, 28, 32, 32, 7, 24
        };
        int[] vboHandles = new int[4];
        gl.glGenBuffers(4, vboHandles, 0);
        this.vertices = vboHandles[0];
        this.uvs = vboHandles[1];
        this.normalen = vboHandles[2];
        this.indicies = vboHandles[3];

        LarryEngineKern.createBuffer(gl, this.vertices, this.erzeugeBuffer(this.verticesPlain));
        LarryEngineKern.createBuffer(gl, this.uvs, this.erzeugeBuffer(this.uvsPlain));
        LarryEngineKern.createBuffer(gl, this.normalen, this.erzeugeBuffer(this.normalenPlain));
        LarryEngineKern.createBuffer(gl, this.indicies, this.erzeugeBuffer(this.indiciesPlain));
    }
}
