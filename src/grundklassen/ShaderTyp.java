package grundklassen;

import com.jogamp.opengl.GL4;

public enum ShaderTyp {
	VERTEX, GEOMETRIE, FRAGMENT;
	public static String holEndung(ShaderTyp typ) {
		if (typ.equals(VERTEX)) {
			return "vert";
		} else if (typ.equals(GEOMETRIE)) {
			return "geom";
		} else if (typ.equals(FRAGMENT)) {
			return "frag";
		} else {
			return null;
		}
	}
	public static Integer holGLTyp(ShaderTyp typ) {
		if (typ.equals(VERTEX)) {
			return GL4.GL_VERTEX_SHADER;
		} else if (typ.equals(GEOMETRIE)) {
			return GL4.GL_GEOMETRY_SHADER;
		} else if (typ.equals(FRAGMENT)) {
			return GL4.GL_FRAGMENT_SHADER;
		} else {
			return null;
		}
	}
};
