package tripAdvisor;

import java.util.Collection;
import java.util.HashSet;

public class TrainCompany {
	public String name;
	public Collection<DirectRoute> directRoutes;
	public int stationCount;
	public boolean stationCountChanged;
	public static Collection<String> names = new HashSet<String>();
	public static Collection<TrainCompany> companys = new HashSet<TrainCompany>();

	public TrainCompany(String name) throws IllegalArgumentException {
		if (name == null) {
			throw new IllegalArgumentException("TrainCompany name cannot be null.");
		}
		name = name.trim();
		if (name.equalsIgnoreCase("") || TrainCompany.names.contains(name)) {
			throw new IllegalArgumentException("TrainCompany name is not valid.");
		}
		this.setName(name);
		TrainCompany.names.add(name);
		TrainCompany.companys.add(this);
		directRoutes = new HashSet<DirectRoute>();
		stationCount = 0;
		stationCountChanged = false;
	}

	/**
	 * @return The Collection<TrainCompany> of all companies that have been
	 *         created.
	 */
	public static Collection<TrainCompany> getAllCompanies() {
		return companys;
	}

	/**
	 * @return The Collection<String> of all company names.
	 */
	public static Collection<String> getAllNames() {
		return names;
	}

	/**
	 * @return The Collection<DirectRoute> of all routes from all companies.
	 */
	public static Collection<DirectRoute> getAllDirectRoutesFromAllCompany() {
		Collection<DirectRoute> allRoutes = new HashSet<DirectRoute>();
		for (TrainCompany tc : companys) {
			allRoutes.addAll(tc.getAllDirectRoutes());
		}
		return allRoutes;
	}

	/**
	 * clean things up, remove the company records
	 */
	public static void cleanup() {
		names = new HashSet<String>();
		companys = new HashSet<TrainCompany>();
	}

	@Override
	public String toString() {
		return String.format("%s, offering %d routes between %d stations", getName(), getDirectRoutesCount(),
				getStationsCount());
	}

	/**
	 * @return The name of the company.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the company.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return The DirectRoute object that was created/updated.
	 */
	public DirectRoute createOrUpdateDirectRoute(String fromStation, String toStation, double price) {
		DirectRoute newDirectRoute = new DirectRoute(this, fromStation, toStation, price);
		deleteDirectRoute(fromStation, toStation);
		directRoutes.add(newDirectRoute);
		stationCountChanged = true;
		return newDirectRoute;
	}

	/**
	 * Delete the specified route, if it exists.
	 */
	public void deleteDirectRoute(String fromStation, String toStation) {
		DirectRoute oldDirectRoute = getDirectRoute(fromStation, toStation);
		if (!(oldDirectRoute == null)) {
			directRoutes.remove(oldDirectRoute);
		}
		stationCountChanged = true;
	}

	/**
	 * @return null if there is no route from <code>fromStation</code> to
	 *         <code>toStation</code> with this TrainCompany.
	 */
	public DirectRoute getDirectRoute(String fromStation, String toStation) {
		for (DirectRoute dr : directRoutes) {
			if ((dr.getFromStation().equals(fromStation)) && (dr.getToStation().equals(toStation))) {
				return dr;
			}
		}
		return null;
	}

	/**
	 * @return the Collection<DirectRoute> from a specific station
	 */
	public Collection<DirectRoute> getDirectRoutesFrom(String fromStation) {
		Collection<DirectRoute> result = new HashSet<DirectRoute>();
		for (DirectRoute dr : directRoutes) {
			if (dr.getFromStation().equals(fromStation)) {
				result.add(dr);
			}
		}
		return result;
	}

	/**
	 * @return the Collection<DirectRoute> of all routes from a specific station
	 */
	public Collection<DirectRoute> getRoutesTo(String toStation) {
		Collection<DirectRoute> result = new HashSet<DirectRoute>();
		for (DirectRoute dr : directRoutes) {
			if (dr.getToStation().equals(toStation)) {
				result.add(dr);
			}
		}
		return result;
	}

	/**
	 * @return the Collection<DirectRoute> of all routes for the company
	 */
	public Collection<DirectRoute> getAllDirectRoutes() {
		return new HashSet<DirectRoute>(directRoutes);
	}

	/**
	 * @return the quantity of all routes for the company
	 */
	public int getDirectRoutesCount() {
		return getAllDirectRoutes().size();
	}

	/**
	 * @return The number of stations with service by this TrainCompany. To be
	 *         clearer: - Take the union of all stations (from and to) from
	 *         this.getAllDirectRoutes() - Count the unique number of stations
	 *         (i.e. You only count a station once, even if there are multiple
	 *         routes from/to this station)
	 */
	public int getStationsCount() {
		if (stationCountChanged) {
			Collection<String> stations = new HashSet<String>();
			for (DirectRoute dr : directRoutes) {
				stations.add(dr.getFromStation());
				stations.add(dr.getToStation());
			}
			stationCount = stations.size();
			stationCountChanged = false;
		}
		return stationCount;
	}

}
