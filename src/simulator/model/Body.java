package simulator.model;

import simulator.misc.Vector;

public class Body {
	protected String id;
	protected double m;
	protected Vector v;
	protected Vector p;
	protected Vector a;

	public Body(String id, double m, Vector v, Vector p, Vector a) {
		this.id = id;
		this.m = m;
		this.v = v;
		this.p = p;
		this.a = a;
	}

	/**
	 * move: mueve el cuerpo durante t segundos utilizando los atributos del mismo.
	 */
	protected void move(double t) {
		Vector acc = this.a.scale(0.5).scale(t * t);
		Vector vel = this.v.scale(t);
		
		this.p = this.p.plus(acc).plus(vel);

		acc = this.a.scale(t);
		this.v = this.v.plus(acc);
	}

	@Override
	public String toString() {
		return "{ \"id\": \"" + id + "\", \"mass\": " + m + ", \"pos\": " + p + ", \"vel\": " + v
				+ ", \"acc\": " + a + " }";
	}

	/* Metodos getter y setter de los atributos de la clase Body */

	public Vector getVelocity() {
		return v;
	}

	public void setVelocity(Vector v) {
		this.v = new Vector(v);
	}

	public Vector getPosition() {
		return p;
	}

	public void setPosition(Vector p) {
		this.p = new Vector(p);
	}

	public Vector getAcceleration() {
		return a;
	}

	public void setAcceleration(Vector a) {
		this.a = new Vector(a);
	}

	public String getId() {
		return id;
	}

	public double getMass() {
		return m;
	}

}
