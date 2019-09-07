package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class BodiesTableModel extends AbstractTableModel implements SimulatorObserver {

	private static final long serialVersionUID = 1L;

	private List<Body> _bodies;

	BodiesTableModel(Controller ctrl) {
		_bodies = new ArrayList<>();
		ctrl.addObserver(this);
	}

	@Override
	public int getRowCount() {
		return this._bodies.size();
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public String getColumnName(int column) {
		String name = "";
		if (column == 0) {
			name = "Id";
		} else if (column == 1) {
			name = "Mass";
		} else if (column == 2) {
			name = "Position";
		} else if (column == 3) {
			name = "Velocity";
		} else if (column == 4) {
			name = "Acceleration";
		}
		return name;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return this._bodies.get(rowIndex).getId();
		} else if (columnIndex == 1) {
			return this._bodies.get(rowIndex).getMass();
		} else if (columnIndex == 2) {
			return this._bodies.get(rowIndex).getPosition();
		} else if (columnIndex == 3) {
			return this._bodies.get(rowIndex).getVelocity();
		} else if (columnIndex == 4) {
			return this._bodies.get(rowIndex).getAcceleration();
		} else
			return null;
	}

	/*
	 * Métodos encargados de notificar cambios y actualiza así la interfaz
	 * consecuentemente.
	 */
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				_bodies = bodies;
				fireTableStructureChanged();

			}
		});
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				_bodies = bodies;
				fireTableStructureChanged();

			}
		});
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				_bodies = bodies;
				fireTableStructureChanged();

			}
		});
	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				_bodies = bodies;
				fireTableStructureChanged();

			}
		});
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGravityLawsChanged(String gLawsDesc) {
		// TODO Auto-generated method stub

	}
}
