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
public class GUIInfo {
	protected int weite;
	protected int hoehe;
	protected float[] texturKoordinaten;
	
	public GUIInfo(float[] texturKoordinaten, int weite, int hoehe) 
	{
		this.texturKoordinaten = texturKoordinaten;
		this.weite = weite;
		this.hoehe = hoehe;
	}
	
	public GUIInfo()
	{
		this.texturKoordinaten = new float[]{0,0,0,0};
		this.weite = 0;
		this.hoehe = 0;
	}
	
	public float[] getTexturKoordinatene(){return this.texturKoordinaten;};
	public int getHohe(){return this.hoehe;};
	public int getWeite(){return this.weite;};

	public void setTexturKoordinatene(float[] texturKoordinaten){this.texturKoordinaten = texturKoordinaten;};
	public void setHohe(int hoehe){this.hoehe = hoehe;};
	public void setWeite(int weite){this.weite = weite;};
}
