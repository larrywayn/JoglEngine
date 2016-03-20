package klassen;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by larry on 20.03.2016.
 */
public class Steuerung implements KeyListener {
    Kamera kamera;
    Spieler spieler;

    public Steuerung(Kamera kamera, Spieler spieler){
        this.kamera = kamera;
        this.spieler = spieler;
    }

    public void aktualisiereKamera(){

    }
    @Override
    public void keyTyped(KeyEvent e) {
        if(e.getKeyChar() == 'w'){
            System.out.println("do w");
            this.spieler.bewegeVorwaerts();
        }else if(e.getKeyChar() == 's'){
            System.out.println("do s");
            this.spieler.bewegeRueckwaerts();
        }else if(e.getKeyChar() == 'a'){
            System.out.println("do a");
            this.spieler.bewegeLinks();
        }else if(e.getKeyChar() == 'd'){
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
}
