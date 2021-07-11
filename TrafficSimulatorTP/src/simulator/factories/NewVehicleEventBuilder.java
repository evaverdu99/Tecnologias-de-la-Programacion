package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder  extends Builder<Event> {

	public NewVehicleEventBuilder() {
		super("new_vehicle");
	}

	protected Event createTheInstance(JSONObject data) {
		
		int time = data.getInt("time");
		String id = data.getString("id");
		int maxSpeed = data.getInt("maxspeed");
		int vclass = data.getInt("class");
		List<String> itinerary = new ArrayList<String>();
		JSONArray itineraryArray =data.getJSONArray("itinerary");
		for(int i=0;i<itineraryArray.length();i++) {
			itinerary.add(itineraryArray.getString(i));
		}
		return new NewVehicleEvent(time,id,maxSpeed,vclass,itinerary);
	}

}
