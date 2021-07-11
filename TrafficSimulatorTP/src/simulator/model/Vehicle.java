package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import simulator.model.VehicleStatus;
import org.json.JSONObject;

public class Vehicle extends SimulatedObject {
	private List<Junction> itinerary;
	private int indice;
	private int maxSpeed; 
	private int speed; 
	private VehicleStatus status;
	private Road road; 
	private int loc; 
	private int totalPoll;
	private int distance;
	private int contClas;
	
	Vehicle(String id, int maxSpeed, int contClas, List<Junction> itinerary) {
		super(id);
		
		Collections.unmodifiableList(new ArrayList<>(itinerary));
		
		this._id = id;
		if(maxSpeed < 0 && contClas > 10 && contClas < 0 && itinerary.size() >= 2) {
			throw new IllegalArgumentException("Parametro no valido");
		}
		else {
			this.indice = 0;
			this.status = VehicleStatus.PENDING;
			this.maxSpeed=maxSpeed;
			this.contClas = contClas;
			this.itinerary = itinerary;
		}	
	}

	void advance(int time) {
		int l = 0;
		int c = 0;
		boolean end = false;
		if(VehicleStatus.TRAVELING.equals(status)) {
			if(road.getLength() > (this.loc + this.speed)) {
				l = this.loc + this.speed;
			}
			else {
				l = road.getLength();
				end = true;
			}
			c = ((l - this.loc)*contClas);
			distance += (l - this.loc);
			this.totalPoll += c;
			this.loc = l;
			road.addContamination(c);
			if(end) {
				this.status = VehicleStatus.WAITING;
				this.speed = 0;
				road.destJunc.enter(road,this);
				end = false;
			}
		}
	}
	
	void setSpeed(int s) {
		if(s >= 0) {
			if(s <= maxSpeed) {
				this.speed = s;
			}
			else {
				this.speed=maxSpeed;
			}
		}
		else {
			throw new IllegalArgumentException("Velocidad negativa");
		}
	}
	
	void setContaminationClass(int c){
		if(c >= 0 && c <= 10) {
			this.contClas = c;
		}
		else {
			throw new IllegalArgumentException("La clase de contaminacion no está entre 0 y 10");
		}
	}
	
	void setStatus(VehicleStatus s) {
		this.status = s;
	}
	
	public VehicleStatus getStatus() {
		return this.status;
	}
	void moveToNextRoad() {
		Junction junsrc;
		Junction jundest;
		if(VehicleStatus.PENDING.equals(status)||VehicleStatus.WAITING.equals(status)) {
			this.loc = 0;
			if(VehicleStatus.PENDING.equals(status)) {
				 junsrc = this.itinerary.get(this.indice);
				 jundest = this.itinerary.get(this.indice+1);
				 road = junsrc.roadTo(jundest);
				 road.enter(this);
				 status = VehicleStatus.TRAVELING;
			}
			else {
				road.exit(this);
				indice++;
				if(indice < this.itinerary.size()-1) {
					junsrc = this.itinerary.get(this.indice);
					jundest = this.itinerary.get(this.indice+1);
					road = junsrc.roadTo(jundest);
					road.enter(this);
				}
				else {
					status = VehicleStatus.ARRIVED;
				}
			}
		}
		else {
			throw new IllegalArgumentException("Este coche no se puede mover");
		}
	}
	
	
	public int getSpeed() {
		return this.speed;
	}
	 
	public List<Junction> getItinerary(){
		return this.itinerary;	
	}
	
	public int getLocation() {
		return this.loc;
	}
	
	public int getContClas() {
		return this.contClas;
	}
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("id", _id);
		jo.put("speed", speed);
		jo.put("distance", distance);
		jo.put("co2",totalPoll);
		jo.put("class", contClas);
		jo.put("status", status);
		if(status == VehicleStatus.WAITING || status == VehicleStatus.TRAVELING ) {
			jo.put("road", road);
			jo.put("location", loc);
		}
		return jo;
	}

	public Road getRoad() {
		return road;
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public int getTotalCO2() {
		return totalPoll;
	}

	public int getDistance() {
		return distance;
	}

}