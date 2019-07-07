//MapBox


import java.util.*;


public class MapBox {
	public List<Location> locations;
	
	Set<String> loc;
	Hashtable<String, Double> box;
	
	public MapBox() {
		this.locations = new ArrayList<Location>();
		this.loc = new HashSet<String>();
		this.box = new Hashtable<String, Double>();
	}
	
	public double Westmost() {
		return box.get("xmin");
	}
	
	public double Eastmost() {
		return box.get("xmax");
	}
	
	public double Southmost() {
		return box.get("ymin");
	}
	
	public double Northmost() {
		return box.get("ymax");
	}
	
	public boolean recordLocation(Location loc) {
		if (this.locations.size() == 0) {
			this.locations.add(loc);
			this.loc.add(loc.name);
			this.box.put("xmin", loc.longitude);
			this.box.put("xmax", loc.longitude);
			this.box.put("ymin", loc.latitude);
			this.box.put("ymax", loc.latitude);
			return true;
		}
		
		if (this.loc.contains(loc.name)) {
			return false;
		} else {
			this.loc.add(loc.name);
			this.locations.add(loc);
			
			if (loc.longitude < box.get("xmin")) {
				box.put("xmin", loc.longitude);
			}
			if (loc.longitude > box.get("xmax")) {
				box.put("xmax", loc.longitude);
			}
			if (loc.latitude < box.get("ymin")) {
				box.put("ymin", loc.latitude);
			}
			if (loc.latitude > box.get("ymax")) {
				box.put("ymax", loc.latitude);
			}
			
			return true;
		}
	}

}
