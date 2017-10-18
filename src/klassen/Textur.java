/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package klassen;

import com.jogamp.opengl.util.texture.Texture;
import grundklassen.TexturTyp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author larry
 */
public class Textur implements Runnable {

    protected int texturID;
    protected Texture textur;
    protected BufferedImage origIMG;
    protected String dateipfad;
    protected boolean istGeladen;
    protected TexturManager tm;
    protected int objektID;
    protected File texturFile;
    protected TexturTyp texturTyp;
    protected String texturPfad;

    public Textur(String dateipfad, TexturManager tm, TexturTyp texturTyp, int objektID) {
        this.istGeladen = false;
        this.texturPfad = Configuration.holTexturPfad();
        this.dateipfad = dateipfad;
        this.tm = tm;
        this.texturTyp = texturTyp;
        this.objektID = objektID;
    }

    protected synchronized void ladeBildDaten() {
        try {
            System.out.println("Pfad: "+(this.texturPfad+dateipfad));
            this.texturFile = new File(this.texturPfad+dateipfad);
            if(this.texturFile.exists()){
                this.origIMG = ImageIO.read(this.texturFile);
                this.istGeladen = true;
                tm.speichereTextur(this);
            }
        } catch (IOException ex) {
        }
    }

    public boolean istGeladen() {
        return this.istGeladen;
    }
    @Override
    public void run() {
        this.ladeBildDaten();
    }

    public int holObjektID() {
        return this.objektID;
    }

    public Texture holGLTexture() {
        return this.textur;
    }
    
    public String holDateipfad(){
        return this.dateipfad;
    }

    public BufferedImage holImage() {
        return this.origIMG;
    }

    void setzTextur(Texture textur) {
       this.textur = textur;
    }

    File holFile() {
        return texturFile;
    }
}
