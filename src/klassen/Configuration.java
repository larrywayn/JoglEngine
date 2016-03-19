package klassen;

public class Configuration {
	private final static String ShaderPfad = "src/resource/shader/";
	private final static float Sichtfeld = 45.0f;
	private final static float Near = 1.0f;
	private final static float Far = 100000.0f;
    private final static String Titel = "MMO Game 1.0";

    protected Configuration() {
	}
	public static String holShaderPfad(){
		return ShaderPfad;
	}

	public static float holSichtfeld() {
		return Sichtfeld;
	}

	public static float holNear() {
		return Near;
	}

	public static float holFar() {
		return Far;
	}

	public static String holTitel() { return Titel; }
}