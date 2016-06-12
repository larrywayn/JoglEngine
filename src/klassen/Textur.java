/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package klassen;

import com.jogamp.opengl.util.texture.Texture;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author larry
 */
public class Textur implements Runnable {

    private int texturID;
    private Texture textur;
    private BufferedImage origIMG;
    private String dateipfad;
    private boolean istGeladen;
    private TexturManager tm;
    private int objektID;

    public Textur(String dateipfad, TexturManager tm, int objektID) {
        this.istGeladen = false;
        this.dateipfad = dateipfad;
        this.tm = tm;
        this.objektID = objektID;
    }

    private synchronized void ladeBildDaten() {
        try {
            System.out.println("Pfad: "+(Configuration.holTexturPfad()+dateipfad));
            File file = new File(Configuration.holTexturPfad()+dateipfad);
            if(file.exists()){
                System.out.println("Ergebnis:"+file.toString());
                this.origIMG = ImageIO.read(file);
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
}
