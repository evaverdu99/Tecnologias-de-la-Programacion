package simulator.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


public abstract class Road extends SimulatedObject{
	protected Junction srcJunc;
	protected Junction destJunc;
	protected int length;
	protected int maxSpeed;
	protected int speedLimit;
	protected int contLimit;
	protected Weather weather;
	protected int totalPoll;
	protected List<Vehicle> list;
	
	Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id);
		this.list = new ArrayList<Vehicle>();
		this.weather = weather;
		this.length = length;
		this.maxSpeed = maxSpeed;
		this.contLimit = contLimit;
		this.srcJunc = srcJunc;
		this.destJunc = destJunc;
		srcJunc.addOutGoingRoad(this);
		destJunc.addIncommingRoad(this);
	}
	
	abstract void reduceTotalContamination();
	abstract void updateSpeedLimit();
	abstract int calculateVehicleSpeed(Vehicle v);
	
	void enter(Vehicle v) {
		if (v.getLocation() != 0) {
			throw new IllegalArgumentException("La localizacion del vehiculo es incorrecta para entrar en la carretera");
		}
		if (v.getSpeed() != 0) {
			throw new IllegalArgumentException("The speed of the vehicle is not 0, it cannot enter the new road");	
		}
		list.add(v); //adds at the end of the list.
	}
	
	void exit(Vehicle v) {
		list.remove(list.indexOf(v));
		
	}

	void advance(int time) {
		Comparator <Vehicle> c = new CompVehicles();
		reduceTotalContamination();
		updateSpeedLimit();

		for(Vehicle v : list) {
			int s = calculateVehicleSpeed(v);
			v.setSpeed(s);
			v.advance(time);
		}
		
		list.sort(c);
	}

	void addContamination(int c) {
		if (c < 0) {
			throw new IllegalArgumentException ("The value introduced for the road must be a positive number");
		}
		this.totalPoll += c;
	}
	
	void setWeather(Weather w) {
		if(w == null) {
			throw new IllegalArgumentException("The weather is not correctly defined");
		}
		this.weather = w;
	}
	
	public int getLength() {
		return this.length;
	}
	
	public Weather getWeather() {
		return weather;
	}
	
	public int getSpeedLimit() {
		return this.speedLimit;
	}
	
	void setSpeedLimit(int limit) {
		this.speedLimit = limit;
	}

	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("id", _id);
		jo.put("speedlimit", speedLimit);
		jo.put("weather", getWeather());
		jo.put("co2",totalPoll);
		
		JSONArray v = new JSONArray();
		for( int j = 0; j< list.size(); j++) 			
			v.put(list.get(j)._id);
		
		jo.put("vehicles", v);
		
		return jo;
	}

	public Junction getSrc() {
		return this.srcJunc;
	}

	public Junction getDest() {
		return this.destJunc;
	}

	public int getTotalCO2() {
		return this.totalPoll;
	}

	public int getCO2Limit() {
		return contLimit;
	}

	public Object getMaxSpeed() {
		return maxSpeed;
	}

	

}
