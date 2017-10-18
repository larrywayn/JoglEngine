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
import com.jogamp.opengl.GL4;
import java.awt.Dimension;
abstract public class GUIElement {

    public GUIElement() {
        this.hoeheneu = 50;
        this.border = false;
        this.visible = false;
        this.borderbackground = 0;
        this.background = 0;
    }

    protected Dimension d;
    protected boolean visible;
    protected float[] p;
    protected float[] fc;
    protected float[] ec;
    protected int background;
    protected int borderbackground;
    protected boolean border;
    protected float[] bc;
    protected int borderw;
    protected GUIFont gf;
    protected String text;
    protected int hoeheneu;

    public void setSize(Dimension d) {
        this.d = d;
    }

    public void setFontColor(float[] fc) {
        this.fc = fc;
    }

    public void setElementColor(float[] ec) {
        this.ec = ec;
    }

    public void setBorderColor(float[] bc) {
        this.bc = bc;
    }

    public Dimension getSize() {
        return d;
    }

    public void setBorder(boolean border) {
        this.border = border;
    }

    public void setBorderWidth(int borderw) {
        this.borderw = borderw;
    }

    public void setBackgroundImage(int bi) {
        this.background = bi;
    }

    public void setBorderBackgroundImage(int bbi) {
        this.borderbackground = bbi;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setFont(GUIFont gf) {
        this.gf = gf;
    }

    public void setPosition(float[] p) {
        this.p = p;
    }

    public float[] getPosition() {
        return p;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void resizeFont(int factor) {
        if (factor > 0 && factor < 100) {
            hoeheneu = factor;
        }
    }

    private void paintText(GL4 gl) {
        /*  gl.glColor3f(1f, 1f, 1f);
        gl.glPushMatrix();
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        if (text != null && !text.isEmpty() && fc != null && gf != null) {
            gl.glActiveTexture(GL_TEXTURE0);
            gl.glEnable(GL_TEXTURE_2D);
            gl.glBindTexture(GL_TEXTURE_2D, gf.texturName);
            int posx = (int) (p[0]);
            int posy = (int) ((p[1] + d.getHeight()));

            for (int i = 0; i < text.length(); ++i) {
                GUIChar tmpChar = gf.getCharInfo(text.charAt(i));
                int hoeheneu = this.hoeheneu;
                int vorneu = 0;
                if (text.charAt(i) > 0) {
                    try {
                        int weiteneu = (hoeheneu * tmpChar.getWeite() / tmpChar.getHohe());
                        vorneu = (hoeheneu * tmpChar.getVor() / tmpChar.getHohe());
                        posy -= hoeheneu;
                        float[] t = tmpChar.getTexturKoordinatene();

                        gl.glBegin(GL_QUADS);
                        //1
                        if (fc.length == 4) {
                            gl.glColor4f(fc[0], fc[1], fc[2], fc[3]);
                        } else {
                            gl.glColor3f(fc[0], fc[1], fc[2]);
                        }
                        gl.glTexCoord2f(t[2], t[1]);
                        gl.glVertex3f(posx + weiteneu, posy + hoeheneu, 0);
                        //2
                        if (fc.length == 4) {
                            gl.glColor4f(fc[0], fc[1], fc[2], fc[3]);
                        } else {
                            gl.glColor3f(fc[0], fc[1], fc[2]);
                        }
                        gl.glTexCoord2f(t[0], t[1]);
                        gl.glVertex3f(posx + 0, posy + hoeheneu, 0);
                        //3
                        if (fc.length == 4) {
                            gl.glColor4f(fc[0], fc[1], fc[2], fc[3]);
                        } else {
                            gl.glColor3f(fc[0], fc[1], fc[2]);
                        }
                        gl.glTexCoord2f(t[0], t[3]);
                        gl.glVertex3f(posx + 0, posy + 0, 0);
                        //4
                        if (fc.length == 4) {
                            gl.glColor4f(fc[0], fc[1], fc[2], fc[3]);
                        } else {
                            gl.glColor3f(fc[0], fc[1], fc[2]);
                        }
                        gl.glTexCoord2f(t[2], t[3]);
                        gl.glVertex3f(posx + weiteneu, posy + 0, 0);
                        gl.glEnd();
                        posy += hoeheneu;
                    } catch (NullPointerException npe) {
                    }
                }
                float weite = gf.getSpaceWidth();
                float weitenext = gf.getSpaceWidth();
                float hoehe = gf.getSize();
                if (tmpChar != null) {
                    weite = vorneu;
                    hoehe = hoeheneu;
                }

                if (i + 1 < text.length()) {
                    try {
                        GUIChar tmpChar2 = gf.getCharInfo(text.charAt(i + 1));
                        weitenext = (hoeheneu * tmpChar2.getWeite() / tmpChar2.getHohe());
                    } catch (NullPointerException npe) {
                    }
                }
                posx += weite;
                if (posx + weitenext >= this.p[0] + d.getWidth()) {
                    posx = (int) (p[0]);
                    posy -= hoehe;
                    if (posy - hoehe <= p[1]) {
                        break;
                    }
                }
                tmpChar = null;
            }
            gl.glActiveTexture(GL_TEXTURE0);
            gl.glBindTexture(GL_TEXTURE_2D, 0);
            gl.glDisable(GL_TEXTURE_2D);
        }
        gl.glDisable(GL_BLEND);
        gl.glPopMatrix();
        gl.glColor3f(1f, 1f, 1f);
    }

    private void paintBorder(GL4 gl) {
        if (border && bc != null) {
            gl.glColor3f(1f, 1f, 1f);
            gl.glPushMatrix();
            gl.glEnable(GL_BLEND);
            gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            if (borderbackground > 0) {
                gl.glActiveTexture(GL_TEXTURE0);
                gl.glBindTexture(GL_TEXTURE_2D, borderbackground);
                gl.glDisable(GL_TEXTURE_2D);
            }

            gl.glBegin(GL_QUADS);
            if (bc.length == 4) {
                gl.glColor4f(bc[0], bc[1], bc[2], bc[3]);
            } else {
                gl.glColor3f(bc[0], bc[1], bc[2]);
            }
            gl.glVertex3f((float) (p[0]), p[1], 0.2f);
            if (bc.length == 4) {
                gl.glColor4f(bc[0], bc[1], bc[2], bc[3]);
            } else {
                gl.glColor3f(bc[0], bc[1], bc[2]);
            }
            gl.glVertex3f((float) (p[0] - borderw), (float) (p[1]), 0.2f);
            if (bc.length == 4) {
                gl.glColor4f(bc[0], bc[1], bc[2], bc[3]);
            } else {
                gl.glColor3f(bc[0], bc[1], bc[2]);
            }
            gl.glVertex3f(p[0] - borderw, (float) (p[1] - borderw), 0.2f);
            if (bc.length == 4) {
                gl.glColor4f(bc[0], bc[1], bc[2], bc[3]);
            } else {
                gl.glColor3f(bc[0], bc[1], bc[2]);
            }
            gl.glVertex3f(p[0], p[1] - borderw, 0.2f);

            if (bc.length == 4) {
                gl.glColor4f(bc[0], bc[1], bc[2], bc[3]);
            } else {
                gl.glColor3f(bc[0], bc[1], bc[2]);
            }
            gl.glVertex3f(p[0], p[1], 0.2f);
            if (bc.length == 4) {
                gl.glColor4f(bc[0], bc[1], bc[2], bc[3]);
            } else {
                gl.glColor3f(bc[0], bc[1], bc[2]);
            }
            gl.glVertex3f(p[0], (float) (p[1] + d.getHeight()), 0.2f);
            if (bc.length == 4) {
                gl.glColor4f(bc[0], bc[1], bc[2], bc[3]);
            } else {
                gl.glColor3f(bc[0], bc[1], bc[2]);
            }
            gl.glVertex3f((float) (p[0] - borderw), (float) (p[1] + d.getHeight()), 0.2f);
            if (bc.length == 4) {
                gl.glColor4f(bc[0], bc[1], bc[2], bc[3]);
            } else {
                gl.glColor3f(bc[0], bc[1], bc[2]);
            }
            gl.glVertex3f((float) (p[0] - borderw), (float) (p[1]), 0.2f);

            if (bc.length == 4) {
                gl.glColor4f(bc[0], bc[1], bc[2], bc[3]);
            } else {
                gl.glColor3f(bc[0], bc[1], bc[2]);
            }
            gl.glVertex3f(p[0], (float) (p[1] + d.getHeight() + borderw), 0.2f);
            if (bc.length == 4) {
                gl.glColor4f(bc[0], bc[1], bc[2], bc[3]);
            } else {
                gl.glColor3f(bc[0], bc[1], bc[2]);
            }
            gl.glVertex3f(p[0] - borderw, (float) (p[1] + d.getHeight() + borderw), 0.2f);
            if (bc.length == 4) {
                gl.glColor4f(bc[0], bc[1], bc[2], bc[3]);
            } else {
                gl.glColor3f(bc[0], bc[1], bc[2]);
            }
            gl.glVertex3f((float) (p[0] - borderw), (float) (p[1] + d.getHeight()), 0.2f);
            if (bc.length == 4) {
                gl.glColor4f(bc[0], bc[1], bc[2], bc[3]);
            } else {
                gl.glColor3f(bc[0], bc[1], bc[2]);
            }
            gl.glVertex3f((float) (p[0]), (float) (p[1] + d.getHeight()), 0.2f);

            if (bc.length == 4) {
                gl.glColor4f(bc[0], bc[1], bc[2], bc[3]);
            } else {
                gl.glColor3f(bc[0], bc[1], bc[2]);
            }
            gl.glVertex3f(p[0], (float) (p[1] + d.getHeight()), 0.2f);
            if (bc.length == 4) {
                gl.glColor4f(bc[0], bc[1], bc[2], bc[3]);
            } else {
                gl.glColor3f(bc[0], bc[1], bc[2]);
            }
            gl.glVertex3f((float) (p[0] + d.getWidth()), (float) (p[1] + d.getHeight()), 0.2f);
            if (bc.length == 4) {
                gl.glColor4f(bc[0], bc[1], bc[2], bc[3]);
            } else {
                gl.glColor3f(bc[0], bc[1], bc[2]);
            }
            gl.glVertex3f((float) (p[0] + d.getWidth()), (float) (p[1] + d.getHeight() + borderw), 0.2f);
            if (bc.length == 4) {
                gl.glColor4f(bc[0], bc[1], bc[2], bc[3]);
            } else {
                gl.glColor3f(bc[0], bc[1], bc[2]);
            }
            gl.glVertex3f((float) (p[0]), (float) (p[1] + d.getHeight() + borderw), 0.2f);

            if (bc.length == 4) {
                gl.glColor4f(bc[0], bc[1], bc[2], bc[3]);
            } else {
                gl.glColor3f(bc[0], bc[1], bc[2]);
            }
            gl.glVertex3f((float) (p[0] + d.getWidth()), (float) (p[1] + d.getHeight()), 0.2f);
            if (bc.length == 4) {
                gl.glColor4f(bc[0], bc[1], bc[2], bc[3]);
            } else {
                gl.glColor3f(bc[0], bc[1], bc[2]);
            }
            gl.glVertex3f((float) (p[0] + d.getWidth() + borderw), (float) (p[1] + d.getHeight()), 0.2f);
            if (bc.length == 4) {
                gl.glColor4f(bc[0], bc[1], bc[2], bc[3]);
            } else {
                gl.glColor3f(bc[0], bc[1], bc[2]);
            }
            gl.glVertex3f((float) (p[0] + d.getWidth() + borderw), (float) (p[1] + d.getHeight() + borderw), 0.2f);
            if (bc.length == 4) {
                gl.glColor4f(bc[0], bc[1], bc[2], bc[3]);
            } else {
                gl.glColor3f(bc[0], bc[1], bc[2]);
            }
            gl.glVertex3f((float) (p[0] + d.getWidth()), (float) (p[1] + d.getHeight() + borderw), 0.2f);

            if (bc.length == 4) {
                gl.glColor4f(bc[0], bc[1], bc[2], bc[3]);
            } else {
                gl.glColor3f(bc[0], bc[1], bc[2]);
            }
            gl.glVertex3f((float) (p[0] + d.getWidth()), (float) (p[1] + d.getHeight()), 0.2f);
            if (bc.length == 4) {
                gl.glColor4f(bc[0], bc[1], bc[2], bc[3]);
            } else {
                gl.glColor3f(bc[0], bc[1], bc[2]);
            }
            gl.glVertex3f((float) (p[0] + d.getWidth()), (float) (p[1]), 0.2f);
            if (bc.length == 4) {
                gl.glColor4f(bc[0], bc[1], bc[2], bc[3]);
            } else {
                gl.glColor3f(bc[0], bc[1], bc[2]);
            }
            gl.glVertex3f((float) (p[0] + d.getWidth() + borderw), (float) (p[1]), 0.2f);
            if (bc.length == 4) {
                gl.glColor4f(bc[0], bc[1], bc[2], bc[3]);
            } else {
                gl.glColor3f(bc[0], bc[1], bc[2]);
            }
            gl.glVertex3f((float) (p[0] + d.getWidth() + borderw), (float) (p[1] + d.getHeight()), 0.2f);

            if (bc.length == 4) {
                gl.glColor4f(bc[0], bc[1], bc[2], bc[3]);
            } else {
                gl.glColor3f(bc[0], bc[1], bc[2]);
            }
            gl.glVertex3f((float) (p[0]), (float) (p[1]), 0.2f);
            if (bc.length == 4) {
                gl.glColor4f(bc[0], bc[1], bc[2], bc[3]);
            } else {
                gl.glColor3f(bc[0], bc[1], bc[2]);
            }
            gl.glVertex3f((float) (p[0]), (float) (p[1] - borderw), 0.2f);
            if (bc.length == 4) {
                gl.glColor4f(bc[0], bc[1], bc[2], bc[3]);
            } else {
                gl.glColor3f(bc[0], bc[1], bc[2]);
            }
            gl.glVertex3f((float) (p[0] + d.getWidth()), (float) (p[1] - borderw), 0.2f);
            if (bc.length == 4) {
                gl.glColor4f(bc[0], bc[1], bc[2], bc[3]);
            } else {
                gl.glColor3f(bc[0], bc[1], bc[2]);
            }
            gl.glVertex3f((float) (p[0] + d.getWidth()), (float) (p[1]), 0.2f);

            if (bc.length == 4) {
                gl.glColor4f(bc[0], bc[1], bc[2], bc[3]);
            } else {
                gl.glColor3f(bc[0], bc[1], bc[2]);
            }
            gl.glVertex3f((float) (p[0] + d.getWidth()), (float) (p[1]), 0.2f);
            if (bc.length == 4) {
                gl.glColor4f(bc[0], bc[1], bc[2], bc[3]);
            } else {
                gl.glColor3f(bc[0], bc[1], bc[2]);
            }
            gl.glVertex3f((float) (p[0] + d.getWidth()), (float) (p[1] - borderw), 0.2f);
            if (bc.length == 4) {
                gl.glColor4f(bc[0], bc[1], bc[2], bc[3]);
            } else {
                gl.glColor3f(bc[0], bc[1], bc[2]);
            }
            gl.glVertex3f((float) (p[0] + d.getWidth() + borderw), (float) (p[1] - borderw), 0.2f);
            if (bc.length == 4) {
                gl.glColor4f(bc[0], bc[1], bc[2], bc[3]);
            } else {
                gl.glColor3f(bc[0], bc[1], bc[2]);
            }
            gl.glVertex3f((float) (p[0] + d.getWidth() + borderw), (float) (p[1]), 0.2f);
            gl.glEnd();
            gl.glDisable(GL4.GL_BLEND);
            gl.glPopMatrix();
            gl.glColor3f(1f, 1f, 1f);
        }
    }

    public void paintElement(GL4 gl) {
        gl.glColor3f(1f, 1f, 1f);
        gl.glPushMatrix();
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        gl.glBegin(GL_QUADS);
        if (ec.length == 4) {
            gl.glColor4f(ec[0], ec[1], ec[2], ec[3]);
        } else {
            gl.glColor3f(ec[0], ec[1], ec[2]);
        }
        gl.glVertex3f((float) (p[0] + d.getWidth()), p[1], 0.0f);

        if (ec.length == 4) {
            gl.glColor4f(ec[0], ec[1], ec[2], ec[3]);
        } else {
            gl.glColor3f(ec[0], ec[1], ec[2]);
        }
        gl.glVertex3f((float) (p[0] + d.getWidth()), (float) (p[1] + d.getHeight()), 0.0f);

        if (ec.length == 4) {
            gl.glColor4f(ec[0], ec[1], ec[2], ec[3]);
        } else {
            gl.glColor3f(ec[0], ec[1], ec[2]);
        }
        gl.glVertex3f(p[0], (float) (p[1] + d.getHeight()), 0.0f);

        if (ec.length == 4) {
            gl.glColor4f(ec[0], ec[1], ec[2], ec[3]);
        } else {
            gl.glColor3f(ec[0], ec[1], ec[2]);
        }
        gl.glVertex3f(p[0], p[1], 0.0f);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL4.GL_BLEND);
        gl.glColor3f(1f, 1f, 1f);*/
    }

    public void paint(GL4 gl) {
        /*paintBorder(gl);
        paintElement(gl);
        paintText(gl);*/

    }
}
