package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.RoundRobinStrategy;


public class RoundRobinStrategyBuilder extends Builder<LightSwitchingStrategy>{

	public RoundRobinStrategyBuilder() {
		super("round_robin_lss");
	}

	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		int timeslot;// = data.getInt("timeslot");
		if (data.get("timeslot").equals(null)) {
			timeslot = 1;
		}
		else {
			timeslot = data.getInt("timeslot");
		}
		return new RoundRobinStrategy(timeslot);
	}	

}
