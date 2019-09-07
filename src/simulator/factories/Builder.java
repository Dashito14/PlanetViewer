package simulator.factories;

import org.json.JSONObject;

public abstract class Builder<T> {

	protected String typeTag;
	protected String desc;

	public abstract T createInstance(JSONObject info) throws IllegalArgumentException;

	public abstract JSONObject getBuilderInfo();

	protected abstract T createTheInstance(JSONObject jsonObject);
}
