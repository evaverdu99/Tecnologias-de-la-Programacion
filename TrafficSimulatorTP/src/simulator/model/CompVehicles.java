package simulator.model;

import java.util.Comparator;

public class CompVehicles implements Comparator<Vehicle>{ //Esta clase es para ordenar listas de vehiculos por Localización

	public int compare(Vehicle o1, Vehicle o2) {
		if (o1.getLocation() == o2.getLocation()) return 0;
		 else if (o1.getLocation() > o2.getLocation()) return -1;
		 else return 1;

	}

}
