package main;

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.GL.GL_ELEMENT_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_LEQUAL;
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
import java.util.TreeMap;
import klassen.geometrie.Cone;
import klassen.geometrie.Quad;

public class LarryEngineKern1 extends GLJPanel implements GLEventListener {

    private static final long serialVersionUID = -3272370017135790421L;
    private static final long bytesPerFloat = Float.SIZE / Byte.SIZE;
    private static final long bytesPerInt = Integer.SIZE / Byte.SIZE;
    private GLU glu;
    private GL4 gl;
    private ShaderBlock sb;
    private ShaderBlock sbOL;
    private ArrayList<DrawObjekt> objektlist;
    private ArrayList<DrawObjekt> overlaylist;
    private Kamera kamera;
    private int width;
    private int height;
    private Steuerung steuerung;
    private TexturManager tm;
    private GeometrieManager gm;
    private Spieler spieler;
    private Random zufallsGenerator;
    private TreeMap<String, Double> welt;
    private FrameRenderBufferManager frm;
    private ShaderBlock sbFB;
    private Quad mainPlane;
    private ShaderBlock sbSim;

    public LarryEngineKern1() {
        super();
        this.addGLEventListener(this);
    }

    public float zufallsZahl(int min, int max) {
        return zufallsGenerator.nextInt((max - min) + 1) + min;
    }

    public static double Noise(int seed, int x, int y, int octaves, int amplitude, double frequency, double persistence) {
        //returns -1 to 1
        double total = 0.0;
        double freq = frequency, amp = amplitude;
        for (int i = 0; i < octaves; ++i) {
            total = total + Smooth(seed, x * freq, y * freq) * amp;
            freq *= 2;
            amp *= persistence;
        }
        if (total < -2.4) {
            total = -2.4;
        } else if (total > 2.4) {
            total = 2.4;
        }

        return (total / 2.4);
    }

    public static double NoiseGeneration(int seed, int x, int y) {
        int n = x + y * 57;
        n = (n << 13) ^ n;

        return (1.0 - ((n * (n * n * 15731 + 789221) + seed) & 0x7fffffff) / 1073741824.0);
    }

    private static double Interpolate(double x, double y, double a) {
        double value = (1 - Math.cos(a * Math.PI)) * 0.5;
        return x * (1 - value) + y * value;
    }

    private static double Smooth(int seed, double x, double y) {
        double n1 = NoiseGeneration(seed, (int) x, (int) y);
        double n2 = NoiseGeneration(seed, (int) x + 1, (int) y);
        double n3 = NoiseGeneration(seed, (int) x, (int) y + 1);
        double n4 = NoiseGeneration(seed, (int) x + 1, (int) y + 1);

        double i1 = Interpolate(n1, n2, x - (int) x);
        double i2 = Interpolate(n3, n4, x - (int) x);

        return Interpolate(i1, i2, y - (int) y);
    }

    private void zeichneWelt() {
        ShaderBlock sb = this.sb;
        gl.glUseProgram(sb.holProgram());

        Quaternion tmpcam = this.spieler.holAusrichtung().kopiere();
        tmpcam.konjugante();
        Vektor4 line = this.spieler.holStandort().kopiere();
        //   System.out.println("Line");
        //    line.ausgabe();
        //   line = tmpcam.multipliziereV4(line);
        //    line.normalisiere();
        // line.setzY(line.holY()+2);    
        //   line.ausgabe();
        //  line.skaliere(20.0f);
        //  Vektor4 linestart = (line.kopiere()).addiereV4(this.kamera.holStandort());
        Circle circle0 = new Circle(this.gm);
        gl.glUniform3f((gl.glGetUniformLocation(sb.holProgram(), "colorIn")), 0.0f, 1.0f, 1.0f);
        gl.glUniform1f((gl.glGetUniformLocation(sb.holProgram(), "isTex")), 2.0f);

        gl.glEnableVertexAttribArray(0);
        gl.glBindBuffer(GL_ARRAY_BUFFER, circle0.holVertices());
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        float[] matrix = new Quaternion(0, 0, 0, 1).erzeugeMatrix(new Vektor4(0, 0, 0, 1));
        gl.glUniformMatrix4fv((gl.glGetUniformLocation(sb.holProgram(), "projMat")), 1, false, this.kamera.holPerspektive(), 0);
        gl.glUniformMatrix4fv((gl.glGetUniformLocation(sb.holProgram(), "camMat")), 1, false, this.kamera.holMatrix(), 0);
        gl.glUniformMatrix4fv((gl.glGetUniformLocation(sb.holProgram(), "modelMat")), 1, false, matrix, 0);

        gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, circle0.holIndizies());
        gl.glDrawElements(GL_LINES, circle0.holIndiziesPlain().length, GL_UNSIGNED_INT, 0);

        gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        gl.glBindBuffer(GL_ARRAY_BUFFER, 0);
        gl.glDisableVertexAttribArray(0);

        gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        gl.glBindBuffer(GL_ARRAY_BUFFER, 0);
        gl.glDisableVertexAttribArray(0);

        Linie lineright = new Linie(this.gm, new Vektor4(00, 0, 0, 1), new Vektor4(10, 0, 0, 1));
        gl.glUniform3f((gl.glGetUniformLocation(sb.holProgram(), "colorIn")), 0.0f, 1.0f, 0.0f);
        gl.glUniform1f((gl.glGetUniformLocation(sb.holProgram(), "isTex")), 2.0f);
        gl.glEnableVertexAttribArray(0);
        gl.glBindBuffer(GL_ARRAY_BUFFER, lineright.holVertices());
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        matrix = new Quaternion(0, 0, 0, 1).erzeugeMatrix(new Vektor4(0, 0, 0, 1));
        gl.glUniformMatrix4fv((gl.glGetUniformLocation(sb.holProgram(), "projMat")), 1, false, this.kamera.holPerspektive(), 0);
        gl.glUniformMatrix4fv((gl.glGetUniformLocation(sb.holProgram(), "camMat")), 1, false, this.kamera.holMatrix(), 0);
        gl.glUniformMatrix4fv((gl.glGetUniformLocation(sb.holProgram(), "modelMat")), 1, false, matrix, 0);

        gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, lineright.holIndizies());
        gl.glDrawElements(GL_LINES, lineright.holIndiziesPlain().length, GL_UNSIGNED_INT, 0);

        gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        gl.glBindBuffer(GL_ARRAY_BUFFER, 0);
        gl.glDisableVertexAttribArray(0);

        //  System.out.println("534, 20, 400 " + Noise(534, 20, 400, 8, 1, 0.015, 0.65));
        //  System.out.println("534, 563, 36 " + Noise(534, 563, 36, 8, 1, 0.015, 0.65));
        //  System.out.println("12, 563, 36 " + Noise(12, 563, 36, 8, 1, 0.015, 0.65));
        // System.out.println("12, 53, 36 " + Noise(12, 53, 36, 8, 1, 0.015, 0.65));
    }

    private void zeichneOverlay() {
        ShaderBlock sb = this.sbOL;
        //   Quaternion onceQ = new Quaternion();
        //   Vektor4 onceV = new Vektor4(0f, -2f, 0f, 1f);

        for (DrawObjekt zeichenObjekt : overlaylist) {
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
                System.out.println(t);
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
                }
                gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mesh.holIndizies());
                float[] yyy = {
                    2.0f / (800 - 0),
                    0,
                    0,
                    0,
                    0,
                    2.0f / (600 - 0),
                    0,
                    0,
                    0,
                    0,
                    -2.0f / (1000 - -1000),
                    0,
                    -(800 + 0) / (800 - 0),
                    -(600 + 0) / (600 - 0),
                    -(1000 + -1000) / (1000 - -1000),
                    1.0f}; //this.kamera.holMatrix()

                float[] zzz = {
                    2.0f / (-400f - 400f),
                    0,
                    0,
                    -(-400f + 400f) / (-400f - 400f),
                    0,
                    2.0f / (-300f - 300f),
                    0,
                    -(-300f + 300f) / (-300f - 300f),
                    0,
                    0,
                    -2.0f / (-100 - 100),
                    -(-100 + 100) / (-100 - 100),
                    0,
                    0,
                    0,
                    1.0f}; //this.kamera.holMatrix()
                gl.glUniformMatrix4fv((gl.glGetUniformLocation(sb.holProgram(), "projMat")), 1, false, this.kamera.holOrthogonal(), 0);
                gl.glUniformMatrix4fv((gl.glGetUniformLocation(sb.holProgram(), "camMat")), 1, false, this.kamera.holMatrix(), 0);
                gl.glUniformMatrix4fv((gl.glGetUniformLocation(sb.holProgram(), "modelMat")), 1, false, matrix, 0);
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
            }
            LarryEngineKern1.gabEsFehler("Display Objekte", gl, glu);
        }

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
        //   Quaternion onceQ = new Quaternion();
        //   Vektor4 onceV = new Vektor4(0f, -2f, 0f, 1f);
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

                /*    Linie lineright = new Linie(this.gm, new Vektor4(0, 0, 0, 1), new Vektor4(0, 0, 5, 1));
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
                }*/
                if (uvbuf != 0 && t != null) {
                    t.disable(gl);
                    gl.glDisable(GL_TEXTURE_2D);
                    gl.glDisableVertexAttribArray(2);
                }
            }
            LarryEngineKern1.gabEsFehler("Display Objekte", gl, glu);
        }
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        gl = drawable.getGL().getGL4();
        Framebuffer fbm = frm.holBuffer("Hauptbuffer");
        Framebuffer fbg = frm.holBuffer("Overlay");

        gl.glBindFramebuffer(GL_FRAMEBUFFER, fbm.holFramebufferID());
        gl.glClearColor(0.2f, 0.5f, 0.2f, 1.0f);
        //   gl.glClearDepth(1.0f);
        gl.glHint(GL_FRAGMENT_SHADER_DERIVATIVE_HINT, GL_NICEST);
        gl.glViewport(0, 0, this.width, this.height);
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthMask(true);
        gl.glDepthFunc(GL_LEQUAL);
        // gl.glDepthRange(0.0f, 1.0f);
        gl.glDrawBuffer(GL_COLOR_ATTACHMENT0);
        // gl.glFrontFace(GL_CW);
        // gl.glCullFace(GL4.GL_FRONT_AND_BACK); 
        this.steuerung.aktualisiereKamera();
        tm.legeTexturAn();
        // zeichneWelt();
        // zeichneSpieler();
        zeichneObjekte();
        gl.glBindFramebuffer(GL_FRAMEBUFFER, 0);

        gl.glBindFramebuffer(GL_FRAMEBUFFER, fbg.holFramebufferID());
        gl.glClearColor(0.2f, 0.2f, 0.5f, 0f);
        //  gl.glClearDepth(1.0f);
        //  gl.glHint(GL_FRAGMENT_SHADER_DERIVATIVE_HINT, GL_NICEST);
        //gl.glViewport(0, 0, this.width, this.height);
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        // gl.glDisable(GL_DEPTH_TEST);
        gl.glDepthMask(true);
        gl.glDepthFunc(GL_LEQUAL);
        //  gl.glDepthRange(0.0f, 1.0f);
        gl.glDrawBuffer(GL_COLOR_ATTACHMENT0);
        zeichneOverlay();
        gl.glBindFramebuffer(GL_FRAMEBUFFER, 0);
        gl.glUseProgram(this.sbSim.holProgram());
        gl.glClearColor(1.0f, 0.0f, 0.0f, 1f);
        //  gl.glDepthRange(1.0f, 0.0f);
        gl.glViewport(0, 0, this.width, this.height);
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        gl.glEnable(GL_DEPTH_TEST);
        gl.glEnableVertexAttribArray(0);

        gl.glBindBuffer(GL_ARRAY_BUFFER, this.mainPlane.holVertices());
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(1);
        gl.glBindBuffer(GL_ARRAY_BUFFER, this.mainPlane.holTexturenUV());
        gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        gl.glEnable(GL_TEXTURE_2D);
        gl.glActiveTexture(GL_TEXTURE0);

        int ttx = this.tm.holTexturID("grid.jpg");
        if (ttx != -1) {
            System.out.println("TexturID: " + ttx);
        }
        gl.glBindTexture(GL_TEXTURE_2D, fbm.holTexturID());
        gl.glUniform1i((gl.glGetUniformLocation(this.sbSim.holProgram(), "texture0")), 0);
        gl.glActiveTexture(GL_TEXTURE1);
        gl.glBindTexture(GL_TEXTURE_2D, fbg.holTexturID());
        gl.glUniform1i((gl.glGetUniformLocation(this.sbSim.holProgram(), "texture1")), 1);

        float rr = 20;
        float ll = 10;
        float tt = 7;
        float bb = 4;
        float ff = 100;
        float nn = -100;
        float[] xxx = {1.74f, 0, 0, 0,
            0, 1.9f, 0, 0,
            0, 0, -1.414f, -1f,
            -1.088f, 0, -1.66f, 3};
        float[] yyy = {
            2.0f / (rr - ll), 0, 0, 0,
            0, 2.0f / (tt - bb), 0, 0,
            0, 0, -2.0f / (ff - nn), 0,
            -(rr + ll) / (rr - ll),
            -(tt + bb) / (tt - bb),
            -(ff + nn) / (ff - nn), 1.0f}; //this.kamera.holMatrix()
        float[] nnn = {0.5f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f}; //this.kamera.holMatrix()
        float[] zzz = {2.0f / (rr - ll), 0, 0, -(rr + ll) / (rr - ll),
            0, 2.0f / (tt - bb), 0, -(tt + bb) / (tt - bb),
            0, 0, -2.0f / (ff - nn), -(ff + nn) / (ff - nn),
            0, 0, 0, 1.0f};
        float[] uuu = createOrtho(
                //float left, float right,
                0, 1000f,
                //float bottom, float top,
                0f, 600f,
                //float zNear, float zFar
                -1f, 1f
        );

        gl.glUniformMatrix4fv((gl.glGetUniformLocation(sbSim.holProgram(), "projMat")), 1, false, uuu, 0);
        gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.mainPlane.holIndizies());
        //gl.glDrawArrays(GL_TRIANGLES, 0, 6);       
        gl.glDrawElements(GL_TRIANGLES, this.mainPlane.holIndiziesPlain().length, GL_UNSIGNED_INT, 0);

        gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        gl.glBindBuffer(GL_ARRAY_BUFFER, 0);
        gl.glDisableVertexAttribArray(0);
        gl.glDisableVertexAttribArray(1);
        gl.glBindFramebuffer(GL_FRAMEBUFFER, 0);
        gl.glUseProgram(0);
    }

    @Override
    public void dispose(GLAutoDrawable arg0) {
    }

    public float[] createOrtho(
            float left, float right,
            float bottom, float top,
            float zNear, float zFar) {

                   final float dx = right - left;
        final float dy = top - bottom;
        final float dz = zFar - zNear;
        final float tx = -1.0f * (right + left) / dx;
        final float ty = -1.0f * (top + bottom) / dy;
        final float tz = -1.0f * (zFar + zNear) / dz;
        float[] m = new float[16];
          m[0 + 4 * 0] = 2.0f / dx;
        m[1 + 4 * 0] = 0f;
        m[2 + 4 * 0] = 0f;
        m[3 + 4 * 0] = tx;//0f;

        m[0 + 4 * 1] = 0f;
        m[1 + 4 * 1] = 2.0f / dy;
        m[2 + 4 * 1] = 0f;
        m[3 + 4 * 1] = ty;//0f;

        m[0 + 4 * 2] = 0f;
        m[1 + 4 * 2] = 0f;
        m[2 + 4 * 2] = -2.0f / dz;
        m[3 + 4 * 2] = tz;//0f;
        
        m[0 + 4 * 3] = 0f;//tx;
        m[1 + 4 * 3] = 0f;//ty;
        m[2 + 4 * 3] = 0f;//tz;
        m[3 + 4 * 3] = 0f;//1f;
        return m;

     /*   final float zNear2 = 2.0f * zNear;
        final float dx = right - left;
        final float dy = top - bottom;
        final float dz = zFar - zNear;
        final float A = (right + left) / dx;
        final float B = (top + bottom) / dy;
        final float C = -1.0f * (zFar + zNear) / dz;
        final float D = -2.0f * (zFar * zNear) / dz;
        m[0 + 4 * 0] = zNear2 / dx;
        m[1 + 4 * 0] = 0f;
        m[2 + 4 * 0] = 0f;
        m[3 + 4 * 0] = 0f;

        m[0 + 4 * 1] = 0f;
        m[1 + 4 * 1] = zNear2 / dy;
        m[2 + 4 * 1] = 0f;
        m[3 + 4 * 1] = 0f;

        m[0 + 4 * 2] = A;
        m[1 + 4 * 2] = B;
        m[2 + 4 * 2] = C;
        m[3 + 4 * 2] = -1.0f;

        m[0 + 4 * 3] = 0f;
        m[1 + 4 * 3] = 0f;
        m[2 + 4 * 3] = D;
        m[3 + 4 * 3] = 0f;

        return m;*/
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        this.width = drawable.getSurfaceWidth();
        this.height = drawable.getSurfaceHeight();
        System.out.println("Dimension: " + this.width + " # " + this.height);
        gl = drawable.getGL().getGL4();
        glu = new GLU();
        //   gl.glClearColor(0.1f, 0.1f, 0.1f, 0.0f);
        //   gl.glClearDepth(1.0f);
        //  gl.glEnable(GL_DEPTH_TEST);
        //  gl.glDepthFunc(GL_LEQUAL);
        // gl.glHint(GL_FRAGMENT_SHADER_DERIVATIVE_HINT, GL_NICEST);
        gl.glViewport(0, 0, this.width, this.height);

        this.zufallsGenerator = new Random();
        this.tm = new TexturManager(gl);
        this.gm = new GeometrieManager(gl);
        this.frm = new FrameRenderBufferManager(gl);
        this.objektlist = new ArrayList<>();
        this.overlaylist = new ArrayList<>();
        this.welt = new TreeMap<>();
        this.sb = new ShaderBlock("standard");
        this.sb.erzeugeProgram(gl, true, true, false);
        this.sbOL = new ShaderBlock("overlay");
        this.sbOL.erzeugeProgram(gl, true, true, false);
        this.sbFB = new ShaderBlock("framebuffer");
        this.sbFB.erzeugeProgram(gl, true, true, false);
        this.sbSim = new ShaderBlock("simple");
        this.sbSim.erzeugeProgram(gl, true, true, false);
        this.frm.erzeugeFramebuffer("Hauptbuffer", this.width, this.height);
        this.frm.erzeugeFramebuffer("Overlay", this.width, this.height);
        this.kamera = new Kamera((this.width / this.height));
        this.spieler = new Spieler();
        Wuerfel w = new Wuerfel(this.gm);
        w.setzTexturName("grid.jpg");
        this.spieler.setzMesh(w);
        this.spieler.setzShader(sb);
        tm.ladeTextur(w);
        gm.ladeGeometrie("conecube.obj");
        gm.ladeGeometrie("plane.obj");
        gm.ladeGeometrie("haus.obj");
        this.objektlist.add(this.spieler);
        Cone w2 = new Cone(this.gm);
        w2.setzTexturName("grid.jpg");
        for (int i = 1000; i >= 0; --i) {
            Objekt3D tmpO = new Objekt3D();
            tmpO.setzShader(this.sb);
            Vektor4 vec = new Vektor4(zufallsZahl(-500, 500), zufallsZahl(-10, 10), zufallsZahl(-500, 500), 1.0f);
            tmpO.setzStandort(vec);
            tmpO.setzAusrichtung(new Quaternion(0.0f, 0.0f, 0.0f, 1.0f));
            tmpO.setzMesh(w2);
            this.objektlist.add(tmpO);
        }
        Wuerfel w1 = new Wuerfel(this.gm);
        w1.setzTexturName("grid.jpg");
        for (int i = 1; i >= 0; --i) {
            Objekt3D tmpO = new Objekt3D();
            tmpO.setzShader(this.sbOL);
            Vektor4 vec = new Vektor4(10f, 10f, 0.0f, 0.0f);
            tmpO.setzStandort(vec);
            tmpO.setzAusrichtung(new Quaternion(0.0f, 0.0f, 0.0f, 1f));
            tmpO.setzMesh(w1);
            this.overlaylist.add(tmpO);
        }
        this.mainPlane = new Quad(gm);

        /* Objekt3D tmpO = new Objekt3D();
        tmpO.setzShader(this.sb);
        Vektor4 vec = new Vektor4(0f, 0f, 0f, 1.0f);
        tmpO.setzStandort(vec);
        tmpO.setzAusrichtung(new Quaternion(0.0f, 0.0f, 0.0f, 1f));
        Geometrie test = gm.holGeometrie("plane.obj");
        System.out.println("Geladen: "+test.istGeladen());
        tmpO.setzMesh();
        this.objektlist.add(tmpO);*/
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
        System.out.println("Dimension: " + this.width + " # " + this.height);
        // this.kamera.setzAspekt(this.width / this.height);
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
