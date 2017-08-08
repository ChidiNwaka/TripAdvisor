package tripAdvisor;

/**
 * The DirectRoute class that calculates the cheapest route to our destination.
 */
public class DirectRoute {

	TrainCompany trainCompany;
	String fromStation;
	String toStation;
	double price;
	public double cumPrice = Double.POSITIVE_INFINITY;
	public DirectRoute previous = null;

	/**
	 * The constructor of the class.
	 * 
	 * @param trainCompany
	 * @param fromStation
	 * @param toStation
	 * @param price
	 * @throws IllegalArgumentException
	 */
	public DirectRoute(TrainCompany trainCompany, String fromStation, String toStation, double price)
			throws IllegalArgumentException {

		// Throw an error if any of the inputs is invalid.
		if (fromStation.contentEquals(toStation)) {
			throw new IllegalArgumentException("FromStation and ToStation cannot be the same.");
		}
		if (price < 0) {
			throw new IllegalArgumentException("Price must be non-negative.");
		}
		if (trainCompany == null) {
			throw new IllegalArgumentException("Train company cannot be null.");
		}
		if ((toStation == null) || (fromStation == null)) {
			throw new IllegalArgumentException("Destination station cannot be null.");
		}

		this.trainCompany = trainCompany;
		this.fromStation = fromStation;
		this.toStation = toStation;
		this.price = price;

	}

	/**
	 * Return the name of the train company.
	 * 
	 * @return
	 */
	public TrainCompany getTrainCompany() {
		return trainCompany; // getter
	}

	/**
	 * Set the name of the train company to the passed in parameter.
	 * 
	 * @param trainCompany
	 */
	public void setTrainCompany(TrainCompany trainCompany) {

		if (trainCompany == null) {
			throw new IllegalArgumentException("Train company cannot be null.");
		}

		this.trainCompany = trainCompany; // setter
	}

	/**
	 * Show the route of how you'll get to your destination from this station.
	 * 
	 * @return
	 */
	public String getFromStation() {
		return fromStation; // getter
	}

	/**
	 * Set the route of how you'll get to your destination from this station.
	 * 
	 * @param fromStation
	 */
	public void setFromStation(String fromStation) {

		if (fromStation == null) {
			throw new IllegalArgumentException("From station cannot be null.");
		}

		if (fromStation.contentEquals(getToStation())) {
			throw new IllegalArgumentException("FromStation and ToStation cannot be the same.");
		}

		this.fromStation = fromStation; // setter
	}

	/**
	 * Show the route of how you'll get to the station from where you are.
	 * 
	 * @return toStation
	 */
	public String getToStation() {
		return toStation; // getter
	}

	/**
	 * Set the route of how you'll get to the station from where you are.
	 * 
	 * @param toStation
	 */
	public void setToStation(String toStation) {

		if (toStation == null) {
			throw new IllegalArgumentException("Destination station cannot be null.");
		}

		this.toStation = toStation;
	}

	/**
	 * Return the price for using this route.
	 * 
	 * @return price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * Set the price of the trip to the value of the passed in parameter.
	 * 
	 * @param price
	 */
	public void setPrice(double price) {
		if (price < 0) {
			throw new IllegalArgumentException("Price must be non-negative.");
		}

		this.price = price;
	}

	public boolean equals(DirectRoute dr) {
		if (this.getFromStation().contentEquals(dr.getFromStation())
				& this.getToStation().contentEquals(dr.getToStation())
				& this.getTrainCompany().getName().contentEquals(dr.getTrainCompany().getName())
				& this.getPrice() == dr.getPrice()) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return String.format("%s from %s to %s, %.2f$", getTrainCompany().getName(), getFromStation(), getToStation(),
				getPrice());
	}
}
