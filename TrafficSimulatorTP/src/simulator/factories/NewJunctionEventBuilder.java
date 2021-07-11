package simulator.factories;

import org.json.JSONObject;
import simulator.factories.Factory;
import simulator.factories.Builder;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event> {
	
	private static Factory<LightSwitchingStrategy> _lssFactory;
	private static Factory<DequeuingStrategy> _dqsFactory;
	
	public NewJunctionEventBuilder(Factory<LightSwitchingStrategy> lss, Factory<DequeuingStrategy> dqs ) {
		super("new_junction");
		_lssFactory = lss;
		_dqsFactory = dqs;
	}

	
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		String id = data.getString("id");
		int xCoor = data.getJSONArray("coor").getInt(0);
		int yCoor = data.getJSONArray("coor").getInt(1);
		LightSwitchingStrategy lss = _lssFactory.createInstance(data.getJSONObject("ls_strategy"));
		DequeuingStrategy dqs =_dqsFactory.createInstance(data.getJSONObject("dq_strategy"));
		return new NewJunctionEvent(time,id,xCoor,yCoor,lss,dqs);
	}

}
