package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
@SuppressWarnings("serial")
public class RoadsTableModel extends AbstractTableModel implements TrafficSimObserver{
	
	private List<Road> _roads;
	private static String[] columns = { "Id", "Lenght", "Weather", "Max.Speed", "Speed Limit", "Total CO2", "CO2 Limit"};
	
	public RoadsTableModel(Controller _ctrl) {
		_roads = new ArrayList<>();
		_ctrl.addObserver(this);
	}
	
	public int getColumnCount() {
		return columns.length;
	}
	
	public String getColumnName(int column) {
		return columns[column];
	}
	@Override
	public int getRowCount() {
		return _roads.size();
	}
	
	public Object getValueAt(int row, int colum) {
		Object value = null;
		switch(colum) {
		case 0:
			value = _roads.get(row).getId();
			break;
		case 1:
			value = _roads.get(row).getLength();
			break;
		case 2:
			value = _roads.get(row).getWeather();
			break;
		case 3:
			value = _roads.get(row).getMaxSpeed();
			break;
		case 4:
			value = _roads.get(row).getSpeedLimit();
			break;
		case 5:
			value = _roads.get(row).getTotalCO2();
			break;
		case 6:
			value = _roads.get(row).getCO2Limit();
			break;
		}
		return value;
	}

	public void update() {
		fireTableStructureChanged();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		_roads = map.getRoads();
		update();
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		_roads = map.getRoads();
		update();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		_roads = map.getRoads();
		update();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		_roads = map.getRoads();
		update();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		_roads = map.getRoads();
		update();
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}
	

}
