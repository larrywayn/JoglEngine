package klassen;

public class Quaternion extends Vektor4 {

    static Quaternion slerp(Quaternion inA, Quaternion inB, double weight) {
        Quaternion out = new Quaternion();
        if (weight < 0.0) {
            return inA;
        } else if (weight > 1.0) {
            return inB;
        }
        float dot = inA.skalarProduktV4(inB);
        float x2, y2, z2, w2;
        if (dot < 0.0) {
            dot = 0.0f - dot;
            x2 = 0.0f - inB.holX();
            y2 = 0.0f - inB.holY();
            z2 = 0.0f - inB.holZ();
            w2 = 0.0f - inB.holW();
        } else {
            x2 = inB.holX();
            y2 = inB.holY();
            z2 = inB.holZ();
            w2 = inB.holW();
        }
        float t1, t2;
        final double EPSILON = 0.0001;
        if ((1.0 - dot) > EPSILON) {
            double angle = Math.acos(dot);
            double sinAngle = Math.sin(angle);
            t1 = (float) (Math.sin((1.0f - weight) * angle) / sinAngle);
            t2 = (float) (Math.sin(weight * angle) / sinAngle);
        } else {
            t1 = (float) (1.0f - weight);
            t2 = (float) weight;
        }
        out.setzX((inA.holX() * t1) + (x2 * t2));
        out.setzY((inA.holY() * t1) + (y2 * t2));
        out.setzZ((inA.holZ() * t1) + (z2 * t2));
        out.setzW((inA.holW() * t1) + (w2 * t2));
        return out;
    }

    protected float xSkalierung;
    protected float ySkalierung;
    protected float zSkalierung;

    public Quaternion() {
        this(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f);
    }

    public Quaternion(float x, float y, float z, float w) {
        this(x, y, z, w, 1.0f, 1.0f, 1.0f);
    }

    public Quaternion(float xSkalierung, float ySkalierung, float zSkalierung) {
        this(0.0f, 0.0f, 0.0f, 1.0f, xSkalierung, ySkalierung, zSkalierung);
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
       /*
        float A, B, C, D, E, F, G, H;
A = (q1->w + q1->x)*(q2->w + q2->x);
B = (q1->z - q1->y)*(q2->y - q2->z);
C = (q1->w - q1->x)*(q2->y + q2->z);
D = (q1->y + q1->z)*(q2->w - q2->x);
E = (q1->x + q1->z)*(q2->x + q2->y);
F = (q1->x - q1->z)*(q2->x - q2->y);
G = (q1->w + q1->y)*(q2->w - q2->z);
H = (q1->w - q1->y)*(q2->w + q2->z);
res->w = B + (-E - F + G + H) /2;
res->x = A - (E + F + G + H)/2;
res->y = C + (E - F + G - H)/2;
res->z = D + (E - F - G + H)/2; 
        */
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
        /*   float tmpW = (aw * bw) - (ax * bx) - (ay * by) - (az * bz);
        float tmpX = (aw * bx) - (ax * bw) - (ay * bz) - (az * by);
        float tmpY = (aw * by) - (ax * bw) - (ay * bw) - (az * bx);
        float tmpZ = (aw * bz) - (ax * by) - (ay * bx) - (az * bw);*/
        return new Quaternion(tmpX, tmpY, tmpZ, tmpW);
    }

    public Quaternion multipliziereVV(Vektor4 rechtesQuaternion) {
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
        /*    float tmpW = (aw * bw) - (ax * bx) - (ay * by) - (az * bz);
        float tmpX = (aw * bx) - (ax * bw) - (ay * bz) - (az * by);
        float tmpY = (aw * by) - (ax * bw) - (ay * bw) - (az * bx);
        float tmpZ = (aw * bz) - (ax * by) - (ay * bx) - (az * bw);*/
        return new Quaternion(tmpX, tmpY, tmpZ, tmpW);
    }

    public static Quaternion lookAt(Vektor4 dir, Vektor4 up) {
        Quaternion q = new Quaternion();
        Vektor4 xVec = up.kreuzProduktV4(dir);
        Vektor4 yVec = dir.kreuzProduktV4(xVec);
        float tr = xVec.holX() + yVec.holY() + dir.holZ();
        q.setzX(yVec.holZ() - dir.holY());
        q.setzY(dir.holX() - xVec.holZ());
        q.setzZ(xVec.holY() - yVec.holX());
        q.setzW(tr + 1.0f);
        q.normalisiere();
        return q;
    }

