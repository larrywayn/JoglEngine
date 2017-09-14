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
        this.mouseFaktorX = 3.0f;
        this.mouseFaktorY = 3.0f;
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
        Vektor4 targetPosition = this.spieler.holStandort();
        Vektor4 tmpv = this.spieler.holAusrichtung().holVorwaerts().kopiere();
        tmpv.skaliere(-7.0f);
        Quaternion targetRotation = this.spieler.holAusrichtung().kopiere();

                        System.out.println("-------------");System.out.print("TR: ");
                        targetRotation.ausgabe();
        Quaternion tmpQuaternion = Quaternion.createFromAxisAngle(new Vektor4(0f, 1f, 0f, 0f), this.rotX);
        System.out.print("TMP: ");tmpQuaternion.ausgabe();
        tmpQuaternion.multipliziereQ(targetRotation);
        System.out.print("TR: ");tmpQuaternion.ausgabe();
        //targetRotation.normalisiere();
//THREE.Quaternion.slerp(this.camera.quaternion, targetRotation, qm, 0.07);
        Quaternion qm = Quaternion.slerp(this.kamera.holAusrichtung(),tmpQuaternion,  0.7);
       // qm.normalisiere();      
       System.out.println( this.rotX);System.out.print("QM: ");
       qm.ausgabe();
        this.kamera.setzAusrichtung(qm);
        this.kamera.setzStandort(targetPosition.subtrahiereV4(tmpv));
        /*   Quaternion spielerQ = this.spieler.holAusrichtung();
        Quaternion kameraQ = this.kamera.holAusrichtung();

        Vektor4 zAchseSpieler = spielerQ.holVertikal();
        Quaternion spielerRotXQ = Quaternion.createFromAxisAngle(zAchseSpieler, this.rotX);
        Quaternion spielerNeuQ = spielerRotXQ.multipliziereQ(spielerQ);
        spielerNeuQ.normalisiere();
        zAchseSpieler = spielerNeuQ.holVertikal();
        Vektor4 xAchseKamera = spielerQ.holSeitwerts();
        Quaternion kameraNeuQQ2 = kameraQ.kopiere();
        kameraNeuQQ2.konjugante();
        zAchseSpieler = kameraNeuQQ2.multipliziereV(zAchseSpieler);
        xAchseKamera = kameraNeuQQ2.multipliziereV(xAchseKamera);
        Quaternion kameraRotXQ = Quaternion.createFromAxisAngle(zAchseSpieler, this.rotX);
        kameraRotXQ.normalisiere();
        Quaternion kameraNeuQ = kameraQ.multipliziereQ(kameraRotXQ);
        Quaternion kameraRotXQQ = Quaternion.createFromAxisAngle(xAchseKamera, this.rotY);
        kameraRotXQQ.normalisiere();
        kameraNeuQ = kameraNeuQ.multipliziereQ(kameraRotXQQ);
        kameraNeuQ.normalisiere();
        //         Quaternion targetRotation = this.spieler.holAusrichtung().kopiere();
        //Quaternion tmpQuaternion = Quaternion.createFromAxisAngle(new Vektor4(0f, 1f, 0f, 1f), 180f * (float) (Math.PI / 180f));
        //targetRotation.multipliziereQ(tmpQuaternion);
        //targetRotation.normalisiere();

        //Quaternion qm = Quaternion.slerp(this.kamera.holAusrichtung(), targetRotation, 0.7);
        //qm.normalisiere();
        //   this.kamera.setzAusrichtung(qm);
        this.kamera.setzAusrichtung(kameraNeuQ);
        this.spieler.setzAusrichtung(spielerNeuQ);

        Vektor4 tmp = this.spieler.holStandort().kopiere();
        Quaternion kameraNeuQQ = kameraNeuQ.kopiere();
        kameraNeuQQ.konjugante();
        Vektor4 abstand = new Vektor4(0, 5, 10, 1.0f);
        tmp = kameraNeuQQ.multipliziereV(tmp);
        tmp = tmp.addiereV4(abstand);
        this.kamera.setzStandort(tmp);*/

        this.bewegeVorwaerts = false;
        this.bewegeRueckwaerts = false;
        this.bewegeLinks = false;
        this.bewegeRechts = false;
       // this.rotX = 0;
      //  this.rotY = 0;
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
        this.rotX -= deltaX;
        this.rotY -= deltaY;

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
