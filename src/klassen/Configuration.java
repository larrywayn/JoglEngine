package klassen;

public class Configuration {
    private final static String shaderPfad = "src/resource/shader/";
    private final static float sichtfeld = 45.0f;
    private final static float near = 1.0f;
    private final static float far = 100000.0f;
    private final static int fensterBreite = 1200;
    private final static int fensterHoehe = 740;
    private final static String titel = "MMO Game 1.0";
    private final static int framerate = 120;
    private final static String texturPfad = "C:\\Users\\larry\\Documents\\Projekte\\JoglEngine\\src\\resource\\texturen\\";
    private final static String geometriePfad = "C:\\Users\\larry\\Documents\\Projekte\\JoglEngine\\src\\resource\\meshes\\";

    public static String holGeometriePfad() {
        return geometriePfad;
    }

    protected Configuration() {
    }

    public static String holShaderPfad() {
        return shaderPfad;
    }

    public static float holSichtfeld() {
        return sichtfeld;
    }
    
    public static String holTexturPfad() {
        return texturPfad;
    }
    
    public static float holNear() {
        return near;
    }

    public static float holFar() {
        return far;
    }

    public static int holFensterBreite() {
        return fensterBreite;
    }

    public static int holFensterHoehe() {
        return fensterHoehe;
    }

    public static String holTitel() {
        return titel;
    }

    public static int holFramerate() {
        return framerate;
    }
}