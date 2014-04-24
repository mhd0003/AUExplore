package edu.auburn.csse.comp3710.group10.auexplore;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DetailViewFragment extends Fragment{
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_detail, container, false);
        
  //      ((DetailViewActivity)getActivity()).getLocation(position);
        
        return rootView;
    }
	
}
