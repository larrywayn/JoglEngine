package klassen;

import com.jogamp.opengl.awt.GLJPanel;

import java.awt.event.*;
import java.util.HashSet;

/**
 * Created by larry on 20.03.2016.
 */
public class Steuerung implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

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
    private boolean beschleunigeSpieler;
    private int zoomAnsicht;
    private final HashSet<Integer> pressed;
    private float zoomLevel;

    public Steuerung(GLJPanel fenster, Kamera kamera, Spieler spieler) {
        this.kamera = kamera;
        this.spieler = spieler;
        this.fenster = fenster;
        this.rotX = 0;
        this.rotY = 0;
        this.lastX = 0;
        this.lastY = 0;
        this.zoomAnsicht = 0;
        this.zoomLevel = 5.0f;
        this.mouseFaktorX = 5.0f;
        this.mouseFaktorY = 5.0f;
        this.pressed = new HashSet<>();
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
        this.checkeEingaben();
        if (this.beschleunigeSpieler) {
            this.spieler.bewegeSchneller();
        }
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
        this.rotY = ((this.rotY > -1) ? -1 : ((this.rotY < -85) ? -85 : this.rotY));
        Quaternion spielerQ = this.spieler.holAusrichtung().kopiere();
        Vektor4 spielerV = (this.spieler.holStandort().kopiere());
        Vektor4 zAchseSpieler = spielerQ.holVertikal();
        Quaternion spielerRotXQ = Quaternion.createFromAxisAngle(zAchseSpieler, this.rotX);
        Quaternion spielerNeuQ = spielerRotXQ.multipliziereQ(spielerQ);
        Quaternion spielerQI = spielerNeuQ.kopiere();
        spielerQI.invertiere();
        spielerNeuQ.setzSkalierung(1.f, 10.f, 1.f);
        this.spieler.setzAusrichtung(spielerNeuQ);
        //Quaternion.slerp( spielerQI,this.kamera.holAusrichtung(), 0.02) 
        Vektor4 xAchseSpieler = spielerQI.holSeitwerts();
        Quaternion spielerRotYQ = Quaternion.createFromAxisAngle(xAchseSpieler, this.rotY);
        Quaternion spielerQIQ = spielerRotYQ.multipliziereQ(spielerQI);
        this.kamera.setzAusrichtung(spielerQIQ);
        Quaternion spielerQIQI = spielerQIQ.kopiere();
        spielerQIQI.invertiere();
        spielerV = spielerQIQI.multipliziereV(spielerV);

        if (this.zoomAnsicht < 0) {
            this.zoomLevel -= 0.5;
        } else if (this.zoomAnsicht > 0) {
            this.zoomLevel += 0.5;
        }
        
        System.out.println("Level B: " + this.zoomLevel);
        if (this.zoomLevel <= 1.5f) {
            this.zoomLevel = 1.5f;
        } else if (this.zoomLevel >= 20.0f) {
            this.zoomLevel = 20.0f;
        }
        float level = 8f * ((75.0f / Configuration.holSichtfeld())) * this.zoomLevel;
        System.out.println("Zoom dir: " + this.zoomAnsicht + " Zoom: " + this.zoomLevel+ " Level: " + level);
        Vektor4 vor = new Vektor4(0f, level / 10f, level, 1);
        spielerV = spielerV.addiereV4(vor);
        this.kamera.setzStandort(spielerV);
        this.bewegeVorwaerts = false;
        this.bewegeRueckwaerts = false;
        this.bewegeLinks = false;
        this.bewegeRechts = false;
        this.rotX = 0;
        this.zoomAnsicht = 0;
        this.spieler.bewegeNormal();
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
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressed.add(e.getExtendedKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getExtendedKeyCode();
        pressed.remove(key);
        checkeKeyEingaben(key, false);
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
        float newX = relativeMousePositions[0] / this.mouseFaktorX;
        float newY = relativeMousePositions[1] / this.mouseFaktorY;
        float deltaX = newX - this.lastX;
        float deltaY = newY - this.lastY;
        this.lastX = newX;
        this.lastY = newY;
        this.rotX += deltaX;
        this.rotY -= deltaY;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    private void checkeKeyEingaben(int key, boolean startstop) {
        switch (key) {
            case KeyEvent.VK_W:
                System.out.println("do w");
                this.bewegeVorwaerts = startstop;
                break;
            case KeyEvent.VK_S:
                System.out.println("do s");
                this.bewegeRueckwaerts = startstop;
                break;
            case KeyEvent.VK_A:
                System.out.println("do a");
                this.bewegeLinks = startstop;
                break;
            case KeyEvent.VK_D:
                System.out.println("do d");
                this.bewegeRechts = startstop;
                break;
            case KeyEvent.VK_SHIFT:
                System.out.println("do shift");
                this.beschleunigeSpieler = startstop;
                break;
            default:
                break;
        }
    }

    private void checkeEingaben() {
        for (int key : pressed) {
            checkeKeyEingaben(key, true);
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        System.out.println("Wheel: " + e.getWheelRotation());
        this.zoomAnsicht = e.getWheelRotation();
    }

}
