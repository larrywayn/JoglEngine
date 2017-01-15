package klassen;

import static com.jogamp.opengl.GL.GL_REPEAT;
import static com.jogamp.opengl.GL.GL_TEXTURE_WRAP_S;
import static com.jogamp.opengl.GL.GL_TEXTURE_WRAP_T;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.awt.ImageUtil;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import grundklassen.Geometrie;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author larry
 */
public class TexturManager {

    private ConcurrentHashMap<Integer, Texture> arT;
    private ConcurrentHashMap<String, Integer> arM;
    private ConcurrentHashMap<Integer, Geometrie> arGTMP;
    private ConcurrentLinkedQueue<Textur> clqT;
    private GL4 gl;
    private static int aid;

    public TexturManager(GL4 gl) {
        this.arT = new ConcurrentHashMap<>();
        this.arGTMP = new ConcurrentHashMap<>();
        this.arM = new ConcurrentHashMap<>();
        this.clqT = new ConcurrentLinkedQueue<>();
        this.gl = gl;
        this.aid = 0;
    }

    public synchronized void ladeTextur(Geometrie geom) {
        int caid = ++aid;
        String tmpTex = geom.holTexturName(); //"grid.jpg";
        if (tmpTex != null) {
            if (!arM.containsKey(tmpTex)) {
                Textur t = new Textur(tmpTex, this, caid);
                Thread thread = new Thread(t);
                thread.start();
                arGTMP.put(caid, geom);
            } else {
                int texId = arM.get(tmpTex);
                if (texId > 0) {
                    Texture tex = arT.get(texId);
                    if (tex != null) {
                        geom.setzTextur(tex);
                    }
                }
            }
        }
    }

    public GL4 holGL() {
        return this.gl;
    }

    public synchronized void legeTexturAn() {
        if (!clqT.isEmpty()) {
            Textur t = clqT.poll();
            System.out.println("Textur geholt");
            if (t.istGeladen()) {
                System.out.println("Textur Bild geladen");
                ImageUtil.flipImageVertically(t.holImage());
                Texture textur = AWTTextureIO.newTexture(gl.getGLProfile(), t.holImage(), true);
                textur.setTexParameteri(gl, GL_TEXTURE_WRAP_S, GL_REPEAT);
                textur.setTexParameteri(gl, GL_TEXTURE_WRAP_T, GL_REPEAT);
                int oid = t.holObjektID();
                Geometrie geom = arGTMP.get(oid);
                int texID = arT.size() + 1;
                arM.put(t.holDateipfad(), texID);
                arT.put(texID, textur);
                arGTMP.remove(oid);
                geom.setzTextur(textur);
                System.out.println("Textur gesetzt " + textur.toString() + " " + geom.toString());
            }
        }
    }

    public synchronized void speichereTextur(Textur textur) {
        clqT.add(textur);
        System.out.println("Textur angefügt");
    }
}
