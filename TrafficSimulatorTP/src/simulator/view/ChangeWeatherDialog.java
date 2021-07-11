package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import simulator.model.Road;
import simulator.model.Weather;

@SuppressWarnings("serial")
public class ChangeWeatherDialog extends JDialog{
	
	private JComboBox<String> object;
	private DefaultComboBoxModel<String> _objectModel;
	@SuppressWarnings("rawtypes")
	private JComboBox<Enum> object2;
	private String _name1;
	private String _name2;
	protected JSpinner ticks;
	private int _status;
	private String _info;
	private Weather[] options;
	
	public ChangeWeatherDialog(Frame frame) {
		super(frame, true);
		this._info = " Schedule an event to change the weather of a road after a given number of simulation ticks from now."; ;
		this._name1 = "Road: ";
		this._name2 = " Weather: ";
		this.options = Weather.values();
		this.setTitle("Change Weather");
		initGUI();
		
	}
	
	@SuppressWarnings("rawtypes")
	private void initGUI() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout(80, 40));
		
		//Panel de arriba
		
		JLabel info = new JLabel(_info);

		//Panel central
		JPanel centerPanel = new JPanel(new FlowLayout());
		
		JLabel name1 =  new JLabel(_name1);
		JLabel name2 =  new JLabel(_name2);
		JLabel namet =  new JLabel(" Ticks: ");
		
		_objectModel = new DefaultComboBoxModel<>();
		object = new JComboBox<String>(_objectModel); 
		
		object2 = new JComboBox<Enum>(options);
		
		ticks = new JSpinner(new SpinnerNumberModel(1, 0, 1000, 1));
		
		centerPanel.add(name1);
		centerPanel.add(object);
		centerPanel.add(name2);
		centerPanel.add(object2);
		centerPanel.add(namet);
		centerPanel.add(ticks);
		
		//Panel de abajo
		JPanel surPanel = new JPanel(new FlowLayout());
		
		JButton cancelbtn = new JButton("Cancel");
		cancelbtn.setPreferredSize(new Dimension(80, 30));
		cancelbtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_status = 0;
				ChangeWeatherDialog.this.setVisible(false);
			}
		});
		JButton okbtn = new JButton("OK");
		okbtn.setPreferredSize(new Dimension(80, 30));
		okbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (object.getSelectedItem() != null && object2.getSelectedItem() != null && ticks.getComponentCount() != 0) {
					_status = 1;
					ChangeWeatherDialog.this.setVisible(false);
				}
			}
		});
		surPanel.add(cancelbtn);
		surPanel.add(okbtn);
		
		//Main Panel
		mainPanel.add(info, BorderLayout.NORTH);
		mainPanel.add(centerPanel,  BorderLayout.CENTER);
		mainPanel.add(surPanel,  BorderLayout.SOUTH);
		
		this.add(mainPanel);
	    this.setVisible(false); 
	    this.pack();
	    
	}
	
	public int open(List<Road> rlist) {
		_objectModel.removeAllElements();
		for (int i = 0; i < rlist.size(); i++) {
			_objectModel.addElement(rlist.get(i).getId());
		}
		setVisible(true);	
		return _status;
	}
	public Weather getObject2() {
		return (Weather)object2.getSelectedItem();
	}
	public String getObject() {
		return (String)object.getSelectedItem();
		
	}
	public int getTicks() {
		return (int)ticks.getValue();	
	}

}

