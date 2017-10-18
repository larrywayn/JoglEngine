package grundklassen;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import com.jogamp.common.nio.Buffers;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import klassen.Configuration;
import klassen.GeometrieManager;
import klassen.Textur; 

public class Geometrie implements Runnable {

    public Geometrie(GeometrieManager mM) {
        this.mM = mM;
        this.istGeladen = false;
        this.istLadbar = false;
    }

    protected GeometrieManager mM;
    protected float[] verticesPlain;
    protected float[] uvsPlain;
    protected float[] normalenPlain;
    protected int[] indiciesPlain;
    protected int vertices;
    protected int uvs;
    protected int geomID;
    protected int normalen;
    protected int indicies;
    protected Textur textur;
    protected int texturID;
    protected boolean istGeladen;
    protected boolean istLadbar;
    protected String dateipfad;
    protected String texturname;

    public void setzDateipfad(String dateipfad) {
        this.dateipfad = dateipfad;
        this.istLadbar = true;
    }

    public int holVertices() {
        return this.vertices;
    }

    public int holNormalen() {
        return this.normalen;
    }

    public int holTexturenUV() {
        return this.uvs;
    }

    public int holIndizies() {
        return this.indicies;
    }

    public boolean hatVertices() {
        return (this.verticesPlain != null && this.verticesPlain.length > 0);
    }

    public boolean hatNormalen() {
        return (this.normalenPlain != null && this.normalenPlain.length > 0);
    }

    public boolean hatTexturenUV() {
        return (this.uvsPlain != null && this.uvsPlain.length > 0);
    }

    public boolean hatIndizies() {
        return (this.indiciesPlain != null && this.indiciesPlain.length > 0);
    }

    public float[] holVerticesPlain() {
        return this.verticesPlain;
    }

    public float[] holNormalenPlain() {
        return this.normalenPlain;
    }

    public float[] holTexturenUVPlain() {
        return this.uvsPlain;
    }

    public int[] holIndiziesPlain() {
        return this.indiciesPlain;
    }

    public int holTexturID() {
        return this.texturID;
    }

    public void setzTexturID(int texturID) {
        this.texturID = texturID;
    }

    protected FloatBuffer erzeugeBuffer(float[] daten) {
        FloatBuffer vertexBuffer = Buffers.newDirectFloatBuffer(daten);
        vertexBuffer.rewind();
        return vertexBuffer;
    }

    protected IntBuffer erzeugeBuffer(int[] daten) {
        IntBuffer indexBuffer = Buffers.newDirectIntBuffer(daten);
        indexBuffer.rewind();
        return indexBuffer;
    }

    public void setzTexturName(String texturname) {
        this.texturname = texturname;
    }

    public String holTexturName() {
        return this.texturname;
    }

    public void setzTextur(Textur textur) {
        this.textur = textur;
    }

    public Textur holTextur() {
        return this.textur;
    }

    public void setzGeometrieID(int geomID) {
        this.geomID = geomID;
    }

    private synchronized void ladeGeometrieDaten() {
        if (this.istLadbar) {
            try {
                System.out.println("Pfad: " + (Configuration.holGeometriePfad() + this.dateipfad));
                File file = new File(Configuration.holGeometriePfad() + this.dateipfad);
                if (file.exists()) {
                    this.ladeVonDatei(file);
                    System.out.println("Ergebnis:" + file.toString());
                    this.istGeladen = true;
                    mM.speichereMesh(this.geomID);
                }
            } catch (FileNotFoundException ex) {
                System.out.println("Datei fehlerhaft oder nicht gefunden:" + this.dateipfad);
            }
        }
    }

    public boolean istGeladen() {
        return this.istGeladen;
    }

    @Override
    public void run() {
        this.ladeGeometrieDaten();
    }

    public String holDateipfad() {
        return this.dateipfad;
    }

