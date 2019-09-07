package simulator.factories;

import org.json.JSONObject;

import simulator.model.GravityLaws;
import simulator.model.NoGravity;

public class NoGravityBuilder extends Builder<GravityLaws> {

	@Override
	public GravityLaws createInstance(JSONObject info) {
		if (info.getString("type").equalsIgnoreCase("ng")) {
			return createTheInstance(info);
		}
		return null;
	}

	@Override
	public JSONObject getBuilderInfo() {
		JSONObject obj = new JSONObject();
		obj.put("type", "ng");

		obj.put("desc", "No gravity");

		return obj;
	}

	@Override
	protected GravityLaws createTheInstance(JSONObject jsonObject) {
		return new NoGravity();
	}

}
