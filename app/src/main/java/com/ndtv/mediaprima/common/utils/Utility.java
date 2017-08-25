package com.ndtv.mediaprima.common.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.ndtv.mediaprima.R;

/**
 * Created by Elaa on 8/27/2016.
 */
public class Utility {

    private static final String MIME_TYPE = "message/rfc822";
    private static final String PLANE_TEXT = "text/plain";

    public static void showAlert(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.app_name));
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public static void showIntentShare(Context ctx, String title, String link) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType(PLANE_TEXT);
//        sharingIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
//        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        sharingIntent.putExtra(Intent.EXTRA_TEXT,"\n\n" + link + "\n\n" + "Sent via Drama Sangat app ");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);

//        "Get the Drama Sangat app on Play Store and on the App Store"
        ctx.startActivity(Intent.createChooser(sharingIntent, "Share using"));
/*        try{

        }catch (ActivityNotFoundException ex){
            Utility.showToast(ctx, "There are no email clients installed");
        }*/

    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static void hideKeyboard(Context ctx) {
        View view = ((Activity) ctx).getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) (ctx).getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void startActivity(Activity activity, Intent intent) {
        activity.startActivity(intent);
    }

    public static void setPortraitOrientation(Context ctx) {
        ((Activity) ctx).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
    }

    public static void setLandscapeOrientation(Context ctx) {
        ((Activity) ctx).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
    }

    public static RecyclerView.LayoutManager setVerticalLayoutManager(Context ctx) {
        return new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
    }

    public static RecyclerView.LayoutManager setHorizondalLayoutManager(Context ctx) {
        return new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false);
    }

    public static RecyclerView.LayoutManager setGridLayoutManager(Context ctx, int size) {
        GridLayoutManager layoutManager = new GridLayoutManager(ctx, size);
        return layoutManager;
    }

    public static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public static ProgressDialog createProgressDialog(Context context) {

        ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please wait...");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
//        progress.setProgress(0);
//        progress.show();
        return progress;
    }

    public static View getProgressBar(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View progressBar = inflater.inflate(R.layout.progress_dialog, null, false);
        return progressBar;
    }

    public static ViewGroup.LayoutParams getParams() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        return layoutParams;
    }

    public static boolean isLoggedIn(Activity activity) {
        return !TextUtils.isEmpty(PreferenceManager.getsInstance(activity).getAuthorizationToken());
    }

    public static boolean isLoggedInWithFB(Activity activity) {
        return !TextUtils.isEmpty(PreferenceManager.getsInstance(activity).getAuthorizationToken());
    }
}
