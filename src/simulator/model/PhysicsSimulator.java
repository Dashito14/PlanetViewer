package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class PhysicsSimulator {

	private double realTime;
	private GravityLaws gravityLaws;
	private double actualTime;
	private List<Body> list;
	private List<SimulatorObserver> so;

	/* Constructora */
	public PhysicsSimulator(double realTime, GravityLaws gravityLaws) {
		this.realTime = realTime;
		this.gravityLaws = gravityLaws;
		this.actualTime = 0.0;
		this.list = new ArrayList<Body>();
		this.so = new ArrayList<SimulatorObserver>();
	}

	public void advance() {
		this.gravityLaws.apply(list);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).move(this.realTime);

		}
		this.actualTime += this.realTime;
		/* Envia la seÃ±al onAdvance a todos los observadores */
		for (int i = 0; i < this.so.size(); i++) {
			this.so.get(i).onAdvance(this.list, this.actualTime);
		}
	}

	public void addBody(Body b) throws IllegalArgumentException {
		int i = 0;
		while (i < this.list.size()) {
			if (equalID(b, this.list.get(i))) {
				throw new IllegalArgumentException("Existen dos elementos con el mismo ID");
			}
			i++;
		}
		this.list.add(b);
		/* Envia la señal onBodyAdded a todos los observadores */
		for (int j = 0; j < this.so.size(); j++) {
			this.so.get(j).onBodyAdded(this.list, b);
		}
	}

	/* addObserver: aï¿½adde un observer a a la lista si no estï¿½ */
	public void addObserver(SimulatorObserver o) {
		int i = 0;
		boolean encontrado = false;
		while (i < this.so.size() && !encontrado) {
			if (this.so.get(i).equals(o)) {
				encontrado = true;
			}
			i++;
		}
		if (!encontrado) {
			this.so.add(o);
			o.onRegister(this.list, this.actualTime, this.realTime, this.gravityLaws.toString());
		}
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("\"time\": ");
		s.append(actualTime);
		s.append(", \"bodies\": [ ");
		if (!this.list.isEmpty()) {
			s.append(this.list.get(0).toString());

			for (int i = 1; i < this.list.size(); i++) {
				s.append(", ");
				s.append(this.list.get(i).toString());
			}
		}
		s.append(" ]");
		return s.toString();
	}

	private boolean equalID(Body a, Body b) {
		if (a.getId() == b.getId()) {
			return true;
		}
		return false;
	}

	public void setGravityLaws(GravityLaws gravityLaws) throws IllegalArgumentException {
		if (gravityLaws == null) {
			throw new IllegalArgumentException("La ley de Gravedad no es valida.");
		} else {
			this.gravityLaws = gravityLaws;
		}
		/* Envia la señal onGravityLawsChanged a todos los observadores */
		for (int i = 0; i < this.so.size(); i++) {
			this.so.get(i).onGravityLawsChanged(this.gravityLaws.toString());
		}
	}

	public void setDeltaTime(double dt) throws IllegalArgumentException {
		if (dt < 0) {
			throw new IllegalArgumentException("El valor de dt es invalido.");
		} else {
			this.realTime = dt;
		}

		/* Envia la señal onDeltaTimeChanged a todos los observadores */
		for (int i = 0; i < this.so.size(); i++) {
			this.so.get(i).onDeltaTimeChanged(this.realTime);
		}
	}

	public void reset() {
		int size = this.list.size();
		if (!this.list.isEmpty()) {
			for (int i = 0; i < size; i++) {
				this.list.remove(0);
			}
		}

		actualTime = 0.0;

		/* Envia la señal onReset a todos los observadores */
		for (int i = 0; i < this.so.size(); i++) {
			this.so.get(i).onReset(this.list, this.actualTime, this.realTime, this.gravityLaws.toString());
		}
	}

	/* getters */
	public double getRealTime() {
		return realTime;
	}

	public GravityLaws getGravityLaws() {
		return gravityLaws;
	}

	public List<Body> getList() {
		return list;
	}

}
