/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package klassen;

import static com.jogamp.opengl.GL.*;
import com.jogamp.opengl.GL4;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author larry
 */
public class FrameRenderBufferManager {

    private ConcurrentHashMap<String, Framebuffer> arF;
    private GL4 gl;
    private static int fid;

    public FrameRenderBufferManager(GL4 gl) {
        this.arF = new ConcurrentHashMap<>();
        this.gl = gl;
        this.fid = 0;
    }

    public void ausgabe() {
        for (String key : this.arF.keySet()) {
            System.out.print("Key: " + key + " - ");
            System.out.print("Value: " + this.arF.get(key) + "\n");
        }
    }

    public Framebuffer holBuffer(String framebufferName) {
        return this.arF.get(framebufferName);
    }

    public Framebuffer erzeugeFramebuffer(String framebufferName, int width, int height) {
        int cid = ++this.fid;
        Framebuffer frameBufferSpeicher = new Framebuffer(cid);
        frameBufferSpeicher.setzName(framebufferName);
        int[] fboHandles = new int[1];
        // IntBuffer ib = IntBuffer.wrap(fboHandles);
        this.gl.glGenFramebuffers(1, fboHandles, 0);
        this.arF.put(framebufferName, frameBufferSpeicher);
        this.gl.glBindFramebuffer(GL_FRAMEBUFFER, fboHandles[0]);
                gl.glViewport(0, 0, width, height);
        this.gl.glEnable(GL_TEXTURE_2D);
        int[] texHandles = new int[1];
        // IntBuffer tb = IntBuffer.wrap(texHandles);
        this.gl.glGenTextures(1, texHandles, 0);
        this.gl.glBindTexture(GL_TEXTURE_2D, texHandles[0]);
      //  this.gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
      //  this.gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
                this.gl.glTexParameteri(GL_TEXTURE_2D, GL4.GL_TEXTURE_BASE_LEVEL, 0);
        this.gl.glTexParameteri(GL_TEXTURE_2D, GL4.GL_TEXTURE_MAX_LEVEL, 0);
        this.gl.glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, null);//ByteBuffer.allocate(width * height * 4)); // 

        int[] renHandles = new int[1];
        //IntBuffer rb = IntBuffer.wrap(renHandles);
        this.gl.glGenRenderbuffers(1, renHandles, 0);
        this.gl.glBindRenderbuffer(GL_RENDERBUFFER, renHandles[0]);
        this.gl.glRenderbufferStorage(GL_RENDERBUFFER, GL4.GL_DEPTH_COMPONENT, width, height);

        this.gl.glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texHandles[0], 0);
        this.gl.glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, renHandles[0]);
        //this.gl.glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, tb.get(0), 0);
       /* this.gl.glDrawBuffer(GL_COLOR_ATTACHMENT0);
        int[] framHandles = new int[1];
        IntBuffer fb = IntBuffer.wrap(framHandles);
        fb.put(GL_COLOR_ATTACHMENT0);
        this.gl.glDrawBuffers(1, fb);*/

        frameBufferSpeicher.setzRenderbufferID(renHandles[0]);
        frameBufferSpeicher.setzFramebufferID(fboHandles[0]);
        frameBufferSpeicher.setzTexturID(texHandles[0]);

        if (this.gl.glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            System.out.println("Framebuffer Fehler beim erzeugen");
        }
        this.gl.glBindTexture(GL_TEXTURE_2D, 0);
        this.gl.glBindRenderbuffer(GL_RENDERBUFFER, 0);
        this.gl.glBindFramebuffer(GL_FRAMEBUFFER, 0);
        this.gl.glDisable(GL_TEXTURE_2D);
        return frameBufferSpeicher;
    }

    /* public Integer erzeugeRenderbuffer(String framebufferName, int size) {
        int cid = ++this.fid;
        int[] vboHandles = new int[1];
        this.gl.glGenBuffers(1, vboHandles, 0);

        // this.gl.glGenFramebuffers(size, vboHandles[0]);
        return this.arF.put(framebufferName, cid);
    }*/
}
