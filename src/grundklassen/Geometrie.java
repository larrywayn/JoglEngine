package grundklassen;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import com.jogamp.common.nio.Buffers;

public class Geometrie {
	protected float[] verticesPlain;
	protected float[] uvsPlain;
	protected float[] normalenPlain;
	protected int[] indiciesPlain;
	protected int vertices;
	protected int uvs;
	protected int normalen;
	protected int indicies;

	public int holVertices(){ return vertices; };
	public int holNormalen(){ return normalen; };
	public int holTexturenUV(){ return uvs; };
	public int holIndizies(){ return indicies; };
	
	public float[] holVerticesPlain(){ return verticesPlain; };
	public float[] holNormalenPlain(){ return normalenPlain; };
	public float[] holTexturenUVPlain(){ return uvsPlain; };
	public int[] holIndiziesPlain(){ return indiciesPlain; };
	
	protected FloatBuffer erzeugeBuffer(float[] daten){
		FloatBuffer vertexBuffer = Buffers.newDirectFloatBuffer(daten);
		vertexBuffer.rewind();	
		return vertexBuffer;
	};
	
	protected IntBuffer erzeugeBuffer(int[] daten){
		IntBuffer indexBuffer = Buffers.newDirectIntBuffer(daten);
		indexBuffer.rewind();
		return indexBuffer;
	};
}