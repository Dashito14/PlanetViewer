package simulator.factories;

import org.json.JSONObject;

import simulator.model.GravityLaws;
import simulator.model.NewtonUniversalGravitation;

public class NewtonUniversalGravitationBuilder extends Builder<GravityLaws> {

	@Override
	public GravityLaws createInstance(JSONObject info) {
		if (info.getString("type").equalsIgnoreCase("nlug")) {
			return createTheInstance(info);
		}
		return null;
	}

	@Override
	public JSONObject getBuilderInfo() {
		JSONObject obj = new JSONObject();
		obj.put("type", "nlug");

		obj.put("desc", "Newton's law of universal gravitation");

		return obj;
	}

	@Override
	protected GravityLaws createTheInstance(JSONObject jsonObject) {
		return new NewtonUniversalGravitation();
	}

}
