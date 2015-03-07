package com.gao.app.where;

import android.app.Application;
import android.content.Context;
import android.view.WindowManager;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;

/**
 * Created by Administrator on 15-1-29.
 */
public class WhereApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
    }
}
