package simulator.view;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class MainWindow extends JFrame implements SimulatorObserver {

	private static final long serialVersionUID = 1L;

	// añadir atributos para componentes
	private ControlPanel cp;
	private BodiesTable bt;
	private Viewer vw;
	private StatusBar sb;

	private Controller _ctrl;

	/* constructora */
	public MainWindow(Controller ctrl) {
		super("Physics Simulator");
		this._ctrl = ctrl;
		this.initGUI();
		this._ctrl.addObserver(this);
	}

	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);

		/* panel de control al principio de la ventana */
		JPanel panelControl = new JPanel();
		this.cp = new ControlPanel(this._ctrl);
		panelControl.add(this.cp);
		this.add(panelControl, BorderLayout.PAGE_START);

		/* panel con tabla de cuerpos y viewer */
		JPanel panelView = new JPanel();
		panelView.setLayout(new BoxLayout(panelView, BoxLayout.Y_AXIS));
		this.bt = new BodiesTable(this._ctrl);
		this.vw = new Viewer(this._ctrl);
		panelView.add(this.bt);
		panelView.add(this.vw);
		this.add(panelView, BorderLayout.CENTER);

		/* barra de estado al final de la ventana */
		JPanel panelStatus = new JPanel();
		this.sb = new StatusBar(this._ctrl);
		panelStatus.add(this.sb);
		this.add(panelStatus, BorderLayout.PAGE_END);

		/* visibilidad */
		this.pack();
		this.setVisible(true);
	}

	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGravityLawsChanged(String gLawsDesc) {
		// TODO Auto-generated method stub

	}

	// añade cosas
}
