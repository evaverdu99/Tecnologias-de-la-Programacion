package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public abstract class TrafficTableModel extends AbstractTableModel implements TrafficSimObserver{
	
	private static final long serialVersionUID = 1L;
	
	protected List<Road> _roads;
	protected List<Event> _events;
	protected List<Junction> _junctions;
	protected List<Vehicle> _vehicles;
	private String[] columns;
	
	public TrafficTableModel(Controller _ctrl, String[] columns) {
		this.columns = columns;
		_ctrl.addObserver(this);
	}

	abstract public int getRowCount();
	abstract public Object getValueAt(int arg0, int arg1);
	
	@Override
	public int getColumnCount() {
		return columns.length;
	}

	public String getColumnName(int column) {
		return columns[column];
	}
	
	
	public void update() {
		fireTableStructureChanged();
	}
	
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		_roads = map.getRoads();
		_vehicles = map.getVehicles();
		_events = events;
		_junctions = map.getJunctions();
		update();
	}
	
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		_roads = map.getRoads();
		_vehicles = map.getVehicles();
		_events = events;
		_junctions = map.getJunctions();
		update();
	}
	
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		_roads = map.getRoads();
		_vehicles = map.getVehicles();
		_events = events;
		_junctions = map.getJunctions();
		update();
	}
	
	public void onReset(RoadMap map, List<Event> events, int time) {
		_roads = map.getRoads();
		_vehicles = map.getVehicles();
		_events = events;
		_junctions = map.getJunctions();
		update();
	}
	
	public void onRegister(RoadMap map, List<Event> events, int time) {
		_roads = map.getRoads();
		_vehicles = map.getVehicles();
		_events = events;
		_junctions = map.getJunctions();
		update();
	}
	
	public void onError(String err) {
	}
	
	



}
