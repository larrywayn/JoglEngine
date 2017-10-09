/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package klassen;

import com.jogamp.opengl.GL4;
import grundklassen.DrawObjekt;
import grundklassen.Geometrie;
import grundklassen.GeometrieTypen;
import static grundklassen.GeometrieTypen.CONE;
import static grundklassen.GeometrieTypen.QUAD;
import java.util.concurrent.ConcurrentHashMap;
import klassen.geometrie.Quad;

/**
 *
 * @author larry
 */
public class ObjectManager {

    private ConcurrentHashMap<String, ConcurrentHashMap> drawlist;
    private GeometrieManager gm;
    private final Quad framebufferQuad;
    private GL4 gl;
    private static int aid;
    private ShaderBlock standardShader;

    public ObjectManager(GL4 gl, GeometrieManager gm, ShaderBlock standard) {
        this.gl = gl;
        this.gm = gm;
        this.aid = 0;
        this.standardShader = standard;
        this.drawlist = new ConcurrentHashMap<>();
        this.framebufferQuad = (Quad) gm.erzeuge(QUAD);
    }

    public Quad holFramebufferQuad() {
        return this.framebufferQuad;
    }

    public void erzeugeObjectListe(String listname) {
        if (!drawlist.containsKey(listname)) {
            ConcurrentHashMap<String, DrawObjekt> objektlist = new ConcurrentHashMap<>();
            drawlist.put(listname, objektlist);
            System.out.println("OM: List created " + listname);
        }
    }

    public ConcurrentHashMap holObjectListe(String listname) {
        if (drawlist.containsKey(listname)) {
            return drawlist.get(listname);
        }
        return null;
    }

    public DrawObjekt holObject(String objectName, String listname) {
        if (drawlist.containsKey(listname)) {
            ConcurrentHashMap<String, DrawObjekt> objektlist = drawlist.get(listname);
            if (!objektlist.containsKey(objectName)) {
                return objektlist.get(objectName);
            }
        }
        return null;
    }

    public GL4 holGL() {
        return this.gl;
    }

    public void hinzufuegenObject(DrawObjekt objekt, String listname) {
        if (drawlist.containsKey(listname)) {
            ConcurrentHashMap<String, DrawObjekt> objektlist = this.holObjectListe(listname);
            objektlist.put(("Object" + (++ObjectManager.aid)), objekt);
        }
    }

    public void erzeugeObject(GeometrieTypen geometrieTypen, String listname) {
        Quaternion q = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
        Vektor4 v = new Vektor4(0f, 0f, 0f, 1.0f);
        this.erzeugeObject(CONE, listname, q, v, this.standardShader);
    }

    public void erzeugeObject(GeometrieTypen geometrieTyp, String listname, Quaternion quaternion, Vektor4 vektor4, ShaderBlock shaderBlock) {
        if (drawlist.containsKey(listname)) {
            ConcurrentHashMap<String, DrawObjekt> objektlist = this.holObjectListe(listname);
            Objekt3D tmpO = new Objekt3D();
            tmpO.setzShader(shaderBlock);
            tmpO.setzMesh(gm.erzeuge(geometrieTyp));
            tmpO.setzStandort(vektor4);
            tmpO.setzAusrichtung(quaternion);
            objektlist.put(("Object" + (++ObjectManager.aid)), tmpO);
        }
    }

    public void erzeugeObject(Geometrie f, String listname, Quaternion quaternion, Vektor4 vektor4, ShaderBlock shaderBlock) {
         if (drawlist.containsKey(listname)) {
            ConcurrentHashMap<String, DrawObjekt> objektlist = this.holObjectListe(listname);
            Objekt3D tmpO = new Objekt3D();
            tmpO.setzShader(shaderBlock);
            tmpO.setzMesh(f);
            tmpO.setzStandort(vektor4);
            tmpO.setzAusrichtung(quaternion);
            objektlist.put(("Object" + (++ObjectManager.aid)), tmpO);
        }
    }
}
