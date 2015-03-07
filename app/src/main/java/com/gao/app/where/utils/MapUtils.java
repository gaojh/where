package com.gao.app.where.utils;

import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.gao.app.where.R;

/**
 * Created by Administrator on 15-1-23.
 */
public class MapUtils {

    private static boolean isFirstLoc = true;
    private static LocationClient locationClient = null;
    private static LocationClientOption locationClientOption = null;
    private static MapView mMapView = null;
    private static LatLng currLatLng = null;
    private static int height;
    private static int width;
    private static LayoutInflater layoutInflater;
    private static View messageView;

    private MapUtils() {

    }

    public static void initBaiduMap(MapView mapView, View locView) {
        if (mapView == null) {
            return;
        }

        MapViewLayoutParams params = new MapViewLayoutParams.Builder().point(new Point(100, MapUtils.height - 600)).layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode).build();
        mapView.addView(locView, params);
        BaiduMap baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(16);
        baiduMap.animateMapStatus(u);
        mapView.showZoomControls(false);
        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_center_point);
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, mCurrentMarker);
        baiduMap.setMyLocationConfigeration(config);
        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                showMessageView(latLng,"niddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddhao");
               /* GeoCoder getCoder = GeoCoder.newInstance();
                ReverseGeoCodeOption reCodeOption = new ReverseGeoCodeOption();
                reCodeOption.location(latLng);
                getCoder.reverseGeoCode(reCodeOption);
                getCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                    @Override
                    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

                    }

                    @Override
                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                        showMessageView(latLng, reverseGeoCodeResult.getAddress());
                    }
                });*/

            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
        setMapView(mapView);
    }

    public static LocationClientOption getLocationClientOption() {
        if (locationClientOption != null) {
            return locationClientOption;
        }
        locationClientOption = new LocationClientOption();
        locationClientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        locationClientOption.setCoorType("gcj02");//返回的定位结果是百度经纬度,默认值gcj02
        locationClientOption.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
        locationClientOption.setIsNeedAddress(true);//返回的定位结果包含地址信息
        locationClientOption.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
        locationClientOption.setOpenGps(true);
        locationClientOption.setProdName("where");
        return locationClientOption;
    }

    public static void setMapLocation(BDLocation location) {
        // map view 销毁后不在处理新接收的位置
        if (location == null || mMapView == null) {
            return;
        }

        LatLng ll = new LatLng(location.getLatitude(),
                location.getLongitude());

        if (!equalLatLng(currLatLng, ll)) {
            BaiduMap baiduMap = mMapView.getMap();
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            baiduMap.setMyLocationData(locData);

            if (isFirstLoc) {
                isFirstLoc = false;
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                baiduMap.animateMapStatus(u);
            }
        }

        currLatLng = ll;
    }


    public static void showMessageView(LatLng ll, String message) {

        TextView textView = (TextView)messageView.findViewById(R.id.message_text);
        textView.setText(message);
        textView.setPadding(20,20,20,20);

        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(messageView);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(ll)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mMapView.getMap().addOverlay(option);
    }


    public static String getAddress(LatLng latLng) {
        return null;
    }


    private static boolean equalLatLng(LatLng old, LatLng last) {
        if (old == null || last == null) {
            return false;
        }

        if (old.latitude == last.latitude && old.longitude == last.longitude) {
            return true;
        }

        return false;
    }

    public static void setScreenHeightAndWidth(int height, int width) {
        MapUtils.height = height;
        MapUtils.width = width;
    }

    public static MapView getMapView() {
        return mMapView;
    }

    public static void setMapView(MapView mMapView) {
        MapUtils.mMapView = mMapView;
    }

    public static LocationClient getLocationClient() {
        return locationClient;
    }

    public static void setLocationClient(LocationClient locationClient) {
        MapUtils.locationClient = locationClient;
    }

    public static LatLng getCurrLatLng() {
        return currLatLng;
    }

    public static void setMessageView(View messageView) {
        MapUtils.messageView = messageView;
    }
}
