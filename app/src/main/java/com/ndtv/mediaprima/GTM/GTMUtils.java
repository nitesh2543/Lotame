package com.ndtv.mediaprima.GTM;

import android.content.Context;

import com.google.android.gms.tagmanager.DataLayer;
import com.google.android.gms.tagmanager.TagManager;

/**
 * Created by Administrator on 1/11/2017.
 */

public class GTMUtils {


    private GTMUtils() {
    }

    public static void pushOpenScreenEvent(Context context, String screenName) {
        DataLayer dataLayer = TagManager.getInstance(context).getDataLayer();
        dataLayer.pushEvent("OpenScreen", DataLayer.mapOf("screenName", screenName));
    }
}
