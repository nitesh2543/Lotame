package com.ndtv.mediaprima.common;

import android.content.Context;
import android.util.Log;

import com.lotame.android.CrowdControl;
import com.ndtv.mediaprima.common.utils.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by nitesh on 1/20/2017.
 */

public class CrowdControlHelper implements CrowdControl.CCInitializationListner {


    private static final String LOG_TAG = CrowdControlHelper.class.getSimpleName();

    private CrowdControl cc;
    private String lotameEvent;


    public CrowdControlHelper(final Context context, final String event) {
        cc = new CrowdControl(context, Constants.Lotame.LOTAME_CLIENT_ID, CrowdControl.Protocol.HTTPS, this);
        lotameEvent = event;
    }

    @Override
    public void onCCInitialized() {
        CrowdControl.enableDebug(true);
        cc.add(Constants.Lotame.LOTAME_KEY, lotameEvent);
        try {
            cc.bcp();
            String response = cc.getAudienceJSON(5, TimeUnit.SECONDS);
            Log.e(LOG_TAG, "onCCInitialized: " + response + " " + cc.isInitialized());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "onCCInitialized: " + e.getMessage());
        }
    }
}
