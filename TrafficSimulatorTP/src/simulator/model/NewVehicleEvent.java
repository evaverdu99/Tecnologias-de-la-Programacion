package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class NewVehicleEvent extends Event{

	private String id;
	private int maxSpeed;
	private int vclass;
	private List<String > itinerary;
	
	public NewVehicleEvent(int time, String id, int maxSpeed, int vclass, List<String> itinerary) {
		super(time);
		this.id = id;
		this.maxSpeed = maxSpeed;
		this.vclass = vclass;
		this.itinerary = itinerary;
	}

	void execute(RoadMap map) {
		List<Junction> j = new ArrayList<Junction>();
		for (int i = 0; i< itinerary.size();i++) {
			 j.add(i, map.getJunction(itinerary.get(i)));
		}
		Vehicle v = new Vehicle(id, maxSpeed, vclass, j); 
		map.addVehicle(v);
		v.moveToNextRoad();
	}
	
	public String toString() {
		return "New Vehicle '"+this.id+"'";
	}
}
