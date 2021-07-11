package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;

public class Controller {
	private TrafficSimulator sim;
	private Factory<Event> eventsFactory;
	
	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) {
		this.sim = sim;
		this.eventsFactory = eventsFactory;
	}
	
	public void loadEvents(InputStream in) {
		JSONObject jsonInput = new JSONObject(new JSONTokener(in));
		JSONArray events = jsonInput.getJSONArray("events");
		
		for (int i = 0; i < events.length(); i++) {
			sim.addEvent(eventsFactory.createInstance(events.getJSONObject(i)));
		}	
	}
	
	public void run(int n, OutputStream out) {
		PrintStream p = new PrintStream(out);
		p.println("{ \n \"states\": [");
		for(int i = 1; i <= n; ++i) {
			sim.advance();
			p.print(sim.report());
			if(i <= n-1) {
				p.println(", ");
			}
			else {
				p.println();
			}
		}
		p.print("] \n}");
	}
	
	public void run(int n) {
		for(int i = 1; i <= n; ++i) {
			sim.advance();
		}
	}
	
	public void reset() {
		sim.reset();
	}
	
	public void addObserver(TrafficSimObserver o) {
		sim.addObserver(o);
	}
	public void removeObserver(TrafficSimObserver o) {
		sim.removeObserver(o);
	}
	public void addEvent(Event e) {
		sim.addEvent(e);
	}

}

