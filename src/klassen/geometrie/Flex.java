package klassen.geometrie;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.GLBuffers;
import grundklassen.Geometrie;
import klassen.GeometrieManager;
import main.LarryEngineKern;

public class Flex extends Geometrie {
    public Flex(GeometrieManager mM, float[] vertices, int[] indicies) {
        super(mM);
        GL4 gl = this.mM.holGL();
        this.verticesPlain = vertices;
        this.indiciesPlain = indicies;
        int[] vboHandles = new int[2];
        gl.glGenBuffers(2, vboHandles, 0);
        this.vertices = vboHandles[0];
        this.indicies = vboHandles[1];
        vboHandles = null;

        FloatBuffer vertexBuffer = GLBuffers.newDirectFloatBuffer(this.verticesPlain.length);
        vertexBuffer.put(this.verticesPlain);
        vertexBuffer.rewind();

        IntBuffer indexBuffer = GLBuffers.newDirectIntBuffer(this.indiciesPlain.length);
        indexBuffer.put(this.indiciesPlain);
        indexBuffer.rewind();

        LarryEngineKern.createBuffer(gl, this.vertices, vertexBuffer);
        LarryEngineKern.createBuffer(gl, this.indicies, indexBuffer);
    }
}