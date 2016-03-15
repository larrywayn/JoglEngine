package klassen.geometrie;

import com.jogamp.opengl.GL4;

import grundklassen.Geometrie;
import main.LarryEngineKern;

public class Wuerfel extends Geometrie {

	public Wuerfel(GL4 gl) {
		this.verticesPlain = new float[] { -1.0f, -1.0f, 1.0f, 
				 1.0f, -1.0f, 1.0f, 
				 1.0f, 1.0f, 1.0f, 
				 -1.0f, 1.0f, 1.0f, 
				 -1.0f, -1.0f, -1.0f, 
				 1.0f, -1.0f, -1.0f, 
				 1.0f, 1.0f, -1.0f, 
				 -1.0f, 1.0f, -1.0f };
		this.uvsPlain = new float[] { -1.0f, -1.0f, 1.0f, 
				 1.0f, -1.0f, 1.0f, 
				 1.0f, 1.0f, 1.0f, 
				 -1.0f, 1.0f, 1.0f, 
				 -1.0f, -1.0f, -1.0f, 
				 1.0f, -1.0f, -1.0f, 
				 1.0f, 1.0f, -1.0f, 
				 -1.0f, 1.0f, -1.0f };
		this.indiciesPlain = new int[] { 0, 1, 2, 2, 3, 0, 
				3, 2, 6, 6, 7, 3, 
				7, 6, 5, 5, 4, 7, 
				4, 0, 3, 3, 7, 4, 
				0, 1, 5, 5, 4, 0,
				1, 5, 6, 6, 2, 1 };
		int[] vboHandles = new int[3];
		gl.glGenBuffers(3, vboHandles, 0);
		this.vertices = vboHandles[0];
		this.uvs = vboHandles[1];
		this.indicies = vboHandles[2];

		LarryEngineKern.createBuffer(gl, this.vertices, this.erzeugeBuffer(this.verticesPlain));
		LarryEngineKern.createBuffer(gl, this.uvs, this.erzeugeBuffer(this.uvsPlain));
		LarryEngineKern.createBuffer(gl, this.indicies, this.erzeugeBuffer(this.indiciesPlain));
	}
}