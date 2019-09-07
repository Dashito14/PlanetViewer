package simulator.model;

import java.util.List;

public interface GravityLaws {

	/*
	 * apply: aplica las leyes de la gravedad particulares de la clase para cambiar
	 * las distintas propiedades de los atributos de los cuerpos.
	 */
	public void apply(List<Body> bodies);
}
