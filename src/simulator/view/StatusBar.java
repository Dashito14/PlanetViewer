package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class StatusBar extends JPanel implements SimulatorObserver {

	private static final long serialVersionUID = 1L;

	private Controller _ctrl;

	private JLabel _currTime; // for current time
	private JLabel _currLaws; // for gravity laws
	private JLabel _numOfBodies; // for num of bodies

	public StatusBar(Controller ctrl) {
		this._ctrl = ctrl;
		initGUI();
		this._ctrl.addObserver(this);
	}

	private void initGUI() {

		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setLayout(new GridLayout(1, 3));
		this.setBorder(BorderFactory.createBevelBorder(1));

		this._currTime = new JLabel("Time: " + this._ctrl.getPhysics().getRealTime()); // for current time
		this._numOfBodies = new JLabel("Bodies: " + this._ctrl.getPhysics().getList().size()); // for num of bodies
		this._currLaws = new JLabel("Laws: " + this._ctrl.getPhysics().getGravityLaws().toString()); // for gravity laws
		this._currLaws.setPreferredSize(new Dimension(300, 20));

		this.add(this._currTime);
		this.add(this._numOfBodies);
		this.add(this._currLaws);
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
				_currTime.setText("Time: " + time);
				_numOfBodies.setText("Bodies: " + bodies.size());
				_currLaws.setText("Laws: " + gLawsDesc);

			}
		});
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				_currTime.setText("Time: " + time);
				_numOfBodies.setText("Bodies: " + bodies.size());
				_currLaws.setText("Laws: " + gLawsDesc);

			}
		});
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				_numOfBodies.setText("Bodies: " + bodies.size());
			}
		});
	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				_currTime.setText("Time: " + time);
				_numOfBodies.setText("Bodies: " + bodies.size());
			}
		});
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				_currTime.setText("Time: " + dt);
			}
		});
	}

	@Override
	public void onGravityLawsChanged(String gLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				_currLaws.setText("Laws: " + gLawsDesc);
			}
		});
	}

}
