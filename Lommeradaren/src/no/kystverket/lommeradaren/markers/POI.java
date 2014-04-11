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

	public POI(int id, String name, double lat, double lng, double alt,
			String mmsi, double distance, String has_detail_page,
			String webpage, String positionTime, String speed, String course,
			String imo) {
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

	public POI(String info) {
		String[] set = info.split(",");
		for (int i = 0; i < set.length; i++) {
			String[] data = set[i].split(";");
			if(data.length > 1){
				if (data[0].equals("id")) {
					this.id = Integer.parseInt(data[1]);
				} else if (data[0].equals("title")) {
					this.name = data[1];
				} else if (data[0].equals("lat")) {
					this.lat = Double.parseDouble(data[1]);
				} else if (data[0].equals("lng")) {
					this.lng = Double.parseDouble(data[1]);
				} else if (data[0].equals("elevation")) {
					this.alt = Double.parseDouble(data[1]);
				} else if (data[0].equals("mmsi")) {
					this.mmsi = data[1];
				} else if (data[0].equals("distance")) {
					this.distance = Double.parseDouble(data[1]);
				} else if (data[0].equals("has_detail_page")) {
					this.has_detail_page = data[1];
				} else if (data[0].equals("webpage")) {
					this.webpage = data[1];
				} else if (data[0].equals("positionTime")) {
					this.positionTime = data[1];
				} else if (data[0].equals("speed")) {
					this.speed = data[1];
				} else if (data[0].equals("course")) {
					this.course = data[1];
				} else if (data[0].equals("imo")) {
					this.imo = data[1];
				}
			}
		}
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

	public String toString(){
		return "id: "+id+" name: "+name+" lat: "+lat+" lng: "+lng+" alt: "+alt+" mmsi: "+mmsi+" distance: "+distance+" has_detail_page: "+has_detail_page+" webpage: "+webpage+" imo: "+imo+" speed: "+speed+" course: "+course;
	}
}
