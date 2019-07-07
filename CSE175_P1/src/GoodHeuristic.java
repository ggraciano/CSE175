//
// GoodHeuristic
//
// This class extends the Heuristic class, providing a reasonable
// implementation of the heuristic function method. The provided "good"
// heuristic function is admissible. To complete this assignment, the
// following resource was used to construct an appropriate heuristic
// function:
// 		http://theory.stanford.edu/~amitp/GameProgramming/Heuristics.html
//
// Gerardo Graciano -- Thur Oct 04 04:01:58 PST 2018
//


import java.lang.Math;


public class GoodHeuristic extends Heuristic {
	
	public StreetMap graph;
	public double velocity;
	
	// Constructor with graph StreetMap and destination Location specified ...
	public GoodHeuristic(StreetMap graph, Location destination) {
		this.graph = graph;
		// velocity keeps the velocity of the fastest route on the graph
		this.velocity = velocity();
		super.setDestination(destination);
	}
	
	// heuristicValue -- Return the appropriate heuristic value for the
	// given search tree node. Note that the cost assigned to road segments
	// is to be taken as a time cost. The heuristic value returned must be
	// in the same scale.
	@Override
	public double heuristicValue(Node thisNode) {
		double hVal = 0.0;
		
		// Distance divided by velocity returns time.
		hVal = euclideanDistance(thisNode.loc, destination)/velocity;
		
		return (hVal);
	}
	
	// velocity -- Return an estimate of the minimum cost from any location
	// to the destination. This results in an "admissible" heuristic.
	public double velocity() {
		double temp, velocity = 0.0;
		
		// Calculates the fastest route between any two locations in the
		// graph by comparing all of the velocities between any two locations.
		for (Location location : graph.locations) {
			for (Road road : location.roads) {
				temp = euclideanDistance(road.fromLocation, road.toLocation)/road.cost;
				if (velocity < temp) {
					velocity = temp;
				}
			}
		}
		
		return velocity;
	}
	
	// euclideanDistance -- Return the Euclidean distance between two locations.
	public double euclideanDistance(Location loc1, Location loc2) {
		return Math.hypot(loc1.latitude - loc2.latitude, loc1.longitude - loc2.longitude);
	}

}
