package klassen;

public class Configuration {
	private final static String ShaderPfad = "src/resource/shader/";
	private final static float Sichtfeld = 60.0f;
	private final static float Near = 0.1f;
	private final static float Far = 10000.0f;
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