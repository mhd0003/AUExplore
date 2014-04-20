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
	private LatLng[] markersArray = {new LatLng(32.606034, -85.487478), new LatLng(32.604924, -85.486713),
											new LatLng(32.605852, -85.483075), new LatLng(32.605798, -85.488953),
											new LatLng(32.604766, -85.484863), new LatLng(32.602487, -85.486959),
											new LatLng(32.603395, -85.486793), new LatLng(32.595024, -85.483477),
											new LatLng(32.603170, -85.490397), new LatLng(32.603181, -85.490632),
											new LatLng(32.603336, -85.484822), new LatLng(32.599580, -85.485168),
											new LatLng(32.600493, -85.486382), new LatLng(32.604048, -85.493804),
											new LatLng(32.599587, -85.490317)};
	private String[] descriptions;
	private String[] names;
	private ArrayList<AULocation> locationList = new ArrayList<AULocation>();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        descriptions = getResources().getStringArray(R.array.descriptions);
        names = getResources().getStringArray(R.array.names);

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
        if (id == R.id.action_settings) {
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
							
							// Launch Travel Log Activity
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
