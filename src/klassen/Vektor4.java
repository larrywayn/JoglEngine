package klassen;

import grundklassen.Vektor;

public class Vektor4 extends Vektor {
	protected float w;

	public Vektor4() {
		super(0.0f,0.0f,0.0f);
		this.w = 0.0f;
	}
	
	public Vektor4(float x, float y, float z, float w) {
		super(x, y, z);
		this.w = w;
	}

	public float holW() {
		return this.w;
	}

	public void setzW(float w) {
		this.w = w;
	}

	public Vektor4 addiereV4(Vektor4 rechterVektor4) {
		float tmpX = this.x + rechterVektor4.holX();
		float tmpY = this.y + rechterVektor4.holY();
		float tmpZ = this.z + rechterVektor4.holZ();
		float tmpW = this.w + rechterVektor4.holW();
		return new Vektor4(tmpX, tmpY, tmpZ, tmpW);
	}

	public Vektor4 multipliziereV4(Vektor4 rechterVektor4) {
		float tmpX = this.x * rechterVektor4.x;
		float tmpY = this.y * rechterVektor4.y;
		float tmpZ = this.z * rechterVektor4.z;
		float tmpW = this.w * rechterVektor4.w;
		return new Vektor4(tmpX, tmpY, tmpZ, tmpW);
	}

	public Vektor4 subtrahiereV4(Vektor4 rechterVektor4) {
		float tmpX = this.x - rechterVektor4.x;
		float tmpY = this.y - rechterVektor4.y;
		float tmpZ = this.z - rechterVektor4.z;
		float tmpW = this.w - rechterVektor4.w;
		return new Vektor4(tmpX, tmpY, tmpZ, tmpW);
	}

	private float fixNumber(float value) {
		final float mult = (float) Math.pow(10.0f, 3.0f);
		return Math.round(value * mult) / mult;
	}

	public boolean vergleicheV4(Vektor4 rechterVektor4) {
		float x1 = this.x;
		x1 = (x1 > -EPSILON && x1 < EPSILON) ? 0.0f : fixNumber(x1);
		float x2 = rechterVektor4.holX();
		x2 = (x2 > -EPSILON && x2 < EPSILON) ? 0.0f : fixNumber(x2);
		float y1 = this.y;
		y1 = (y1 > -EPSILON && y1 < EPSILON) ? 0.0f : fixNumber(y1);
		float y2 = rechterVektor4.holY();
		y2 = (y2 > -EPSILON && y2 < EPSILON) ? 0.0f : fixNumber(y2);
		float z1 = this.z;
		z1 = (z1 > -EPSILON && z1 < EPSILON) ? 0.0f : fixNumber(z1);
		float z2 = rechterVektor4.holZ();
		z2 = (z2 > -EPSILON && z2 < EPSILON) ? 0.0f : fixNumber(z2);
		float w1 = this.w;
		w1 = (w1 > -EPSILON && w1 < EPSILON) ? 0.0f : fixNumber(w1);
		float w2 = rechterVektor4.holW();
		w2 = (w2 > -EPSILON && w2 < EPSILON) ? 0.0f : fixNumber(w2);
		if (x1 == x2 && y1 == y2 && z1 == z2 && w1 == w2) {
			return true;
		}
		return false;
	}

	public float skalarProduktV4(Vektor4 rechterVektor4) {
		Vektor4 tmpVektor = this.multipliziereV4(rechterVektor4);
		return (tmpVektor.x + tmpVektor.y + tmpVektor.z + tmpVektor.w);
	}

	public Vektor4 kreuzProduktV4(Vektor4 rechterVektor4) {
		float tmpX = (this.y * rechterVektor4.z) - (this.z * rechterVektor4.y);
		float tmpY = (this.z * rechterVektor4.x) - (this.x * rechterVektor4.z);
		float tmpZ = (this.x * rechterVektor4.y) - (this.y * rechterVektor4.x);
		float tmpW = (this.w + rechterVektor4.w);
		return new Vektor4(tmpX, tmpY, tmpZ, tmpW);
	}

	public float[] alsArray() {
		return new float[] { this.x, this.y, this.z, this.w };
	}

	public void invertiere() {
		this.x *= -1.0f;
		this.y *= -1.0f;
		this.z *= -1.0f;
		this.w *= -1.0f;
	}

	public void skaliere(float skalar) {
		this.x *= skalar;
		this.y *= skalar;
		this.z *= skalar;
		this.w *= skalar;
	}

	public void skaliereV4(Vektor4 vektor) {
		this.x *= vektor.x;
		this.y *= vektor.y;
		this.z *= vektor.z;
		this.w *= vektor.w;
	}

	public float laenge() {
		float erg = (this.x * this.x) + (this.y * this.y) + (this.z * this.z) + (this.w * this.w);
		if (erg > 0) {
			return (float) Math.sqrt(erg);
		} else {
			return 1.0f;
		}
	}

	public void normalisiere() {
		float len = 1.0f / this.laenge();
		if (len > 0.0f) {
			this.x *= len;
			this.y *= len;
			this.z *= len;
			this.w *= len;
		}
	}
}
