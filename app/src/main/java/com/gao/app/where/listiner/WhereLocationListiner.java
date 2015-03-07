package com.gao.app.where.listiner;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.gao.app.where.utils.MapUtils;

/**
 * Created by Administrator on 14-12-24.
 */
public class WhereLocationListiner implements BDLocationListener {
    @Override
    public void onReceiveLocation(BDLocation location) {
        if (location == null) {
            return;
        }
        MapUtils.setMapLocation(location);
    }
}
