package edu.auburn.csse.comp3710.group10.auexplore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
	private LatLng[] markersArray = {new LatLng(32.606034, -85.487478), new LatLng(32.604924, -85.486713),
											new LatLng(32.605852, -85.483075), new LatLng(32.605798, -85.488953),
											new LatLng(32.604766, -85.484863), new LatLng(32.602487, -85.486959),
											new LatLng(32.603395, -85.486793), new LatLng(32.595024, -85.483477),
											new LatLng(32.603170, -85.490397), new LatLng(32.603181, -85.490632),
											new LatLng(32.603336, -85.484822), new LatLng(32.599580, -85.485168),
											new LatLng(32.600493, -85.486382), new LatLng(32.604048, -85.493804),
											new LatLng(32.599587, -85.490317)};
	
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
        
        addMarkers(Arrays.asList(markersArray));
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
			map.addMarker(new MarkerOptions().position(markers.get(i)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
		}
	}
	
	private double distanceBetween(LatLng first, LatLng second) {
		float[] results = new float[1];
		Location.distanceBetween(first.latitude, first.longitude, second.latitude, second.longitude, results);
		return (double) results[0];
	}
	
	private boolean atLocation(LatLng location) {
		Location currentLocation = locationManager.getLastKnownLocation(locationProvider);
		LatLng currLatLng = new LatLng(location.latitude, location.longitude);
		double accuracy = (double) currentLocation.getAccuracy();
		if (accuracy != 0) {
			double distance = distanceBetween(currLatLng, location);
			return distance <= accuracy;
		}
		else {
			// TODO: What if no accuracy info?
			return false;
		}
	}

}
