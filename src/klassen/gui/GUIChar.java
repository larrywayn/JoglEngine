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
public class GUIChar  extends GUIInfo{
int advanced;
int vor;
public GUIChar(float[] texturKoordinaten, int weite, int hoehe, int advanced, int c)
{
	super(texturKoordinaten, weite, hoehe);
	this.advanced = advanced;
	this.vor = c;
}

public GUIChar()
{
	super();	
	this.advanced = 0;
	this.vor = 0;
}

public float[] getTexturKoordinatene(){return this.texturKoordinaten;};
public int getHohe(){return this.hoehe;};
public int getWeite(){return this.weite;};
public int getAdvanced(){return advanced;};
public int getVor(){return vor;};

public void setTexturKoordinatene(float[] tk){this.texturKoordinaten = tk;};
public void setHohe( int hoe){this.hoehe = hoe;};
public void setWeite(int wei){this.weite = wei;};
public void setAdvanced( int a){this.advanced = a;};
public void setVor( int c){this.vor = c;};


}
