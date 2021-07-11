package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class SetWeatherEvent extends Event{
	private List<Pair<String,Weather>> ws;
	
	public SetWeatherEvent(int time, List<Pair<String,Weather>> ws) {
		super(time);
		this.ws = ws;
	}

	void execute(RoadMap map) {
		if(ws != null) {
			for(int i = 0; i< ws.size(); i++) {
				Road r = map.getRoad(ws.get(i).getFirst());
				if(r != null) {
					r.setWeather(ws.get(i).getSecond());
				}
				else {
					throw new IllegalArgumentException("Parametro no valido"); 
				}
			}
		}
		else {
			throw new IllegalArgumentException("Parametro no valido");
		}
		
	}
	
	public String toString() {
		String frase = "Change Weather: [";
		for(int i = 0; i< ws.size(); i++) {
			frase += "(" + ws.get(i).getFirst() +"," + ws.get(i).getSecond() + ")";
			if(i != ws.size()-1) {
				frase += ", ";
			}
		}
		frase += "]";
		return frase;
	}

}
