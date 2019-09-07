package simulator.factories;

import org.json.JSONObject;

import simulator.model.FallingToCenterG;
import simulator.model.GravityLaws;

public class FallingToCenterGravityBuilder extends Builder<GravityLaws> {

	@Override
	public GravityLaws createInstance(JSONObject info) {
		if (info.getString("type").equalsIgnoreCase("ftcg")) {
			return createTheInstance(info);
		}
		return null;
	}

	@Override
	public JSONObject getBuilderInfo() {
		JSONObject obj = new JSONObject();
		obj.put("type", "ftcg");

		obj.put("desc", "Falling to center gravity");

		return obj;
	}

	@Override
	protected GravityLaws createTheInstance(JSONObject jsonObject) {
		return new FallingToCenterG();
	}

}
