package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetContClassEvent;

public class SetContClassEventBuilder  extends Builder<Event> {

	public SetContClassEventBuilder() {
		super("set_cont_class");
	}

	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		
		List<Pair<String,Integer>> info = new ArrayList<Pair<String,Integer>>();
		JSONArray infoArray =data.getJSONArray("info");
		
		for(int i=0;i<infoArray.length();i++) {
			JSONObject o = infoArray.getJSONObject(i);
			Pair <String,Integer> e = new Pair<>(o.getString("vehicle"),o.getInt("class"));
			info.add(e);
		}
		return new SetContClassEvent(time, info);
	}

}
