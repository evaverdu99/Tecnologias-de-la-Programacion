package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class RoadMap {
	private List<Junction> jlist;
	private List<Road> rlist;
	private List<Vehicle> vlist;
	private Map<String, Junction> jmap;
	private Map<String,Road> rmap;
	private Map<String,Vehicle> vmap;
	
	RoadMap(){
		this.jlist = new ArrayList<Junction>();
		this.jmap = new HashMap<String, Junction>();
		this.rlist = new ArrayList<Road>();
		this.rmap = new HashMap<String, Road>();
		this.vlist = new ArrayList<Vehicle>();
		this.vmap = new HashMap<String, Vehicle>();
	}
	
	void addJunction(Junction j) {
		if(jlist.indexOf(j) == -1) {
			jlist.add(j);
			jmap.put(j._id, j);
		}
		else {
			throw new IllegalArgumentException("Este cruce ya ha sido añadido");
		}
		
	}
	void addRoad(Road r) {
		if(rlist.indexOf(r) == -1 ) {
			if(jmap.containsKey(r.destJunc._id) && jmap.containsKey(r.srcJunc._id)) {
				rlist.add(r);
				rmap.put(r._id, r);
			}
			else {
				throw new IllegalArgumentException("Los cruces de destino o origen no existen");
			}
		}
		else {
			throw new IllegalArgumentException("Esta carretera ya ha sido añadida");
		}
	}
	
	void addVehicle(Vehicle v) {
		boolean ok = true;
		int i = 0;
		List<Junction> lj = v.getItinerary();
		if(vlist.indexOf(v) == -1) {
			while(ok && i < lj.size() - 1) {
				if(lj.get(i).roadTo(lj.get(i + 1)) == null) {
					ok = false;
				}
				i++;
			}
			if(ok) {
				vlist.add(v);
				vmap.put(v._id, v);
			}
			else {
				throw new IllegalArgumentException("The itinerary is not correct");
			}	
		}
		else {
			throw new IllegalArgumentException("Esta carretera ya ha sido añadida");
		}
	}
	
	public Junction getJunction(String id) {
		if (jmap.containsKey(id)) {
			return jmap.get(id);
		}
		else {
			return null;
		}
	}
	
	public Road getRoad(String id) {
		if (rmap.containsKey(id)) {
			return rmap.get(id);
		}
		else {
			return null;
		}
	}
	
	public Vehicle getVehicle(String id) {
		if (vmap.containsKey(id)) {
			return vmap.get(id);
		}
		else {
			return null;
		}
		
	}
	
	public List<Junction>getJunctions(){
		return jlist;
		
	}
	
	public List<Road>getRoads(){
		return rlist;
		
	}
	
	public List<Vehicle>getVehicles(){
		return vlist;
		
	}
	
	void reset() {
		this.jlist.clear();
		this.jmap.clear();
		this.rlist.clear();
		this.rmap.clear();
		this.vlist.clear();
		this.vmap.clear();
	}
	
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		JSONArray jarray = new JSONArray();
		JSONArray rarray = new JSONArray();
		JSONArray varray = new JSONArray();
		for(int i = 0; i< this.jlist.size();i++) {
			jarray.put(jlist.get(i).report());
		}
		for(int i = 0; i< this.rlist.size();i++) {
			rarray.put(rlist.get(i).report());
		}
		for(int i = 0; i< this.vlist.size();i++) {
			varray.put(vlist.get(i).report());
		}
		jo.put("junctions",	jarray);
		jo.put("roads",	rarray);
		jo.put("vehicles",	varray);
		return jo;
		
	}
	
}
