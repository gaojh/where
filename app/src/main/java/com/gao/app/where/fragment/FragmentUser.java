package com.gao.app.where.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gao.app.where.R;
import com.gao.app.where.login.LoginCons;
import com.gao.app.where.utils.WhereUtils;
import com.gao.app.where.view.RoundAngleImageView;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;


public class FragmentUser extends Fragment implements View.OnClickListener{
    private View view = null;
    private RoundAngleImageView userlogo;
    private Tencent tencent;
    private TextView loginTextView;
    private ListView listView;
    private ArrayAdapter<String> listAdapter;

    Bitmap bitmap = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_user, null);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }

        loginTextView = (TextView) view.findViewById(R.id.login_textview);
        loginTextView.setOnClickListener(this);

        userlogo = (RoundAngleImageView)view.findViewById(R.id.login_user_img);
        listView = (ListView)view.findViewById(R.id.listView);

        listAdapter = new ArrayAdapter<String>(this.getActivity(),R.layout.array_item,WhereUtils.listArr);
        listView.setAdapter(listAdapter);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_textview) {
            loginQQ();
        }
    }

    private void loginQQ() {
        tencent = Tencent.createInstance(LoginCons.QQ_API_ID, this.getActivity().getApplicationContext());
        tencent.login(this,"all",new BaseUiListener());
    }

    private class BaseUiListener implements IUiListener {
        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onComplete(Object o) {
            Toast.makeText(FragmentUser.super.getActivity(), "登录成功", Toast.LENGTH_LONG).show();
            QQToken qqToken = tencent.getQQToken();
            UserInfo info = new UserInfo(FragmentUser.super.getActivity().getApplicationContext(), qqToken);
            info.getUserInfo(new IUiListener() {

                @Override
                public void onComplete(final Object o) {

                    Message msg = new Message();
                    msg.obj = o;
                    msg.what = 0;
                    mHandler.sendMessage(msg);

                    /**由于图片需要下载所以这里使用了线程，如果是想获得其他文字信息直接
                     * 在mHandler里进行操作
                     *
                     */
                    new Thread(){

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            JSONObject json = (JSONObject)o;
                            try {
                                bitmap = WhereUtils.getbitmap(json.getString("figureurl_qq_2"));
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            Message msg = new Message();
                            msg.obj = bitmap;
                            msg.what = 1;
                            mHandler.sendMessage(msg);
                        }
                    }.start();
                }

                @Override
                public void onError(UiError uiError) {

                }

                @Override
                public void onCancel() {

                }
            });

        }

    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
               JSONObject response = (JSONObject) msg.obj;
                if (response.has("nickname")) {
                    try {
                        String nicknameString=response.getString("nickname");

                        loginTextView.setText(nicknameString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else if(msg.what == 1){
                Bitmap bitmap = (Bitmap)msg.obj;
                userlogo.setImageBitmap(bitmap);

            }
        }

    };
}
