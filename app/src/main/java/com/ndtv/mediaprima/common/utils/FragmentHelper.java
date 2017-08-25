package com.ndtv.mediaprima.common.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.ndtv.mediaprima.R;

/**
 * Created by Elaa on 8/27/2016.
 */
public class FragmentHelper {

    public static void replaceAndAddFragment(final FragmentActivity activity, final int containerId,
                                             final Fragment fragment) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
        transaction.replace(containerId, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    public static void addFragment(final FragmentActivity activity, final int containerId,
                                   final Fragment fragment) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
        transaction.add(containerId, fragment);
        transaction.commitAllowingStateLoss();
    }

    public static void replaceFragment(final FragmentActivity activity, final int containerId,
                                                    final Fragment fragment) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
        transaction.replace(containerId, fragment);
        transaction.commitAllowingStateLoss();
    }

    public static void popBackStackImmediate(final FragmentActivity activity) {
        activity.getSupportFragmentManager().popBackStackImmediate();
    }

    public static void clearBackStack(final FragmentActivity activity) {
        try {
            activity.getSupportFragmentManager().popBackStackImmediate(null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public static int getStackCount(final FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        return fragmentManager.getBackStackEntryCount();
    }

    public static Fragment getFragment(final FragmentActivity activity, final int containerId) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        return fragmentManager.findFragmentById(containerId);
    }

}
