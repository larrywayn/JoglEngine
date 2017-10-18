/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package klassen;

/**
 *
 * @author larry
 */
import static com.jogamp.opengl.GL4.*;
import java.io.File;
import java.util.HashMap;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.texture.Texture;
import grundklassen.Geometrie;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import klassen.geometrie.Flex;
import klassen.gui.GUICharTMP;
import klassen.gui.GUIFont;
import klassen.gui.GUILabel;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class GUIManager {

    private TexturManager tm;
    private ObjectManager om;
    private String guiOverlay;
    private HashMap<String, GUIFont> fonts;
    private HashMap<String, GUILabel> guiElements;
    private Flex buchstabenQuad;

    public void loadFont(String fontName) {
        try {
            Textur tex = this.tm.ladeTextur(fontName + ".tga", true);
            File fXmlFile = new File(Configuration.holFontTexturPfad() + fontName + ".xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document xmlDoc = dBuilder.parse(fXmlFile);
            xmlDoc.getDocumentElement().normalize();
            GUIFont font = new GUIFont(this.om.holGL(), fontName, tex, xmlDoc);
            fonts.put(fontName, font);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(GUIManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void erzeugeLabel(String labelName, String labelText, int posX, int posY, String gFontName, float fontSize) {
        GUILabel lbl = new GUILabel(labelText, new Quaternion(), posX, posY, this.fonts.get(gFontName), fontSize);
        this.guiElements.put(labelName, lbl);
        /*
               Textur texB = tm.ladeTextur("backchat.png");//
        
              float[] uvsPlain;        float x1 = 0.080078125f;
        float x2 = 0.126953125f;
        float y1 = 1f - 0.0f;
        float y2 = 1f - 0.048828125f;
        //X1="0,080078125" Y1="0" X2="0,126953125" Y2="0,048828125"
        uvsPlain = new float[]{
            x1, y1,
            x1, y2,
            x2, y2,
            x2, y1};
        
        Quaternion g2 = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
        g2.setzSkalierung(20f, 30f, 1f);
        Quaternion g3 = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
        g3.setzSkalierung(70f, 70f, 1f);
        Objekt3D o;
        Geometrie mesh;
        o = this.om.erzeugeObject(QUAD, "overlay", g3, new Vektor4(75, 75, 0.01f, 1.0f), this.sbO);
        mesh = o.holMesh();
        mesh.setzTextur(texB);
        for (int i = 99; i >= 0; i = i - 100) {
            for (int j = 99; j >= 0; j = j - 100) {
                // Quaternion qq = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
                //qq.setzSkalierung(0.2f, 0.2f, 0.02f);
                o = this.om.erzeugeObject(f2, "overlay", g2, new Vektor4(25, 35, 0.05f, 1.0f), this.sbO);
                mesh = o.holMesh();
                mesh.setzTextur(tex);
                // this.om.erzeugeObject(DICE, "overlay", new Quaternion(0.0f, 0.0f, 0.0f, 1.0f), new Vektor4(1000f, 380f, 0f, 1.0f), this.sbO);
                // this.om.erzeugeObject(DICE, "overlay", new Quaternion(0.0f, 0.0f, 0.0f, 1.0f), new Vektor4(1000f, 0f, 0f, 1.0f), this.sbO);
                // this.om.erzeugeObject(DICE, "overlay", new Quaternion(0.0f, 0.0f, 0.0f, 1.0f), new Vektor4(0f, 380f, 0f, 1.0f), this.sbO);
                // this.om.erzeugeObject(DICE, "overlay", new Quaternion(0.0f, 0.0f, 0.0f, 1.0f), new Vektor4(500f, 200f, 0f, 1.0f), this.sbO);
            }
        }
         */
    }

    public GUIManager(GL4 gl, TexturManager tm, ObjectManager om, Flex buchstabenQuad, String overlay) {
        this.om = om;
        this.guiOverlay = overlay;
        this.tm = tm;
        this.fonts = new HashMap<>();
        this.guiElements = new HashMap<>();
        this.buchstabenQuad = buchstabenQuad;
    }

    public void drawText(GL4 gl, Framebuffer scene3D, ShaderBlock guiShader, int width, int height) {
        gl.glBindFramebuffer(GL_FRAMEBUFFER, scene3D.holFramebufferID());
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0f);
        gl.glViewport(0, 0, width, height);
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        gl.glEnable(GL_BLEND);
        gl.glDrawBuffer(GL_COLOR_ATTACHMENT0);
        gl.glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_SRC_ALPHA, GL_ONE);
        Geometrie mesh = buchstabenQuad;
        if (mesh != null) {
            int program = guiShader.holProgram();
            gl.glUseProgram(program);
            if (mesh.hatVertices()) {
                gl.glEnableVertexAttribArray(0);
                gl.glBindBuffer(GL_ARRAY_BUFFER, mesh.holVertices());
                gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
            }
            if (mesh.hatNormalen()) {
                gl.glEnableVertexAttribArray(1);
                gl.glBindBuffer(GL_ARRAY_BUFFER, mesh.holNormalen());
                gl.glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
            }
            gl.glUniform2f((gl.glGetUniformLocation(program, "posS")), width, height);
            Map mp = this.guiElements;
            if (mp != null) {
                Iterator it = mp.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    GUILabel oo = (GUILabel) pair.getValue();
                    if (oo != null) {
                        GUIFont gFont = oo.holFont();
                        ArrayList<GUICharTMP> chars = oo.holDaten();
                        Textur tObj = gFont.getTextur();
                        Texture t = null; 
                        Vektor4 nPos =oo.holPosition().kopiere();
                        for (GUICharTMP gc : chars) {
                            if (gc != null) {
                                nPos.setzX(nPos.holX()+oo.holFontSize()); 
                                nPos.setzZ(nPos.holZ()-0.1f);
                                float[] matrix = oo.holAusrichtung().erzeugeMatrix(nPos);
                                if (tObj != null) {
                                    t = tObj.holGLTexture();
                                    if (t != null) {
                                        gl.glEnable(GL_TEXTURE_2D);
                                        gl.glActiveTexture(GL_TEXTURE0);
                                        t.enable(gl);
                                        t.bind(gl);
                                        gl.glUniform1i((gl.glGetUniformLocation(program, "texture0")), 0);
                                        gl.glUniform1f((gl.glGetUniformLocation(program, "isTex")), 1.0f);
                                        gl.glEnableVertexAttribArray(2);
                                        gl.glBindBuffer(GL_ARRAY_BUFFER, gc.holUVBuffer());
                                        gl.glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
                                        gl.glBindTexture(GL_TEXTURE_2D, t.getTextureObject());
                                    }
                                }
                                if (tObj == null || t == null) {
                                    gl.glUniform1f((gl.glGetUniformLocation(program, "isTex")), 0.0f);
                                }
                                gl.glUniformMatrix4fv((gl.glGetUniformLocation(program, "modelMat")), 1, false, matrix, 0);
                                if (mesh.hatIndizies()) {
                                    gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mesh.holIndizies());
                                    gl.glDrawElements(GL_TRIANGLES, mesh.holIndiziesPlain().length, GL_UNSIGNED_INT, 0);
                                }
                            }else{ 
                                nPos.setzX(nPos.holX()+oo.holFontSize());
                            }
                        }
                        if (t != null && tObj != null) {
                            t.disable(gl);
                            gl.glDisable(GL_TEXTURE_2D);
                            gl.glDisableVertexAttribArray(2);
                        }
                    }
                }
            }
            gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
            gl.glBindBuffer(GL_ARRAY_BUFFER, 0);
            gl.glDisableVertexAttribArray(0);
            if (mesh.hatNormalen()) {
                gl.glDisableVertexAttribArray(1);
            }
            gl.glUseProgram(0);
        }
        gl.glDisable(GL_BLEND);
        gl.glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void anpassenLabel(String lblBezeichnung, String neuerText) {
       GUILabel gl = this.guiElements.get(lblBezeichnung);
       gl.anpassenText(neuerText);
    }
}
