package simulator.model;

import java.util.List;

public class FallingToCenterG implements GravityLaws {

	static final double g = -9.81;

	@Override
	public void apply(List<Body> bodies) {
		for (int i = 0; i < bodies.size(); i++) {
			bodies.get(i).setAcceleration(bodies.get(i).getPosition().direction().scale(g));
		}
	}
	
	/*Método toString() a la clase FallingToCenter que devuelva una breve descripción (cadena) de
	las ley de gravedad correspondiente.*/
	public String toString(){
		return "Falling to center gravity(ftcg)";
	}

}
