package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;
import simulator.model.Weather;

public class MapByRoadComponent extends JComponent implements TrafficSimObserver{

	private static final long serialVersionUID = 1L;
	
	private static final int _JRADIUS = 10;
	
	//Defining the colors we'll use for backgrounds and elements
	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _JUNCTION_COLOR_ORIGIN = Color.BLUE;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;
	//private static final Color _JUNCTION_COLOR_DESTINY_RED = Color.RED;
	
	
	//Declaring all the images we'll use as attributes
	private RoadMap _map;
	private Image _car;
	private Image _rain, _storm, _sun, _wind, _cloud;
	private Image _CO0, _CO1, _CO2, _CO3, _CO4, _CO5;

	public MapByRoadComponent(Controller _ctrl) {
		intiGUI();
		_ctrl.addObserver(this);
		setPreferredSize(new Dimension(200, 300));
	}

	private void intiGUI(){
		_car = loadImage("car.png");
		_rain = loadImage("rain.png");
		_storm = loadImage("storm.png");
		_sun = loadImage("sun.png");
		_wind = loadImage("wind.png");
		_cloud = loadImage("cloud.png");
		_CO0 = loadImage("cont_0.png");
		_CO1 = loadImage("cont_1.png");
		_CO2 = loadImage("cont_2.png");
		_CO3 = loadImage("cont_3.png");
		_CO4 = loadImage("cont_4.png");
		_CO5 = loadImage("cont_5.png");
	}
	
	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}

	//Drawing the actual window
	public void paintComponent (Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		//Draw white background
		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());
	
		//For each road draw its vehicles, weather conditions and contamination
		if (_map == null || _map.getJunctions().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			drawMap(g);
		}
		
	}
	
	private void drawMap (Graphics g) {
		drawRoad (g);
	}

	
	//Drawing of each specific road
	private void drawRoad (Graphics g) {
		int i = 0;
		for (Road r : _map.getRoads()) {
			//coordinates from where to where do the roads go. From (x1,y1) to (x2,y2)
			
			int x1 = 50;
			int y = (i+1)*50;
			int x2 = getWidth()-100;

			//Choosing the colour of the road depending on the traffic light
			Color junctionColor = _RED_LIGHT_COLOR;
			int idx = r.getDest().getGreenLightIndex();
			if (idx != -1 &&  r.equals( r.getDest().getInRoads().get(idx))) {
				junctionColor = _GREEN_LIGHT_COLOR;
			}
			//Drawing the line that represents the road
			g.setColor(Color.GRAY);
			g.drawLine(x1, y, x2, y);
			drawSrcJunctions(r.getSrc(), x1, y, g);
			drawDestJunctions(r.getDest(), x2, y, g, junctionColor);
			drawVehicles (g , r, x1, x2, y);
			drawWeather (g, y, r.getWeather());
			drawContamination (g, y, r);
			i++;
		}	
	}
	
	private void drawSrcJunctions(Junction j, int x, int y, Graphics g) {
		// (x,y) are the coordinates of the junction

		// draw a circle with center at (x,y) with radius _JRADIUS
		g.setColor(_JUNCTION_COLOR_ORIGIN);
		g.fillOval(x - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);

		// draw the junction's identifier at (x,y)
		g.setColor(_JUNCTION_LABEL_COLOR);
		g.drawString(j.getId(), x, y-10); 

	}

	private void drawDestJunctions(Junction j, int x, int y, Graphics g, Color junctionColor) {
		g.setColor(junctionColor);
		g.fillOval(x - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);

		// draw the junction's identifier at (x,y)
		g.setColor(junctionColor);
		g.drawString(j.getId(), x, y-10); 

		
	}

	private void drawContamination(Graphics g, int y, Road r) {
		int c= (int)Math.floor(Math.min((double) r.getTotalCO2()/(1.0+(double)r.getCO2Limit()), 1.0)/0.19);
		switch(c) {
			case 0:
		 		g.drawImage(_CO0, getWidth()-40, y-20, 32, 32, this);
			break;
		 	case 1:
		 		g.drawImage(_CO1, getWidth()-40, y-20, 32, 32, this);
			break;
		 	case 2:
		 		g.drawImage(_CO2, getWidth()-40, y-20, 32, 32, this);
				break;
		 	case 3:
		 		g.drawImage(_CO3, getWidth()-40, y-20, 32, 32, this);
				break;
		 	case 4:
		 		g.drawImage(_CO4, getWidth()-40, y-20, 32, 32, this);
				break;
		 	case 5:
		 		g.drawImage(_CO5, getWidth()-40, y-20, 32, 32, this);
				break;
		}
	}

	private void drawWeather(Graphics g, int y, Weather w) {
		switch(w) {
		 	case STORM:
		 		g.drawImage(_storm, getWidth()-80, y-20, 32, 32, this);
			break;
		 	case WINDY:
		 		g.drawImage(_wind, getWidth()-80, y-20, 32, 32, this);
				break;
		 	case SUNNY:
		 		g.drawImage(_sun, getWidth()-80, y-20, 32, 32, this);
				break;
		 	case RAINY:
		 		g.drawImage(_rain, getWidth()-80, y-20, 32, 32, this);
				break;
		 	case CLOUDY:
		 		g.drawImage(_cloud, getWidth()-80, y-20, 32, 32, this);
				break;
		}
	}

	private void drawVehicles(Graphics g, Road r, int x1, int x2, int y) {
		for (Vehicle v : _map.getVehicles()) {
			if (v.getStatus() != VehicleStatus.ARRIVED) {
				Road rv = v.getRoad();
				if(rv.getId() == r.getId()) {
					int x = x1+(int)((x2-x1)*((double)v.getLocation()/(double)r.getLength()));

					// draw an image of a car (with circle as background) and it identifier
					g.drawImage(_car, x, y-12, 20, 20, this);
					g.setColor(Color.BLACK);
					g.drawString(v.getId(), x, y - 13);
				}
			}
		}
		
	}
	
	public void update(RoadMap map) {
		_map = map;
		repaint();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map);
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map);
		
	}

	@Override
	public void onError(String err) {
		
	}

}
