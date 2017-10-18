package klassen;

import static com.jogamp.opengl.GL.GL_REPEAT;
import static com.jogamp.opengl.GL.GL_TEXTURE_WRAP_S;
import static com.jogamp.opengl.GL.GL_TEXTURE_WRAP_T;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.util.awt.ImageUtil;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import grundklassen.Geometrie;
import grundklassen.TexturTyp;
import java.io.File;
import java.io.IOException;
import klassen.Textur;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author larry
 */
public class TexturManager {

    private ConcurrentHashMap<Integer, Texture> arT;
    private ConcurrentHashMap<String, Integer> arM;
    private ConcurrentHashMap<String, Textur> arMOT;
    private ConcurrentLinkedQueue<Textur> clqT;
    private GL4 gl;
    private static int aid;

    public TexturManager(GL4 gl) {
        this.arT = new ConcurrentHashMap<>();
        this.arMOT = new ConcurrentHashMap<>();
        this.arM = new ConcurrentHashMap<>();
        this.clqT = new ConcurrentLinkedQueue<>();
        this.gl = gl;
        this.aid = 0;
    }

    public Textur ladeTextur(String texturName, boolean isFont) {
        if (isFont) {
            int caid = ++aid;
            if (texturName != null) {
                if (!arM.containsKey(texturName)) {
                    Textur t = new TexturFont(texturName, this, TexturTyp.TGA, caid);
                    Thread thread = new Thread(t);
                    thread.start();
                    return t;
                }
            }
        } else {
            return ladeTextur(texturName);
        }
        return null;
    }

    public synchronized Textur ladeTextur(String texturName) {
        int caid = ++aid;
        if (texturName != null) {
            if (!arM.containsKey(texturName)) {
                Textur t = new Textur(texturName, this, TexturTyp.JPG, caid);
                Thread thread = new Thread(t);
                thread.start();
                return t;
            }
        }
        return null;
    }

    public synchronized Textur ladeTextur(Geometrie geom) {
        int caid = ++aid;
        String tmpTex = geom.holTexturName(); //"grid.jpg";
        if (tmpTex != null) {
            if (!arM.containsKey(tmpTex)) {
                Textur t = new Textur(tmpTex, this, TexturTyp.JPG, caid);
                Thread thread = new Thread(t);
                thread.start();
                return t;
            }
        }
        return null;
    }

    public GL4 holGL() {
        return this.gl;
    }

    public synchronized void legeTexturenAn() {
        if (!clqT.isEmpty()) {
            Textur t = clqT.poll();
            System.out.println("Textur geholt");
            if (t.istGeladen()) {
                try {
                    System.out.println("Textur Bild geladen");
                    Texture textur = null;
                    if (t.texturTyp == TexturTyp.JPG) { 
                        ImageUtil.flipImageVertically(t.holImage());
                        textur = AWTTextureIO.newTexture(gl.getGLProfile(), t.holImage(), true);
                    } else {
                        textur = TextureIO.newTexture(t.holFile(), true);
                    }
                    textur.setTexParameteri(gl, GL_TEXTURE_WRAP_S, GL_REPEAT);
                    textur.setTexParameteri(gl, GL_TEXTURE_WRAP_T, GL_REPEAT);
                    //  int oid = t.holObjektID();
                    int texID = arT.size() + 1;
                    arM.put(t.holDateipfad(), texID);
                    arT.put(texID, textur);
                    arMOT.put(t.holDateipfad(), t);
                    t.setzTextur(textur);
                    System.out.println("Textur komplett angelegt ");
                } catch (IOException | GLException ex) {
                    Logger.getLogger(TexturManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public synchronized void speichereTextur(Textur textur) {
        clqT.add(textur);
        System.out.println("Textur angefügt");
    }

    public synchronized Texture holTextur(String texturName) {
        if (arMOT.containsKey(texturName)) {
            Textur textur = arMOT.get(texturName);
            return textur.holGLTexture();
        }
        return null;
    }

    public synchronized int holTexturID(String texturName) {
        if (arM.containsKey(texturName)) {
            return arM.get(texturName);
        }
        return -1;
    }
}
