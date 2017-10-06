package klassen;

import java.io.File;
import java.util.Scanner;

import grundklassen.ShaderTyp;

public class Shader {
	protected String name;
	protected ShaderTyp typ;
	protected String[] inhalt;
	protected boolean geladen;
	protected int setzProgramID;

	public Shader(ShaderTyp typ, String name) {
		this.name = name;
		this.typ = typ;
		this.geladen = false;
	}

	public ShaderTyp holTyp() {
		return this.typ;
	}

	public boolean ladeShader(File shaderDatei) {
			Scanner sr;
			try {
				sr = new Scanner(shaderDatei);
				// sr.useDelimiter("\\Z");
				// this.inhalt = sr.next();

				StringBuilder sb = new StringBuilder();
				while (sr.hasNextLine()) {
					sb.append(sr.nextLine()+"\n");
				}
				this.inhalt = new String[] { sb.toString() };
				sr.close();
				this.geladen = true;
                                System.out.println("Shader geladen: " + this.name+":"+this.typ);
				return true;
			} catch (Exception e) {
				return false;
			}
	}

	public String[] holInhalt() {
		return this.inhalt;
	}

	public boolean geladen() {
		return this.geladen;
	}

	public void setzProgramID(int id) {
		this.setzProgramID = id;
	}
}