package edu.auburn.csse.comp3710.group10.auexplore;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends FragmentActivity implements LocationListener {

	//@Override
	//This is new stuff
	// new comment
	// fourth comment
	// last change
	
	private GoogleMap map;
	private LocationManager locationManager;
	String locationProvider;
	private LatLng currentLatLng;
	private final String LOCATIONS_FILE_NAME = "AULocations.json";
	private LatLng[] markersArray = {new LatLng(32.60633, -85.48747), new LatLng(32.60486, -85.48632),
											new LatLng(32.60584, -85.48336), new LatLng(32.60628, -85.48902),
											new LatLng(32.60449, -85.48524), new LatLng(32.60228, -85.48658),
											new LatLng(32.603395, -85.48624), new LatLng(32.59599, -85.48351),
											new LatLng(32.60162, -85.48801), new LatLng(32.60277, -85.49186),
											new LatLng(32.60347, -85.48483), new LatLng(32.59959, -85.48537),
											new LatLng(32.60087, -85.48647), new LatLng(32.60402, -85.49243),
											new LatLng(32.60043, -85.49002)};
	private final String[] descriptions = {"A group of state-of-the-art buildings forming a complex that serves as the hub of the engineering campus. Construction on the buildings ended in early 2011. The center supports multidisciplinary collaboration to advance technology in transportation and other important and emerging areas of engineering. Named for U.S. Senator Richard Shelby, it holds research laboratories, classrooms, lecture halls and administrative offices. When completed, the center will comprise nearly 200,000 square feet.",
			"Home to the AU Department of Electrical Engineering."
			+ "Completed in 1984, Broun Hall was named after the university's fourth president, William Leroy Broun. A Virginia scholar, scientist and teacher, Broun helped bring Auburn to the forefront of scientific institutions in the South during his tenure as president.",
			"In August 2007 Auburn University rededicated its aerospace engineering building in honor of distinguished Auburn alumnus Charles E. “Buddy” Davis. The building, which houses the Department of Aerospace Engineering, will now be called the Charles E. Davis Aerospace Engineering Hall, or simply Davis Hall. ",
			"Lowder Business Building was designed to bring all activities of the college under one roof on the former site of Magnolia and Bullard Halls, which were razed in the late 1980’s after they became obsolete. Lowder was completed in 1992 and is utilized for the classrooms and offices of the College of Business. ",
			"Foy Hall, named for the late James E. Foy, a beloved former dean, contains a food court with five vendor choices. Freshman Year Experience, Study Abroad, and International Education have their offices here. ",
			"A gathering place for Auburn students, the Student Center was completed in 2008. Auburn has more than 300 student organizations; many of their offices are housed here, as well as the Office of the Vice President for Student Affairs. The facility houses seven dining options including a full-service Starbucks. Foy Information Desk, just inside the entrance to the Student Center, was featured in Oprah’s O Magazine and on The Today Show as one of the top 10 phone numbers to have in your cell phone.",
			"Haley Center is home to the College of Education and many of Auburn’s freshman core classes. Classrooms are located on the first two floors, while the tallest part of the building serves as faculty offices. Professors hold scheduled office hours on a weekly basis to meet with students. The College of Education has 23 bachelor’s degree options. The pedestrian area in front of Haley Center is commonly referred to as “The Concourse.” The University Bookstore is located on the first floor of Haley Center.",
			"Donald E. Davis led efforts to establish the Arboretum in 1963. With an extensive collection of native plants, an abundance of wildlife and an open-air pavilion, it is an ideal place for many types of environmental education.",
			"Begun in 1939, the stadium seats 85,000. It was named for Ralph \"Shug\" Jordan and Clifford Leroy Hare. Jordan, a legendary football coach for over two decades at Auburn, retired in 1975 as Auburn's winningest coach. Hare, who played on Auburn's first football team in 1892, was dean of the School of Chemistry and state chemist from 1930 until 1948.",
			"Completed in 2010, this $92 million facility has a capacity of 9,600, including over 29,000 square feet of student-athlete space, a two-court practice facility, coaches' offices, the Auburn University Athletic Ticket Office, an AU Team Store, the Lovelace Museum, two food courts and many other amenities.",
			"Built in 1915, Cater Hall originally served as the President's Home. It is named for Katharine Cooper Cater, dean of women from 1946 until 1976, and dean of student life from 1976 until her death in 1980. The Social Center at Auburn University, now designated by the Board of Trustees as Katharine Cooper Cater Hall, was erected in 1915 to serve as the President's home. The stately, two-story, neo-classical building was designed by Joseph Hudnut.",
			"The first of two buildings to provide modern facilities in chemistry, this building houses classroom and laboratory facilities for student instruction as well as faculty offices and meeting rooms.",
			"The center for math and sciences. Parker Hall was built in 1963 and was named after William Vann Parker. Its current use has computer centers and classrooms for math, chemistry, and physics. ",
			"The Village housing area was completed in 2009 and consists of eight residence halls set up as super-suites with four private bedrooms, two shared bathrooms, and a common living/dining area. Auburn’s 17 social sororities are housed in three of the buildings and a fourth building houses Honors College students. Four buildings are co-educational housing for freshmen and upperclassmen.",
			"The site of Southeastern Conference baseball excitement since 1950, Samford Stadium-Hitchcock Field at Plainsman Park was voted the best collegiate baseball facility in the country by Baseball America prior to the 2003 season."};
	private final String[] names = {"Shelby Center", "Broun Hall", "Davis Hall", "Lowder Business Building", "Foy Dining",
									"Student Center", "AU BookStore/Haley", "Arboretum", "Jordan-Hare", "Auburn Arena",
									"Cater Hall", "Chemistry Building", "Parker Hall", "The Village", "Plainsman Park"};
	private ArrayList<AULocation> locationList = new ArrayList<AULocation>();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.setMyLocationEnabled(true);
        
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Criteria criteria = new Criteria();
        locationProvider = locationManager.getBestProvider(criteria, false);
        
        Location location = locationManager.getLastKnownLocation(locationProvider);
        
        if (gpsEnabled) {
	        if (location != null) {
	        	onLocationChanged(location);
	        }
        }
        else {
        	Toast.makeText(MainActivity.this, "Please enable your GPS", Toast.LENGTH_LONG).show();
        }

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        locationList = getLocations();
        addMarkers(Arrays.asList(markersArray));
    }
    
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	writeToJSON(LOCATIONS_FILE_NAME, locationList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.travel_log) {
        	Intent intent = new Intent();
			intent.setClass(getApplicationContext(), TravelLogActivity.class);
			startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

	@Override
	public void onLocationChanged(Location newLocation) {
		// TODO Auto-generated method stub
		 currentLatLng = new LatLng(newLocation.getLatitude(), newLocation.getLongitude());
		 map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17));
		 
	//	 map.addMarker(new MarkerOptions().position(currentLatLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)).flat(true));
	}


	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	public void addMarkers(List<LatLng> markers) {
		for (int i = 0; i < markers.size(); i++) {
			if (!locationList.get(i).isFound()) {
				map.addMarker(new MarkerOptions().position(markers.get(i))
						.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
						.title(locationList.get(i).getName()));
				map.setOnMarkerClickListener(new OnMarkerClickListener() {
					
					@Override
					public boolean onMarkerClick(Marker marker) {
						if (atLocation(marker.getPosition())) {
							Toast.makeText(getApplicationContext(), "Congratulations, you have added a location to your Travel Log!", Toast.LENGTH_LONG).show();
							marker.remove();
							markFound(marker.getTitle());
							writeToJSON(LOCATIONS_FILE_NAME, locationList);
							
							// Launch Travel Log Activity
							Intent intent = new Intent();
							intent.setClass(getApplicationContext(), TravelLogActivity.class);
							startActivity(intent);
						}
						else {
							Toast.makeText(getApplicationContext(), "Get closer to the location to pick up the pin.", Toast.LENGTH_LONG).show();
							marker.setSnippet("Distance to: " + Integer.toString((int)Math.round(distanceTo(marker.getPosition()))) + " meters");
							marker.showInfoWindow();
						}
						return false;
					}
				});
			}
		}
	}
	
	public ArrayList<AULocation> getLocations() {
		try {
			Gson gson = new Gson();
			locationList = gson.fromJson(readJSONFile(LOCATIONS_FILE_NAME), new TypeToken<ArrayList<AULocation>>(){}.getType());
			if (locationList == null) {
				createFirstLocationList();
			}
		}
		catch (FileNotFoundException e){
			createFirstLocationList();
			writeToJSON(LOCATIONS_FILE_NAME, locationList);
		}
		return locationList;
	}
	
	public int findPointVal(String title) {
		int pointVal = -1;
		for (int i = 0; i < locationList.size(); i++) {
			if (locationList.get(i).getName().equals(title)) {
				pointVal = locationList.get(i).getPointVal();
				return pointVal;
			}
		}
		return pointVal;
	}
	
	public void markFound(String title) {
		for (int i = 0; i < locationList.size(); i++) {
			if (locationList.get(i).getName().equals(title)) {
				locationList.get(i).setFound(true);
			}
		}
	}
	
	public void createFirstLocationList() {
		locationList = new ArrayList<AULocation>();
		ArrayList<Integer> pointVals = intializePointVals();
		for (int i = 0; i < names.length; i++) {
			AULocation auLocation = new AULocation();
			auLocation.setDescription(descriptions[i]);
			auLocation.setName(names[i]);
			LatLng latLng = markersArray[i];
			auLocation.setLatLng(latLng);
			auLocation.setFound(false);
			auLocation.setPointVal(pointVals.get(i));
			locationList.add(auLocation);
		}
	}
	
	private <T> void writeToJSON(String filename, T object){
		Gson gson = new Gson();
		String projectsJSON = gson.toJson(object);
		
		FileOutputStream fos;
		try {
			fos = openFileOutput(filename, Context.MODE_PRIVATE);
				
			fos.write(projectsJSON.getBytes());
				
			   fos.close();
		   	} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
	}
	
	private String readJSONFile(String filename) throws FileNotFoundException{
		FileInputStream fis;
		String contents = "";
		try {
			fis = openFileInput(filename);
			byte[] inputBuffer = new byte[fis.available()];
			fis.read(inputBuffer);
			Gson gson = new Gson();
			contents = new String(inputBuffer);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return contents;
	}
	
	private ArrayList<Integer> intializePointVals() {
		ArrayList<Integer> pointVals = new ArrayList<Integer>();
		pointVals.add(3);
		pointVals.add(3);
		pointVals.add(3);
		pointVals.add(3);
		pointVals.add(3);
		pointVals.add(5);
		pointVals.add(5);
		pointVals.add(5);
		pointVals.add(5);
		pointVals.add(5);
		pointVals.add(10);
		pointVals.add(10);
		pointVals.add(10);
		pointVals.add(15);
		pointVals.add(15);
		Collections.shuffle(pointVals);
		return pointVals;
	}
	
	private double distanceTo(LatLng location) {
		Location currentLocation = locationManager.getLastKnownLocation(locationProvider);
		LatLng currLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
		double distance = distanceBetween(currLatLng, location);
		return distance;
	}
	
	private double distanceBetween(LatLng first, LatLng second) {
		float[] results = new float[1];
		Location.distanceBetween(first.latitude, first.longitude, second.latitude, second.longitude, results);
		return (double) results[0];
	}

	private boolean atLocation(LatLng location) {
		Location currentLocation = locationManager.getLastKnownLocation(locationProvider);
		LatLng currLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
		double accuracy = (double) currentLocation.getAccuracy();
		if (accuracy != 0) {
			double distance = distanceBetween(currLatLng, location);
			Log.d("DISTANCE", Double.toString(distance));
			Log.d("ACCURACY", Double.toString(accuracy));
			return distance <= accuracy;
		}
		else {
			// TODO: What if no accuracy info?
			return false;
		}
	}

}