    public static Quaternion lookAt2(Vektor4 dirToLook, Vektor4 pos) {
        Vektor4 direction = dirToLook.subtrahiereV4(pos);
        direction.normalisiere();
        Vektor4 abc = new Vektor4(0, 0, 1, 1);
        float dot = abc.skalarProduktV4(direction);
        Quaternion rotation = null;
        if (Math.abs(dot - (-1.0f)) < 0.000001f) {
            abc = new Vektor4(0, 1, 0, 1);
            rotation = new Quaternion(abc.holX(), abc.holY(), abc.holZ(), 3.1415926535897932f); //glm::angleAxis(RadiansToDegrees(M_PI), abc);
            return rotation;
        } else if (Math.abs(dot - (1.0f)) < 0.000001f) {
            rotation = new Quaternion();
            return rotation;
        }
        float angle = (float) Math.acos(dot);
        Vektor4 cross = abc.kreuzProduktV4(direction);
        cross.normalisiere();
        rotation = Quaternion.createFromAxisAngle(cross, angle);
        rotation.normalisiere();
        return rotation;
    }

    public static Quaternion createFromAxisAngle(Vektor4 axis, float angle) {
        angle = (float) ((angle / 180.0) * Math.PI);
        float halfAngle = angle * .5f;
        float s = (float) Math.sin(halfAngle);
        Quaternion q = new Quaternion();
        q.setzX(axis.holX() * s);
        q.setzY(axis.holY() * s);
        q.setzZ(axis.holZ() * s);
        q.setzW((float) Math.cos(halfAngle));
        return q;
    }

    public Vektor4 multipliziereV(Vektor4 vektor) {

        /*  this.invertiere();Vektor4 rv = this.multipliziereVV(vektor);
        rv = this.multipliziereVV(rv);
       this.invertiere();
        return rv;*/
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
        this.konjugante();
        this.normalisiere();
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
            //mag = 1.0f / mag;
            this.w /= mag;
            this.x /= mag;
            this.y /= mag;
            this.z /= mag;
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
    }

    public Vektor4 holSeitwerts() {
        Vektor4 vektorSeitwerts = new Vektor4();
        vektorSeitwerts.setzX(1.0f - 2.0f * (this.y * this.y + this.z * this.z));
        vektorSeitwerts.setzY(2.0f * (this.x * this.y + this.w * this.z));
        vektorSeitwerts.setzZ(2.0f * (this.x * this.z - this.w * this.y));
        vektorSeitwerts.setzW(1.0f);
        return vektorSeitwerts;
    }

    public Vektor4 holVorwaerts() {
        Vektor4 vektorVorwaerts = new Vektor4(0.0f, 0.0f, 1.0f, 1.0f);
        vektorVorwaerts = this.multipliziereV(vektorVorwaerts);
        /*   Vektor4 vektorVorwaerts = new Vektor4();
        vektorVorwaerts.setzX(2.0f * (this.x * this.z + this.w * this.y));
        vektorVorwaerts.setzY(2.0f * (this.y * this.x - this.w * this.x));
        vektorVorwaerts.setzZ(1.0f - 2.0f * (this.x * this.x + this.y * this.y));
        vektorVorwaerts.setzW(1.0f);*/
        return vektorVorwaerts;
    }

    public Vektor4 holVertikal() {
        Vektor4 vektorVorwaerts = new Vektor4();
        vektorVorwaerts.setzX(2.0f * (this.x * this.y - this.w * this.z));
        vektorVorwaerts.setzY(1.0f - 2.0f * (this.x * this.x + this.z * this.z));
        vektorVorwaerts.setzZ(2.0f * (this.y * this.z + this.w * this.x));
        vektorVorwaerts.setzW(1.0f);
        return vektorVorwaerts;
    }

    public void ausgabe() {
      //  System.out.println("Q: " + (this.x) + " " + (this.y) + " " + (this.z) + " " + (this.w));
    }

    public void rotationAchseV(Vektor4 s, float winkel) {
        float radians = (float) ((winkel / 180.0) * Math.PI);
        float sinAngle2 = (float) Math.sin(radians / 2.0);
        Vektor4 v = s.kopiere();
        //v.normalisiere();
        this.x = v.holX() * sinAngle2;
        this.y = v.holY() * sinAngle2;
        this.z = v.holZ() * sinAngle2;
        this.w = (float) Math.cos(radians / 2.0);
    }

    public Quaternion kopiere() {
        return new Quaternion(this.x, this.y, this.z, this.w, this.xSkalierung, this.ySkalierung, this.holzSkalierung());
    }
}
