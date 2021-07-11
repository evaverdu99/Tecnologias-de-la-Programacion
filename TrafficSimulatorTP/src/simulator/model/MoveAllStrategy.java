package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class MoveAllStrategy implements DequeuingStrategy{

	public List<Vehicle> dequeue(List<Vehicle> q) {
		List<Vehicle> list =  new ArrayList<Vehicle>();
		for(int i = 0; i < list.size(); i++) {
			list.add(q.get(i));
		}
		return list;
	}

}
