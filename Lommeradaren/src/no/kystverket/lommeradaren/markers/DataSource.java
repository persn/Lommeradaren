package no.kystverket.lommeradaren.markers;

public class DataSource {

	private String name;
	private String url;

	public DataSource(String name, String url) {
		super();
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
	
	//ADD CUSTOM EQUALS AND COMPARETO IF NEEDED

}
