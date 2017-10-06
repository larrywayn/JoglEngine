/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package klassen;

import static com.jogamp.opengl.GL.*;
import com.jogamp.opengl.GL4;
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
        FrameRenderBufferManager.fid = 0;
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
        int cid = ++FrameRenderBufferManager.fid;
        Framebuffer frameBufferSpeicher = new Framebuffer(cid);
        frameBufferSpeicher.setzName(framebufferName);
        int[] fboHandles = new int[1];
        this.gl.glGenFramebuffers(1, fboHandles, 0);
        this.arF.put(framebufferName, frameBufferSpeicher);
        frameBufferSpeicher.setzFramebufferID(fboHandles[0]);
        erneuerTextur( framebufferName, width, height, GL_COLOR_ATTACHMENT0, GL_DEPTH_COMPONENT24);
        return frameBufferSpeicher;
    }

    private int erzeugeRenderTextur(int width, int height, int depth) {
        int[] renHandles = new int[1];
        this.gl.glGenRenderbuffers(1, renHandles, 0);
        this.gl.glBindRenderbuffer(GL_RENDERBUFFER, renHandles[0]);
        this.gl.glRenderbufferStorage(GL_RENDERBUFFER, depth, width, height);
        this.gl.glBindRenderbuffer(GL_RENDERBUFFER, 0);
        return renHandles[0];
    }

    private int erzeugeAbedoTextur(int width, int height) {
        this.gl.glEnable(GL_TEXTURE_2D);
        int[] texHandles = new int[1];
        this.gl.glGenTextures(1, texHandles, 0);
        this.gl.glBindTexture(GL_TEXTURE_2D, texHandles[0]);
        this.gl.glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, null);
        //  this.gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        //  this.gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        //         this.gl.glTexParameteri(GL_TEXTURE_2D, GL4.GL_TEXTURE_BASE_LEVEL, 0);
        // this.gl.glTexParameteri(GL_TEXTURE_2D, GL4.GL_TEXTURE_MAX_LEVEL, 0);
        //    this.gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        // this.gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        this.gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        this.gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        this.gl.glDisable(GL_TEXTURE_2D);
        this.gl.glBindTexture(GL_TEXTURE_2D, 0);
        return texHandles[0];
    }

    public void erneuerTextur(String bufferName, int width, int height, int attachment, int depth) {
        Framebuffer fbm = this.holBuffer(bufferName);
        if (fbm != null) {
            this.gl.glBindFramebuffer(GL_DRAW_FRAMEBUFFER, fbm.holFramebufferID());
            this.gl.glViewport(0, 0, width, height);
            this.gl.glEnable(GL_TEXTURE_2D);
            int renderID = erzeugeRenderTextur(width, height, depth);
            this.gl.glBindRenderbuffer(GL_RENDERBUFFER, renderID);
            int texturID = erzeugeAbedoTextur(width, height);
            this.gl.glBindTexture(GL_TEXTURE_2D, texturID);
            fbm.setzRenderbufferID(renderID);
            fbm.setzTexturID(texturID);
            this.gl.glFramebufferRenderbuffer(GL_DRAW_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, renderID);
            this.gl.glFramebufferTexture2D(GL_DRAW_FRAMEBUFFER, attachment, GL_TEXTURE_2D, texturID, 0);
            this.gl.glDrawBuffer(attachment);
            int[] framHandles = new int[1];
            IntBuffer fb = IntBuffer.wrap(framHandles);
            fb.put(attachment);
            this.gl.glDrawBuffers(1, fb);
            this.gl.glFramebufferParameteri(GL_DRAW_FRAMEBUFFER, GL4.GL_FRAMEBUFFER_DEFAULT_WIDTH, width);
            this.gl.glFramebufferParameteri(GL_DRAW_FRAMEBUFFER, GL4.GL_FRAMEBUFFER_DEFAULT_HEIGHT, height);
            this.gl.glFramebufferParameteri(GL_DRAW_FRAMEBUFFER, GL4.GL_FRAMEBUFFER_DEFAULT_SAMPLES, 4);
            if (this.gl.glCheckFramebufferStatus(GL_DRAW_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
                System.out.println("Framebuffer Fehler beim erzeugen");
            }
            this.gl.glDisable(GL_TEXTURE_2D);
            this.gl.glBindTexture(GL_TEXTURE_2D, 0);
            this.gl.glBindRenderbuffer(GL_RENDERBUFFER, 0);
            this.gl.glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
        }
    }
}
