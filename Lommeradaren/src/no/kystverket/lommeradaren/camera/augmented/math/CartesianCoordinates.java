package no.kystverket.lommeradaren.camera.augmented.math;

/**
 * Currently a placeholder class to test basic math. This class should
 * eventually calculator position from user to object relatively. Will be
 * calculated with data from http://en.wikipedia.org/wiki/World_Geodetic_System
 * 
 * major (equatorial) radius a = 6378137 m at the equator flattening f =
 * 1*298.257223563 The polar semi-minor axis b then equals a times (1-f), or
 * 6356752.3142 m
 * 
 * From GPS we have alt, lat and lon Numerical Analysis Second Edition by Timoty
 * Sauer page 241 and
 * http://gis.stackexchange.com/questions/23793/how-do-i-calculate
 * -a-xyz-position-of-a-gps-position-relative-to-an-other-gps-pos says: r =
 * 6371000 + alt x = r*cos(lat)*cos(lon) y = r*cos(lat)*sin(lon) z = r*sin(lat)
 * multiply z with (1 - 1/298.257223563)
 * 
 * 
 * @author Per Olav Flaten
 * 
 */
public class CartesianCoordinates {

	private double distanceFromSatellite;
	private double xyz[] = new double[3];

	private final static double EQUATORIAL_RADIUS = 6356752.3142;

	public CartesianCoordinates(double alt, double lat, double lon) {
		this.setVectorValues(alt, lat, lon);
	}

	public double getX() {
		return this.xyz[0];
	}

	public double getY() {
		return this.xyz[1];
	}

	public double getZ() {
		return this.xyz[2];
	}

	/**
	 * Distance = sqrt((x_2 - x_1)^2 + (y_2 - y_1)^2 + (z_2 - z_1)^2)
	 * 
	 * @param otherPosition
	 * @return
	 */
	public double calcDistance(CartesianCoordinates otherPosition) {
		double distance = Math.sqrt(Math.pow(
				otherPosition.getX() - this.getX(), 2)
				+ Math.pow(otherPosition.getY() - this.getY(), 2)
				+ Math.pow(otherPosition.getZ() - this.getZ(), 2));
		return distance;
	}

	private void setVectorValues(double alt, double lat, double lon) {
		lat = Math.toRadians(lat);
		lon = Math.toRadians(lon);

		this.setDistanceFromSatellite(alt);
		this.setX(lat, lon);
		this.setY(lat, lon);
		this.setZ(lat);
	}

	private void setDistanceFromSatellite(double alt) {
		this.distanceFromSatellite = EQUATORIAL_RADIUS + alt;
	}

	private void setX(double lat, double lon) {
		this.xyz[0] = this.distanceFromSatellite * Math.cos(lat)
				* Math.cos(lon);
	}

	private void setY(double lat, double lon) {
		this.xyz[1] = this.distanceFromSatellite * Math.cos(lat)
				* Math.sin(lon);
	}

	/**
	 * TODO --- Z-axis might need adjustments for flattening of the earth.
	 * @param lat
	 */
	private void setZ(double lat) {
		this.xyz[2] = (this.distanceFromSatellite * Math.sin(lat)); 
	}

}
