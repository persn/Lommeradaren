package no.kystverket.lommeradaren.photo;

import no.kystverket.lommeradaren.markers.POI;

/**
 * 
 * @author Henrik Reitan
 *
 */
public class Ship extends POI {

	private double speed;
	private String lastPort;
	private String destination;
	private double course;


	public Ship(int id, String name, double lat, double lng, double alt,
			String mmsi, double distance, String has_detail_page, String webpage,
			String positionTime, String imo, double speed, String lastPort,
			String destination, double course) {
		super(id, name, lat, lng, alt, mmsi, distance, has_detail_page,
				webpage, positionTime);
		this.speed = speed;
		this.lastPort = lastPort;
		this.destination = destination;
		this.course = course;
	}

	public double getSpeed() {
		return speed;
	}

	public String getLastPort() {
		return lastPort;
	}

	public String getDestination() {
		return destination;
	}

	public double getCourse() {
		return course;
	}

}
