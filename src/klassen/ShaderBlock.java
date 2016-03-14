package klassen;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import grundklassen.ShaderTyp;
import main.LarryEngineKern;

import static com.jogamp.opengl.GL4.*;

public class ShaderBlock {

	protected String name;
	protected int program;
	protected Map<String, Integer> variablePositionen;
	protected Map<ShaderTyp, Shader> shaderListe;
	protected boolean geladen;

	public ShaderBlock(String name) {
		this.geladen = false;
		this.name = name;
		this.variablePositionen = new HashMap<String, Integer>();
		this.shaderListe = new HashMap<ShaderTyp, Shader>();
	}

	public void erzeugeProgram(GL4 gl, boolean vertex, boolean fragment, boolean geometrie) {
		for (ShaderTyp aktuellerTyp : ShaderTyp.values()) {
			String endung = ShaderTyp.holEndung(aktuellerTyp);
			File f = new File(Configuration.holShaderPfad() + this.name + "." + endung);
			if(f.exists() && !f.isDirectory()) {
				Shader shader;
				shader = new Shader(aktuellerTyp, this.name);
				shader.ladeShader(f);
				setzShader(shader);
			}
		}
		program = gl.glCreateProgram();
		for (Map.Entry<ShaderTyp, Shader> shaderDaten : this.shaderListe.entrySet()) {
			Shader sh = shaderDaten.getValue();
			if (sh != null && sh.geladen()) {
				try {
					System.out.println("Bearbeite shader "+sh.holTyp().toString());
					int shaderProgram = gl.glCreateShader(ShaderTyp.holGLTyp(sh.holTyp()));
					sh.setzProgramID(shaderProgram);
					gl.glShaderSource(shaderProgram, 1, sh.holInhalt(), null);
					gl.glCompileShader(shaderProgram);
					gl.glAttachShader(program, shaderProgram);
                    printShaderlog(gl, shaderProgram);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		gl.glLinkProgram(program);
		gl.glValidateProgram(program);
        printProgramlog(gl,program);
	/*	IntBuffer intBuffer = IntBuffer.allocate(1);
		gl.glGetProgramiv(program, GL_LINK_STATUS, intBuffer);
		geladen = true;
		if (intBuffer.get(0) != 1) {
			gl.glGetProgramiv(program, GL_INFO_LOG_LENGTH, intBuffer);
			int size = intBuffer.get(0);
			System.err.println("Program link error: ");
			if (size > 0) {
				ByteBuffer byteBuffer = ByteBuffer.allocate(size);
				gl.glGetProgramInfoLog(program, size, intBuffer, byteBuffer);
				for (byte b : byteBuffer.array()) {
					System.err.print((char) b);
				}
			} else {
				System.out.println("Unknown ShaderProgramm Error");
			}
			geladen = false;
		}*/
	}

    private void printShaderlog(GL4 gl, int shader) {
        IntBuffer intBuffer = Buffers.newDirectIntBuffer(1);
        gl.glGetShaderiv(shader, GL4.GL_INFO_LOG_LENGTH, intBuffer);
        int infoLength = intBuffer.get(0);
        if (infoLength > 1) {
            ByteBuffer byteBuffer = Buffers.newDirectByteBuffer(infoLength);
            gl.glGetShaderInfoLog(shader, infoLength, intBuffer, byteBuffer);
            byteBuffer.rewind();
            byte dst[] = new byte[byteBuffer.capacity()];
            byteBuffer.get(dst, 0, byteBuffer.capacity());
            String message = new String(dst);
            gl.glDeleteShader(shader);
            System.out.println(message);
            throw new IllegalStateException(message);
        }
    }

    private void printProgramlog(GL4 gl, int program) {
        IntBuffer intBuffer = Buffers.newDirectIntBuffer(1);
        gl.glGetProgramiv(program, GL4.GL_INFO_LOG_LENGTH, intBuffer);
        int infoLength = intBuffer.get(0);
        if (infoLength > 1) {
            ByteBuffer byteBuffer = Buffers.newDirectByteBuffer(infoLength);
            gl.glGetProgramInfoLog(program, infoLength, intBuffer, byteBuffer);
            byteBuffer.rewind();
            byte dst[] = new byte[byteBuffer.capacity()];
            byteBuffer.get(dst, 0, byteBuffer.capacity());
            String message = new String(dst);
            gl.glDeleteProgram(program);
            System.out.println(message);
            throw new IllegalStateException(message);
        }
    }

	public int holProgram() {
		return program;
	}

	public void setzVariable(String variablenName, Integer glPosition) {
		this.variablePositionen.put(variablenName, glPosition);
	}

	private void setzShader(Shader shader) {
		this.shaderListe.put(shader.holTyp(), shader);
	}
}
