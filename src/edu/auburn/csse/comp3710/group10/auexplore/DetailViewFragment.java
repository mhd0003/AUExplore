package edu.auburn.csse.comp3710.group10.auexplore;

import com.google.android.gms.maps.model.LatLng;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailViewFragment extends Fragment{
	
	public static final String ARG_PAGE = "page";
	private int mPageNumber;
	private AULocation myLocation;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_detail, container, false);
        
        myLocation = ((DetailViewActivity)getActivity()).getLocation(mPageNumber);
        ((TextView)rootView.findViewById(R.id.description)).setText(myLocation.getDescription());
        
        Drawable draw = null;
        Resources res = getResources();
        switch(myLocation.getId()) {
        	case 0:
        		draw = res.getDrawable(R.drawable.shelby);
        		break;
        	case 1:
        		draw = res.getDrawable(R.drawable.broun);
        		break;
        	case 2:
        		draw = res.getDrawable(R.drawable.davishall);
        		break;
        	case 3:
        		draw = res.getDrawable(R.drawable.lowder);
        		break;
        	case 4:
        		draw = res.getDrawable(R.drawable.foy);
        		break;
        	case 5:
        		draw = res.getDrawable(R.drawable.student_center);
        		break;
        	case 6:
        		draw = res.getDrawable(R.drawable.haley);
        		break;
        	case 7:
        		draw = res.getDrawable(R.drawable.arboretum);
        		break;
        	case 8:
        		draw = res.getDrawable(R.drawable.jordan_hare);
        		break;
        	case 9:
        		draw = res.getDrawable(R.drawable.arena);
        		break;
        	case 10:
        		draw = res.getDrawable(R.drawable.cater);
        		break;
        	case 11:
        		draw = res.getDrawable(R.drawable.chem);
        		break;
        	case 12:
        		draw = res.getDrawable(R.drawable.parker);
        		break;
        	case 13:
        		draw = res.getDrawable(R.drawable.village);
        		break;
        	case 14:
        		draw = res.getDrawable(R.drawable.park);
        		break;
        }
        ((ImageView)rootView.findViewById(R.id.image)).setBackgroundDrawable(draw);
        
        rootView.findViewById(R.id.button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getDirections();
			}
		});
        
        return rootView;
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPageNumber = getArguments().getInt(ARG_PAGE);
	}
	
	public static DetailViewFragment create(int pageNumber) {
		DetailViewFragment frag = new DetailViewFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE, pageNumber);
		frag.setArguments(args);
		return frag;
	}
	
	public int getPageNumber() {
		return mPageNumber;
	}
	
	public void getDirections() {
		LatLng latLng = myLocation.getLatLng();
		String lat = Double.toString(latLng.latitude);
		String longitude = Double.toString(latLng.longitude);
		String url = "http://maps.google.com/maps?saddr=" + "My Location" + "&daddr="
						+ lat + "," + longitude;
    	final Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
    	startActivity(mapIntent);
	}
	
}
