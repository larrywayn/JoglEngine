package klassen;

import com.jogamp.opengl.awt.GLJPanel;

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
        Quaternion q = new Quaternion();
        Quaternion q2 = new Quaternion();
        Quaternion c = new Quaternion(0.0f,0.0f,0.0f,1.0f);//this.kamera.holAusrichtung();
        Quaternion p = this.spieler.holAusrichtung();
       //System.out.println("------------");
        Vektor4 s = p.holSeitwerts(1f);
        Vektor4 v = p.holVorwaerts(1f);
        q.rotationAchseV(s, this.rotY);
        q2.rotationAchseV(s.kreuzProduktV4(v),  this.rotX*90.0f);
       // c = c.multipliziereQ(q2);
        c = c.multipliziereQ(q);
        this.spieler.setzAusrichtung(c);
     Vektor4 tmp = this.spieler.holStandort().kopiere();
     tmp.setzZ(tmp.holZ()+4.0f);
        this.kamera.setzStandort(tmp);
     //   this.spieler.holStandort().ausgabe();
     //   this.kamera.holStandort().ausgabe();
      //  System.out.println("------------");
    }

    protected int[] relativeMousePosition(int mouseXScreen, int mouseYScreen) {
        return new int[]{mouseXScreen - (int) (fenster.getLocationOnScreen().getX() + (fenster.getWidth() / 2.0)), mouseYScreen - (int) (fenster.getLocationOnScreen().getY() + (fenster.getHeight() / 2.0))};
    }

    protected int clamp(int val) {
        return Math.max(-1, Math.min(1, val));
    }
    protected float clamp(float val) {
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
        int xRichtung = e.getXOnScreen();
        int yRichtung = e.getYOnScreen();
        //  if (event.button === 2) {
        float newX = relativeMousePositions[0];
        float newY = relativeMousePositions[1];
        float deltaX = newX - this.lastX;
        float deltaY = newY - this.lastY;
        this.lastX = newX;
        this.lastY = newY;
        this.rotX += deltaX;
        this.rotY += deltaY;
       /* System.out.println("------------");
        System.out.println("------------");
        System.out.println("A: "+xRichtung+" # "+yRichtung);
        System.out.println("L: "+this.lastX+" # "+this.lastY);
        System.out.println("D: "+deltaX+" # "+deltaY);
        System.out.println("R: "+ this.rotX+" # "+this.rotY);
       */
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
