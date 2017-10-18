/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grundklassen;

import klassen.Quaternion;
import klassen.Vektor4;

/**
 *
 * @author larry
 */
public abstract class GUIObjekt extends DrawObjekt{
    protected int absolutY;
    protected int absolutX;
    protected int marginLeft;
    protected int breite;
    protected int hoehe;
    protected int marginTop;
    protected String value;
       public GUIObjekt(int absolutX, int absolutY, String value) {
        this.absolutY = absolutY;
        this.absolutX = absolutX;
        this.marginLeft = 0;
        this.marginTop = 0;
        this.breite = 0;
        this.hoehe = 0;
        this.value = value;
    }
       
    public GUIObjekt( int breite, int hoehe, int absolutX, int absolutY, String value) {
        this.absolutY = absolutY;
        this.absolutX = absolutX;
        this.marginLeft = 0;
        this.breite = breite;
        this.hoehe = hoehe;
        this.marginTop = 0;
        this.value = value;
    }


    public GUIObjekt( int breite, int hoehe, int absolutX, int absolutY, int marginLeft, int marginTop, String value) {
        this.absolutY = absolutY;
        this.absolutX = absolutX;
        this.marginLeft = marginLeft;
        this.breite = breite;
        this.hoehe = hoehe;
        this.marginTop = marginTop;
        this.value = value;
    }

    public GUIObjekt(int absolutX, int absolutY, int marginLeft, int marginTop) {
        this.absolutY = absolutY;
        this.absolutX = absolutX;
        this.marginLeft = marginLeft;
        this.marginTop = marginTop;     
        this.breite = 0;
        this.hoehe = 0;
        this.value = "";
    }

    public int holAbsolutY() {
        return absolutY;
    }

    public int holAbsolutX() {
        return absolutX;
    }

    public int holMarginTop() {
        return marginTop;
    }

    public int holMarginLeft() {
        return marginLeft;
    }
    
    public int holHoehe() {
        return hoehe;
    }

    public int holBreite() {
        return breite;
    }
    public String holValue() {
        return value;
    }

    public void setzAbsolutX(int absolutX) {
        this.absolutX = absolutX;
    }

    public void setzAbsolutY(int absolutY) {
        this.absolutY = absolutY;
    }

    public void setzHoehe(int hoehe) {
        this.hoehe = hoehe;
    }

    public void setzBreite(int breite) {
        this.breite = breite;
    }

    public void setzValue(String value) {
        this.value = value;
    }

    public void setzMarginTop(int marginTop) {
        this.marginTop = marginTop;
    }

    public void setzMarginLeft(int marginLeft) {
        this.marginLeft = marginLeft;
    }

    @Override
    public Vektor4 holStandort() {
        this.standort.setzX( this.absolutX);
        this.standort.setzY( this.absolutY);
        return super.holStandort(); //To change body of generated methods, choose Tools | Templates.
    }

    
}
