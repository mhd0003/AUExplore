package edu.auburn.csse.comp3710.group10.auexplore;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class DetailViewActivity extends FragmentActivity {
	/**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static int num_pages = 0;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;
    private int currentItem;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private DetailPagerAdapter mPagerAdapter;
    private ArrayList<AULocation> locations = new ArrayList<AULocation>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle = getIntent().getExtras();
        String locationList = bundle.getString("List");
        int firstItem = -50;
        if (bundle.containsKey("firstItem")) {
        	firstItem = bundle.getInt("firstItem");
        }
        Gson gson = new Gson();
        locations = gson.fromJson(locationList, new TypeToken<ArrayList<AULocation>>(){}.getType());
        num_pages = locations.size();
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new DetailPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        if (firstItem != -50) {
        	// get item from adapter blah blah blah
        	mPager.setCurrentItem(firstItem);
        }
    }
    
    public AULocation getLocation(int position) {
    	AULocation location = locations.get(position);
    	return location;
    }

//    @Override
//    public void onBackPressed() {
//        if (mPager.getCurrentItem() == 0) {
//            // If the user is currently looking at the first step, allow the system to handle the
//            // Back button. This calls finish() on this activity and pops the back stack.
//            super.onBackPressed();
//        } else {
//            // Otherwise, select the previous step.
//            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
//        }
//    }

    /**
     * A simple pager adapter that represents all found AULocation objects
     * 
     */
    private class DetailPagerAdapter extends FragmentStatePagerAdapter {
        public DetailPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
        	currentItem = position;
            return DetailViewFragment.create(position);
        }

        @Override
        public int getCount() {
            return num_pages;
        }
    }

}
