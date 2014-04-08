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
	private String mmsi;
	private double distance;
	private String has_detail_page;
	private String webpage;
	private String positionTime;
	private String imo;
	private String speed;
	private String course;

	public POI(int id, String name, double lat, double lng, double alt, String mmsi,
			double distance, String has_detail_page, String webpage,
			String positionTime, String speed, String course, String imo) {
		this.id = id;
		this.name = name;
		this.lat = lat;
		this.lng = lng;
		this.alt = alt;
		this.mmsi = mmsi;
		this.distance = distance;
		this.has_detail_page = has_detail_page;
		this.webpage = webpage;
		this.positionTime = positionTime;
		this.speed = speed;
		this.course = course;
		this.imo = imo;
		
	}

	public String getSpeed() {
		return speed;
	}

	public String getCourse() {
		return course;
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

	public String getMmsi() {
		return mmsi;
	}

	public String getImo() {
		return imo;
	}
	

}
