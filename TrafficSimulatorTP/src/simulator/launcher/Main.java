package simulator.launcher;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import simulator.control.Controller;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.Factory;
import simulator.factories.MostCrowdedStrategyBuilder;
import simulator.factories.MoveAllStrategyBuilder;
import simulator.factories.MoveFirstStrategyBuilder;
import simulator.factories.NewCityRoadEventBuilder;
import simulator.factories.NewInterCityRoadEventBuilder;
import simulator.factories.NewJunctionEventBuilder;
import simulator.factories.NewVehicleEventBuilder;
import simulator.factories.RoundRobinStrategyBuilder;
import simulator.factories.SetContClassEventBuilder;
import simulator.factories.SetWeatherEventBuilder;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;

import simulator.model.LightSwitchingStrategy;
import simulator.model.TrafficSimulator;
import simulator.view.MainWindow;

public class Main {

	private static Integer _time = null;
	private final static Integer _timeLimitDefaultValue = 10;
	private static String _inFile = null;
	private static String _outFile = null;
	private static String _mode = null;
	private static Factory<Event> _eventsFactory = null;
	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseModeOption(line);
			parseInFileOption(line);
			parseOutFileOption(line);
			parseTimeOption(line);
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

		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());
		cmdLineOptions.addOption(Option.builder("t").longOpt("ticks").hasArg().desc("Ticks to the simulator's main loop (default value is 10).").build());
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Modo de ejecución.").build());
		
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
		if (_inFile == null && _mode.equals("console")) {
			throw new ParseException("An events file is missing");
		}
	}
	
	private static void parseTimeOption(CommandLine line) throws ParseException {
		String time = line.getOptionValue("t", _timeLimitDefaultValue.toString());
		_time = Integer.parseInt(time);
		if(_time < 0) {
			throw new ParseException("Invalid time argument: " + time);
		}
		
	}
	
	private static void parseOutFileOption(CommandLine line) throws ParseException {
		_outFile = line.getOptionValue("o");
	}
	
	private static void parseModeOption(CommandLine line) throws ParseException {
		_mode = line.getOptionValue("m");
		if (_mode == null) {
			throw new ParseException("The ejecution mode is missing");
		}
	}

	private static void initFactories() {
		List<Builder<LightSwitchingStrategy>> lsbs = new ArrayList<>();
		lsbs.add( new RoundRobinStrategyBuilder());
		lsbs.add( new MostCrowdedStrategyBuilder());
		Factory<LightSwitchingStrategy> lssFactory = new BuilderBasedFactory<>(lsbs);

		List<Builder<DequeuingStrategy>> dqbs = new ArrayList<>();
		dqbs.add( new MoveFirstStrategyBuilder() );
		dqbs.add( new MoveAllStrategyBuilder() );
		Factory<DequeuingStrategy> dqsFactory = new BuilderBasedFactory<>(dqbs);
		
		List<Builder<Event>> event = new ArrayList<>();
		event.add(new NewJunctionEventBuilder(lssFactory,dqsFactory));
		event.add(new NewInterCityRoadEventBuilder());
		event.add(new NewCityRoadEventBuilder());
		event.add(new NewVehicleEventBuilder());
		event.add(new SetWeatherEventBuilder());
		event.add(new SetContClassEventBuilder());
		_eventsFactory = new BuilderBasedFactory<>(event);
		
	}
	
	private static void startBatchMode() throws IOException {
		InputStream in = new FileInputStream(_inFile);
		OutputStream out;
		if(_outFile == null) {
			out = System.out;
		}
		else {
			out = new FileOutputStream(_outFile);
		}
		TrafficSimulator simulator = new TrafficSimulator();
		Controller controller = new Controller(simulator,_eventsFactory);
		controller.loadEvents(in);
		controller.run(_time, out);
		out.close();
		in.close();
	}
	
	private static void startGUIMode() throws IOException{
		TrafficSimulator simulator = new TrafficSimulator();
		Controller ctrl = new Controller(simulator,_eventsFactory);
		
		if(_inFile != null) {
			ctrl.loadEvents(new FileInputStream(_inFile));
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainWindow(ctrl);
			}
		});
		
	}

	private static void start(String[] args) throws IOException {
		initFactories();
		parseArgs(args);
		if (_mode.equals("console")) {
			startBatchMode();
		}
		else {
			startGUIMode();
		}
	}

	// example command lines:
	
	// -m gui  -i resources/examples/ex1.json
	// -m console -i resources/examples/ex1.json
	// -m console -i resources/examples/ex1.json -t 300
	// -m console -i resources/examples/ex1.json -t 300 -o resources/tmp/ex1out.json
	// 
	// --help
	
	public static void main(String[] args) {
		try {
			start(args);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
