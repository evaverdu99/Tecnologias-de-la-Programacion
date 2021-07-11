package simulator.model;

public class CityRoad extends Road{
	
	CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
		
	}

	void reduceTotalContamination() {
		if(this.totalPoll != 0) {
			switch (getWeather())
			{
			case CLOUDY:
				this.totalPoll -= 2;
				break;
			case RAINY:
				this.totalPoll -= 2;
				break;
			case STORM:
				this.totalPoll -= 10;
				break;
			case SUNNY:
				this.totalPoll -= 2;
				break;
			case WINDY:
				this.totalPoll -= 10;
				break;
			default:
				break;
			}
		}
		
		if (this.totalPoll < 0) {
			throw new IllegalArgumentException("Tha total contamination cannot be under 0");
		}
	}

	void updateSpeedLimit() {
		this.speedLimit = this.maxSpeed;
	}

	int calculateVehicleSpeed(Vehicle v) {
		int s;
		if(v.getStatus().equals(VehicleStatus.TRAVELING)) {
			s = (int)(((11.0-v.getContClas())/11.0)*speedLimit);
		}
		else {
			s = 0;
		}
		return s;
	}

}
