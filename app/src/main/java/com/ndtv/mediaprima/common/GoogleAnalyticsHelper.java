package com.ndtv.mediaprima.common;

import android.content.Context;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by nitesh on 1/5/2017.
 */

public class GoogleAnalyticsHelper {

    public static void screenView(Context context, String name) {
        Tracker tracker = ((DramaSangatApplication) context.getApplicationContext()).getDefaultTracker();
        tracker.setScreenName(name);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public static void trackEvents(Context context, String category, String action) {
        Tracker tracker = ((DramaSangatApplication) context.getApplicationContext()).getDefaultTracker();
        tracker.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).build());
    }
}
