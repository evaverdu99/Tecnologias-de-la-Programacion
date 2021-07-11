package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

@SuppressWarnings("serial")
public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver {

	private List<Vehicle> _vehicles;
	private static String[] columns = { "Id", "Location", "Iterinary", "CO2 Class", "Max. Speed" ,"Speed","Total CO2", "Distance"};
	
	public VehiclesTableModel(Controller _ctrl) {
		_vehicles = new ArrayList<>();
		_ctrl.addObserver(this);
	}
	
	public int getColumnCount() {
		return columns.length;
	}
	
	public String getColumnName(int column) {
		return columns[column];
	}

	public int getRowCount() {
		return _vehicles.size();
	}
	@Override
	public Object getValueAt(int row, int colum) {
		Object value = null;
		switch(colum) {
		case 0:
			value = _vehicles.get(row).getId();
			break;
		case 1:
			value = _vehicles.get(row).getLocation();
			break;
		case 2:
			value = _vehicles.get(row).getItinerary();
			break;
		case 3:
			value = _vehicles.get(row).getContClas();
			break;
		case 4:
			value = _vehicles.get(row).getMaxSpeed();//CAMBIAR POR MAXSPEED
			break;
		case 5:
			value = _vehicles.get(row).getSpeed(); 
			break;
		case 6:
			value = _vehicles.get(row).getTotalCO2();//CAMBIAR POR TOTAL CO2
			break;
		case 7:
			value = _vehicles.get(row).getDistance();//CAMBIAR POR GET DISTANCE
			break;
		}
		return value;
	}
	
	public void update() {
		fireTableStructureChanged();
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		_vehicles = map.getVehicles();
		update();
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		_vehicles = map.getVehicles();
		update();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		_vehicles = map.getVehicles();
		update();
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		_vehicles = map.getVehicles();
		update();
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		_vehicles = map.getVehicles();
		update();
		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}
