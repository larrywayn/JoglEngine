package grundklassen;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.texture.Texture;

public class Geometrie {

    protected Geometrie(){
    }
    protected float[] verticesPlain;
    protected float[] uvsPlain;
    protected float[] normalenPlain;
    protected int[] indiciesPlain;
    protected int vertices;
    protected int uvs;
    protected int normalen;
    protected int indicies;
    protected Texture textur;
    protected int texturID;
    protected int AA;

    public int holVertices() {
        return this.vertices;
    }

    public int holNormalen() {
        return this.normalen;
    }

    public int holTexturenUV() {
        return this.uvs;
    }

    public int holIndizies() {
        return this.indicies;
    }

    public float[] holVerticesPlain() {
        return this.verticesPlain;
    }

    public float[] holNormalenPlain() {
        return this.normalenPlain;
    }

    public float[] holTexturenUVPlain() {
        return this.uvsPlain;
    }

    public int[] holIndiziesPlain() {
        return this.indiciesPlain;
    }

    public int holTexturID() {
        return this.texturID;
    }

    public void setzTexturID(int texturID) {
        this.texturID = texturID;
    }

    protected FloatBuffer erzeugeBuffer(float[] daten) {
        FloatBuffer vertexBuffer = Buffers.newDirectFloatBuffer(daten);
        vertexBuffer.rewind();
        return vertexBuffer;
    }

    protected IntBuffer erzeugeBuffer(int[] daten) {
        IntBuffer indexBuffer = Buffers.newDirectIntBuffer(daten);
        indexBuffer.rewind();
        return indexBuffer;
    }
    
    public void setzTextur(Texture textur) {
        this.AA = 33;
       this.textur = textur;
    }
    
    public Texture holTextur() {
      //  System.out.println("AA "+AA);
       return this.textur;
    }
}
