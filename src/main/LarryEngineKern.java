package main;

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.GL.GL_ELEMENT_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_TEXTURE0;
import static com.jogamp.opengl.GL.GL_TEXTURE_2D;
import static com.jogamp.opengl.GL.GL_UNSIGNED_INT;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import klassen.*;
import static com.jogamp.opengl.GL4.*;
import com.jogamp.opengl.util.texture.Texture;
import grundklassen.Geometrie;
import static grundklassen.GeometrieTypen.CONE;
import static grundklassen.GeometrieTypen.DICE;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import klassen.geometrie.Cone;
import klassen.geometrie.Flex;
import klassen.geometrie.Quad;

public class LarryEngineKern extends GLJPanel implements GLEventListener {

    private static final long serialVersionUID = -3272370017135790421L;
    private static final long bytesPerFloat = Float.SIZE / Byte.SIZE;
    private static final long bytesPerInt = Integer.SIZE / Byte.SIZE;
    private GL4 gl;
    private ShaderBlock sb;
    private Kamera kamera;
    private int width;
    private int height;
    private TexturManager tm;
    private ObjectManager om;
    private MapManager mm;
    private GeometrieManager gm;
    private Random zufallsGenerator;
    private FrameRenderBufferManager frm;
    private ShaderBlock sbS;
    private Cone mainCone;
    private Steuerung steuerung;
    private Spieler spieler;
    private ShaderBlock sbO;

    public LarryEngineKern() {
        super();
        this.addGLEventListener(this);
    }

    public float zufallsZahl(int min, int max) {
        return this.zufallsGenerator.nextInt((max - min) + 1) + min;
    }

