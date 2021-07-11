package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

@SuppressWarnings("serial")
public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver{
	
	private List<Event> _events;
	private static String[] columns = { "Time", "Desc."};
	
	public EventsTableModel(Controller _ctrl) {
		_events = new ArrayList<>();
		_ctrl.addObserver(this);
	}
	
	public int getColumnCount() {
		return columns.length;
		
	}
	
	public String getColumnName(int column) {
		return columns[column];
	}
	
	public int getRowCount() {
		return _events.size();
	}
	
	public Object getValueAt(int row, int colum) {
		Object value = null;
		switch(colum) {
		case 0:
			value = _events.get(row).getTime();
			break;
		case 1:
			value = _events.get(row).getDescription();
			break;
		}
		return value;
	}
	
	public void update() {
		fireTableStructureChanged();
	}
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		_events = events;
		update();
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		_events = events;
		update();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		_events = events;
		update();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		_events = events;
		update();	
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		_events = events;
		update();
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}
	

}
