package klassen;

/**
 * Created by larry on 20.03.2016.
 */
public class Physik {
    protected float gewicht;
    protected float geschwindigkeit;
    protected float kraft;
    protected float gravitation;
    public Physik(float gravitation, float gewicht){
        this.gravitation = gravitation;
        this.geschwindigkeit = 0.0f;
        this.gewicht = gewicht;
        this.kraft = 25.0f;
    }

    public float holGravitation() {
        return this.gravitation;
    }
    public void setzGeschwindigkeit(float geschwindigkeit) {
        this.geschwindigkeit = geschwindigkeit;
    }
    public float holGeschwindigkeit() {
        return this.geschwindigkeit;
    }
    public float holGewicht() {
        return this.gewicht;
    }
    public float holKraft() {
        return this.kraft;
    }
}

/*
*

    this.getGravitation = function() {
        return this.gravitation;
    };
    this.setSpeed = function(speed) {
        this.speed = speed;
    };
    this.getSpeed = function() {
        return this.speed;
    };
    this.getWeight = function() {
        return this.weight;
    };
    this.getForce = function() {
        return this.force;
    };
    this.init(gravitation, weight);
};
*/