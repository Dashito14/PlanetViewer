package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector;
import simulator.model.Body;
import simulator.model.MassLossingBody;

public class MassLossingBodyBuilder extends Builder<Body> {

	@Override
	public Body createInstance(JSONObject info) throws IllegalArgumentException {
		if (info.getString("type").equalsIgnoreCase("mlb")) {
			return createTheInstance(info);
		}
		return null;
	}

	@Override
	public JSONObject getBuilderInfo() {
		JSONObject obj = new JSONObject();
		obj.put("type", "mlb");

		JSONObject dataObj = new JSONObject();
		dataObj.put("id", "bl");
		dataObj.put("pos", "[-3.5e10, 0.0e00]");
		dataObj.put("vel", "[0.0e00, 1.4e03]");
		dataObj.put("mass", 3.0e28);
		dataObj.put("freq", 1e3);
		dataObj.put("factor", 1e-3);

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

			Double freq = aux.getDouble("freq");

			Double factor = aux.getDouble("factor");

			MassLossingBody mlb = new MassLossingBody(id, masa, vel, pos, new Vector(new double[] { 0.0, 0.0 }),
					factor, freq);
			return mlb;
		}
		return null;
	}

	private double[] jsonArrayToDoubleArray(JSONArray ja) {
		double[] array = { ja.getDouble(0), ja.getDouble(1) };
		return array;
	}

}
