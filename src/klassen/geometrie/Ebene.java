package klassen.geometrie;

import com.jogamp.opengl.GL4;
import grundklassen.Geometrie;
import main.LarryEngineKern;

public class Ebene extends Geometrie {

	public Ebene(GL4 gl) {
			this.verticesPlain = new float[] {  0.0f, 0.0f, 0.0f,
			        -1.0f, -1.0f, 0.0f,
			        1.0f, -1.0f, 0.0f,
			        -1.0f, 1.0f, 0.0f,
			        1.0f, 1.0f, 0.0f };
			this.uvsPlain = new float[] { 0.0f, 0.0f,
			        0.0f, 0.0f,
			        1.0f, 0.0f,
			        0.0f, 1.0f,
			        1.0f, 1.0f };
			this.indiciesPlain = new int[] { 1, 2, 3, 3, 2, 4 };
			int[] vboHandles = new int[3];
			gl.glGenBuffers(3, vboHandles, 0);
			this.vertices = vboHandles[0];
			this.uvs = vboHandles[1];
			this.indicies = vboHandles[2];
			vboHandles = null;
			
			this.erzeugeBuffer(this.verticesPlain);
			LarryEngineKern.createBuffer(gl, this.vertices, this.erzeugeBuffer(this.verticesPlain));
			LarryEngineKern.createBuffer(gl, this.uvs, this.erzeugeBuffer(this.uvsPlain));
			LarryEngineKern.createBuffer(gl, this.indicies, this.erzeugeBuffer(this.indiciesPlain));
	}

}
