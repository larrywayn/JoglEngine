package klassen.geometrie;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.GLBuffers;
import grundklassen.Geometrie;
import klassen.Vektor4;
import main.LarryEngineKern;

public class Linie extends Geometrie {

	public Linie(GL4 gl, Vektor4 start, Vektor4 ende) {
		this.verticesPlain = new float[] { 0.0f, 0.0f, 0.0f, start.holX(), start.holY(), start.holZ(), ende.holX(),ende.holY(), ende.holZ() };
		this.indiciesPlain = new int[] { 1, 2 };
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
