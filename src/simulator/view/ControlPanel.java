package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class ControlPanel extends JPanel implements SimulatorObserver, ActionListener {

	private static final long serialVersionUID = 1L;

	private Controller _ctrl;
	volatile Thread _thread;

	JFileChooser fc;
	InputStream iFile;

	/* botones */
	JButton bOpen;
	JButton bPhysics;
	JButton bPlay;
	JButton bStop;
	JButton bExit;

	JLabel lDelay = this.createLabel("Delay: ");
	Long val = 1L;
	Long min = 0L;
	Long max = 1000L;
	Long step = 1L;
	JSpinner dSpinner = new JSpinner(new SpinnerNumberModel(val, min, max, step));

	JLabel lSteps = this.createLabel("Steps: ");
	JLabel lDeltaTime = this.createLabel("Delta-Time: ");

	JSpinner spinner = new JSpinner(new SpinnerNumberModel(150, 1, 100000, 1));
	JTextField tfDeltaTime = new JTextField("0", 6);

	public ControlPanel(Controller ctrl) {
		this._ctrl = ctrl;
		this.initGUI();
		this._ctrl.addObserver(this);
	}

	private void initGUI() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setLayout(new GridLayout(1, 1));
		this.setBorder(BorderFactory.createBevelBorder(1));

		JToolBar toolBar = new JToolBar();

		/*----------------------------------------------------abrir----------------------------------------------------*/
		bOpen = new JButton();
		bOpen.setActionCommand("abrir");
		bOpen.setIcon(new ImageIcon("resources/icons/open.png"));
		bOpen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fc = new JFileChooser();
				fc.setCurrentDirectory(new File("resources/"));
				fc.setFileFilter(new FileNameExtensionFilter("tipos", "txt"));
				int resultado = fc.showOpenDialog(getParent());
				if (resultado == JFileChooser.APPROVE_OPTION) {
					try {
						iFile = new FileInputStream(fc.getSelectedFile());
						_ctrl.reset();
						_ctrl.loadBodies(iFile);
					} catch (FileNotFoundException e1) {
						JOptionPane.showMessageDialog(getParent(), "error", "Error", JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
					}
				}

			}
		});
		toolBar.add(bOpen);
		toolBar.addSeparator();

		/*----------physics-----------*/
		bPhysics = new JButton();
		bPhysics.setActionCommand("physics");
		bPhysics.setIcon(new ImageIcon("resources/icons/physics.png"));
		bPhysics.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String[] possibilities = { "Newton's Law of Universal Gravitation (nlug)",
						"Falling to Center Gravity (ftcg)", "No Gravity (ng)" };

				String option = (String) JOptionPane.showInputDialog(null, "Select gravity laws to be used",
						"Gravity Laws Selector", JOptionPane.PLAIN_MESSAGE, null, possibilities, possibilities[0]);

				if (option.equalsIgnoreCase("Newton's Law of Universal Gravitation (nlug)")) {
					_ctrl.setGravityLaws(_ctrl.getGravityLawsFactory().getInfo().get(0));
				} else if (option.equalsIgnoreCase("Falling to Center Gravity (ftcg)")) {
					_ctrl.setGravityLaws(_ctrl.getGravityLawsFactory().getInfo().get(1));
				} else if (option.equalsIgnoreCase("No Gravity (ng)")) {
					_ctrl.setGravityLaws(_ctrl.getGravityLawsFactory().getInfo().get(2));
				} else {
					_ctrl.setGravityLaws(_ctrl.getGravityLawsFactory().getInfo().get(0));
				}
			}
		});
		toolBar.add(bPhysics);
		toolBar.addSeparator();

		/*-------------------play-------------------*/
		bPlay = new JButton();
		bPlay.setActionCommand("iniciar");
		bPlay.setIcon(new ImageIcon("resources/icons/run.png"));
		bPlay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				bOpen.setEnabled(false);
				bExit.setEnabled(false);
				bPhysics.setEnabled(false);
				bPlay.setEnabled(false);

				_ctrl.setDeltaTime(Double.parseDouble(tfDeltaTime.getText()));

				_thread = new Thread(new Runnable() {

					@Override
					public void run() {
						run_sim((int) spinner.getValue(), (Long) dSpinner.getValue());

						bOpen.setEnabled(true);
						bExit.setEnabled(true);
						bPhysics.setEnabled(true);
						bPlay.setEnabled(true);

						_thread = null;
					}
				});
				_thread.start();

			}
		});
		toolBar.add(bPlay);

		/*--------------------stop---------------------*/
		bStop = new JButton();
		bStop.setActionCommand("parar");
		bStop.setIcon(new ImageIcon("resources/icons/stop.png"));
		bStop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (_thread != null) {
					_thread.interrupt();
				}

			}
		});
		toolBar.add(bStop);
		toolBar.addSeparator();

		/*-------------------delay------------------------*/
		toolBar.add(lDelay);
		toolBar.addSeparator();
		toolBar.add(dSpinner);
		toolBar.addSeparator();

		/*----------------------------------------------------steps----------------------------------------------------*/
		toolBar.add(lSteps);
		toolBar.addSeparator();
		toolBar.add(spinner);
		toolBar.addSeparator();

		/*----------delta-time-----------*/
		toolBar.add(lDeltaTime);
		toolBar.addSeparator();
		tfDeltaTime.setSize(15, 5);
		toolBar.add(tfDeltaTime);
		toolBar.addSeparator();

		toolBar.addSeparator(new Dimension(350, 1));
		/*----------exit-----------*/
		bExit = new JButton();
		bExit.setActionCommand("salir");
		bExit.setIcon(new ImageIcon("resources/icons/exit.png"));
		bExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int option = JOptionPane.showConfirmDialog(null, "Realmente desea salir de la aplicación?",
						"Confirmar salida", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (option == 0) {
					System.exit(0);
				}

			}
		});
		toolBar.add(bExit);

		this.add(toolBar);
	}

	private JLabel createLabel(String s) {
		JLabel label = new JLabel(s);
		return label;
	}

	private void run_sim(int n, long delay) {

		while (n > 0 && !this._thread.isInterrupted()) {
			try {
				_ctrl.run(1);
			} catch (Exception e) {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						JOptionPane.showMessageDialog(getParent(), "Ha ocurrido algún error", "ERROR",
								JOptionPane.ERROR_MESSAGE);
					}
				});

				this.bOpen.setEnabled(true);
				this.bExit.setEnabled(true);
				this.bPhysics.setEnabled(true);
				this.bPlay.setEnabled(true);
				return;
			}

			if (this._thread.isAlive()) {
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			n--;
		}
	}

	// SimulatorObserver methods
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				tfDeltaTime.setText("" + dt);

			}
		});
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				tfDeltaTime.setText("" + dt);

			}
		});
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				tfDeltaTime.setText("" + dt);

			}
		});

	}

	@Override
	public void onGravityLawsChanged(String gLawsDesc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
