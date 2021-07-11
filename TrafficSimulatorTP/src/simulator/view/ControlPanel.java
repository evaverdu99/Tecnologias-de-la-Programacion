package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.awt.Frame;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.SetContClassEvent;
import simulator.model.SetWeatherEvent;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.Weather;

@SuppressWarnings("serial")
public class ControlPanel extends JPanel implements TrafficSimObserver{
	private Controller _ctrl;
	private JToolBar _toolBar;
	private JButton _loadB, _contB, _weatherB, _runB, _stopB, _exitB;
	private JFileChooser _fileChooser;
	private JSpinner _tickSpinner;
	private List<Road> rlist;
	private List<Vehicle> vlist;
	private boolean _stopped;
	private int time;
	private File file;

	public ControlPanel(Controller _ctrl) {
		this._ctrl = _ctrl;
		this.rlist = new ArrayList<Road>();
		this.vlist = new ArrayList<Vehicle>();
		initGUI();
		_ctrl.addObserver(this);
		
	}

	private void initGUI() {

		setLayout(new BorderLayout());
		_toolBar = new JToolBar();
		add(_toolBar);
		

	//Creamos los componentes del ToolBar:
		_fileChooser = new JFileChooser();
		_loadB = new JButton();
		_contB = new JButton();
		_weatherB = new JButton();
		_runB = new JButton();
		_stopB = new JButton();
		_exitB = new JButton();
		_tickSpinner = new JSpinner();
		
	//Componente FileChooser:
		_fileChooser.setCurrentDirectory(new File("C:\\Users\\evavr\\eclipse-workspace\\V2TrafficSimulatorTP\\resources\\examples"));
		_fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		_loadB.setToolTipText("Carga componentes de un json");
		_loadB.setIcon(new ImageIcon ("resources/icons/open.png"));
		_loadB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cargajson();
			}
		});
		
		_toolBar.add(_loadB);
		_toolBar.addSeparator();
		
	//Botón cambiar contaminación:
		_contB.setToolTipText("Cambia la contaminación de una carretera."); 
		_contB.setIcon(new ImageIcon ("resources/icons/co2class.png"));
		_contB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeCO2Class();
			}
		});
		
		_toolBar.add(_contB);
		
	//Botón cambiar tiempo:
		_weatherB.setToolTipText("Cambia el tiempo de una carretera.");
		_weatherB.setIcon(new ImageIcon ("resources/icons/weather.png"));
		_weatherB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 changeWeather();
			}
		});
		
		_toolBar.add(_weatherB);
		_toolBar.addSeparator();
		
	//Boton de ejecucion:
		_runB.setToolTipText("Inicia la ejecución del programa.");
		_runB.setIcon(new ImageIcon ("resources/icons/run.png"));
		_runB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enableToolBar(false);
				_stopped = false;
				int a = (int)_tickSpinner.getValue();
				if(a > 0) {
					run_sim(a);
					_tickSpinner.resetKeyboardActions();
				}
				else {
					JOptionPane.showMessageDialog(null, "No se ha introducido correctamente el numero de ticks.", "ERROR", JOptionPane.ERROR_MESSAGE);
					enableToolBar(true);
				}
				
			}
		});
		
		_toolBar.add(_runB);
		
	//Boton de pausa:
		_stopB.setToolTipText("Pausa la ejecución del programa.");
		_stopB.setIcon(new ImageIcon ("resources/icons/stop.png"));
		_stopB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stop();
				enableToolBar(true);
			}
		});
		
		_toolBar.add(_stopB);
		
	//Ticks de ejecucion:
		_tickSpinner = new JSpinner();
		_tickSpinner.setToolTipText("Indica el numero de pasos a ejecutar");
		_tickSpinner.setMaximumSize(new Dimension(60, 40));
		_tickSpinner.setMinimumSize(new Dimension(60, 40));
		_tickSpinner.setPreferredSize(new Dimension(60, 40));
		_tickSpinner.setValue(1);
		_toolBar.add(new JLabel(" Ticks:"));
		_toolBar.addSeparator();
		_toolBar.add(_tickSpinner);
		
		_toolBar.add(Box.createGlue());
		_toolBar.addSeparator();
		
		
		
		
		
	//Boton exit:
		_exitB.setToolTipText("Exit");
		_exitB.setIcon(new ImageIcon ("resources/icons/exit.png"));
		_exitB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exit();
			}
		});
		_toolBar.addSeparator();
		_toolBar.add(_exitB);
	}
	
	private void update(RoadMap map, int time) {
		rlist = map.getRoads();
		vlist = map.getVehicles();
		this.time = time;
	}

	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		update(map, time);
	}

	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map, time);
	}

	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map, time);
	}

	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map, time);
	}

	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map, time);
	}

	public void onError(String err) {
		JOptionPane.showMessageDialog(null, err, "ERROR", JOptionPane.ERROR_MESSAGE);
	}
	
	
	private void changeCO2Class() {
		ChangeCO2ClassDialog ChangeCO = new ChangeCO2ClassDialog((Frame)SwingUtilities.getWindowAncestor(this));
		int status = ChangeCO.open(vlist);
		if(status == 1) {
			List<Pair<String,Integer>> info = new ArrayList<Pair<String,Integer>>();
			Pair <String,Integer> e = new Pair<>(ChangeCO.getVehicle(),ChangeCO.getCOClass());
			info.add(e);
		
			if(info.size() == 0) {
				JOptionPane.showMessageDialog(this.getParent(), "Error, no has seleccionado ningun vehiculo", "Mensaje de error", JOptionPane.ERROR_MESSAGE);
			}
			else {
				_ctrl.addEvent(new SetContClassEvent(this.time+ChangeCO.getTicks(), info));
			}
		}
	}
	
	private void changeWeather() {
		ChangeWeatherDialog Changewea = new ChangeWeatherDialog((Frame)SwingUtilities.getWindowAncestor(this));
		int status = Changewea.open(rlist);
		if(status == 1) {
			List<Pair<String,Weather>> info = new ArrayList<Pair<String,Weather>>();
			Pair <String,Weather> e = new Pair<>(Changewea.getObject(),Changewea.getObject2());
			info.add(e);

			if(info.size() == 0) {
				JOptionPane.showMessageDialog(this.getParent(), "Error, no has seleccionado ninguna carretera", "Mensaje de error", JOptionPane.ERROR_MESSAGE);
			}
			else {
				_ctrl.addEvent(new SetWeatherEvent( this.time + Changewea.getTicks(), info));
			}
		
		}
	}
	
	private void cargajson() {
		int value = _fileChooser.showOpenDialog(this.getParent());
		if(value == JFileChooser.APPROVE_OPTION) {
			this.file = _fileChooser.getSelectedFile();
			try {
				_ctrl.reset();
				_ctrl.loadEvents(new FileInputStream(this.file));
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this.getParent(), "Error al cargar el archivo", "Mensaje de error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	protected void exit() {
		int option = JOptionPane.showConfirmDialog(null, "Seguro que desea salir?", "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if(option == 0) {
			System.exit(0);
		}
	}
	
	private void run_sim(int n) {
		
		if (n > 0 && !_stopped) {
			try {
				_ctrl.run(1);
			} catch (Exception e) {
				_stopped = true;
				return;
			}
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					run_sim(n - 1);
				}
			});
		} else {
			enableToolBar(true);
			_stopped = true;
		}
		
	}
	
	private void enableToolBar(boolean b) {
		_fileChooser.setEnabled(b);
		_loadB.setEnabled(b);
		_contB.setEnabled(b);
		_weatherB.setEnabled(b);
		_runB.setEnabled(b);
		_exitB.setEnabled(b);
		_tickSpinner.setEnabled(b);
	}

	private void stop() {
		_stopped = true;
	}
}
