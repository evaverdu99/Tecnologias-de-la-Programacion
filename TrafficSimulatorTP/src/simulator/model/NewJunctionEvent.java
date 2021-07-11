package simulator.model;

public class NewJunctionEvent extends Event{
	private String id;
	private int xCoor;
	private int yCoor;
	private LightSwitchingStrategy lss;
	private DequeuingStrategy dqs;
	public NewJunctionEvent(int time, String id, int xCoor, int yCoor, LightSwitchingStrategy lss, DequeuingStrategy dqs) {
		super(time);
		this.id = id;
		this.xCoor = xCoor;
		this.yCoor = yCoor;
		this.dqs = dqs;
		this.lss = lss;
	}

	void execute(RoadMap map) {
		Junction j = new Junction(id, lss, dqs, xCoor, yCoor);
		map.addJunction(j);
	}
	
	public String toString() {
		return "New Junction '"+this.id+"'";
	}

}
