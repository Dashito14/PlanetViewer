package simulator.model;

import java.util.List;

import simulator.misc.Vector;

public class NewtonUniversalGravitation implements GravityLaws {

	private static final double G = 6.67E-11;

	@Override
	public void apply(List<Body> bodies) {

		for (int i = 0; i < bodies.size(); i++) {
			Vector f = new Vector(bodies.get(0).getPosition().dim());
			for (int j = 0; j < bodies.size(); j++) {
				if (bodies.get(i).getMass() != 0.0) {
					if (i != j) {
						f = f.plus(fuerza(bodies.get(i), bodies.get(j)));
					}
				}
			}
			bodies.get(i).setAcceleration(f.scale(1 / bodies.get(i).getMass()));
		}

	}

	public Vector fuerza(Body a, Body b) {
		Vector f = new Vector(a.getPosition().dim());
		Vector dif = new Vector(f.dim());
		
		dif = b.getPosition().minus(a.getPosition());
		double mag = dif.magnitude();
		double fij = (G * a.getMass() * b.getMass()) / (mag * mag);

		f = dif.direction().scale(fij);
		return f;
	}
	
	/*Método toString() a la clase NewtonUniversalGravitation que devuelva una breve descripción (cadena) de
	las ley de gravedad correspondiente.*/
	public String toString(){
		return "Newton's Law of Universal Gravitation (nlug)";
	}

}
