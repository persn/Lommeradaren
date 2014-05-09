package no.kystverket.lommeradaren.markers;

/**
 * Simple class containing a name and base url for a DataSource we can query for
 * JSON information strings
 * 
 * @author Per Olav Flaten
 * 
 */
public class DataSource {

	private String name;
	private String url;

	public DataSource(String name, String url) {
		this.name = name;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
