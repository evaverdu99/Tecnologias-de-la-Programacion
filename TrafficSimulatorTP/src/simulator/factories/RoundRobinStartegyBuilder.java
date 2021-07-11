package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;


public class RoundRobinStartegyBuilder<T>extends Builder<LightSwitchingStrategy>{

	RoundRobinStartegyBuilder(String type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		// TODO Auto-generated method stub
		return null;
	}	

}