    private void ladeVonDatei(File file) throws FileNotFoundException {

        Scanner sc = new Scanner(file);
        String readLine = null;
        String line = null;

        while (sc.hasNext()) {
            readLine = sc.nextLine();
            readLine = readLine.trim();
            String[] parts = readLine.split(" ");
            switch (parts[0]) {
                case "v":
                    this.verarbeiteVertices(parts);
                    break;
                case "vn":
                    this.verarbeiteNormalen(parts);
                    break;
                case "vt":
                    this.verarbeiteUVs(parts);
                    break;
                case "f":
                    this.verarbeiteIndicies(parts);
                    break;
                default:
                    break;
            }
        }
        /*
$vert_array = array();
$norm_array = array();
$tex_array = array();
$index_ver_array = array();
$index_norm_array = array();
$index_tex_array = array();
$kleinste = -1;
$dd = 0;
    $fp = @fopen('../' . $_POST['pfad'] . '/' . $_POST['daten'], "r") or die("Kann Datei nicht lesen.");
    $type = null;
    $buffers = array('v ', 'vn ', 'vt ', 'f ');
    while (!feof($fp)) {
        $line2 = fgets($fp);
        $line = preg_replace("/[\n\r]/", "", $line2);
        foreach ($buffers AS $search) {
            $type = explode($search, $line);
            if (count($type) == 2) {
                $points = explode(' ', $type[1]);
                if ($search == 'v ') {
                    $vert_array[] = array(0 => $points[0], 1 => $points[1], 2 => $points[2]);
                } else if ($search == 'vn ') {
                    $norm_array[] = array(0 => $points[0], 1 => $points[1], 2 => $points[2]);
                } else if ($search == 'vt ') {
                    $tex_array[] = array(0 => $points[0], 1 => $points[1]);
                } else if ($search == 'f ') {
                    foreach ($points AS $doubleindex) {
                        $splitindex = explode('/', $doubleindex);
                        $splitindex[0] = trim($splitindex[0]);
                        $kleinste = ($kleinste == -1 || (intval($splitindex[0]) < $kleinste && !empty($splitindex[0]))) ? intval($splitindex[0]) : $kleinste;
                        if (count($splitindex) == 3) {
                            $splitindex[2] = trim($splitindex[2]);
                            $splitindex[1] = trim($splitindex[1]);
                            if (!empty($splitindex[2])) {
                                $dd += 1;
                                $index_norm_array[] = $splitindex[2];
                            }
                            if (!empty($splitindex[1])) {
                                $index_tex_array[] = $splitindex[1];
                            }
                            $index_ver_array[] = $splitindex[0];
                        } else if (count($splitindex) == 2) {
                            $splitindex[1] = trim($splitindex[1]);
                            if (!empty($splitindex[1])) {
                                $index_tex_array[] = $splitindex[1];
                            }
                            $index_ver_array[] = $splitindex[0];
                        } else if (!empty($splitindex[0])) {
                            $index_ver_array[] = $splitindex[0];
                        }
                    }
                }
                break 1;
            }
        }
    }
    fclose($fp);

$ver_js_array = '';
$index_js_array = '';
$norm_js_array = '';
$text_js_array = '';
$vertexe = 0;
$normalen = 0;
$texturen = 0;
$tmp_ini_array = array();
for ($count = 0; $count < count($index_ver_array); ++$count) {
    $tmp_ini_array[$count] = array('iv' => (intval($index_ver_array[$count])) - 1, 'in' => intval($index_norm_array[$count]) - 1, 'it' => intval($index_tex_array[$count]) - 1);
}
for ($count = 0; $count < count($tmp_ini_array); ++$count) {
    $var_ver = $vert_array[$tmp_ini_array[$count]['iv']];
    $var_norm = $norm_array[$tmp_ini_array[$count]['in']];
    $var_tex = $tex_array[$tmp_ini_array[$count]['it']];

    $index_js_array .= ($count + 1) . ',';
    if (count($var_ver) == 3) {
        $vertexe +=1;
        $ver_js_array .= $var_ver[0] . ',' . $var_ver[1] . ',' . $var_ver[2] . ',';
    }
    if (count($var_norm) == 3) {
        $normalen +=1;
        $norm_js_array .= $var_norm[0] . ',' . $var_norm[1] . ',' . $var_norm[2] . ',';
    }

    if (count($var_tex) == 2) {
        $texturen +=1;
        $text_js_array .= $var_tex[0] . ',' . -$var_tex[1] . ',';
    }
}

$gl_buffer = '{ "FILE": "' . $_POST['daten'] . '",
    "VERTEX": [' . (($vertexe <= 0) ? '' : '0.0,0.0,0.0, ' . substr($ver_js_array, 0, -1)) . '],
    "INDEX": [' . substr($index_js_array, 0, -1) . '],
    "NORMAL": [' . (($normalen <= 0) ? '' : '0.0,0.0,0.0,' . substr($norm_js_array, 0, -1)) . '],
    "TEXTURE": [' . (($texturen <= 0) ? '' : '0.0,0.0,' . substr($text_js_array, 0, -1)) . '] }';
echo $gl_buffer;*/
    }

    private void verarbeiteVertices(String[] parts) {
        System.out.println("v " + parts[1] + " # " + parts[2] + " # " + parts[3]);
    }

    private void verarbeiteNormalen(String[] parts) {
        System.out.println("vn " + parts[1] + " # " + parts[2] + " # " + parts[3]);
    }

    private void verarbeiteUVs(String[] parts) {
        System.out.println("vt " + parts[1] + " # " + parts[2]);
    }

    private void verarbeiteIndicies(String[] parts) {
        System.out.println("f " + parts[1]);
    }
}
