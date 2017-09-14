package klassen;

import com.jogamp.opengl.GL4;
import grundklassen.Geometrie;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author larry
 */
public class GeometrieManager {

    private ConcurrentHashMap<String, Integer> arM;
    private ConcurrentHashMap<Integer, Geometrie> arG;
    private ConcurrentLinkedQueue<Integer> clqT;
    private GL4 gl;
    private static int aid;

    public Geometrie holGeometrie(int geomID){
            return arG.get(geomID);
    }
    
    public Geometrie holGeometrie(String dateiName){
        Integer xx = arM.get(dateiName);
        System.out.println("XX: "+xx);
        return this.holGeometrie(xx);
    }
       
    public GeometrieManager(GL4 gl) {
        this.arG = new ConcurrentHashMap<>();
        this.arM = new ConcurrentHashMap<>();
        this.clqT = new ConcurrentLinkedQueue<>();
        this.gl = gl;
        this.aid = 0;
    }

    public synchronized int ladeGeometrie(String dateipfad) {
        int caid = ++aid;
        if (!arM.containsKey(dateipfad)) {
            Geometrie geom = new Geometrie(this);
            geom.setzDateipfad(dateipfad);
            geom.setzGeometrieID(caid);
            Thread thread = new Thread(geom);
            thread.start();
            arG.put(caid, geom);
        }
        return caid;
    }

    public GL4 holGL() {
        return this.gl;
    }

    public synchronized void legeGeometrieAn() {
        if (!clqT.isEmpty()) {
            int geomID = clqT.poll();
            Geometrie g = arG.get(geomID);
            System.out.println("Geometrie geholt");
            if (g != null && g.istGeladen()) {
                System.out.println("Geometrie Daten geladen");
                arM.put(g.holDateipfad(), geomID);
                System.out.println("Geometrie gesetzt "+geomID+": "+g.holDateipfad());
            }
        }
    }

    public synchronized void speichereMesh(Integer geomID) {
        clqT.add(geomID);
        legeGeometrieAn();
        System.out.println("Geometrie angefügt");
    }
}