package no.kystverket.lommeradaren.photo;

import no.kystverket.lommeradaren.markers.POI;

/**
 * 
 * @author Henrik Reitan
 *
 */
public class Ship extends POI {

	private String speed;
	private String lastPort;
	private String destination;
	private String course;


	public Ship(int id, String name, double lat, double lng, double alt,
			String mmsi, double distance, String has_detail_page, String webpage,
			String positionTime, String imo, String speed, String lastPort,
			String destination, String course) {
		super(id, name, lat, lng, alt, mmsi, distance, has_detail_page,
				webpage, positionTime, speed, course, imo);
		this.speed = speed;
		this.lastPort = lastPort;
		this.destination = destination;
		this.course = course;
	}

	public String getSpeed() {
		return speed;
	}

	public String getLastPort() {
		return lastPort;
	}

	public String getDestination() {
		return destination;
	}

	public String getCourse() {
		return course;
	}

}
