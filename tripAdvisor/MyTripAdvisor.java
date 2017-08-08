package tripAdvisor;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

public class MyTripAdvisor {


	public TrainCompany MyAdvisorCompany;

	public MyTripAdvisor() {
		String name = "CompanyManager";
		MyAdvisorCompany = new TrainCompany(name);
	}

	/**
	 * Adds the trainCompany and all the DirectRoute along with it.
	 * 
	 * @param trainCompany
	 */
	public void addTrainCopmany(TrainCompany trainCompany) {
		for (DirectRoute dr : trainCompany.getAllDirectRoutes()) {
			double price = dr.getPrice();
			String from_station = dr.getFromStation().toString();
			String to_station = dr.getToStation().toString();
			if (MyAdvisorCompany.getDirectRoute(from_station, to_station) == null) {
				DirectRoute newInsertedRoute = MyAdvisorCompany.createOrUpdateDirectRoute(from_station, to_station,
						price);
				newInsertedRoute.setTrainCompany(trainCompany);

			} else if (MyAdvisorCompany.getDirectRoute(from_station, to_station).getPrice() > dr.getPrice()) {
				DirectRoute newInsertedRoute = MyAdvisorCompany.createOrUpdateDirectRoute(from_station, to_station,
						price);
				newInsertedRoute.setTrainCompany(trainCompany);

			}
		}
	}

	/**
	 * Return the price of a cheapest trip from <code>fromStation</code> to
	 * <code>toStation</code>. Return -1, if there is no trip between the two
	 * specified stations.
	 */
	public double getCheapestPrice(String fromStation, String toStation) {

		// A virtual company to make life easier(because of all kinds of
		// changes)
		TrainCompany virtualCompany = new TrainCompany("virtualComp1");

		// Set the company of Routes in virtual company to their previous
		// company
		for (DirectRoute dr : MyAdvisorCompany.getAllDirectRoutes()) {
			DirectRoute newRoute = virtualCompany.createOrUpdateDirectRoute(dr.getFromStation(), dr.getToStation(),
					dr.getPrice());
			newRoute.setTrainCompany(dr.getTrainCompany());
		}
		// If there is no route to destination, throw a exception.
		if (virtualCompany.getRoutesTo(toStation).isEmpty()) {
			TrainCompany.cleanup();
			return -1;
		}
		// Create the temporary group for routes => routesGroup with all the
		// route following the start station.
		// This is the initialization to routesGroup:
		// 1, finds all the routes that follows the source,
		// 2, assigns the price of the routes to its cumPrice.
		Collection<DirectRoute> routesGroup = virtualCompany.getDirectRoutesFrom(fromStation);
		for (DirectRoute dR : routesGroup) {
			dR.cumPrice = dR.getPrice();
			routesGroup.add(dR);
		}

		// while there are routes in routesGroup, we need to find the routes
		// that follows them:
		while (!routesGroup.isEmpty()) {
			// 1, finds the minimum directRoute min_route in current routesGroup
			// 2, finds all the routes following the min_route and make them in
			// a Collection followingRoutes.
			// 3, for each directRoute in followingRoutes, if the new cumulative
			// price is smaller than last one:
			// (1) set its new cumPrice and previous
			// (2) add it to routesGroup
			// (3) save the info back to virtual company.
			// 4, finally removes the min_route from routesGroup
			DirectRoute min_route = searchMin(routesGroup);
			Collection<DirectRoute> followingRoutes = virtualCompany.getDirectRoutesFrom(min_route.getToStation());
			for (DirectRoute dr : followingRoutes) {
				if (dr.cumPrice > min_route.cumPrice + dr.getPrice()) {
					dr.cumPrice = min_route.cumPrice + dr.price;
					dr.previous = min_route;
					routesGroup.add(dr);
					// save cumPrice and previous back to virtual company(keep
					// track of the info)
					virtualCompany.getDirectRoute(dr.getFromStation(), dr.getToStation()).cumPrice = dr.cumPrice;
					virtualCompany.getDirectRoute(dr.getFromStation(), dr.getToStation()).previous = dr.previous;
				}
			}
			routesGroup.remove(min_route);
		}
		TrainCompany.cleanup();
		// Following are for the value to be returned:
		// Finds the route which has the minimum cumPrice among the routes
		// leading to destination.
		DirectRoute finalRoute = searchMin(virtualCompany.getRoutesTo(toStation));
		// Checks if the the finalRoute is a valid route leading to destination.
		if (finalRoute == null) { // there are ways leading to destination
			return -1;
		} else {
			return finalRoute.cumPrice;
		}
	}

