/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package klassen.gui;

/**
 *
 * @author larry
 */
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.GLBuffers;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.FloatBuffer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import klassen.Textur;
import main.LarryEngineKern;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GUIFont {

    protected float offset, basicSize;
    protected HashMap<String, int[]> hmsif;
    protected HashMap<Integer, GUICharTMP> hmiwg;
    protected float size;
    protected int realSize;
    protected float wantedSize;
    protected int spacewidth;
    protected String fontName;
    protected Textur fontTextur;
    protected Document fontXML;

    private GUIFont(String fontName) {
        this.hmiwg = new HashMap<>();
        this.basicSize = 45;
        this.wantedSize = 55;
        this.realSize = 26;
        this.offset = basicSize - wantedSize;
        this.size = (basicSize * wantedSize) / realSize;
        this.fontName = fontName;
        //offsetPos -= (offsetPos-size);

    }

    private GUIFont(String fontName, Textur tex) {
        this(fontName);
        this.fontTextur = tex;
    }

    public GUIFont(GL4 gl, String fontName, Textur tex, Document fontXML) {
        this(fontName, tex);
        this.fontXML = fontXML;
        this.createFont(gl);
    }

    private void createFont(GL4 gl) {
        NodeList nList = this.fontXML.getElementsByTagName("chardata");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                try {
                    // char="33" A="2" C="6" wid="24" hgt="50" X1="0,080078125" Y1="0" X2="0,126953125" Y2="0,048828125"
                    Element eElement = (Element) nNode;
                    int cInt = Integer.parseInt(eElement.getAttribute("char"));
                    NumberFormat format = NumberFormat.getInstance(Locale.GERMAN);
                    String cX1 = eElement.getAttribute("X1");
                    Number number = format.parse(cX1);
                    float x1 = number.floatValue();
                    String cX2 = eElement.getAttribute("X2");
                    number = format.parse(cX2);
                    float x2 = number.floatValue();
                    String cY1 = eElement.getAttribute("Y1");
                    number = format.parse(cY1);
                    float y1 = 1.0f - number.floatValue();
                    String cY2 = eElement.getAttribute("Y2");
                    number = format.parse(cY2);
                    float y2 = 1.0f - number.floatValue();
                    float[] uvsPlain = new float[]{
                        x1, y1,
                        x1, y2,
                        x2, y2,
                        x2, y1};
                    FloatBuffer uvsBuffer = GLBuffers.newDirectFloatBuffer(uvsPlain.length);
                    uvsBuffer.put(uvsPlain);
                    uvsBuffer.rewind();
                    int[] vboHandles = new int[1];
                    gl.glGenBuffers(1, vboHandles, 0);
                    int uvs = vboHandles[0];
                    LarryEngineKern.createBuffer(gl, uvs, uvsBuffer);
                    GUICharTMP gct = new GUICharTMP(cInt, uvs);
                    hmiwg.put(cInt, gct);
                } catch (ParseException ex) {
                    Logger.getLogger(GUIFont.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        /*    Scanner sc = null;
        try {
            sc = new Scanner(new File("src/Resourcen/Font/" + fontname));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (sc != null && sc.hasNext()) {
            String line = sc.nextLine();
            String[] split = line.split("\"");
            if (line.startsWith("<font")) {
                for (int i = 0; i < split.length; ++i) {
                    if (i % 2 == 1) {
                        if (i == 3) {
                            spacewidth = new Integer(split[i]);
                        }
                    }
                }
            } else if (line.startsWith("<chardata")) {
                GUIChar gc = new GUIChar();
                float[] f = new float[]{0, 0, 0, 0};
                Integer charnumber = 0;
                int c = 1;
                for (int i = 0; i < split.length; ++i) {
                    if (i % 2 == 1) {
                        split[i] = split[i].replaceAll(",", ".");
                        if (c == 1) {
                            charnumber = new Integer(split[i]);
                        } else if (c == 2) {
                            gc.setAdvanced(Integer.parseInt(split[i]));
                        } else if (c == 3) {
                            gc.setVor(Integer.parseInt(split[i]));
                        } else if (c == 4) {
                            gc.setWeite(Integer.parseInt(split[i]));
                        } else if (c == 5) {
                            gc.setHohe(Integer.parseInt(split[i]));
                        } else if (c == 6) {
                            float tf = 0.0f;
                            try {
                                tf = Float.parseFloat(split[i]);
                            } catch (NumberFormatException e) {
                                tf = Integer.parseInt(split[i]);
                            }
                            f[0] = tf;
                        } else if (c == 7) {
                            float tf = 0.0f;
                            try {
                                tf = Float.parseFloat(split[i]);
                            } catch (NumberFormatException e) {
                                tf = Integer.parseInt(split[i]);
                            }
                            f[1] = tf;
                        } else if (c == 8) {
                            float tf = 0.0f;
                            try {
                                tf = Float.parseFloat(split[i]);
                            } catch (NumberFormatException e) {
                                tf = Integer.parseInt(split[i]);
                            }
                            f[2] = tf;
                        } else if (c == 9) {
                            float tf = 0.0f;
                            try {
                                tf = Float.parseFloat(split[i]);
                            } catch (NumberFormatException e) {
                                tf = Integer.parseInt(split[i]);
                            }
                            f[3] = tf;
                            //System.out.println("v: "+f[0]+" "+f[1]+" "+f[2]+" "+f[3]);
                            gc.setTexturKoordinatene(f);
                        }
                        c++;
                    }
                }
                hmiwg.put(charnumber, gc);
            }
        }*/
    }

    public float getSize() {
        return this.size;
    }

    public GUICharTMP getChar(char c) {
        return this.hmiwg.get(((int) c));
    }

    public GUICharTMP getChar(int c) {
        return this.hmiwg.get(c);
    }

    public Textur getTextur() {
        return this.fontTextur;
    }

    public int getSpaceWidth() {
        return this.spacewidth;
    }

    public float getOffset() {
        return this.offset;
    }

    public float getBasicSize() {
        return this.basicSize;
    }
}
