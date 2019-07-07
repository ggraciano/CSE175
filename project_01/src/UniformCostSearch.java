//
// UniformCostSearch
//
// This class implements the uniform-cost search algorithm discussed on page
// 84 of Artificial Intelligence: A Modern Approach by Peter Norvig and
// Stuart J. Russell, which uses a strategy that expands the node with the 
// lowest path cost. To complete this assignment, I received help from Jacob
// Rafati Heravi, in which I was redirected to lecture 7 of the slides
// uploaded.
//
// Gerardo Graciano -- Fri Oct 05 09:21:56 PST 2018
//


import java.util.*;


public class UniformCostSearch {
	
	public StreetMap graph;
	public String initialLoc;
	public String destinationLoc;
	public int limit, expansionCount = 0;
	
	// sortedFrontier sorts by g(n), the cost of the path from the start node
	// to n.
	public SortedFrontier sortedFrontier = new SortedFrontier();
	// explored keeps track of the locations explored.
	public Set<String> explored = new HashSet<String>();
	
	public UniformCostSearch(StreetMap graph, String initialLoc, String destinationLoc, int limit) {
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
			// parent is expanded according to the path cost function.
			parent.expand();
			expansionCount++;
			
			// If false, does not check for reoccurring locations, else
			// checks for reoccurring locations using the contents in
			// explored.
			if (!exploredCheck) {
				sortedFrontier.addSorted(parent.children);
			} else {
				for (Node child : parent.children) {
					// Checks to see if child is in the explored set. If child
					// is not in the explored set, child is added to the
					// explored set.
					if (!explored.contains(child.loc.name)) {
						sortedFrontier.addSorted(child);
						explored.add(child.loc.name);
					} else {
						// If child is in the explored set, checks to see if
						// child is in the sorted frontier. If true, a
						// comparison is made to see which child has the
						// lowest cost. The child with the higher cost is
						// replaced by the child with the lower cost.
						if (sortedFrontier.contains(child.loc.name)) {
							Node temp = sortedFrontier.find(child);
							if (child.partialPathCost < temp.partialPathCost) {
								sortedFrontier.remove(temp);
								sortedFrontier.addSorted(child);
							}
						}
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
