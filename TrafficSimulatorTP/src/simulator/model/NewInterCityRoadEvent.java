package simulator.model;

public class NewInterCityRoadEvent extends Event {
	private String id;
	private String src;
	private String dest;
	private int length;
	private int co2limit;
	private int maxspeed;
	private Weather weather;
	
	public NewInterCityRoadEvent(int time, String id, String src, String dest,int length, int co2limit, int maxspeed , Weather weather) {
		super(time);
		this.id = id;
		this.src = src;
		this.dest = dest;
		this.length = length;
		this.co2limit = co2limit;
		this.maxspeed = maxspeed;
		this.weather = weather;
	}

	@Override
	void execute(RoadMap map) {
		InterCityRoad r= new InterCityRoad(id, map.getJunction(src), map.getJunction(dest),  maxspeed, co2limit, length, weather);
		map.addRoad(r);
	}
	
	public String toString() {
		return "New Inter City Road '"+this.id+"'";
	}

}
