package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy{
	private int timeSlot;
	public MostCrowdedStrategy(int timeSlot) {
		this.timeSlot = timeSlot;
	}

	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime, int currTime) {
		int longest = 0;
		int ind = 0;
		if(roads == null) {
			return -1;
		}
		else if(currGreen == -1) {
			for(int i = 0; i < qs.size(); i++) {
				if(qs.get(i).size() > longest) {
					longest = qs.get(i).size();
					ind = i;
				}
			}
			return ind;
		}
		else if(currTime-lastSwitchingTime < timeSlot) {
			return currGreen;
		}
		else {
			for(int i = 1; i <= qs.size(); i++) {
				if(qs.get((i + currGreen)%qs.size()).size() > longest) {
					longest = qs.get(i).size();
					ind = i;
				}
			}
			return ind;
		}
	}

}
