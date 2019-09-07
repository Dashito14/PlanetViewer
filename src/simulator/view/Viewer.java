package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import simulator.control.Controller;
import simulator.misc.Vector;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class Viewer extends JComponent implements SimulatorObserver {
	private static final long serialVersionUID = 1L;

	/*
	 * private static final int _WIDTH = 1000; private static final int _HEIGHT =
	 * 1000;
	 */
	private static final int _radius = 5;

	private int _centerX;
	private int _centerY;
	private double _scale;
	private List<Body> _bodies;
	private boolean _showHelp;

	public Viewer(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
	}

	private void initGUI() {
		setPreferredSize(new Dimension(getWidth(), 250));
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "Viewer",
				TitledBorder.LEFT, TitledBorder.TOP));

		this._bodies = new ArrayList<>();
		this._scale = 1.0;
		this._showHelp = true;

		addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyChar()) {
				case '-':
					_scale = _scale * 1.1;
					break;
				case '+':
					_scale = Math.max(1000.0, _scale / 1.1);
					break;
				case '=':
					autoScale();
					break;
				case 'h':
					_showHelp = !_showHelp;
					break;
				default:
					break;
				}
				repaint();
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});

		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				requestFocus();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

	}

	/* paintComponent */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D gr = (Graphics2D) g;
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		_centerX = getWidth() / 2;
		_centerY = getHeight() / 2;

		/* red cross */
		gr.setColor(Color.RED);
		gr.drawLine(this._centerX, (this._centerY - _radius), this._centerX, (this._centerY + _radius)); // vertical
		gr.drawLine((this._centerX - _radius), this._centerY, (this._centerX + _radius), this._centerY); // horizontal

		/* bodies */
		for (int i = 0; i < this._bodies.size(); i++) {
			gr.setColor(Color.BLACK);
			gr.drawString(this._bodies.get(i).getId(),
					_centerX + (int) (this._bodies.get(i).getPosition().coordinate(0) / _scale) - 4,
					_centerY - (int) (this._bodies.get(i).getPosition().coordinate(1) / _scale) - 2);
			gr.setColor(Color.BLUE);
			gr.fillOval(_centerX + (int) (this._bodies.get(i).getPosition().coordinate(0) / _scale),
					_centerY - (int) (this._bodies.get(i).getPosition().coordinate(1) / _scale), _radius, _radius);
		}

		/* help message */
		if (this._showHelp) {
			gr.setColor(Color.RED);
			gr.drawString("h: toggle help, +: zoom-in, -: zoom-out, =: fit", 7, 25);
			gr.drawString("Scalling ratio: " + this._scale, 7, 40);
		}

	}

	/* autoScale */
	private void autoScale() {
		double max = 1.0;
		for (Body b : _bodies) {
			Vector p = b.getPosition();
			for (int i = 0; i < p.dim(); i++) {
				max = Math.max(max, Math.abs(b.getPosition().coordinate(i)));
			}
		}
		double size = Math.max(1.0, Math.min((double) getWidth(), (double) getHeight()));
		_scale = max > size ? 4.0 * max / size : 1.0;
	}

	// Métodos encargados de notificar cambios y actualiza así la interfaz
	// consecuentemente.
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				_bodies = bodies;
				autoScale();
				repaint();

			}
		});
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				_bodies = bodies;
				autoScale();
				repaint();

			}
		});
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				_bodies = bodies;
				autoScale();
				repaint();

			}
		});
	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				_bodies = bodies;
				repaint();

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
