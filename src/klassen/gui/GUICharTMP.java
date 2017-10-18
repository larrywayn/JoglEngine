/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package klassen.gui;

/**
 *
 * @author larry
 */
public class GUICharTMP{

    protected int textureBufferID;
    protected int charInt;
    public GUICharTMP(int charInt, int textureBufferID){
        this.textureBufferID = textureBufferID;
        this.charInt = charInt;
    }

    public int holCharInt() {
        return this.charInt;
    }

    public int holUVBuffer() {
       return textureBufferID;
    }

}
