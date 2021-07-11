package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class SetContClassEvent extends Event {
	private List<Pair<String,Integer>> cs;
	
	public SetContClassEvent(int time , List<Pair<String,Integer>> cs ) {
		super(time);
		this.cs = cs;
		
	}

	void execute(RoadMap map) {
		if(cs != null) {
			for(int i = 0; i < cs.size(); i++) {
				Vehicle v = map.getVehicle(cs.get(i).getFirst());
				if(v != null) {
					v.setContaminationClass(cs.get(i).getSecond());
				}
			}
		}
		else {
			throw new IllegalArgumentException("Parametro no valido");
		}
	}
	
	public String toString() {
		String frase = "Change CO2 Class: [";
		for(int i = 0; i < cs.size(); i++) {
			frase += "(" + cs.get(i).getFirst() +"," + cs.get(i).getSecond() + ")";
			if(i !=cs.size()-1) {
				frase += ", ";
			}
		}
		frase += "]";
		return frase;
	}

}
