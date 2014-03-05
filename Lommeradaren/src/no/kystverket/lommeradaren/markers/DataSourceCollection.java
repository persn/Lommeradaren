package no.kystverket.lommeradaren.markers;

import java.util.ArrayList;
import java.util.List;

/**
 * Container class for DataSourceHandlers
 * 
 * @author Per Olav Flaten
 *
 */
public class DataSourceCollection {

	private List<DataSourceHandler> dataSourceList = new ArrayList<DataSourceHandler>();
	
	public DataSourceCollection(String[] dataSourceArray,String lat, String lng, String alt){
		for(int i=0;i<dataSourceArray.length;i++){
			String[] currentDataSource = dataSourceArray[i].split("\\|");
			DataSource newDataSource = new DataSource(currentDataSource[0],currentDataSource[1]);
			dataSourceList.add(new DataSourceHandler(newDataSource,lat,lng,alt,"50"));
		}
	}
	
	public DataSourceHandler getDataSourceHandler(int i){
		return this.dataSourceList.get(i);
	}
	
	public POI getPOI(int i, int j){
		return this.dataSourceList.get(i).getPOI(j);
	}
	
	public int getDataSourceListSize(){
		return dataSourceList.size();
	}
	
	public int getPOIArrayLength(int i){
		return this.dataSourceList.get(i).getPointOfInterestsSize();
	}
}
