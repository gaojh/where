package com.gao.app.where.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.gao.app.where.R;
import com.gao.app.where.utils.MapUtils;


public class FragmentHome extends Fragment {
    private MapView mMapView = null;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home, null);
            mMapView = (MapView) view.findViewById(R.id.bmapView);
            View locView = inflater.inflate(R.layout.request_location,null);
            ImageButton locButton = (ImageButton)locView.findViewById(R.id.locButton);

            locButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(MapUtils.getCurrLatLng());
                    mMapView.getMap().animateMapStatus(u);
                }
            });

            MapUtils.initBaiduMap(mMapView,locView);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }

        View messageView = inflater.inflate(R.layout.message, null);
        MapUtils.setMessageView(messageView);
        return view;
    }


    @Override
    public void onPause() {
        mMapView.setVisibility(View.INVISIBLE);
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mMapView.setVisibility(View.VISIBLE);
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

}
