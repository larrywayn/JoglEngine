package main;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;

import klassen.Kamera;
import klassen.Quaternion;
import klassen.ShaderBlock;
import klassen.Vektor4;
import klassen.geometrie.Wuerfel;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.jogamp.opengl.GL4.*;

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
    private ObjectMapper mapper;

    public LarryEngineKern() {
        super();
        this.addGLEventListener(this);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        LarryEngineKern.gabEsFehler("Display 0", gl, glu);
        gl = drawable.getGL().getGL4();
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        gl.glUseProgram(sb.holProgram());

        // gl.glEnableVertexAttribArray(0);

        // Select the VBO, GPU memory data, to use for colors
        // gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, line.holVertices());
        // gl.glVertexAttribPointer(0, 3, GL4.GL_FLOAT, false, 0, 0);

        // gl.glBindBuffer(GL4.GL_ELEMENT_ARRAY_BUFFER, line.holIndizies());
        // //the vertex data
        // Associate Vertex attribute 1 with the last bound VBO
        // gl.glVertexAttribPointer(1,3,GL4.GL_INT, false, 0, 0 );
        LarryEngineKern.gabEsFehler("Display A",gl, glu);


        gl.glEnableVertexAttribArray(0);

        // Select the VBO, GPU memory data, to use for colors
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, line.holVertices());
        gl.glVertexAttribPointer(0, 3, GL4.GL_FLOAT, false, 0, 0);

        // ***************The render routine*****************

        gl.glBindBuffer(GL4.GL_ELEMENT_ARRAY_BUFFER, line.holIndizies());

        Quaternion invertView = mapper.convertValue(kamera.holAusrichtung(), Quaternion.class);
        invertView.invertiere();
        Vektor4 invertStandort = mapper.convertValue(kamera.holStandort(), Vektor4.class);

        gl.glUniformMatrix4fv((gl.glGetUniformLocation(sb.holProgram(), "projMat")), 1, false, kamera.holPerspektive(), 0);
        gl.glUniformMatrix4fv((gl.glGetUniformLocation(sb.holProgram(), "tmpMat")), 1, false, new float[]{0.0f, 0.0f, 0.0f, 0.0f,0.0f, 0.0f, 0.0f, 0.0f,0.0f, 0.0f, 0.0f, 0.0f,0.0f, 0.0f, 0.0f, 0.0f}, 0);
        gl.glUniform4fv((gl.glGetUniformLocation(sb.holProgram(), "viewQuat")), 1, invertView.alsArray(), 0);
        gl.glUniform3fv((gl.glGetUniformLocation(sb.holProgram(), "viewPos")), 1, invertStandort.alsArray(), 0);
        gl.glUniform4fv((gl.glGetUniformLocation(sb.holProgram(), "modelQuat")), 1, new Quaternion(0f,0f,0f,1.0f).alsArray(), 0);
        gl.glUniform3fv((gl.glGetUniformLocation(sb.holProgram(), "modelPos")), 1, new Vektor4(0.0f, 0.0f, 0.0f, 0.0f).alsArray(), 0);

        LarryEngineKern.gabEsFehler("Display B",gl, glu);
        gl.glDrawElements(GL4.GL_TRIANGLES, // mode
                line.holIndiziesPlain().length, // count
                GL4.GL_UNSIGNED_INT, // type
                0 // element array buffer offset
        );
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
        //gl.glClearDepth(1.0f);
        //gl.glEnable(GL_DEPTH_TEST);
        //gl.glDepthFunc(GL_LEQUAL);
       // gl.glHint(GL_FRAGMENT_SHADER_DERIVATIVE_HINT, GL_NICEST);
        sb = new ShaderBlock("standard");
        this.width = drawable.getSurfaceWidth();
        this.height = drawable.getSurfaceHeight();
        kamera = new Kamera((this.width/this.height));
        LarryEngineKern.gabEsFehler("INIT A",gl, glu);
        sb.erzeugeProgram(gl, true, true, false);
        line = new Wuerfel(gl);
        LarryEngineKern.gabEsFehler("INIT B",gl, glu);
        mapper = new ObjectMapper();
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
       // glu.gluPerspective(45.0, aspect, 0.1, 100.0);
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
}