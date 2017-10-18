/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package klassen.gui;

import java.util.ArrayList;
import klassen.Quaternion;
import klassen.Vektor4;

/**
 *
 * @author larry
 */
public class GUILabel {

    protected String viewText;
    protected Vektor4 position;
    protected ArrayList<GUICharTMP> algc;
    protected GUIFont gFont;
    protected Quaternion ausrichtung;
    protected float fontSize;

    public GUILabel(String labelText, Quaternion ausrichtung, int posX, int posY, GUIFont gFont, float fontSize) {
        this.viewText = labelText;
        this.algc = new ArrayList<>();
        this.position = new Vektor4(posX, posY + fontSize, 1f, 1f);
        this.gFont = gFont;
        this.fontSize = fontSize;
        this.ausrichtung = ausrichtung;
        this.ausrichtung.setzSkalierung(fontSize, fontSize+4f, fontSize);
        this.erzeugeDaten();
    }

    private void erzeugeDaten() {
        for (char c : this.viewText.toCharArray()) {
            this.algc.add(gFont.getChar(c));
        }
    }

    public ArrayList<GUICharTMP> holDaten() {
        return this.algc;
    }

    public float holFontSize() {
        return this.fontSize;
    }

    public GUIFont holFont() {
        return this.gFont;
    }

    public Quaternion holAusrichtung() {
        return this.ausrichtung;
    }

    public Vektor4 holPosition() {
        return this.position;
    }

    public void anpassenText(String neuerText) {
        this.viewText = neuerText;
        this.algc.clear();
        for (char c : this.viewText.toCharArray()) {
            this.algc.add(gFont.getChar(c));
        }
    }
}
