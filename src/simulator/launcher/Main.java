package simulator.launcher;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

/*
 * Examples of command-line parameters:
 * 
 *  -h
 *  -i resources/examples/ex4.4body.txt -s 100
 *  -i resources/examples/ex4.4body.txt -o resources/examples/ex4.4body.out -s 100
 *  -i resources/examples/ex4.4body.txt -o resources/examples/ex4.4body.out -s 100 -gl ftcg
 *  -i resources/examples/ex4.4body.txt -o resources/examples/ex4.4body.out -s 100 -gl nlug
 *
 */

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONObject;

import simulator.control.Controller;
import simulator.factories.Factory;
import simulator.factories.FallingToCenterGravityBuilder;
import simulator.factories.MassLossingBodyBuilder;
import simulator.factories.NewtonUniversalGravitationBuilder;
import simulator.factories.NoGravityBuilder;
import simulator.factories.BasicBodyBuilder;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.model.Body;
import simulator.model.GravityLaws;
import simulator.model.PhysicsSimulator;
import simulator.view.MainWindow;

public class Main {

	// default values for some parameters
	//
	private final static Double _dtimeDefaultValue = 2500.0;
	private final static int _stepsDefaultValue = 150;

	// some attributes to stores values corresponding to command-line parameters
	//
	private static Double _dtime = null;
	private static String _inFile = null;
	private static String _outFile = null;
	private static JSONObject _gravityLawsInfo = null;
	private static int _steps = 0;
	private static String _mode = null;

	private static Controller controller;
	private static PhysicsSimulator phy;

	// factories
	private static Factory<Body> _bodyFactory;
	private static Factory<GravityLaws> _gravityLawsFactory;

	/* init: crea e inicializa las factorias */
	private static void init() {

		// initialize the bodies factory
		ArrayList<Builder<Body>> bodyBuilders = new ArrayList<>();
		bodyBuilders.add(new BasicBodyBuilder());
		bodyBuilders.add(new MassLossingBodyBuilder());
		_bodyFactory = new BuilderBasedFactory<Body>(bodyBuilders);

		// initialize the gravity laws factory
		ArrayList<Builder<GravityLaws>> glBuilders = new ArrayList<>();
		glBuilders.add(new NewtonUniversalGravitationBuilder());
		glBuilders.add(new FallingToCenterGravityBuilder());
		glBuilders.add(new NoGravityBuilder());
		_gravityLawsFactory = new BuilderBasedFactory<GravityLaws>(glBuilders);
	}

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseModeOption(line);
			parseHelpOption(line, cmdLineOptions);
			parseInFileOption(line);
			parseDeltaTimeOption(line);
			parseGravityLawsOption(line);
			parseOutputOption(line);
			parseStepsOption(line);
			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		// help
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message.").build());

		// input file
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Bodies JSON input file.").build());

		// delta-time
		cmdLineOptions.addOption(Option.builder("dt").longOpt("delta-time").hasArg()
				.desc("A double representing actual time, in seconds, per simulation step. Default value: "
						+ _dtimeDefaultValue + ".")
				.build());

