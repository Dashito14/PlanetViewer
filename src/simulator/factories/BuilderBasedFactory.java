package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class BuilderBasedFactory<T> implements Factory<T> {

	List<Builder<T>> _builder;

	public BuilderBasedFactory(List<Builder<T>> builders) {
		this._builder = builders;
	}

	@Override
	public T createInstance(JSONObject info) throws IllegalArgumentException {
		T objetoNuevo;
		for (Builder<T> builder : _builder) {
			objetoNuevo = builder.createInstance(info);
			if (objetoNuevo != null) {
				return objetoNuevo;
			}
		}
		throw new IllegalArgumentException();
	}

	@Override
	public List<JSONObject> getInfo() {
		List<JSONObject> infoObjetos = new ArrayList<>();
		for (Builder<T> jsonObject : _builder) {
			infoObjetos.add(jsonObject.getBuilderInfo());
		}
		return infoObjetos;
	}

}
