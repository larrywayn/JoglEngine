/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package klassen;

import com.jogamp.opengl.GL4;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;
import klassen.geometrie.Flex;

/**
 *
 * @author larry
 */
public class MapManager {

    private GL4 gl;
    private FrameRenderBufferManager fbm;
    private int tmpTextur;
    private static int aid;
    private Random zufallsGenerator;
    private final TreeMap<String, float[]> welt;
    private int width;

    public MapManager(GL4 gl, FrameRenderBufferManager fbm) {
        this.gl = gl;
        this.aid = 0;
        this.fbm = fbm;
        this.width = 1000;
        this.welt = new TreeMap<>();
        this.tmpTextur = fbm.erzeugeAbedoTextur(this.width, this.width);
    }

    public float zufallsZahl(float min, float max) {
        return min + (max - min) * this.zufallsGenerator.nextFloat(); //zufallsGenerator.nextInt((max - min) + 1) + min;
    }

    private static double Noise(int seed, int x, int y, int octaves, int amplitude, double frequency, double persistence) {
        //returns -1 to 1
        double total = 0.0;
        double freq = frequency, amp = amplitude;
        for (int i = 0; i < octaves; ++i) {
            total = total + Smooth(seed, x * freq, y * freq) * amp;
            freq *= 2;
            amp *= persistence;
        }
        if (total < -2.4) {
            total = -2.4;
        } else if (total > 2.4) {
            total = 2.4;
        }

        return (total / 2.4);
    }

    private static double NoiseGeneration(int seed, int x, int y) {
        int n = x + y * 57;
        n = (n << 13) ^ n;

        return (1.0 - ((n * (n * n * 15731 + 789221) + seed) & 0x7fffffff) / 1073741824.0);
    }

    private static double Interpolate(double x, double y, double a) {
        double value = (1 - Math.cos(a * Math.PI)) * 0.5;
        return x * (1 - value) + y * value;
    }

    private static double Smooth(int seed, double x, double y) {
        double n1 = NoiseGeneration(seed, (int) x, (int) y);
        double n2 = NoiseGeneration(seed, (int) x + 1, (int) y);
        double n3 = NoiseGeneration(seed, (int) x, (int) y + 1);
        double n4 = NoiseGeneration(seed, (int) x + 1, (int) y + 1);

        double i1 = Interpolate(n1, n2, x - (int) x);
        double i2 = Interpolate(n3, n4, x - (int) x);

        return Interpolate(i1, i2, y - (int) y);
    }

    public GL4 holGL() {
        return this.gl;
    }

    public static int stringToSeed(String s) {
        if (s == null) {
            return 0;
        }
        long hash = 0;
        for (char c : s.toCharArray()) {
            hash = 31L * hash + c;
        }
        return (int) hash;
    }

    private void ermittelHoehe() {
    }

