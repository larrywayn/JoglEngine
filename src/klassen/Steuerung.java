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
    protected float rotX, rotX2;
    protected float rotY;
    protected float lastX;
    protected float lastY;
    protected float mouseFaktorX;
    protected float mouseFaktorY;
    private boolean bewegeRechts;
    private boolean bewegeLinks;
    private boolean bewegeRueckwaerts;
    private boolean bewegeVorwaerts;

    public Steuerung(GLJPanel fenster, Kamera kamera, Spieler spieler) {
        this.kamera = kamera;
        this.spieler = spieler;
        this.fenster = fenster;
        this.rotX = 0;
        this.rotY = 0;
        this.lastX = 0;
        this.lastY = 0;
        this.mouseFaktorX = 5.0f;
        this.mouseFaktorY = 5.0f;
        //     Quaternion spielerQ = this.spieler.holAusrichtung();
        //    Quaternion kameraQ = this.kamera.holAusrichtung();
        //   Quaternion kameraNeuQQ2 = spielerQ.kopiere();
        //    kameraNeuQQ2.konjugante();
        //    Vektor4 xAchseKamera = spielerQ.holSeitwerts();
        //   // xAchseKamera = kameraNeuQQ2.multipliziereV(xAchseKamera);

        //    Quaternion kameraRotXQQ = Quaternion.createFromAxisAngle(xAchseKamera, -10.0f);
        //    kameraRotXQQ.normalisiere();
        //    Quaternion kameraNeuQ = kameraQ.multipliziereQ(kameraRotXQQ);
        //    //   Quaternion kameraRotXQQ = Quaternion.createFromAxisAngle(xAchseKamera, -this.rotY);
        //    // kameraRotXQQ.normalisiere();
        //    // kameraNeuQ = kameraNeuQ.multipliziereQ(kameraRotXQQ);
        //    kameraNeuQ.normalisiere();
        //    this.kamera.setzAusrichtung(kameraNeuQ);
    }

    public void aktualisiereKamera() {
        if (this.bewegeVorwaerts) {
            this.spieler.bewegeVorwaerts();
        }
        if (this.bewegeRueckwaerts) {
            this.spieler.bewegeRueckwaerts();
        }
        if (this.bewegeLinks) {
            this.spieler.bewegeLinks();
        }
        if (this.bewegeRechts) {
            this.spieler.bewegeRechts();
        }
        this.rotY = ((this.rotY > 18) ? 18 : ((this.rotY < -28) ? -28 : this.rotY));
        Quaternion spielerQ = this.spieler.holAusrichtung().kopiere();
        Vektor4 spielerV = (this.spieler.holStandort().kopiere());
        Vektor4 zAchseSpieler = spielerQ.holVertikal();
        Quaternion spielerRotXQ = Quaternion.createFromAxisAngle(zAchseSpieler, this.rotX);
        Quaternion spielerNeuQ = spielerRotXQ.multipliziereQ(spielerQ);
        Quaternion spielerQI = spielerNeuQ.kopiere();
        spielerQI.invertiere();
        this.spieler.setzAusrichtung(spielerNeuQ);
        //Quaternion.slerp( spielerQI,this.kamera.holAusrichtung(), 0.02) 
        Vektor4 xAchseSpieler = spielerQI.holSeitwerts();
        Quaternion spielerRotYQ = Quaternion.createFromAxisAngle(xAchseSpieler, this.rotY);
        Quaternion spielerQIQ = spielerRotYQ.multipliziereQ(spielerQI);
        this.kamera.setzAusrichtung(spielerQIQ);
        Quaternion spielerQIQI = spielerQIQ.kopiere();
        spielerQIQI.invertiere();
        spielerV = spielerQIQI.multipliziereV(spielerV);
        Vektor4 vor = new Vektor4(0f, 4f, 8f, 1);
        spielerV = spielerV.addiereV4(vor);
        this.kamera.setzStandort(spielerV);
        this.bewegeVorwaerts = false;
        this.bewegeRueckwaerts = false;
        this.bewegeLinks = false;
        this.bewegeRechts = false;
        this.rotX = 0;
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
        switch (e.getKeyChar()) {
            case 'w':
                System.out.println("do w");
                this.bewegeVorwaerts = true;
                break;
            case 's':
                System.out.println("do s");
                this.bewegeRueckwaerts = true;
                break;
            case 'a':
                System.out.println("do a");
                this.bewegeLinks = true;
                break;
            case 'd':
                System.out.println("do d");
                this.bewegeRechts = true;
                break;
            default:
                break;
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
        int[] relativeMousePositions = this.relativeMousePosition(e.getXOnScreen(), e.getYOnScreen());
        this.lastX = relativeMousePositions[0] / this.mouseFaktorX;
        this.lastY = relativeMousePositions[1] / this.mouseFaktorY;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.lastX = 0;
        this.lastY = 0;
        //  this.rotX = 0;
        //   this.rotY = 0;
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
        // int xRichtung = e.getXOnScreen();
        //  int yRichtung = e.getYOnScreen();
        //  if (event.button === 2) {
        float newX = relativeMousePositions[0] / this.mouseFaktorX;
        float newY = relativeMousePositions[1] / this.mouseFaktorY;
        float deltaX = newX - this.lastX;
        float deltaY = newY - this.lastY;
        this.lastX = newX;
        this.lastY = newY;
        this.rotX += deltaX;
        this.rotY += deltaY;

        //   System.out.println("------------");
        //    System.out.println("------------");
        //   System.out.println("X:" + relativeMousePositions[0] + " y:" + relativeMousePositions[1]);
// System.out.println("A: "+xRichtung+" # "+yRichtung);
        // System.out.println("L: "+this.lastX+" # "+this.lastY);
        //System.out.println("D: "+deltaX+" # "+deltaY);
        //   System.out.println("R: " + this.rotX + " # " + this.rotY);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