    private void drawFinalScene(GL4 gl, Framebuffer scene3D, Framebuffer sceneOverlay) {
        Quad framebufferQuad = this.om.holFramebufferQuad();
        gl.glBindFramebuffer(GL_FRAMEBUFFER, 0);
        gl.glUseProgram(this.sb.holProgram());
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1f);
        gl.glViewport(0, 0, this.width, this.height);
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        gl.glEnable(GL_DEPTH_TEST);
        gl.glEnableVertexAttribArray(0);
        gl.glBindBuffer(GL_ARRAY_BUFFER, framebufferQuad.holVertices());
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        gl.glEnable(GL_TEXTURE_2D);
        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, scene3D.holTexturID());
        gl.glUniform1i((gl.glGetUniformLocation(this.sb.holProgram(), "texture0")), 0);
        gl.glActiveTexture(GL_TEXTURE1);
        gl.glBindTexture(GL_TEXTURE_2D, sceneOverlay.holTexturID());
        gl.glUniform1i((gl.glGetUniformLocation(this.sb.holProgram(), "texture1")), 1);
        gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, framebufferQuad.holIndizies());
        gl.glDrawElements(GL4.GL_TRIANGLES, framebufferQuad.holIndiziesPlain().length, GL_UNSIGNED_INT, 0);
        gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        gl.glBindBuffer(GL_ARRAY_BUFFER, 0);
        gl.glDisableVertexAttribArray(0);
        gl.glDisableVertexAttribArray(1);
        gl.glBindFramebuffer(GL_FRAMEBUFFER, 0);
        gl.glUseProgram(0);
    }

    private void drawSceneOrthogonal(GL4 gl, Framebuffer scene3D, String objektListe) {
        this.steuerung.aktualisiereKamera();
        gl.glBindFramebuffer(GL_FRAMEBUFFER, scene3D.holFramebufferID());
        gl.glClearColor(0.2f, 0.5f, 0.2f, 0.0f);
        gl.glViewport(0, 0, this.width, this.height);
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthMask(true);
        gl.glDrawBuffer(GL_COLOR_ATTACHMENT0);
        Map mp = this.om.holObjectListe(objektListe);
        if (mp != null) {
            Iterator it = mp.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                Objekt3D oo = (Objekt3D) pair.getValue();
                if (oo != null) {
                    Geometrie mesh = oo.holMesh();
                    if (mesh != null) {
                        float[] matrix = oo.holAusrichtung().erzeugeMatrix(oo.holStandort());
                        int program = oo.holShader().holProgram();
                        gl.glUseProgram(program);
                        gl.glEnableVertexAttribArray(0);
                        gl.glBindBuffer(GL_ARRAY_BUFFER, mesh.holVertices());
                        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
                        gl.glEnableVertexAttribArray(1);
                        gl.glBindBuffer(GL_ARRAY_BUFFER, mesh.holNormalen());
                        gl.glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
                        int uvbuf = mesh.holTexturenUV();
                        Texture t = mesh.holTextur();
                        if (uvbuf != 0 && t != null) {
                            gl.glEnable(GL_TEXTURE_2D);
                            gl.glActiveTexture(GL_TEXTURE0);
                            t.enable(gl);
                            t.bind(gl);
                            gl.glUniform1i((gl.glGetUniformLocation(program, "texture0")), 0);
                            gl.glUniform1f((gl.glGetUniformLocation(program, "isTex")), 1.0f);

                            gl.glEnableVertexAttribArray(2);
                            gl.glBindBuffer(GL_ARRAY_BUFFER, uvbuf);
                            gl.glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
                            gl.glBindTexture(GL_TEXTURE_2D, t.getTextureObject());
                        } else {
                            gl.glUniform1f((gl.glGetUniformLocation(program, "isTex")), 0.0f);
                        }
                        gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mesh.holIndizies());
                        float[] nnn = {1.0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};
                        gl.glUniform2f((gl.glGetUniformLocation(program, "posS")), this.width, this.height);
                        gl.glUniformMatrix4fv((gl.glGetUniformLocation(program, "projMat")), 1, false, this.kamera.holOrthogonal(), 0);
                        //gl.glUniformMatrix4fv((gl.glGetUniformLocation(program, "camMat")), 1, false, this.kamera.holMatrix(), 0);
                        gl.glUniformMatrix4fv((gl.glGetUniformLocation(program, "modelMat")), 1, false, matrix, 0);
                        gl.glDrawElements(GL_TRIANGLES, mesh.holIndiziesPlain().length, GL_UNSIGNED_INT, 0);
                        gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
                        gl.glBindBuffer(GL_ARRAY_BUFFER, 0);
                        gl.glDisableVertexAttribArray(0);
                        gl.glDisableVertexAttribArray(1);
                        if (uvbuf != 0 && t != null) {
                            t.disable(gl);
                            gl.glDisable(GL_TEXTURE_2D);
                            gl.glDisableVertexAttribArray(2);
                        }
                        gl.glUseProgram(0);
                    }
                }
            }
        }
        gl.glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    private void drawScenePerspektive(GL4 gl, Framebuffer scene3D, String objektListe) {
        this.steuerung.aktualisiereKamera();

        gl.glBindFramebuffer(GL_FRAMEBUFFER, scene3D.holFramebufferID());
        gl.glClearColor(0.5f, 0.2f, 0.2f, 1.0f);
        //   gl.glClearDepth(1.0f);
        // gl.glHint(GL_FRAGMENT_SHADER_DERIVATIVE_HINT, GL_NICEST);
        gl.glViewport(0, 0, this.width, this.height);
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthMask(true);
        //gl.glDepthFunc(GL_LEQUAL);
        // gl.glDepthRange(0.0f, 1.0f);
        gl.glDrawBuffer(GL_COLOR_ATTACHMENT0);
        Map mp = this.om.holObjectListe(objektListe);
        if (mp != null) {
            Iterator it = mp.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                Objekt3D oo = (Objekt3D) pair.getValue();
                if (oo != null) {
                    Geometrie mesh = oo.holMesh();
                    if (mesh != null) {
                        float[] matrix = oo.holAusrichtung().erzeugeMatrix(oo.holStandort());
                        int program = oo.holShader().holProgram();
                        gl.glUseProgram(program);
                        if (mesh.hatVertices()) {
                            gl.glEnableVertexAttribArray(0);
                            gl.glBindBuffer(GL_ARRAY_BUFFER, mesh.holVertices());
                            gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
                        }
                        if (mesh.hatNormalen()) {
                            gl.glEnableVertexAttribArray(1);
                            gl.glBindBuffer(GL_ARRAY_BUFFER, mesh.holNormalen());
                            gl.glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
                        }
                        int uvbuf = mesh.holTexturenUV();
                        Texture t = mesh.holTextur();
                        if (mesh.hatTexturenUV() && uvbuf != 0 && t != null) {
                            gl.glEnable(GL_TEXTURE_2D);
                            gl.glActiveTexture(GL_TEXTURE0);
                            t.enable(gl);
                            t.bind(gl);
                            gl.glUniform1i((gl.glGetUniformLocation(program, "texture0")), 0);
                            gl.glUniform1f((gl.glGetUniformLocation(program, "isTex")), 1.0f);
                            gl.glEnableVertexAttribArray(2);
                            gl.glBindBuffer(GL_ARRAY_BUFFER, uvbuf);
                            gl.glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
                            gl.glBindTexture(GL_TEXTURE_2D, t.getTextureObject());
                        } else {
                            gl.glUniform1f((gl.glGetUniformLocation(program, "isTex")), 0.0f);
                        }

                        gl.glUniformMatrix4fv((gl.glGetUniformLocation(program, "projMat")), 1, false, this.kamera.holPerspektive(), 0);
                        gl.glUniformMatrix4fv((gl.glGetUniformLocation(program, "camMat")), 1, false, this.kamera.holMatrix(), 0);
                        gl.glUniformMatrix4fv((gl.glGetUniformLocation(program, "modelMat")), 1, false, matrix, 0);
                        if (mesh.hatIndizies()) {
                            gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mesh.holIndizies());
                            gl.glDrawElements(GL_TRIANGLES, mesh.holIndiziesPlain().length, GL_UNSIGNED_INT, 0);
                            gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
                        }
                        gl.glBindBuffer(GL_ARRAY_BUFFER, 0);
                        gl.glDisableVertexAttribArray(0);
                        if (mesh.hatNormalen()) {
                            gl.glDisableVertexAttribArray(1);
                        }
                        if (mesh.hatTexturenUV() && uvbuf != 0 && t != null) {
                            t.disable(gl);
                            gl.glDisable(GL_TEXTURE_2D);
                            gl.glDisableVertexAttribArray(2);
                        }
                        gl.glUseProgram(0);
                    }
                }
            }
        }
        gl.glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        Framebuffer fbm = this.frm.holBuffer("Hauptbuffer");
        Framebuffer fbo = this.frm.holBuffer("Overlay");
        this.gl = drawable.getGL().getGL4();
        this.drawScenePerspektive(this.gl, fbm, "main");
        this.drawSceneOrthogonal(this.gl, fbo, "overlay");
        this.drawFinalScene(this.gl, fbm, fbo);
    }

    @Override
    public void dispose(GLAutoDrawable arg0) {
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        this.width = drawable.getSurfaceWidth();
        this.height = drawable.getSurfaceHeight();

        this.gl = drawable.getGL().getGL4();
        this.zufallsGenerator = new Random();

        this.sb = new ShaderBlock("framebuffer");
        this.sb.erzeugeProgram(this.gl, true, true, false);
        this.sbS = new ShaderBlock("standard");
        this.sbS.erzeugeProgram(this.gl, true, true, false);
        this.sbO = new ShaderBlock("simple");
        this.sbO.erzeugeProgram(this.gl, true, true, false);
  
        ShaderBlock toon = new ShaderBlock("toon");
        toon.erzeugeProgram(this.gl, true, true, false);
        
        this.tm = new TexturManager(this.gl);
        this.gm = new GeometrieManager(this.gl);
        this.om = new ObjectManager(this.gl, this.gm, this.sbS);
        this.frm = new FrameRenderBufferManager(this.gl);
        this.mm = new MapManager(this.gl, this.frm);

        this.frm.erzeugeFramebuffer("Hauptbuffer", this.width, this.height);
        this.frm.erzeugeFramebuffer("Overlay", this.width, this.height);
        this.kamera = new Kamera(((float) this.width / (float) this.height));
        // Wuerfel w = new Wuerfel(this.gm);
        //w.setzTexturName("grid.jpg");
        //tm.ladeTextur(w);
        //Cone w2 = new Cone(this.gm);
        // w2.setzTexturName("grid.jpg");

        this.om.erzeugeObjectListe("main");
        this.om.erzeugeObjectListe("overlay");

        this.mainCone = new Cone(this.gm);
        this.mainCone.setzTexturName("grid.jpg");

        // for (int i = 1; i >= 0; --i) {
        Flex f = this.mm.createMap(this.gm, "Some Seed");
        //  }
        Quaternion g = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
        g.setzSkalierung(2f, 2f, 2f);
        this.om.erzeugeObject(f, "main",g, new Vektor4(0, 0, 0, 1.0f), this.sbS);

        for (int i = 15000; i >= 0; --i) {
            Quaternion qq = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f); 
            float scal = zufallsZahl(3, 30);
            float scalw = zufallsZahl(3, 10);
            qq.setzSkalierung(scalw, scalw, scalw);
            this.om.erzeugeObject(CONE, "main", qq , new Vektor4(zufallsZahl(-6000, 6000), scalw, zufallsZahl(-6000, 6000), 1.0f), toon);
            //this.om.erzeugeObject(CONE, "main", new Quaternion(0.0f, 0.0f, 0.0f, 1.0f), new Vektor4(20f, 0f, 20f, 1.0f), this.sbS);
            //this.om.erzeugeObject(CONE, "main", new Quaternion(0.0f, 0.0f, 0.0f, 1.0f), new Vektor4(20f, 0f, 0f, 1.0f), this.sbS);
            //this.om.erzeugeObject(CONE, "main", new Quaternion(0.0f, 0.0f, 0.0f, 1.0f), new Vektor4(0f, 0f, 20f, 1.0f), this.sbS);
            //this.om.erzeugeObject(CONE, "main", new Quaternion(0.0f, 0.0f, 0.0f, 1.0f), new Vektor4(10f, 0f, 20f, 1.0f), this.sbS);
        }

        for (int i = 1000; i >= 0; i = i - 100) {
            for (int j = 500; j >= 0; j = j - 100) {
                Quaternion qq = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
                qq.setzSkalierung(zufallsZahl(1, 5), zufallsZahl(1, 5), 0);
                this.om.erzeugeObject(DICE, "overlay", qq, new Vektor4(i * zufallsZahl(1, 5), j * zufallsZahl(1, 5), 0f, 1.0f), this.sbO);
                // this.om.erzeugeObject(DICE, "overlay", new Quaternion(0.0f, 0.0f, 0.0f, 1.0f), new Vektor4(1000f, 380f, 0f, 1.0f), this.sbO);
                // this.om.erzeugeObject(DICE, "overlay", new Quaternion(0.0f, 0.0f, 0.0f, 1.0f), new Vektor4(1000f, 0f, 0f, 1.0f), this.sbO);
                // this.om.erzeugeObject(DICE, "overlay", new Quaternion(0.0f, 0.0f, 0.0f, 1.0f), new Vektor4(0f, 380f, 0f, 1.0f), this.sbO);
                // this.om.erzeugeObject(DICE, "overlay", new Quaternion(0.0f, 0.0f, 0.0f, 1.0f), new Vektor4(500f, 200f, 0f, 1.0f), this.sbO);
            }
        }
        this.spieler = new Spieler();
        this.spieler.setzMesh(this.gm.erzeuge(DICE));
        this.spieler.setzShader(sbS);

        this.om.hinzufuegenObject(this.spieler, "main");

        this.steuerung = new Steuerung(this, this.kamera, this.spieler);
        this.addKeyListener(this.steuerung);
        this.addMouseMotionListener(this.steuerung);
        this.addMouseListener(this.steuerung); 
        this.addMouseWheelListener(this.steuerung);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        this.width = width;
        this.height = height;
        float aspekt = ((float) this.width / (float) this.height);
        System.out.println("W: " + width + " H: " + height + " A:" + aspekt);
        this.kamera.setzAspekt(aspekt);
        this.frm.erneuerTextur("Hauptbuffer", this.width, this.height, GL_COLOR_ATTACHMENT0, GL_DEPTH_COMPONENT24);
        this.frm.erneuerTextur("Overlay", this.width, this.height, GL_COLOR_ATTACHMENT0, GL_DEPTH_COMPONENT24);
    }

    public static void createBuffer(GL4 gl, int bufferNummer, FloatBuffer bufferData) {
        gl.glBindBuffer(GL_ARRAY_BUFFER, bufferNummer);
        gl.glBufferData(GL_ARRAY_BUFFER, bufferData.capacity() * bytesPerFloat, bufferData, GL_STATIC_DRAW);
        gl.glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public static void createBuffer(GL4 gl, int bufferNummer, IntBuffer bufferData) {
        gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, bufferNummer);
        gl.glBufferData(GL_ELEMENT_ARRAY_BUFFER, bufferData.capacity() * bytesPerInt, bufferData, GL_STATIC_DRAW);
        gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public static void gabEsFehler(String wo, GL4 gl) {
        int err;
        while ((err = gl.glGetError()) != GL_NO_ERROR) {
            System.out.println(wo + " # " + err + " ");
        }
    }

    public static <T> T kopiereObject(T o) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(o);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        return (T) new ObjectInputStream(bais).readObject();
    }
}
