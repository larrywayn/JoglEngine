package klassen.geometrie;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.GLBuffers;
import grundklassen.Geometrie;
import klassen.GeometrieManager;
import main.LarryEngineKern;

public class Circle extends Geometrie {
    public Circle(GeometrieManager mM) {
        super(mM);
        GL4 gl = this.mM.holGL();
        this.verticesPlain = new float[]{0, 0, 0, 
             0.000000f, 0.000000f, -3.000000f,
             -1.148050f, 0.000000f, -2.771638f, 
             -2.121320f, 0.000000f, -2.121320f, 
             -2.771638f, 0.000000f, -1.148050f, 
             -3.000000f, 0.000000f, 0.000000f,
             -2.771638f, 0.000000f, 1.148051f, 
             -2.121320f, 0.000000f, 2.121320f,
             -1.148050f, 0.000000f, 2.771638f, 
             -0.000000f, 0.000000f, 3.000000f, 
             1.148050f, 0.000000f, 2.771639f,
             2.121320f, 0.000000f, 2.121321f,
             2.771638f, 0.000000f, 1.148051f, 
             3.000000f, 0.000000f, -0.000000f, 
             2.771638f, 0.000000f, -1.148051f, 
             2.121320f, 0.000000f, -2.121321f,
             1.148049f, 0.000000f, -2.771639f};
        this.indiciesPlain = new int[]{
             2, 1,
             3, 2,
             4, 3,
             5, 4,
             6, 5,
             7, 6,
             8, 7,
             9, 8,
             10, 9,
             11, 10,
             12, 11,
             13, 12,
             14, 13,
             15, 14,
             16, 15,
             1, 16};
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
        /*	gl.glBindBuffer(GL_ARRAY_BUFFER, vboHandles[0]);
		gl.glBufferData(GL_ARRAY_BUFFER, this.verticesPlain.length * Float.SIZE, vertexBuffer, GL_STATIC_DRAW);
		gl.glBindBuffer(GL_ARRAY_BUFFER, vboHandles[0]);
		gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboHandles[1]);
		gl.glBufferData(GL_ELEMENT_ARRAY_BUFFER, this.indiciesPlain.length * Integer.SIZE, indexBuffer, GL_STATIC_DRAW);
		gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboHandles[1]);*/
    }
}