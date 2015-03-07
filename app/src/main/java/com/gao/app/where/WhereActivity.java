package com.gao.app.where;

import android.content.Intent;
import android.support.v4.app.FragmentTabHost;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.gao.app.where.fragment.FragmentDynamic;
import com.gao.app.where.fragment.FragmentFrends;
import com.gao.app.where.fragment.FragmentHome;
import com.gao.app.where.fragment.FragmentUser;
import com.gao.app.where.utils.MapUtils;


public class WhereActivity extends ActionBarActivity {


    //定义FragmentTabHost对象
    private FragmentTabHost mTabHost;

    //定义一个布局
    private LayoutInflater layoutInflater;

    private Intent locationIntent;

    //定义数组来存放Fragment界面
    private Class fragmentArray[] = {FragmentHome.class, FragmentFrends.class, FragmentDynamic.class, FragmentUser.class};

    //定义数组来存放按钮图片
    private int mImageViewArray[] = {R.drawable.tab_home_btn, R.drawable.tab_home_btn, R.drawable.tab_home_btn, R.drawable.tab_home_btn};

    //Tab选项卡的文字
    private String mTextviewArray[] = {"首页", "好友", "动态", "我的"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        if (locationIntent == null) {
            locationIntent = new Intent();
            locationIntent.setAction("com.gao.app.where.LocationService");
            startService(locationIntent);
        }

        //获取屏幕的高度和宽度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        MapUtils.setScreenHeightAndWidth(dm.heightPixels, dm.widthPixels);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(locationIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_where, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化组件
     */
    private void initView() {
        //实例化布局对象
        layoutInflater = LayoutInflater.from(this);
        //实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        //得到fragment的个数
        int count = fragmentArray.length;

        for (int i = 0; i < count; i++) {
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            //设置Tab按钮的背景
            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_selector_background);
        }
    }


    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextviewArray[index]);

        return view;
    }
}
