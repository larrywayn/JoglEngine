package klassen;

import java.io.Serializable;

public class Quaternion extends Vektor4 implements Serializable {
	protected float xSkalierung;
	protected float ySkalierung;
	protected float zSkalierung;

	public Quaternion() {
		this(0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
	}

	public Quaternion(float x, float y, float z, float w) {
		this(x, y, z, w, 1.0f, 1.0f, 1.0f);
	}

	public Quaternion(float xSkalierung, float ySkalierung, float zSkalierung) {
		this(0.0f, 0.0f, 0.0f, 0.0f, xSkalierung, ySkalierung, zSkalierung);
	}

	public Quaternion(float x, float y, float z, float w, float xSkalierung, float ySkalierung, float zSkalierung) {
		super(x, y, z, w);
		this.xSkalierung = xSkalierung;
		this.ySkalierung = ySkalierung;
		this.zSkalierung = zSkalierung;
	}

	public void setzSkalierung(float xSkalierung, float ySkalierung, float zSkalierung) {
		this.xSkalierung = xSkalierung;
		this.ySkalierung = ySkalierung;
		this.zSkalierung = zSkalierung;
	}

	public float holXSkalierung() {
		return this.xSkalierung;
	}

	public float holySkalierung() {
		return this.ySkalierung;
	}

	public float holzSkalierung() {
		return this.zSkalierung;
	}

	public Quaternion multipliziereQ(Quaternion rechtesQuaternion) {
		float ax = this.x;
		float ay = this.y;
		float az = this.z;
		float aw = this.w;
		float bx = rechtesQuaternion.holX();
		float by = rechtesQuaternion.holY();
		float bz = rechtesQuaternion.holZ();
		float bw = rechtesQuaternion.holW();
		float tmpX = (aw * bx + ax * bw + ay * bz - az * by);
		float tmpY = (aw * by + ay * bw + az * bx - ax * bz);
		float tmpZ = (aw * bz + az * bw + ax * by - ay * bx);
		float tmpW = (aw * bw - ax * bx - ay * by - az * bz);
		return new Quaternion(tmpX, tmpY, tmpZ, tmpW);
	}

	public Vektor4 multipliziereV(Vektor4 vektor) {
		float vx = vektor.holX();
		float vy = vektor.holY();
		float vz = vektor.holZ();
		float a00 = this.w * this.w;
		float a01 = this.w * this.x;
		float a02 = this.w * this.y;
		float a03 = this.w * this.z;
		float a11 = this.x * this.x;
		float a12 = this.x * this.y;
		float a13 = this.x * this.z;
		float a22 = this.y * this.y;
		float a23 = this.y * this.z;
		float a33 = this.z * this.z;
		float tmpX = ((vx * this.xSkalierung) * (+a00 + a11 - a22 - a33) + 2.0f * (a12 * (vy * this.ySkalierung)
				+ a13 * (vz * this.zSkalierung) + a02 * (vz * this.zSkalierung) - a03 * (vy * this.ySkalierung)));
		float tmpY = ((vy * this.ySkalierung) * (+a00 - a11 + a22 - a33) + 2.0f * (a12 * (vx * this.xSkalierung)
				+ a23 * (vz * this.zSkalierung) + a03 * (vx * this.xSkalierung) - a01 * (vz * this.zSkalierung)));
		float tmpZ = ((vz * this.zSkalierung) * (+a00 - a11 - a22 + a33) + 2.0f * (a13 * (vx * this.xSkalierung)
				+ a23 * (vy * this.ySkalierung) - a02 * (vx * this.xSkalierung) + a01 * (vy * this.ySkalierung)));
		return new Vektor4(tmpX, tmpY, tmpZ, 1.0f);
	}

	public float holWinkel() {
		return (float) (((Math.acos(this.w) * 2.0f) * Math.PI) / 180.0f);
	}

	public void invertiere() {
		this.normalisiere();
		this.konjugante();
	}

	public float magnitude() {
		return super.laenge();
	}

	public void normalisiere() {
		float mag = this.magnitude();
		if (mag == 0.0f) {
			this.w = 1.0f;
			this.x = 0.0f;
			this.y = 0.0f;
			this.z = 0.0f;
		} else {
			mag = 1.0f / mag;
			this.w *= mag;
			this.x *= mag;
			this.y *= mag;
			this.z *= mag;
		}
	}

	public void konjugante() {
		this.x = -this.x;
		this.y = -this.y;
		this.z = -this.z;
	}

	public float[] erzeugeMatrix(Vektor4 vektor) {
		float[] matrixArray = new float[16];
		float xy = this.x * this.y;
		float xz = this.x * this.z;
		float xx = this.x * this.x;
		float yy = this.y * this.y;
		float yw = this.y * this.w;
		float xw = this.x * this.w;
		float zz = this.z * this.z;
		float zw = this.z * this.w;
		float zy = this.z * this.y;
		matrixArray[0] = this.xSkalierung * (1.0f - (2.0f * (yy + zz)));
		matrixArray[1] = this.xSkalierung * 2.0f * (xy - zw);
		matrixArray[2] = this.xSkalierung * 2.0f * (xz + yw);
		matrixArray[3] = 0.0f;
		matrixArray[4] = this.ySkalierung * 2.0f * (xy + zw);
		matrixArray[5] = this.ySkalierung * (1.0f - (2.0f * (xx + zz)));
		matrixArray[6] = this.ySkalierung * 2.0f * (zy - xw);
		matrixArray[7] = 0.0f;
		matrixArray[8] = this.zSkalierung * 2.0f * (xz - yw);
		matrixArray[9] = this.zSkalierung * 2.0f * (zy + xw);
		matrixArray[10] = this.zSkalierung * (1.0f - (2.0f * (xx + yy)));
		matrixArray[11] = 0.0f;
		matrixArray[12] = vektor.holX();
		matrixArray[13] = vektor.holY();
		matrixArray[14] = vektor.holZ();
		matrixArray[15] = 1.0f;
		return matrixArray;
	};

	public void ausgabe() {
		System.out.println("Q: " + (this.x) + " " + (this.y) + " " + (this.z) + " " + (this.w));
	};
}
