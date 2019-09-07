package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector;
import simulator.model.Body;

public class BasicBodyBuilder extends Builder<Body> {

	@Override
	public Body createInstance(JSONObject info) {
		if (info.getString("type").equalsIgnoreCase("basic")) {
			return createTheInstance(info);
		}
		return null;
	}

	@Override
	public JSONObject getBuilderInfo() {
		JSONObject obj = new JSONObject();
		obj.put("type", "basic");

		JSONObject dataObj = new JSONObject();
		dataObj.put("id", "");
		dataObj.put("pos", "[0.0e00, 0.0e00]");
		dataObj.put("vel", "[0.05e04, 0.0e00]");
		dataObj.put("mass", 5.97e24);

		obj.put("data", dataObj);

		return obj;
	}

	@Override
	protected Body createTheInstance(JSONObject jsonObject) {
		if (jsonObject.getJSONObject("data") != null) {
			JSONObject aux = jsonObject.getJSONObject("data");

			String id = aux.getString("id");

			Double masa = aux.getDouble("mass");

			JSONArray vec1 = aux.getJSONArray("vel");
			Vector vel = new Vector(jsonArrayToDoubleArray(vec1));

			JSONArray vec2 = aux.getJSONArray("pos");
			Vector pos = new Vector(jsonArrayToDoubleArray(vec2));

			Body body = new Body(id, masa, vel, pos, new Vector(new double[] { 0.0, 0.0 }));
			return body;
		}
		return null;
	}

	private double[] jsonArrayToDoubleArray(JSONArray ja) {
		double[] array = { ja.getDouble(0), ja.getDouble(1) };
		return array;
	}
}
