package main;

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_ELEMENT_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_LINES;
import static com.jogamp.opengl.GL.GL_TEXTURE0;
import static com.jogamp.opengl.GL.GL_TEXTURE_2D;
import static com.jogamp.opengl.GL.GL_TRIANGLES;
import static com.jogamp.opengl.GL.GL_UNSIGNED_INT;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import grundklassen.DrawObjekt;
import grundklassen.Geometrie;
import klassen.*;
import klassen.geometrie.Wuerfel;
import klassen.geometrie.Linie;
import klassen.geometrie.Circle;
import static com.jogamp.opengl.GL4.*;
import com.jogamp.opengl.util.texture.Texture;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Random;

public class LarryEngineKern extends GLJPanel implements GLEventListener {

    private static final long serialVersionUID = -3272370017135790421L;
    private static final long bytesPerFloat = Float.SIZE / Byte.SIZE;
    private static final long bytesPerInt = Integer.SIZE / Byte.SIZE;
    private GLU glu;
    private GL4 gl;
    private ShaderBlock sb;
    private ArrayList<DrawObjekt> objektlist;
    private Kamera kamera;
    private int width;
    private int height;
    private Steuerung steuerung;
    private TexturManager tm;
    private GeometrieManager gm;
    private Spieler spieler;
    private Random zufallsGenerator;

    public LarryEngineKern() {
        super();
        this.addGLEventListener(this);
    }

    public float zufallsZahl(int min, int max) {
        return zufallsGenerator.nextInt((max - min) + 1) + min;
    }

    private void zeichneWelt() {
    }

    private void zeichneSpieler() {
        /*   gl.glUseProgram(sb.holProgram());
        Wuerfel wu = (Wuerfel)this.spieler.holMesh();
        gl.glEnableVertexAttribArray(0);
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, wu.holVertices());
        gl.glVertexAttribPointer(0, 3, GL4.GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(1);
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, wu.holNormalen());
        gl.glVertexAttribPointer(1, 3, GL4.GL_FLOAT, false, 0, 0);
        gl.glBindBuffer(GL4.GL_ELEMENT_ARRAY_BUFFER, wu.holIndizies());

        gl.glUniformMatrix4fv((gl.glGetUniformLocation(sb.holProgram(), "projMat")), 1, false, kamera.holPerspektive(), 0);
        gl.glUniformMatrix4fv((gl.glGetUniformLocation(sb.holProgram(), "camMat")), 1, false, kamera.holMatrix(), 0);
        gl.glUniformMatrix4fv((gl.glGetUniformLocation(sb.holProgram(), "modelMat")), 1, false, spieler.holMatrix(), 0);

        gl.glDrawElements(GL4.GL_TRIANGLES, wu.holIndiziesPlain().length, GL4.GL_UNSIGNED_INT, 0);
        gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        gl.glBindBuffer(GL_ARRAY_BUFFER, 0);
        gl.glDisableVertexAttribArray(0);
        gl.glDisableVertexAttribArray(1);
        LarryEngineKern.gabEsFehler("Display Spieler",gl, glu);*/
    }

    private void zeichneObjekte() {
        boolean onceC = false;
        Quaternion onceQ = new Quaternion();
        Vektor4 onceV = new Vektor4(0f, -2f, 0f, 1f);
        for (DrawObjekt zeichenObjekt : objektlist) {
            ShaderBlock sb = zeichenObjekt.holShader();
            if (sb == null) {
                sb = this.sb;
            }
            Geometrie mesh = zeichenObjekt.holMesh();
            if (mesh != null) {
                float[] matrix = zeichenObjekt.holAusrichtung().erzeugeMatrix(zeichenObjekt.holStandort());
                gl.glUseProgram(sb.holProgram());

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
                    gl.glUniform1i((gl.glGetUniformLocation(sb.holProgram(), "texture0")), 0);
                    gl.glUniform1f((gl.glGetUniformLocation(sb.holProgram(), "isTex")), 1.0f);

                    gl.glEnableVertexAttribArray(2);
                    gl.glBindBuffer(GL_ARRAY_BUFFER, uvbuf);
                    gl.glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
                    gl.glBindTexture(GL_TEXTURE_2D, t.getTextureObject());
                } else {
                    gl.glUniform1f((gl.glGetUniformLocation(sb.holProgram(), "isTex")), 0.0f);
                    // System.out.println("Textur im render nicht da");
                }
                gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mesh.holIndizies());

                gl.glUniformMatrix4fv((gl.glGetUniformLocation(sb.holProgram(), "projMat")), 1, false, this.kamera.holPerspektive(), 0);
                gl.glUniformMatrix4fv((gl.glGetUniformLocation(sb.holProgram(), "camMat")), 1, false, this.kamera.holMatrix(), 0);
                gl.glUniformMatrix4fv((gl.glGetUniformLocation(sb.holProgram(), "modelMat")), 1, false, matrix, 0);
                gl.glDrawElements(GL_TRIANGLES, mesh.holIndiziesPlain().length, GL_UNSIGNED_INT, 0);

                gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
                gl.glBindBuffer(GL_ARRAY_BUFFER, 0);
                gl.glDisableVertexAttribArray(0);
                gl.glDisableVertexAttribArray(1);

                Linie lineright = new Linie(this.gm, new Vektor4(0, 0, 0, 1), new Vektor4(0, 0, 5, 1));
                gl.glUniform3f((gl.glGetUniformLocation(sb.holProgram(), "colorIn")), 0.0f, 1.0f, 0.0f);
                gl.glUniform1f((gl.glGetUniformLocation(sb.holProgram(), "isTex")), 2.0f);
                gl.glEnableVertexAttribArray(0);
                gl.glBindBuffer(GL_ARRAY_BUFFER, lineright.holVertices());
                gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
                gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, lineright.holIndizies());
                gl.glDrawElements(GL_LINES, lineright.holIndiziesPlain().length, GL_UNSIGNED_INT, 0);

                gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
                gl.glBindBuffer(GL_ARRAY_BUFFER, 0);
                gl.glDisableVertexAttribArray(0);
                gl.glDisableVertexAttribArray(1);

                lineright = new Linie(this.gm, new Vektor4(0, 0, 0, 1), new Vektor4(0, 5, 0, 1));
                gl.glUniform3f((gl.glGetUniformLocation(sb.holProgram(), "colorIn")), 0.0f, 0.0f, 1.0f);
                gl.glUniform1f((gl.glGetUniformLocation(sb.holProgram(), "isTex")), 2.0f);
                gl.glEnableVertexAttribArray(0);
                gl.glBindBuffer(GL_ARRAY_BUFFER, lineright.holVertices());
                gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
                gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, lineright.holIndizies());
                gl.glDrawElements(GL_LINES, lineright.holIndiziesPlain().length, GL_UNSIGNED_INT, 0);

                gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
                gl.glBindBuffer(GL_ARRAY_BUFFER, 0);
                gl.glDisableVertexAttribArray(0);
                gl.glDisableVertexAttribArray(1);

                lineright = new Linie(this.gm, new Vektor4(0, 0, 0, 1), new Vektor4(5, 0, 0, 1));
                gl.glUniform1f((gl.glGetUniformLocation(sb.holProgram(), "isTex")), 2.0f);
                gl.glUniform3f((gl.glGetUniformLocation(sb.holProgram(), "colorIn")), 1.0f, 0.0f, 0.0f);
                gl.glEnableVertexAttribArray(0);
                gl.glBindBuffer(GL_ARRAY_BUFFER, lineright.holVertices());
                gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
                gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, lineright.holIndizies());
                gl.glDrawElements(GL_LINES, lineright.holIndiziesPlain().length, GL_UNSIGNED_INT, 0);

                gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
                gl.glBindBuffer(GL_ARRAY_BUFFER, 0);
                gl.glDisableVertexAttribArray(0);
                gl.glDisableVertexAttribArray(1);

                if (!onceC) {
                    gl.glUniformMatrix4fv((gl.glGetUniformLocation(sb.holProgram(), "modelMat")), 1, false, onceQ.erzeugeMatrix(onceV), 0);
                    Circle circle0 = new Circle(this.gm);
                    gl.glUniform3f((gl.glGetUniformLocation(sb.holProgram(), "colorIn")), 1.0f, 1.0f, 1.0f);
                    gl.glUniform1f((gl.glGetUniformLocation(sb.holProgram(), "isTex")), 2.0f);
                    gl.glEnableVertexAttribArray(0);
                    gl.glBindBuffer(GL_ARRAY_BUFFER, circle0.holVertices());
                    gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
                    gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, circle0.holIndizies());
                    gl.glDrawElements(GL_LINES, circle0.holIndiziesPlain().length, GL_UNSIGNED_INT, 0);
                    gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
                    gl.glBindBuffer(GL_ARRAY_BUFFER, 0);
                    gl.glDisableVertexAttribArray(0);
                    gl.glDisableVertexAttribArray(1);

                    lineright = new Linie(this.gm, new Vektor4(0, 0, 0, 1), new Vektor4(0, 0, 5, 1));
                    gl.glUniform3f((gl.glGetUniformLocation(sb.holProgram(), "colorIn")), 0.0f, 1.0f, 0.0f);
                    gl.glUniform1f((gl.glGetUniformLocation(sb.holProgram(), "isTex")), 2.0f);
                    gl.glEnableVertexAttribArray(0);
                    gl.glBindBuffer(GL_ARRAY_BUFFER, lineright.holVertices());
                    gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
                    gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, lineright.holIndizies());
                    gl.glDrawElements(GL_LINES, lineright.holIndiziesPlain().length, GL_UNSIGNED_INT, 0);

                    gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
                    gl.glBindBuffer(GL_ARRAY_BUFFER, 0);
                    gl.glDisableVertexAttribArray(0);
                    gl.glDisableVertexAttribArray(1);

                    lineright = new Linie(this.gm, new Vektor4(0, 0, 0, 1), new Vektor4(0, 5, 0, 1));
                    gl.glUniform3f((gl.glGetUniformLocation(sb.holProgram(), "colorIn")), 0.0f, 0.0f, 1.0f);
                    gl.glUniform1f((gl.glGetUniformLocation(sb.holProgram(), "isTex")), 2.0f);
                    gl.glEnableVertexAttribArray(0);
                    gl.glBindBuffer(GL_ARRAY_BUFFER, lineright.holVertices());
                    gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
                    gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, lineright.holIndizies());
                    gl.glDrawElements(GL_LINES, lineright.holIndiziesPlain().length, GL_UNSIGNED_INT, 0);

                    gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
                    gl.glBindBuffer(GL_ARRAY_BUFFER, 0);
                    gl.glDisableVertexAttribArray(0);
                    gl.glDisableVertexAttribArray(1);

                    lineright = new Linie(this.gm, new Vektor4(0, 0, 0, 1), new Vektor4(5, 0, 0, 1));
                    gl.glUniform1f((gl.glGetUniformLocation(sb.holProgram(), "isTex")), 2.0f);
                    gl.glUniform3f((gl.glGetUniformLocation(sb.holProgram(), "colorIn")), 1.0f, 0.0f, 0.0f);
                    gl.glEnableVertexAttribArray(0);
                    gl.glBindBuffer(GL_ARRAY_BUFFER, lineright.holVertices());
                    gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
                    gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, lineright.holIndizies());
                    gl.glDrawElements(GL_LINES, lineright.holIndiziesPlain().length, GL_UNSIGNED_INT, 0);

                    gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
                    gl.glBindBuffer(GL_ARRAY_BUFFER, 0);
                    gl.glDisableVertexAttribArray(0);
                    gl.glDisableVertexAttribArray(1);
                    onceC = true;
                }
                if (uvbuf != 0 && t != null) {
                    t.disable(gl);
                    gl.glDisable(GL_TEXTURE_2D);
                    gl.glDisableVertexAttribArray(2);
                }
            }
            LarryEngineKern.gabEsFehler("Display Objekte", gl, glu);
        }

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        gl = drawable.getGL().getGL4();
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        this.steuerung.aktualisiereKamera();
        tm.legeTexturAn();
        zeichneWelt();
        zeichneSpieler();
        zeichneObjekte();
    }

    @Override
    public void dispose(GLAutoDrawable arg0) {
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        this.width = drawable.getSurfaceWidth();
        this.height = drawable.getSurfaceHeight();

        gl = drawable.getGL().getGL4();
        glu = new GLU();
        gl.glClearColor(0.1f, 0.1f, 0.1f, 0.0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);
        gl.glHint(GL_FRAGMENT_SHADER_DERIVATIVE_HINT, GL_NICEST);
        gl.glViewport(0, 0, this.width, this.height);

        this.zufallsGenerator = new Random();
        this.tm = new TexturManager(gl);
        this.gm = new GeometrieManager(gl);
        this.objektlist = new ArrayList<>();
        this.sb = new ShaderBlock("standard");
        this.sb.erzeugeProgram(gl, true, true, false);

        this.kamera = new Kamera((this.width / this.height));
        this.spieler = new Spieler();
        Wuerfel w = new Wuerfel(this.gm);
        w.setzTexturName("grid.jpg");
        this.spieler.setzMesh(w);
        this.spieler.setzShader(sb);
        tm.ladeTextur(w);
        gm.ladeGeometrie("conecube.obj");
        this.objektlist.add(this.spieler);

        for (int i = 1000; i >= 0; --i) {
            Objekt3D tmpO = new Objekt3D();
            tmpO.setzShader(this.sb);
            Vektor4 vec = new Vektor4(zufallsZahl(-1000, 1000), zufallsZahl(-1000, 1000), zufallsZahl(-1000, 1000), 1.0f);
            tmpO.setzStandort(vec);
            tmpO.setzAusrichtung(new Quaternion(0.0f, 0.0f, 0.0f, 1.0f));
            Wuerfel w2 = new Wuerfel(this.gm);
            w2.setzTexturName("grid.jpg");
            tmpO.setzMesh(w2);
            objektlist.add(tmpO);
        }

        this.steuerung = new Steuerung(this, this.kamera, this.spieler);
        this.addKeyListener(this.steuerung);
        this.addMouseMotionListener(this.steuerung);
        this.addMouseListener(this.steuerung);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        gl = drawable.getGL().getGL4();
        if (height == 0) {
            height = 1;
        }
        this.width = width;
        this.height = height;
        this.kamera.setzAspekt(this.width / this.height);
        gl.glViewport(0, 0, this.width, this.height);
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

    public static void gabEsFehler(String wo, GL4 gl, GLU glu) {
        int err;
        while ((err = gl.glGetError()) != GL_NO_ERROR) {
            System.out.println(wo + " # " + err + ": " + glu.gluErrorString(err));
        }
    }

    public static <T> T kopiereObject(T o) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(o);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        return (T) new ObjectInputStream(bais).readObject();
    }
}
