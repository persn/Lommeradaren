package no.kystverket.lommeradaren.markers;

/**
 * 
 * @author Henrik Reitan
 * 
 */
public class POI {

	private int id;
	private String name;
	private double lat;
	private double lng;
	private double alt;
	//private int mmsi;
	private double distance;
	private String has_detail_page;
	private String webpage;
	private String positionTime;
	//private String imo;

	public POI(int id, String name, double lat, double lng, double alt,
			double distance, String has_detail_page, String webpage,
			String positionTime) {
		this.id = id;
		this.name = name;
		this.lat = lat;
		this.lng = lng;
		this.alt = alt;
		this.distance = distance;
		this.has_detail_page = has_detail_page;
		this.webpage = webpage;
		this.positionTime = positionTime;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public double getLat() {
		return lat;
	}

	public double getLng() {
		return lng;
	}

	public double getAlt() {
		return alt;
	}

	public double getDistance() {
		return distance;
	}

	public String getHas_detail_page() {
		return has_detail_page;
	}

	public String getWebpage() {
		return webpage;
	}

	public String getPositionTime() {
		return positionTime;
	}

}
