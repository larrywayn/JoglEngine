package main;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;

import klassen.*;
import klassen.geometrie.Wuerfel;
import static com.jogamp.opengl.GL4.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class LarryEngineKern extends GLJPanel implements GLEventListener {

    private static final long serialVersionUID = -3272370017135790421L;
    private static final long bytesPerFloat = Float.SIZE / Byte.SIZE;
    private static final long bytesPerInt = Integer.SIZE / Byte.SIZE;
    private GLU glu;
    private GL4 gl;
    private ShaderBlock sb;
    private Wuerfel line;
    private Kamera kamera;
    private int width;
    private int height;
    private Steuerung steuerung;
    private Spieler spieler;

    public LarryEngineKern() {
        super();
        this.addGLEventListener(this);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        gl = drawable.getGL().getGL4();
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        gl.glUseProgram(sb.holProgram());

        gl.glEnableVertexAttribArray(0);
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, line.holVertices());
        gl.glVertexAttribPointer(0, 3, GL4.GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(1);
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, line.holNormalen());
        gl.glVertexAttribPointer(1, 3, GL4.GL_FLOAT, false, 0, 0);
        gl.glBindBuffer(GL4.GL_ELEMENT_ARRAY_BUFFER, line.holIndizies());

      //  Quaternion invertView = mapper.convertValue(kamera.holAusrichtung(), Quaternion.class);
    //    invertView.invertiere();
      //  Vektor4 invertStandort = mapper.convertValue(kamera.holStandort(), Vektor4.class);
        //Vektor4 modelPos = new Vektor4(-5.0f, -5.0f, -10.0f, 0.0f);
       // Quaternion q = new Quaternion(0.0f,0.0f,0.0f,0.0f);
        //float[] modelMat = q.erzeugeMatrix(modelPos);
        /*q.ausgabe();
        System.out.println(modelMat[0]+" # "+modelMat[1]+" # "+modelMat[2]+" # "+modelMat[3]);
        System.out.println(modelMat[4]+" # "+modelMat[5]+" # "+modelMat[6]+" # "+modelMat[7]);
        System.out.println(modelMat[8]+" # "+modelMat[9]+" # "+modelMat[10]+" # "+modelMat[11]);
        System.out.println(modelMat[12]+" # "+modelMat[13]+" # "+modelMat[14]+" # "+modelMat[15]);
        System.out.println("-----------------------------");*/

        gl.glUniformMatrix4fv((gl.glGetUniformLocation(sb.holProgram(), "projMat")), 1, false, kamera.holPerspektive(), 0);
        gl.glUniformMatrix4fv((gl.glGetUniformLocation(sb.holProgram(), "camMat")), 1, false, kamera.holMatrix(), 0);
        gl.glUniformMatrix4fv((gl.glGetUniformLocation(sb.holProgram(), "modelMat")), 1, false, spieler.holMatrix(), 0);

    //    kamera.holAusrichtung().ausgabe();
     //   q.ausgabe();
      //  System.out.println("-----------------------------");
      //  gl.glUniform4fv((gl.glGetUniformLocation(sb.holProgram(), "camQuat")), 1, kamera.holAusrichtung().alsArray(), 0);
      //  gl.glUniform4fv((gl.glGetUniformLocation(sb.holProgram(), "camPos")), 1, kamera.holStandort().alsArray(true), 0);
       // gl.glUniformMatrix4fv((gl.glGetUniformLocation(sb.holProgram(), "tmpMat")), 1, false, new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f}, 0);
      //  gl.glUniform4fv((gl.glGetUniformLocation(sb.holProgram(), "viewQuat")), 1, invertView.alsArray(), 0);
      //  gl.glUniform3fv((gl.glGetUniformLocation(sb.holProgram(), "viewPos")), 1, invertStandort.alsArray(), 0);
      //  gl.glUniform4fv((gl.glGetUniformLocation(sb.holProgram(), "modelQuat")), 1, new Quaternion(1.0f,1.0f,1.0f,1.0f).alsArray(), 0);
      //  gl.glUniform3fv((gl.glGetUniformLocation(sb.holProgram(), "modelPos")), 1, new Vektor4(0.0f, 0.0f, 0.0f, 1.0f).alsArray(true), 0);

        gl.glDrawElements(GL4.GL_TRIANGLES, line.holIndiziesPlain().length, GL4.GL_UNSIGNED_INT, 0);
        gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        gl.glBindBuffer(GL_ARRAY_BUFFER, 0);
        gl.glDisableVertexAttribArray(0);
        gl.glDisableVertexAttribArray(1);
        LarryEngineKern.gabEsFehler("Display C",gl, glu);
    }

    @Override
    public void dispose(GLAutoDrawable arg0) {
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        gl = drawable.getGL().getGL4();
        glu = new GLU();
        gl.glClearColor(0.1f, 0.1f, 0.1f, 0.0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);
        gl.glHint(GL_FRAGMENT_SHADER_DERIVATIVE_HINT, GL_NICEST);
        this.sb = new ShaderBlock("standard");
        this.width = drawable.getSurfaceWidth();
        this.height = drawable.getSurfaceHeight();
        this.kamera = new Kamera((this.width/this.height));
        this.spieler = new Spieler();
        this.sb.erzeugeProgram(gl, true, true, false);
        this.line = new Wuerfel(gl);
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

    public static void gabEsFehler(String wo, GL4 gl, GLU glu){
        int err;
        while ((err = gl.glGetError()) != GL_NO_ERROR) {
            System.out.println(wo+" # "+err+": "+glu.gluErrorString(err));
        }
    }

    public static <T> T kopiereObject( T o ) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new ObjectOutputStream( baos ).writeObject( o );
        ByteArrayInputStream bais = new ByteArrayInputStream( baos.toByteArray() );
        return (T) new ObjectInputStream(bais).readObject();
    }
}