		/* nuevas opciones introducidas de Output y Steps */
		// output file
		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg()
				.desc("Output file, where output is written. Default value:" + " the standard output.").build());

		// steps
		cmdLineOptions.addOption(Option.builder("s").longOpt("steps").hasArg().desc(
				"An integer representing the number of simulation steps. Default value: " + _stepsDefaultValue + ".")
				.build());

		// Practica 5. Modo
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc(
				"Execution Mode. Possible values: 'batch' (Batch Mode), 'gui' (Graphical User Interface mode). Default value: 'batch'.")
				.build());

		// gravity laws -- there is a workaround to make it work even when
		// _gravityLawsFactory is null.
		//
		String gravityLawsValues = "N/A";
		String defaultGravityLawsValue = "N/A";
		if (_gravityLawsFactory != null) {
			gravityLawsValues = "";
			for (JSONObject fe : _gravityLawsFactory.getInfo()) {
				if (gravityLawsValues.length() > 0) {
					gravityLawsValues = gravityLawsValues + ", ";
				}
				gravityLawsValues = gravityLawsValues + "'" + fe.getString("type") + "' (" + fe.getString("desc") + ")";
			}
			defaultGravityLawsValue = _gravityLawsFactory.getInfo().get(0).getString("type");
		}
		cmdLineOptions.addOption(Option.builder("gl").longOpt("gravity-laws").hasArg()
				.desc("Gravity laws to be used in the simulator. Possible values: " + gravityLawsValues
						+ ". Default value: '" + defaultGravityLawsValue + "'.")
				.build());

		return cmdLineOptions;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		if (_inFile == null) {
			throw new ParseException("An input file of bodies is required");
		}
	}

	private static void parseDeltaTimeOption(CommandLine line) throws ParseException {
		String dt = line.getOptionValue("dt", _dtimeDefaultValue.toString());
		try {
			_dtime = Double.parseDouble(dt);
			assert (_dtime > 0);
		} catch (Exception e) {
			throw new ParseException("Invalid delta-time value: " + dt);
		}
	}

	private static void parseGravityLawsOption(CommandLine line) throws ParseException {

		// this line is just a work around to make it work even when _gravityLawsFactory
		// is null, you can remove it when've defined _gravityLawsFactory
		if (_gravityLawsFactory == null)
			return;

		String gl = line.getOptionValue("gl");
		if (gl != null) {
			for (JSONObject fe : _gravityLawsFactory.getInfo()) {
				if (gl.equals(fe.getString("type"))) {
					_gravityLawsInfo = fe;
					break;
				}
			}
			if (_gravityLawsInfo == null) {
				throw new ParseException("Invalid gravity laws: " + gl);
			}
		} else {
			_gravityLawsInfo = _gravityLawsFactory.getInfo().get(0);
		}
	}

	/*
	 * parseOutputOption: se parsea el archivo destino especificado por el usuario.
	 */
	private static void parseOutputOption(CommandLine line) throws ParseException {
		if (!_mode.equalsIgnoreCase("gui")) {
			_outFile = line.getOptionValue("o");
			if (_outFile == null) {
				throw new ParseException("An output file of bodies is required");
			}
		}
	}

	/*
	 * parseStepsOption: se parsea el n�mero de pasos que el simulador a de
	 * ejecutar.
	 */
	private static void parseStepsOption(CommandLine line) throws ParseException {
		if (!_mode.equalsIgnoreCase("gui")) {
			String st = line.getOptionValue("s");
			try {
				int pasos = Integer.parseInt(st);
				if (pasos > 0) {
					_steps = pasos;
				} else
					_steps = _stepsDefaultValue;
			} catch (Exception e) {
				throw new ParseException("Invalid steps value: " + st);
			}
		}
	}

	/*
	 * parseModeOption: permite al usuario usar el simulador en modo BATCH y en modo
	 * GUI
	 */
	private static void parseModeOption(CommandLine Line) throws ParseException {
		_mode = Line.getOptionValue("m");
		try {
			if (_mode.equalsIgnoreCase("gui")) {
				_mode = "gui";
			} else {
				_mode = "batch";
			}
		} catch (Exception e) {
			throw new ParseException("Invalid mode: " + _mode);
		}

	}

	private static void startGUIMode() throws InvocationTargetException, InterruptedException, FileNotFoundException {
		phy = new PhysicsSimulator(_dtime, _gravityLawsFactory.createInstance(_gravityLawsInfo));
		controller = new Controller(phy, _bodyFactory, _gravityLawsFactory);

		if (_inFile != null) {
			controller.loadBodies(new FileInputStream(_inFile));
		}

		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				new MainWindow(controller);
			}
		});
	}

	/*
	 * startBatchMode: crea, instanciae/o inicializa todo lo que este involucrado
	 * con la simulacion.
	 */
	private static void startBatchMode() throws Exception {
		// create and connect components, then start the simulator
		int i = 0;
		boolean encontrado = false;
		while (!encontrado && _gravityLawsFactory.getInfo().size() > i) {
			if (_gravityLawsInfo.getString("type") == _gravityLawsFactory.getInfo().get(i).getString("type")) {
				encontrado = true;
			} else
				i++;
		}
		phy = new PhysicsSimulator(_dtime, _gravityLawsFactory.createInstance(_gravityLawsInfo));
		controller = new Controller(phy, _bodyFactory, _gravityLawsFactory);

		FileInputStream iFile = new FileInputStream(_inFile);
		FileOutputStream oFile = new FileOutputStream(_outFile);

		controller.loadBodies(iFile);
		controller.run(_steps, oFile);

		iFile.close();
		oFile.close();
	}

	private static void start(String[] args) throws Exception {
		parseArgs(args);
		if (_mode.equalsIgnoreCase("gui")) {
			startGUIMode();
		} else if (_mode.equalsIgnoreCase("batch")) {
			startBatchMode();
		}
	}

	public static void main(String[] args) {
		try {
			init();
			start(args);
		} catch (Exception e) {
			System.err.println("Something went wrong ...");
			System.err.println();
			e.printStackTrace();
		}
	}

}
