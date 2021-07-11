package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event>{

	public SetWeatherEventBuilder() {
		super("set_weather");
	}

	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		List<Pair<String,Weather>> info = new ArrayList<Pair<String,Weather>>();
		JSONArray infoArray =data.getJSONArray("info");

		for(int i=0;i<infoArray.length();i++) {
			JSONObject o = infoArray.getJSONObject(i);
			Pair <String,Weather> e = new Pair<>(o.getString("road"),o.getEnum(Weather.class, "weather"));
			info.add(e);
		}
		return new SetWeatherEvent(time, info);
	}

}
