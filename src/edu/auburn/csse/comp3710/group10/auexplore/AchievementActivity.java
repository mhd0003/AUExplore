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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AchievementActivity extends ListActivity{
	private ArrayList<AULocation> locations;
	private final String LOCATIONS_FILE_NAME = "AULocations.json";
	private String foundLocationList = "";
	private final String[] achievements = {"Freshman", "Sophomore", "Junior", "Senior", "Congratulations! You've graduated!"};
	private ArrayList<String> earnedAchievements = new ArrayList<String>();
	private ArrayList<AULocation> foundLocations = new ArrayList<AULocation>();
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        ActionBar ab = getActionBar();
        ab.setTitle("Achievements");
    }
	
	@Override
	protected void onResume() {
		super.onResume();
		
		locations = getLocationList();
        for (int i = 0; i < locations.size(); i++) {
        	if (locations.get(i).isFound()) {
        		foundLocations.add(locations.get(i));
        	}
        }
        
        int pointValue = getCurrentPointValue();
        if (pointValue >= 10) {
        	earnedAchievements.add(achievements[0]);
        }
        if (pointValue >= 25) {
        	earnedAchievements.add(achievements[1]);
        }
        if (pointValue >= 50) {
        	earnedAchievements.add(achievements[2]);
        }
        if (pointValue >= 75) {
        	earnedAchievements.add(achievements[3]);
        }
        if (pointValue == 100) {
        	earnedAchievements.add(achievements[4]);
        }
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.listtext, earnedAchievements);
        setListAdapter(adapter);
        
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
			contents = new String(inputBuffer);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return contents;
	}
	
	private int getCurrentPointValue() {
		int points = 0;
		for (int i = 0; i < foundLocations.size(); i++) {
			points += foundLocations.get(i).getPointVal();
		}
		return points;
	}
}
