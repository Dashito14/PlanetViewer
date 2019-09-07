package simulator.model;

import simulator.misc.Vector;

public class MassLossingBody extends Body {

	private double lossFactor;
	private double lossFrequency;
	private double c;

	/* Constructora */
	public MassLossingBody(String id, double masa, Vector pos, Vector vel, Vector acc, double lossFactor,
			double lossFrequency) {
		super(id, masa, vel, pos, acc);
		this.lossFactor = lossFactor;
		this.lossFrequency = lossFrequency;
		this.c = 0.0;
	}

	/**
	 * move: mueve el cuerpo durante t segundos utilizando los atributos del mismo. Ademas comprueba si han pasado
	 * un numero determinado de segundos desde la ultima vez que se redujo la masa de los cuerpos. 
	 */
	public void move(double t) {
		super.move(t);
		c += t;
		if (c >= this.lossFrequency) {
			this.m = this.m * (1 - this.lossFactor);
			c = 0.0;
		}
	}
}
