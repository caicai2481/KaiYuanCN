package net.oschina.app.improve.main.discover;

import com.loopj.android.http.TextHttpResponseHandler;

import net.oschina.app.R;
import net.oschina.app.api.remote.OSChinaApi;

import cz.msebera.android.httpclient.Header;

/**
 * Created by haibin
 * on 2016/10/11.
 */

public class ShakePresentFragment extends BaseSensorFragment {

    public static ShakePresentFragment newInstance() {
        ShakePresentFragment fragment = new ShakePresentFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shake_present;
    }

    @Override
    public void onShake() {
        OSChinaApi.getShakePresent(0, "", "", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

            }
        });
    }
}
