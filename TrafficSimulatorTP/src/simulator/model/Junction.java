package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;


public class Junction extends SimulatedObject{
	private List <Road> roadin;
	private Map <Junction, Road> roadout;
	private List<List<Vehicle>> qs;
	private Map <Road,List<Vehicle>> roadcola;
	private int currGreen;
	private int lastSwitchingTime;
	private LightSwitchingStrategy lsStrategy;
	private DequeuingStrategy dqStrategy;
	private int xCoor;
	private int yCoor;
	
	Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		super(id);
		this._id = id;
		if(dqStrategy == null ||  lsStrategy == null  || xCoor <  0 ||  yCoor <  0  ) {
			throw new IllegalArgumentException("Parametro no valido");
		}
		else {
			this.roadin = new ArrayList<Road>();
			this.roadout = new HashMap<Junction, Road>();
			this.qs = new ArrayList<List<Vehicle>>();
			this.roadcola = new HashMap<Road, List<Vehicle>>();
			this.dqStrategy = dqStrategy;
			this.lsStrategy = lsStrategy;
			this.xCoor = xCoor;
			this.yCoor = yCoor;
			this.currGreen = -1;
		}
	}
	void addIncommingRoad(Road r) {
		if(r.destJunc.equals(this)) {
			roadin.add(r);
			List<Vehicle> cola = new LinkedList<Vehicle>();
			qs.add(cola);
			roadcola.put(r, cola);
		}
		else {
			throw new IllegalArgumentException("El cruce destino no coincide con el cruze actual");
		}
	}
	
	void addOutGoingRoad(Road r) {
		if(!roadout.containsKey(r.destJunc) && r.srcJunc.equals(this)) {
			roadout.put(r.destJunc, r);
		}
	}
	
	void enter(Road r,Vehicle v) {
		int i = roadin.indexOf(r);
		qs.get(i).add(v);
		
	}
	
	Road roadTo(Junction j) {
		Road road = roadout.get(j);
		return road;
	}
	
	void advance(int time) {
		List<Vehicle> l;
		int green = -1;
		if(roadin.size() != 0) {
			if(currGreen != -1 && !qs.get(currGreen).isEmpty()) {
				l = dqStrategy.dequeue(qs.get(currGreen));
				for (int j = 0; j < l.size(); j++) {
					l.get(j).moveToNextRoad();
					if(l.get(j).getStatus() != VehicleStatus.ARRIVED) {
						l.get(j).setStatus(VehicleStatus.TRAVELING);
					}
					qs.get(currGreen).remove(0);
				}
			}
		
			green = lsStrategy.chooseNextGreen(roadin, qs, currGreen, lastSwitchingTime, time);
			if(green == qs.size()) {
				green = 0;
			}

			if(this.currGreen != green) { 
				currGreen = green;
				this.lastSwitchingTime = time;
			} 
		}
	} 
	

	public JSONObject report() {
		JSONObject jo = new JSONObject();
		JSONArray joarray = new JSONArray();
		jo.put("id", this._id);
		if(this.currGreen == -1 || roadin.size() == 0) {
			jo.put("green", "none");
		}
		else {
			jo.put("green",	roadin.get(currGreen)._id );
		}
		for(int i = 0; i < qs.size();i++) {
				JSONObject q = new JSONObject();
				q.put("road", roadin.get(i)._id);
				JSONArray v = new JSONArray();
				for( int j = 0; j< qs.get(i).size();j++ ) 			
					v.put(qs.get(i).get(j)._id);
				q.put("vehicles", v);
				joarray.put(q);
		}
		jo.put("queues", joarray);
		
		return jo;
	}
	
	public int getX() {
		return xCoor;
	}
	
	public int getY() {
		return yCoor;
	}
	
	public int getGreenLightIndex() {
		return this.currGreen;
	}
	
	public List<List<Vehicle>> getQueques() {
		return qs;
	}
	
	public Map <Road,List<Vehicle>> getRoadcolas() {
		return roadcola;
	}
	
	public String getGreenLightId() {
		String green;
		if(this.currGreen == -1 || roadin.size() == 0) {
			green = "none";
		}
		else {
			green = roadin.get(currGreen)._id;
		}
		return green;
	}
	
	public List<Road> getInRoads() {
		return roadin;
	}

}
