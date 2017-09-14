package klassen;

import grundklassen.DrawObjekt;

/**
 * Created by larry on 01.04.2016.
 */
public class Framebuffer extends DrawObjekt {

    private int ID;
    private int texturID;
    private int frameBufferID;
    private String name;
    private int renderBufferID;

    public Framebuffer(int newID) {
        super();
        this.ID = newID;
    }

    public String holName() {
        return this.name;
    }

    public int holTexturID() {
        return this.texturID;
    }

    public int holFramebufferID() {
        return this.frameBufferID;
    }

    public int holRenderbufferID() {
        return this.renderBufferID;
    }

    void setzName(String framebufferName) {
        this.name = framebufferName;
    }

    void setzRenderbufferID(int newRenderBufferID) {
        this.renderBufferID = newRenderBufferID;
    }

    void setzFramebufferID(int newFrameBufferID) {
        this.frameBufferID = newFrameBufferID;
    }

    void setzTexturID(int newTexturID) {
        this.texturID = newTexturID;
    }
}
