package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable<TrafficSimObserver> {
	private RoadMap map;
	private List<Event> events;
	private List<TrafficSimObserver> observers;
	private int time;
	public TrafficSimulator() {
		events = new SortedArrayList<>();
		map = new RoadMap();
		time = 0;
		observers = new ArrayList<>();
	}
	
	public void addEvent(Event e) {
		events.add(e);
		onEventAdded(e);
	}
	
	public void advance() {
		try {
			this.time++;
			boolean notime = true;
			onAdvanceStart();
			while( events.size() > 0 && notime) {
				if(events.get(0)._time == this.time) {
					events.get(0).execute(map);
					events.remove(0);
				}
				else {
					notime = false;
				}
			}
			for(int i = 0; i < map.getJunctions().size(); i++) {
				map.getJunctions().get(i).advance(this.time);
			}
			for(int i = 0; i < map.getRoads().size(); i++) {
				map.getRoads().get(i).advance(this.time);
			}
			onAdvanceEnd();
		}catch(Exception e) {
			String m = e.getMessage();
			onError(m);
			throw e;
		}
	}
	
	public void reset() {
		map.reset();
		events.clear();
		this.time = 0;
		onReset();
	}
	
	public JSONObject report(){
		JSONObject jo = new JSONObject();
		jo.put("time", this.time);
		jo.put("state", map.report());
		return jo;
	}
	
	@Override
	public void addObserver(TrafficSimObserver o) {
		if(!observers.contains(o)) {
			observers.add(o);
			o.onRegister(map, events, time);
		}
		
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		if(observers.contains(o)) {
			observers.remove(o);
		}
		
	}
	public void onAdvanceStart() {
		for(int i = 0; i < observers.size(); ++i) {
			observers.get(i).onAdvanceStart(map, events, time);
		}
			
	}
	public void onAdvanceEnd() {
		for(int i = 0; i < observers.size(); ++i) {
			observers.get(i).onAdvanceEnd(map, events, time);
		}
	}
	public void onEventAdded(Event e) {
		for(int i = 0; i < observers.size(); ++i) {
			observers.get(i).onEventAdded(map, events, e, time);
		}
	}
	public void onReset() {
		for(int i = 0; i < observers.size(); ++i) {
			observers.get(i).onReset(map, events, time);
		}
	}
	public void onRegister() {
		for(int i = 0; i < observers.size(); ++i) {
			observers.get(i).onRegister(map, events, time);
		}
	}
	public void onError(String m) {
		for(int i = 0; i < observers.size(); ++i) {
			observers.get(i).onError(m);
		}
	}
}
