package klassen;

import com.jogamp.opengl.awt.GLJPanel;
import main.LarryEngineKern;

import java.awt.event.*;

/**
 * Created by larry on 20.03.2016.
 */
public class Steuerung implements KeyListener, MouseListener, MouseMotionListener {
    protected Kamera kamera;
    protected Spieler spieler;
    protected GLJPanel fenster;
    protected float rotX;
    protected float rotY;
    protected float lastX;
    protected float lastY;

    public Steuerung(GLJPanel fenster, Kamera kamera, Spieler spieler) {
        this.kamera = kamera;
        this.spieler = spieler;
        this.fenster = fenster;
        this.rotX = 0;
        this.rotY = 0;
        this.lastX = 0;
        this.lastY = 0;
    }

    public void aktualisiereKamera() {

    }

    protected int[] relativeMousePosition(int mouseXScreen, int mouseYScreen) {
        return new int[]{mouseXScreen - (int) (fenster.getLocationOnScreen().getX() + (fenster.getWidth() / 2.0)), mouseYScreen - (int) (fenster.getLocationOnScreen().getY() + (fenster.getHeight() / 2.0))};
    }

    protected int clamp(int val) {
        return Math.max(-1, Math.min(1, val));
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'w') {
            System.out.println("do w");
            this.spieler.bewegeVorwaerts();
        } else if (e.getKeyChar() == 's') {
            System.out.println("do s");
            this.spieler.bewegeRueckwaerts();
        } else if (e.getKeyChar() == 'a') {
            System.out.println("do a");
            this.spieler.bewegeLinks();
        } else if (e.getKeyChar() == 'd') {
            System.out.println("do d");
            this.spieler.bewegeRechts();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int[] relativeMousePositions = this.relativeMousePosition(e.getXOnScreen(), e.getYOnScreen());
        //  if (event.button === 2) {
        float newX = relativeMousePositions[0];
        float newY = relativeMousePositions[1];
        float deltaX = newX - this.lastX;
        float deltaY = newY - this.lastY;
        this.lastX = newX;
        this.lastY = newY;
        this.rotX += deltaX;
        this.rotY += deltaY;
        //  }


        int xRichtung = this.clamp(relativeMousePositions[0]);
        int yRichtung = this.clamp(relativeMousePositions[1]);
        Quaternion q = new Quaternion();
        Quaternion q2 = new Quaternion();
        Quaternion c = this.kamera.holAusrichtung();
        System.out.println("------------");
        Vektor4 s = c.holSeitwerts(1);
        Vektor4 v = c.holVorwaerts(1);
     //   s.ausgabe();
      //  v.ausgabe();
        q.rotationAchseV(s, this.rotX);
        q2.rotationAchseV(s.kreuzProduktV4(v),  this.rotY);
        q.ausgabe();
        q2.ausgabe();
        c = c.multipliziereQ(q);
        c = c.multipliziereQ(q2);
        this.kamera.setzAusrichtung(c);
        c.ausgabe();
        //    System.out.println(relativeMousePositions[0]+" # "+relativeMousePositions[1]+" ## "+xRichtung+" # "+yRichtung);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
