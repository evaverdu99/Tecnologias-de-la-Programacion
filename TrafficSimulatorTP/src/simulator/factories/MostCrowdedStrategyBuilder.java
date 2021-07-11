package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.MostCrowdedStrategy;


public class MostCrowdedStrategyBuilder extends Builder<LightSwitchingStrategy>{

	public MostCrowdedStrategyBuilder() {
		super("most_crowded_lss");
	}

	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		int timeslot;// = data.getInt("timeslot");
		if (data.get("timeslot").equals(null)) {
			timeslot = 1;
		}
		else {
			timeslot = data.getInt("timeslot");
		}
		return new MostCrowdedStrategy(timeslot);
	}

	

}