    private float[] ermittelWasserLand(int posx, int posy, ArrayList<ArrayList<float[]>> map, double wasserNebenWasser, double landNebenLand, double land, float offset) {
        //16
        //System.out.println("");
        //System.out.println("x:" + posx + " y:" + posy);
        //   System.out.println("x,y " + posx + " " + posy);
        float[] data = new float[4];
        //  ArrayList<Double> mapdata = map.get(posx);
        float w1 = this.zufallsZahl(-40.0f, 40.0f);
        float w2 = this.zufallsZahl(-40.0f, 40.0f);
        float w3 = this.zufallsZahl(-40.0f, 40.0f);
        float w4 = this.zufallsZahl(-40.0f, 40.0f);
        //   double ergebnis = (wl < land) ? 1 : -1;
        //   mapdata.set(posy, ergebnis);

        float[] linkerfaktor = null;
        float[] obererefaktor = null;
        float[] olfaktor = null;

        //links
        if (posy - 1 >= 0) {
            ArrayList<float[]> tmpmap = map.get((posx));
            float[] dataf = tmpmap.get((posy - 1));
            //System.out.println("FDATA Y 0: " + dataf[0] + " 1: " + dataf[1] + " 2: " + dataf[2] + " 3: " + dataf[3]);
            linkerfaktor = new float[1];
            linkerfaktor[0] = dataf[3];
            olfaktor = new float[1];
            olfaktor[0] = dataf[1];
        }
        //oben
        if (posx - 1 >= 0) {
            ArrayList<float[]> tmpmap = map.get((posx - 1));
            float[] dataf = tmpmap.get((posy));
            obererefaktor = new float[1];
            obererefaktor[0] = dataf[3];
            //System.out.println("FDATA X 0: " + dataf[0] + " 1: " + dataf[1] + " 2: " + dataf[2] + " 3: " + dataf[3]);
            if (olfaktor == null) {
                olfaktor = new float[1];
                olfaktor[0] = dataf[2];
            }
        }
        if (linkerfaktor == null) {
            linkerfaktor = new float[1];
            linkerfaktor[0] = w1;
        }
        if (obererefaktor == null) {
            obererefaktor = new float[1];
            obererefaktor[0] = w2;
        }
        if (olfaktor == null) {
            olfaktor = new float[1];
            olfaktor[0] = w3;
        }

        //ol
        data[0] = olfaktor[0];//
        
        //or
        data[1] = obererefaktor[0];//
        
        //ul
        data[2] = linkerfaktor[0];//
        
        //ur
        data[3] = w4;

        //System.out.println("0: " + data[0] + " 1: " + data[1] + " 2: " + data[2] + " 3: " + data[3]);
        //System.out.println("old l: " + linkerfaktor[0] + " o: " + obererefaktor[0] + " ol: " + olfaktor[0]);
        return data;
    }

    public Flex createMap(GeometrieManager gm, String seed) {
        int seednumber = MapManager.stringToSeed(seed);
        this.zufallsGenerator = new Random(seednumber);
        System.out.println("Seed " + seed + " Seednumber " + seednumber);
        double wasserNebenWasser = 0.7;
        double landNebenLand = 0.9;
        double land = 0.8;
        int persistence = 40;
        this.width = 200;
        double wasserHoehe = 0.3;

        float[] verticesPlain = new float[(this.width * this.width * 3 * 4) + 1];
        int[] indiciesPlain = new int[(this.width * this.width * 3 * 2) + 1];

        int korx = -(this.width/2)*persistence*2;
        int kory = -(this.width/2)*persistence*2;
        int counter = 0;
        int counter2 = 0;
        int counter3 = -1;
        ArrayList<ArrayList<float[]>> map = new ArrayList<>();
        for (int x = 0; x < this.width; ++x) {
            ArrayList<float[]> mapdata = new ArrayList<>();
            map.add(mapdata);
            korx = -(this.width/2)*persistence*2;
            for (int y = 0; y < this.width; ++y) {
                float[] npos = ermittelWasserLand(x, y, map, wasserNebenWasser, landNebenLand, land, this.width);
                verticesPlain[counter++] = korx;
                verticesPlain[counter++] = npos[2];
                verticesPlain[counter++] = kory + (persistence*2);
                ++counter3;
                verticesPlain[counter++] = korx;
                verticesPlain[counter++] = npos[0];
                verticesPlain[counter++] = kory;
                ++counter3;
                verticesPlain[counter++] = korx + (persistence*2);
                verticesPlain[counter++] = npos[1];
                verticesPlain[counter++] = kory;
                ++counter3;
                verticesPlain[counter++] = korx + (persistence*2);
                verticesPlain[counter++] = npos[3];
                verticesPlain[counter++] = kory + (persistence*2);
                ++counter3;

                indiciesPlain[counter2++] = counter3 - 3;
                indiciesPlain[counter2++] = counter3 - 2;
                indiciesPlain[counter2++] = counter3 - 1;
                indiciesPlain[counter2++] = counter3 - 3;
                indiciesPlain[counter2++] = counter3 - 1;
                indiciesPlain[counter2++] = counter3;
                mapdata.add(npos);
                korx+=(persistence*2);
            }
            kory+=(persistence*2);

        }
        return new Flex(gm, verticesPlain, indiciesPlain);
    }
}
