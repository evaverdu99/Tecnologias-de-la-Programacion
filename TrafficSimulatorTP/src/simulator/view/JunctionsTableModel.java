package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

@SuppressWarnings("serial")
public class JunctionsTableModel extends AbstractTableModel implements TrafficSimObserver{
	
	private List<Junction> _junctions;
	private static String[] columns = { "Id", "Green", "Queues"};
	
	public JunctionsTableModel(Controller _ctrl) {
		_junctions = new ArrayList<>();
		_ctrl.addObserver(this);
	}
	
	public int getColumnCount() {
		return columns.length;
	}
	
	public String getColumnName(int column) {
		return columns[column];
	}
	
	public int getRowCount() {
		return _junctions.size();
	}

	@Override
	public Object getValueAt(int row, int colum) {
		Object value = null;
		switch(colum) {
		case 0:
			value = _junctions.get(row).getId();
			break;
		case 1:
			value = _junctions.get(row).getGreenLightId(); 
			break;
		case 2:
			value = _junctions.get(row).getRoadcolas();  
			break;
			
		}
		return value;
	}
	
	public void update() {
		fireTableStructureChanged();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		_junctions = map.getJunctions();
		update();
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		_junctions = map.getJunctions();
		update();
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		_junctions = map.getJunctions();
		update();
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		_junctions = map.getJunctions();
		update();
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		_junctions = map.getJunctions();
		update();
		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

	
}
