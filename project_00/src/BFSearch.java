//
// BFSearch
//
// This class implements the depth-first search algorithm discussed on page
// 81 of Artificial Intelligence: A Modern Approach by Peter Norvig and
// Stuart J. Russell, which uses a strategy that expands the root node and
// then its successors in a search tree to find the goal node. To complete
// the assignment, I worked with Ahmed Alhag and the used the following
// resource:
//      https://docs.oracle.com/javase/7/docs/api/java/util/HashSet.html
//
// Gerardo Graciano -- Mon Sept 17 10:24:58 PST 2018
//


import java.util.*;


public class BFSearch {

	public Map graph;
	public String initialLoc;
	public String destinationLoc;
	public int limit, expansionCount = 0;
	
	public Frontier frontier = new Frontier();
	// explored keeps track of the locations explored.
	public Set<String> explored = new HashSet<String>();
	
	public BFSearch(Map graph, String initialLoc, String destinationLoc, int limit) {
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
		// else it adds it to the frontier and an entry is created in
		// explored.
		if (initialLoc == destinationLoc) {
			return parent;
		}
		frontier.addToBottom(parent);
		explored.add(parent.loc.name);
		
		// Checks if the frontier is empty and if the depth is greater than
		// the limit. Returns parent if a solution is found, else it returns
		// null.
		while (!frontier.isEmpty() && parent.depth < limit) {
			// Removes the node at the top of the frontier.
			parent = frontier.removeTop();
			
			// Checks to see if the location of parent matches destinationLoc,
			// else expands parent and increments expasionCount.
			if (parent.isDestination(destinationLoc)) {
				return parent;
			}
			parent.expand();
			expansionCount++;
			
			// If false, does not check for reoccurring locations, else
			// checks for reoccurring locations using the contents in
			// explored.
			if (!exploredCheck) {
				for (Node child : parent.children) {
					frontier.addToBottom(child);
				}
			} else {
				for (Node child : parent.children) {
					if (!explored.contains(child.loc.name)) {
						frontier.addToBottom(child);
						explored.add(child.loc.name);
					}
				}
			}
		}
		
		return null;
	}
	
	public void newSearch() {
		// Empties the frontier if not empty.
		while (!frontier.isEmpty()) {
			frontier.removeTop();
		}
		// Clears explore and sets the expansion count back to 0.
		explored.clear();
		expansionCount = 0;
		
		return;
	}

}
