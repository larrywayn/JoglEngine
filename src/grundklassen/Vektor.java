package grundklassen;

public abstract class Vektor {
	protected static final float EPSILON = 0.00001f;
	protected float x;
	protected float y;
	protected float z;
	public Vektor() {
		this.x = 0.0f;
		this.y = 0.0f;
		this.z = 0.0f;
	}
	public Vektor(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public float holX(){
		return this.x;
	}
	public float holY(){
		return this.y;
	}
	public float holZ(){
		return this.z;
	}
	
	public void setzX(float x){
		this.x = x;
	}
	public void setzY(float y){
		this.y = y;
	}
	public void setzZ(float z){
		this.z = z;
	}

	public float[] alsArray() {
		return new float[] { this.x, this.y, this.z };
	}
}
