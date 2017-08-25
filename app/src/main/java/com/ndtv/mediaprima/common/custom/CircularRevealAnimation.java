package com.ndtv.mediaprima.common.custom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by Elaa on 9/9/2016.
 */
public class CircularRevealAnimation {
    public static void calculateRevealDimension(View view) {
        int x = (view.getLeft() + view.getRight()) / 2;
        int y = (view.getTop() + view.getBottom()) / 2;
        float radiusOfFab = 1f * (100) / 2f;
        float radiusFromFabToRoot = (float) Math.hypot(
                Math.max(x, view.getWidth() - x),
                Math.max(y, view.getHeight() - y));
        //hideMenu(x, y, radiusFromFabToRoot, radiusOfFab);
        showMenu(x, y, radiusOfFab, radiusFromFabToRoot, view);
    }

    public static void showSearchView(View view, View rootView) {

        int x = (view.getLeft() + view.getRight()) / 2;
        int y = (view.getTop() + view.getBottom()) / 2;
        float radiusOfFab = 1f * (view.getWidth()) / 2f;
        float radiusFromFabToRoot = (float) Math.hypot(
                Math.max(x, rootView.getWidth() - x),
                Math.max(y, rootView.getHeight() - y));
        //hideMenu(x, y, radiusFromFabToRoot, radiusOfFab);

        showMenu(x, y, radiusOfFab, radiusFromFabToRoot, rootView);
    }

    public static void hideSearchView(View view, View rootView, View searchLayout) {

        int x = (view.getLeft() + view.getRight()) / 2;
        int y = (view.getTop() + view.getBottom()) / 2;
        float radiusOfFab = 1f * (view.getWidth()) / 2f;
        float radiusFromFabToRoot = (float) Math.hypot(
                Math.max(x, rootView.getWidth() - x),
                Math.max(y, rootView.getHeight() - y));
        hideMenu(x, y, radiusFromFabToRoot, radiusOfFab, rootView, searchLayout);

//        showMenu(x, y, radiusOfFab, radiusFromFabToRoot, rootView);
    }

    private static void showMenu(int cx, int cy, float startRadius, float endRadius, View view) {
        Animator revealAnim = createCircularReveal((ClipRevealFrame) view, cx, cy, startRadius, endRadius);
        revealAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        revealAnim.setDuration(300);
        revealAnim.start();
    }

    private static void hideMenu(int cx, int cy, float startRadius, float endRadius, final View view, final View searchLayout) {

        Animator revealAnim = createCircularReveal((ClipRevealFrame) view, cx, cy, startRadius, endRadius);
        revealAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        revealAnim.setDuration(1000);
        revealAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                searchLayout.setVisibility(View.INVISIBLE);
            }
        });
        revealAnim.start();
    }

    private static Animator createCircularReveal(final ClipRevealFrame view, int x, int y, float startRadius, float endRadius) {

        final Animator reveal;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            reveal = ViewAnimationUtils.createCircularReveal(view, x, y, startRadius, endRadius);
        } else {
            view.setClipOutLines(true);
            view.setClipCenter(x, y);
            reveal = ObjectAnimator.ofFloat(view, "ClipRadius", startRadius, endRadius);
            reveal.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setClipOutLines(false);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        reveal.setDuration(500);
        return reveal;
    }

}