	// This is the helper function which can find the route with the cheapest
	// culPrice
	public static DirectRoute searchMin(Collection<DirectRoute> routes) {

		double min_cumPrice = Double.POSITIVE_INFINITY;
		DirectRoute temp = null;

		for (DirectRoute dr : routes) {
			if (dr.cumPrice < min_cumPrice) {
				temp = dr;
				min_cumPrice = dr.cumPrice;
			}
		}
		return temp;
	}

	/**
	 * Return a cheapest trip from <code>fromStation</code> to
	 * <code>toStation</code>, as a list of DirectRoute objects. Return null, if
	 * there is no trip between the two specified stations.
	 */
	public List<DirectRoute> getCheapestTrip(String fromStation, String toStation) throws IllegalArgumentException {
		if (fromStation.contentEquals(toStation)) {
			throw new IllegalArgumentException("fromStation and toStation cannot be the same.");
		}
		// A virtual company to make life easier(because of all kinds of
		// changes)
		TrainCompany virtualCompany = new TrainCompany("virtualComp2");

		// Set the company of Routes in virtual company to their previous
		// company
		for (DirectRoute dr : MyAdvisorCompany.getAllDirectRoutes()) {
			DirectRoute newRoute = virtualCompany.createOrUpdateDirectRoute(dr.getFromStation(), dr.getToStation(),
					dr.getPrice());
			newRoute.setTrainCompany(dr.getTrainCompany());
		}

		// If there is no route to destination, throw a exception.
		if (virtualCompany.getRoutesTo(toStation).isEmpty()) {
			TrainCompany.cleanup();
			return null;
		}

		// Create the temporary group for routes => routesGroup with all the
		// route following the start station.
		Collection<DirectRoute> routesGroup = virtualCompany.getDirectRoutesFrom(fromStation);
		for (DirectRoute dR : routesGroup) {
			dR.cumPrice = dR.getPrice();
			routesGroup.add(dR);
		}

		// while
		while (!routesGroup.isEmpty()) {
			// 1, finds all the routes that follows the source,
			// 2, assigns the price of the routes to its cumPrice.
			// 3, finds the route with minimum cumPrice and finally removes it.
			DirectRoute min_route = searchMin(routesGroup);
			Collection<DirectRoute> followingRoutes = virtualCompany.getDirectRoutesFrom(min_route.getToStation());
			for (DirectRoute dr : followingRoutes) {
				if (dr.cumPrice > min_route.cumPrice + dr.getPrice()) {
					dr.cumPrice = min_route.cumPrice + dr.price;
					dr.previous = min_route;
					routesGroup.add(dr);
					// save cumPrice and previous back to virtual company(keep
					// track of the info)
					virtualCompany.getDirectRoute(dr.getFromStation(), dr.getToStation()).cumPrice = dr.cumPrice;
					virtualCompany.getDirectRoute(dr.getFromStation(), dr.getToStation()).previous = dr.previous;
				}

			}
			routesGroup.remove(min_route);
		}
		TrainCompany.cleanup();
		// Following are for the value to be returned:
		// Finds the route which has the minimum cumPrice among the routes
		// leading to destination.
		DirectRoute finalRoute = searchMin(virtualCompany.getRoutesTo(toStation));
		// Checks if the the finalRoute is a valid route leading to destination.
		if (finalRoute != null) { // there are ways leading to destination
			List<DirectRoute> solution = new ArrayList<DirectRoute>();
			while (finalRoute != null) {
				solution.add(0, finalRoute); // Add to the first of the list,
												// because we are tracing the
												// routes back from the
												// destination.
				finalRoute = finalRoute.previous;
			}
			return solution;
		} else {
			return null;
		}
	}

}
