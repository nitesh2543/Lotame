package com.ndtv.mediaprima.common;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.gigya.socialize.android.GSAPI;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.ndtv.mediaprima.R;
import com.ndtv.mediaprima.common.utils.Constants;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

/**
 * Created by nitesh on 1/4/2017.
 */

public class DramaSangatApplication extends MultiDexApplication {

    private static final String LOG_TAG = DramaSangatApplication.class.getSimpleName();

    public static DramaSangatApplication getApplication(Context context) {
        return (DramaSangatApplication) context.getApplicationContext();
    }

    /*****************************
     * Google Analytics START
     ******************************/
    private static final String GA_PROPERTY_ID = "UA-90408278-6";
    private Tracker mTracker;
    private FirebaseAnalytics mFirebaseAnalytics;
    /*
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */


    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(GA_PROPERTY_ID);
        }
        return mTracker;
    }

    /*****************************
     * Google Analytics END
     ******************************/

    @Override
    public void onCreate() {
        super.onCreate();
        initializeAllSDK();
    }


    private void initializeAllSDK() {

/*
        for debugging OneSignal
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.DEBUG);
*/

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        AppEventsLogger.activateApp(this);
        FacebookSdk.sdkInitialize(this);
        OneSignal.startInit(this).setNotificationOpenedHandler(new ExampleNotificationOpenedHandler()).init();
        GoogleAnalyticsHelper.screenView(this, Constants.DramaSangatScreenName.APP_LAUNCH);
        GSAPI.getInstance().initialize(this, getResources().getString(R.string.gigya_api_key));
        /*only instantiation of CrowdControlHelper will push the event*/
        new CrowdControlHelper(getBaseContext(), Constants.DramaSangatEventType.OPEN_APP_EVENT);
    }

    private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {

        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            try {
                if (result != null) {
                    // TODO: handle notification click
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
