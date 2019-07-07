//
// GreedySearch
//
// This class implements the greedy best-first search algorithm discussed on
// page 92 of Artificial Intelligence: A Modern Approach by Peter Norvig and
// Stuart J. Russell, which tries to expand the node closest to the goal by
// just using the heuristic function. To complete this assignment, I received
// help from Jacob Rafati Heravi, in which I was redirected to lecture 7 of
// the slides uploaded.
//
// Gerardo Graciano -- Thur Oct 04 17:27:32 PST 2018
//


import java.util.*;


public class GreedySearch {
	
	// GreedySeach uses the heuristic function.
	public Heuristic function;
	public StreetMap graph;
	public String initialLoc;
	public String destinationLoc;
	public int limit, expansionCount = 0;
	
	// sortedFrontier sorts by h(n), the estimated cost of the cheapest path
	// from n to the goal.
	public SortedFrontier sortedFrontier = new SortedFrontier(SortBy.h);
	// explored keeps track of the locations explored.
	public Set<String> explored = new HashSet<String>();
	
	public GreedySearch(StreetMap graph, String initialLoc, String destinationLoc, int limit) {
		this.function = new GoodHeuristic(graph, graph.findLocation(destinationLoc));
		this.graph = graph;
		this.initialLoc = initialLoc;
		this.destinationLoc = destinationLoc;
		// 0-999 produces a depth of 1000.
		this.limit = limit - 1;
	}
	
	public Node search(boolean exploredCheck) {
		// Prepares a new search.
		newSearch();
		
		Node parent = new Node(graph.findLocation(initialLoc));
		
		// Returns parent if initialLoc and destinationLoc are the same,
		// else it adds it to the sorted frontier and an entry is created in
		// explored.
		if (initialLoc == destinationLoc) {
			return parent;
		}
		sortedFrontier.addSorted(parent);
		explored.add(parent.loc.name);
		
		// Checks if the sorted frontier is empty and if the depth is greater
		// than the limit. Returns parent if a solution is found, else it
		// returns null.
		while (!sortedFrontier.isEmpty() && parent.depth < limit) {
			// Removes the node at the top of the sorted frontier.
			parent = sortedFrontier.removeTop();
			
			// Checks to see if the location of parent matches destinationLoc,
			// else expands parent and increments expasionCount.
			if (parent.isDestination(destinationLoc)) {
				return parent;
			}
			// parent is expanded according the heuristic function.
			parent.expand(function);
			expansionCount++;
			
			// If false, does not check for reoccurring locations, else
			// checks for reoccurring locations using the contents in
			// explored.
			if (!exploredCheck) {
				sortedFrontier.addSorted(parent.children);
			} else {
				for (Node child : parent.children) {
					if (!explored.contains(child.loc.name)) {
						sortedFrontier.addSorted(child);
						explored.add(child.loc.name);
					}
				}
			}
		}
		
		return null;
	}
	
	private void newSearch() {
		// Empties the sorted frontier if not empty.
		while (!sortedFrontier.isEmpty()) {
			sortedFrontier.removeTop();
		}
		// Clears explore and sets the expansion count back to 0.
		explored.clear();
		expansionCount = 0;
		
		return;
	}

}
