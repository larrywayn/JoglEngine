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
    private final TreeMap<String, Double> welt;
    private final int width;

    public MapManager(GL4 gl, FrameRenderBufferManager fbm) {
        this.gl = gl;
        this.aid = 0;
        this.fbm = fbm;
        this.width = 1000;
        this.welt = new TreeMap<>();
        this.tmpTextur = fbm.erzeugeAbedoTextur(this.width, this.width);
    }

    private float zufallsZahl(int min, int max) {
        return zufallsGenerator.nextInt((max - min) + 1) + min;
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

    private double ermittelWasserLand(int posx, int posy, ArrayList<ArrayList<Double>> map, double wasserNebenWasser, double landNebenLand, double land) {
        //  ArrayList<Double> mapdata = map.get(posx);
        double wl = zufallsZahl(0, 1);
        double ergebnis = (wl < land) ? 1 : -1;
        //   mapdata.set(posy, ergebnis);
        return ergebnis;
    }

    public Flex createMap(GeometrieManager gm, String seed) {
        int seednumber = MapManager.stringToSeed(seed);
        this.zufallsGenerator = new Random(seednumber);
        System.out.println("Seed " + seed + " Seednumber " + seednumber);
        double wasserNebenWasser = 0.7;
        double landNebenLand = 0.9;
        double land = 0.8;
        double persistence = 10;
        double wasserHoehe = 0.3;

        float[] verticesPlain = new float[(this.width * this.width * 3 * 4) + 1];
        int[] indiciesPlain = new int[(this.width * this.width * 3 * 2) + 1];

        int korx = (this.width / 2);
        int kory = (this.width / 2);
        int counter = 0;
        int counter2 = 0;
        int counter3 = -1;
        ArrayList<ArrayList<Double>> map = new ArrayList<>();
        for (int x = 0; x < this.width; ++x) {
            ArrayList<Double> mapdata = new ArrayList<>();
            kory = (this.width / 2);
            for (int y = 0; y < this.width; ++y) {
                float npos = (float) ermittelWasserLand(x, y, map, wasserNebenWasser, landNebenLand, land)-1.8f;
                verticesPlain[counter++] = korx;
                verticesPlain[counter++] = npos;
                verticesPlain[counter++] = kory - 1;
                ++counter3;
                verticesPlain[counter++] = korx;
                verticesPlain[counter++] = npos;
                verticesPlain[counter++] = kory;
                ++counter3;
                verticesPlain[counter++] = korx - 1;
                verticesPlain[counter++] = npos;
                verticesPlain[counter++] = kory;
                ++counter3;
                verticesPlain[counter++] = korx - 1;
                verticesPlain[counter++] = npos;
                verticesPlain[counter++] = kory - 1;
                ++counter3;

                indiciesPlain[counter2++] = counter3 - 3;
                indiciesPlain[counter2++] = counter3 - 2;
                indiciesPlain[counter2++] = counter3 - 1;
                indiciesPlain[counter2++] = counter3 - 3;
                indiciesPlain[counter2++] = counter3 - 1;
                indiciesPlain[counter2++] = counter3;

                --kory;
            }
            --korx;
            map.add(mapdata);
        }
        return new Flex(gm, verticesPlain, indiciesPlain);
    }
}
