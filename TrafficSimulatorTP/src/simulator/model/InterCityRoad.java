package simulator.model;

public class InterCityRoad extends Road {

	InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int lenght, Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, lenght, weather);
		
	}

	void reduceTotalContamination() {
		switch (weather)
		{
		case CLOUDY:
			this.totalPoll=((int)((100.0 - 3)/100.0 * this.totalPoll));
			break;
		case RAINY:
			this.totalPoll=((int)((100.0 - 10)/100.0 * this.totalPoll));
			break;
		case STORM:
			this.totalPoll=((int)((100.0 - 20)/100.0 * this.totalPoll));
			break;
		case SUNNY:
			this.totalPoll=((int)((100.0 - 2)/100.0 * this.totalPoll));
			break;
		case WINDY:
			this.totalPoll=((int)((100.0 - 15)/100.0 * this.totalPoll));
			break;
		default:
			break;
		
		}
	}

	void updateSpeedLimit() {
		if (this.totalPoll > this.contLimit ) {
			this.speedLimit = (int)(maxSpeed*0.5) ;
		}
		else {
			this.speedLimit = this.maxSpeed;
		}
		
	}

	int calculateVehicleSpeed(Vehicle v) {
		int s;
		if(v.getStatus().equals(VehicleStatus.TRAVELING)) {
			if(Weather.STORM.equals(weather)) {
				s = (int) (this.speedLimit*0.8); 
			}
			else {
				s = this.speedLimit;
			}
		}
		else {
			s = 0;
		}
		return s;
	}
	

}
