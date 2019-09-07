package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.GravityLaws;
import simulator.model.PhysicsSimulator;
import simulator.model.SimulatorObserver;

public class Controller {

	private PhysicsSimulator physics;
	private Factory<Body> factory;
	private Factory<GravityLaws> factoryG;

	public Controller(PhysicsSimulator ps, Factory<Body> fb, Factory<GravityLaws> fg) {
		this.physics = ps;
		this.factory = fb;
		this.factoryG = fg;
	}

	/*
	 * loadBodies: transforma una entrada JSON en un objeto JSONObject, y luego
	 * extrae los cuerpos y los crea y aï¿½ade al simulador.
	 */
	public void loadBodies(InputStream in) {
		JSONObject jsonInput = new JSONObject(new JSONTokener(in));
		JSONArray array = jsonInput.getJSONArray("bodies");
		for (Object object : array) {
			this.physics.addBody(this.factory.createInstance((JSONObject) object));
		}
	}

	/*
	 * run: ejecuta el simulador n pasos y muestra los diferentes estados en el
	 * archivo especificado en out.
	 */
	public void run(int n, OutputStream out) {
		PrintStream printStream = new PrintStream(out);
		printStream.print("{\n");
		printStream.print("\"states\": [\n");

		printStream.print("{ ");
		printStream.print(this.physics.toString());
		printStream.print(" }");
		printStream.print("\n");
		for (int i = 0; i < n; i++) {
			this.physics.advance();
			printStream.print("{ ");
			printStream.print(this.physics.toString());
			printStream.print(" },");
			printStream.print("\n");
		}

		printStream.print("]\n}");
		printStream.close();
	}
	
	/*run: ejecuta n pasos del simulador sin escribir nada e consola */
	public void run(int n) {
		for (int i = 0; i < n; i++) {
			this.physics.advance();
		}
	}
	
	/* setGravityLaws: */
	public void setGravityLaws(JSONObject info) {
		this.physics.setGravityLaws(this.factoryG.createInstance(info));
	}
	
	/* invoca al metodo reset del simulador.*/
	public void reset(){
		this.physics.reset();
	}
	
	/*invoca al metodo setDeltaTime del simulador.*/
	public void setDeltaTime(double dt){
		this.physics.setDeltaTime(dt);
	}
	
	/*invoca al metodo addObserver del simulador.*/
	public void addObserver(SimulatorObserver o){
		this.physics.addObserver(o);
	}
	
	/*devuelve la factoria de leyes de gravedad.*/
	public Factory<GravityLaws> getGravityLawsFactory(){
		return this.factoryG;
	}

	/* método getter del simulador */
	public PhysicsSimulator getPhysics() {
		return physics;
	}
	
	
}
