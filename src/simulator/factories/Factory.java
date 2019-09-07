package simulator.factories;

import java.util.List;

import org.json.JSONObject;

public interface Factory<T> {

	/*
	 * createInstance: ejecuta los constructores de los Builders uno por uno hasta
	 * que encuentre el constructor capaz de crear el objeto.
	 */
	public T createInstance(JSONObject info) throws IllegalArgumentException;

	/* getInfo: devuelve una lista con las distintas estructuras JSON disponibles */
	public List<JSONObject> getInfo();
}
