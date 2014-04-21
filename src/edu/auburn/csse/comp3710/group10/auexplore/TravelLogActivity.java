package edu.auburn.csse.comp3710.group10.auexplore;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import android.app.ActionBar;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class TravelLogActivity extends ListActivity{
	private ArrayList<AULocation> locations;
	private final String LOCATIONS_FILE_NAME = "AULocations.json";
	private int foundCount = 0;
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        ActionBar ab = getActionBar();
        ab.setTitle("Travel Log");
    }
	
	@Override
	protected void onResume() {
		super.onResume();
		
		locations = getLocationList();
        ArrayList<AULocation> foundLocations = new ArrayList<AULocation>();
        foundCount = 0;
        for (int i = 0; i < locations.size(); i++) {
        	if (locations.get(i).isFound()) {
        		foundLocations.add(locations.get(i));
        		foundCount++;
        	}
        }
        
        String[] listItems = new String[foundCount];
        for (int i = 0; i < foundCount; i++) {
        	listItems[i] = foundLocations.get(i).getName();
        }
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.listtext, listItems);
        setListAdapter(adapter);
        
        if (foundLocations.size() == 0) {
        	Toast.makeText(getApplicationContext(), "Check back later after collecting pins from the map :)", Toast.LENGTH_LONG).show();
        }
	}
	
	private ArrayList<AULocation> getLocationList() {
		Gson gson = new Gson();
		ArrayList<AULocation> locationList = new ArrayList<AULocation>();
		try {
			locationList = gson.fromJson(readJSONFile(LOCATIONS_FILE_NAME), new TypeToken<ArrayList<AULocation>>(){}.getType());
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (locationList == null) {
			locationList = new ArrayList<AULocation>();
		}
		return locationList;
		
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
}
