package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

@SuppressWarnings("serial")
public class StatusBar extends JPanel implements TrafficSimObserver {
	
	private JLabel _Time;
	private JLabel _Event;
	
	public StatusBar(Controller _ctrl) {
		inicializa();
		_ctrl.addObserver(this);
	}

	private void inicializa() {
		
		this.setLayout( new FlowLayout( FlowLayout.LEFT ));
		this.setBorder( BorderFactory.createBevelBorder( 1 ));

		_Time = new JLabel();
		this.add(new JLabel("Time: "));
		this.add(_Time);
		this.add(Box.createRigidArea(new Dimension(25, 0)));
		JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
		separator.setPreferredSize(new Dimension(1, 20));
		this.add(separator);
		
		_Event = new JLabel();
		this.add(new JLabel(""));
		this.add(_Event);
		this.add(Box.createRigidArea(new Dimension(25, 0)));
		this.add(new JSeparator(SwingConstants.VERTICAL));
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		_Time.setText(Integer.toString(time));
		_Event.setText("");
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		_Time.setText(Integer.toString(time));
		_Event.setText("");
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		_Time.setText(Integer.toString(time));
		_Event.setText("Event added: " + e.toString());
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		_Time.setText(Integer.toString(time));
		_Event.setText("");
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		_Time.setText(Integer.toString(time));
		_Event.setText("");
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}
