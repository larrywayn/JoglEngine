/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package klassen;

import grundklassen.TexturTyp;
import java.io.File;

/*
 * @author larry
 */
public class TexturFont extends Textur {

    public TexturFont(String texturName, TexturManager tm, TexturTyp texturTyp, int caid) {
        super(texturName, tm, texturTyp ,caid);
        this.texturPfad = Configuration.holFontTexturPfad();
    }

    @Override
    protected synchronized void ladeBildDaten() {
        System.out.println("Pfad: " + (this.texturPfad + dateipfad));
        this.texturFile = new File(this.texturPfad + dateipfad);
        if (this.texturFile.exists()) {
            this.istGeladen = true;
            tm.speichereTextur(this);
        }
    }
}
