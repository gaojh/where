package com.gao.app.where.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.baidu.location.LocationClient;
import com.gao.app.where.listiner.WhereLocationListiner;
import com.gao.app.where.utils.MapUtils;

public class LocationService extends Service {

    private LocationClient locationClient = null;
    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationClient = new LocationClient(this.getApplicationContext());
        locationClient.setLocOption(MapUtils.getLocationClientOption());
        locationClient.registerLocationListener(new WhereLocationListiner());
        locationClient.start();
        MapUtils.setLocationClient(locationClient);
    }

    @Override
    public void onDestroy() {
        if (locationClient != null) {
            locationClient.stop();
        }
        super.onDestroy();
    }
}
