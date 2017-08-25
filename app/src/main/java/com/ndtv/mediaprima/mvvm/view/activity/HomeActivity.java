package com.ndtv.mediaprima.mvvm.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.gigya.socialize.android.GSAPI;
import com.ndtv.mediaprima.R;
import com.ndtv.mediaprima.common.CrowdControlHelper;
import com.ndtv.mediaprima.common.GoogleAnalyticsHelper;
import com.ndtv.mediaprima.common.ui.BaseActivity;
import com.ndtv.mediaprima.common.utils.ClickEvents;
import com.ndtv.mediaprima.common.utils.Constants;
import com.ndtv.mediaprima.common.utils.Constants.BundleKeys;
import com.ndtv.mediaprima.common.utils.FragmentHelper;
import com.ndtv.mediaprima.common.utils.PreferenceManager;
import com.ndtv.mediaprima.common.utils.Utility;
import com.ndtv.mediaprima.exception.InvalidTypeException;
import com.ndtv.mediaprima.mvvm.APIService;
import com.ndtv.mediaprima.mvvm.ServiceGenerator;
import com.ndtv.mediaprima.mvvm.model.Banner;
import com.ndtv.mediaprima.mvvm.model.ConfigurationItem;
import com.ndtv.mediaprima.mvvm.model.Data;
import com.ndtv.mediaprima.mvvm.model.PostResponse;
import com.ndtv.mediaprima.mvvm.model.SectionItems;
import com.ndtv.mediaprima.mvvm.view.fragment.AboutFragment;
import com.ndtv.mediaprima.mvvm.view.fragment.ArtistDetailFragment;
import com.ndtv.mediaprima.mvvm.view.fragment.ArtistHomeFragment;
import com.ndtv.mediaprima.mvvm.view.fragment.DramaDetailFragment;
import com.ndtv.mediaprima.mvvm.view.fragment.DramaHomeFragment;
import com.ndtv.mediaprima.mvvm.view.fragment.DramaSangatFragment;
import com.ndtv.mediaprima.mvvm.view.fragment.LifestyleDetailFragment;
import com.ndtv.mediaprima.mvvm.view.fragment.LifestyleHomeFragment;
import com.ndtv.mediaprima.mvvm.view.fragment.MenuFragment;
import com.rampo.updatechecker.UpdateChecker;
import com.rampo.updatechecker.notice.Notice;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.net.ssl.HttpsURLConnection;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Elaa on 8/29/2016.
 * modified by nitesh
 */

public class HomeActivity extends BaseActivity implements ClickEvents.LeftMenuItemListener, ClickEvents.BannerItemListener,
        ClickEvents.ToolBarScrollingListener, DrawerLayout.DrawerListener {

    private UpdateChecker checker;
    private DrawerLayout drawerLayout;
    private String TAG = HomeActivity.class.getSimpleName();
    private ProgressDialog progressDialogue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.activity_home);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        checker = new UpdateChecker(this);
        checker.setNotice(Notice.DIALOG);
        checker.start();
        FragmentHelper.addFragment(HomeActivity.this, R.id.menu_container, new MenuFragment());
        AppRate.with(this)
                .setLaunchTimes(22) // default 10
                .setRemindInterval(22) // default 1
                .setShowLaterButton(true) // default true
                .setTitle(R.string.app_name)
                .setMessage(getResources().getString(R.string.rate_app_msg))
                .setDebug(false) // default false
                .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                    @Override
                    public void onClickButton(int which) {

                        switch (which) {
                            case -1:
                                Log.d(HomeActivity.class.getSimpleName(), Integer.toString(which));
                                break;
                            case -2:
                                Log.d(HomeActivity.class.getSimpleName(), Integer.toString(which));
                                break;
                            case -3:
                                Log.d(HomeActivity.class.getSimpleName(), Integer.toString(which));
                                break;
                            default:
                                throw new InvalidTypeException("not a valid option");
                        }
                    }
                })
                .monitor();

        // Show a dialog if meets conditions
        AppRate.showRateDialogIfMeetsConditions(this);
        //loadBannerAds();
        drawerLayout.setDrawerListener(this);
       // showAdContainer();
    }

    @Override
    public void onListItemClick(SectionItems item, String fragmentType, int position, String actionBarTitle) {
        Utility.hideKeyboard(this);
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.ACTION_BAR_TITLE, actionBarTitle);
        bundle.putInt(BundleKeys.POSITION, position);
        bundle.putString(BundleKeys.URL, item.uri);
        bundle.putString(BundleKeys.SHARE_IMAGE, item.image_url);
        Fragment fragment;
        switch (fragmentType) {
            case Constants.SectionType.DRAMA_DETAIL:
                fragment = new DramaDetailFragment();
                break;
            case Constants.SectionType.ARTIST_DETAIL:
                fragment = new ArtistDetailFragment();
                break;
            case Constants.SectionType.LIFESTYLE_DETAIL:
                fragment = new LifestyleDetailFragment();
                break;
            default:
                throw new InvalidTypeException("Invalid navigation type");
        }
        fragment.setArguments(bundle);
        FragmentHelper.replaceAndAddFragment(this, R.id.home_container, fragment);
    }

    @Override
    public void onLeftMenuItemClick(ConfigurationItem item, int position, boolean isCloseMenu) {
        if (isCloseMenu)
            drawerLayout.closeDrawers();
        Utility.hideKeyboard(this);
        FragmentHelper.clearBackStack(this);
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.URL, item.uri);
        bundle.putString(BundleKeys.ACTION_BAR_TITLE, item.name);
        Fragment fragment;
        switch (item.type) {
            case Constants.SectionType.HOME:
                fragment = new DramaSangatFragment();
                GoogleAnalyticsHelper.screenView(this, Constants.DramaSangatScreenName.MAIN_PAGE);
                break;
            case Constants.SectionType.DRAMA_LIST:
                fragment = new DramaHomeFragment();
                break;
            case Constants.SectionType.ARTIST_LIST:
                fragment = new ArtistHomeFragment();
                break;
            case Constants.SectionType.LIFESTYLE_LIST:
                fragment = new LifestyleHomeFragment();
                break;
            case Constants.SectionType.ABOUT:
                fragment = new AboutFragment();
                break;
            case Constants.SectionType.RECOMMEND_APP:
                recommendApp();
                return;
            case Constants.SectionType.LOGIN:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, Constants.ActivityRequestCode.LOGIN_ACTIVITY);
                return;
            case Constants.SectionType.LOGOUT:
                progressDialogue = Utility.createProgressDialog(this);
                progressDialogue.show();
                if (!PreferenceManager.getsInstance(this).getAuthorizationToken().equals("") ||
                        PreferenceManager.getsInstance(this).getAuthorizationToken() != null) {
                    GSAPI.getInstance().logout();
                    updateMenu("logout");
                    progressDialogue.dismiss();
                    Utility.showToast(getApplicationContext(), getResources().getString(R.string.logout_success_msg));
                } else
                    logout();
                return;
            default:
                throw new InvalidTypeException("Invalid navigation type");
        }
        pushLotameEvent(item.name);
        fragment.setArguments(bundle);
        FragmentHelper.replaceFragment(this, R.id.home_container, fragment);
    }

    private void logout() {
        APIService apiService = ServiceGenerator.createService(APIService.class);
        Call<PostResponse> call = apiService.logout(PreferenceManager.getsInstance(this).getAuthorizationToken());
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                switch (response.code()) {
                    case HttpsURLConnection.HTTP_OK:
                        updateMenu("logout");
                        progressDialogue.dismiss();
                        Utility.showToast(getApplicationContext(), getResources().getString(R.string.logout_success_msg));
                        PreferenceManager.getsInstance(getApplicationContext()).setAuthorizationToken("");
                        break;
                    default:
                        progressDialogue.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response.errorBody().string());
                            showAlert(obj.get("error").toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                }

            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void setActionBarTitle(Toolbar toolbar, String title) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(title);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerToggle.syncState();
        drawerLayout.addDrawerListener(drawerToggle);
    }

    @Override
    public void setCollapsingTitle(CollapsingToolbarLayout collapsingToolbarLayout, Toolbar toolbar, String title) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        collapsingToolbarLayout.setTitle(title);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.expandedappbar);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerToggle.syncState();
        drawerLayout.addDrawerListener(drawerToggle);
    }

    @Override
    public void onBannerItemClick(Banner item, String sectionType, String actionBarTitle) {
        Utility.hideKeyboard(this);
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.ACTION_BAR_TITLE, actionBarTitle);
        switch (sectionType) {
            case Constants.SectionType.DRAMA_DETAIL:
                Fragment fragment = new DramaDetailFragment();
                bundle.putString(BundleKeys.URL, item.uri);
                fragment.setArguments(bundle);
                FragmentHelper.replaceAndAddFragment(this, R.id.home_container, fragment);
                break;
            case Constants.SectionType.ARTIST_DETAIL:
                Fragment fragment1 = new ArtistDetailFragment();
                bundle.putString(BundleKeys.URL, item.uri);
                fragment1.setArguments(bundle);
                FragmentHelper.replaceAndAddFragment(this, R.id.home_container, fragment1);
                break;
            case Constants.SectionType.PLAYER:
                Intent intent = new Intent(this, DetailActivity.class);
                bundle.putString(BundleKeys.ACTION_BAR_TITLE, actionBarTitle);
                bundle.putString(BundleKeys.URL, item.embed_code);
                bundle.putString(BundleKeys.FRAGMENT_TYPE, sectionType);
                intent.putExtras(bundle);
                Utility.startActivity(this, intent);
                break;
        }
    }

    @Override
    public void onHomeListItemClick(Data item, String sectionType, int position, String actionBarTitle) {
        Utility.hideKeyboard(this);
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.ACTION_BAR_TITLE, actionBarTitle);
        bundle.putInt(BundleKeys.POSITION, position);
        bundle.putString(BundleKeys.URL, item.sectionButton.uri);
        Fragment fragment;
        switch (sectionType) {
            case Constants.SectionType.DRAMA_LIST:
                fragment = new DramaHomeFragment();
                break;
            case Constants.SectionType.ARTIST_LIST:
                fragment = new ArtistHomeFragment();
                break;
            case Constants.SectionType.LIFESTYLE_LIST:
                fragment = new LifestyleHomeFragment();
                break;
            default:
                throw new InvalidTypeException("Invalid navigation type");
        }

        fragment.setArguments(bundle);
        FragmentHelper.replaceAndAddFragment(this, R.id.home_container, fragment);
    }

    @Override
    public void onGalleryItemClick(int position, String actionBarTitle) {
        Utility.hideKeyboard(this);
        Intent intent = new Intent(this, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(BundleKeys.POSITION, position);
        bundle.putString(BundleKeys.ACTION_BAR_TITLE, actionBarTitle);
        bundle.putString(BundleKeys.FRAGMENT_TYPE, Constants.SectionType.FULL_SCREEN_IMAGE);
        intent.putExtras(bundle);
        Utility.startActivity(this, intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utility.hideKeyboard(this);
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawers();
            return;
        }
    }

    @Override
    public void toolbarScrolling(AppBarLayout appBarLayout, CollapsingToolbarLayout collapsingToolbarLayout,
                                 Toolbar toolbar, FrameLayout frameLayout, boolean scrolling) {

        AppBarLayout.LayoutParams toolbarLayoutParams = (AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();
        CollapsingToolbarLayout.LayoutParams collapseParams = (CollapsingToolbarLayout.LayoutParams) frameLayout.getLayoutParams();
        if (scrolling) {
            toolbarLayoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                    | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED);
            collapsingToolbarLayout.setLayoutParams(toolbarLayoutParams);
            CoordinatorLayout.LayoutParams appBarLayoutParams = (CoordinatorLayout.LayoutParams)
                    appBarLayout.getLayoutParams();
            appBarLayoutParams.setBehavior(new AppBarLayout.Behavior());
            appBarLayout.setLayoutParams(appBarLayoutParams);
            collapseParams.setCollapseMode(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX);
            frameLayout.setLayoutParams(collapseParams);

        } else {
            toolbarLayoutParams.setScrollFlags(0);
            collapsingToolbarLayout.setLayoutParams(toolbarLayoutParams);
            CoordinatorLayout.LayoutParams appBarLayoutParams = (CoordinatorLayout.LayoutParams)
                    appBarLayout.getLayoutParams();
            appBarLayoutParams.setBehavior(null);
            appBarLayout.setLayoutParams(appBarLayoutParams);
            collapseParams.setCollapseMode(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_OFF);
            frameLayout.setLayoutParams(collapseParams);
        }
    }

    private void recommendApp() {
        final String appPackageName = getPackageName();
        Utility.showIntentShare(this, getResources().getString(R.string.app_name),
                getResources().getString(R.string.play_store_url) + appPackageName);
    }

    private void pushLotameEvent(String event) {
        /*only instantiation of CrowdControlHelper will push the event*/
        new CrowdControlHelper(this, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode) {
            case Constants.ActivityRequestCode.LOGIN_ACTIVITY:
                updateMenu("login");
                break;
        }
    }

    private void updateMenu(String type) {
        MenuFragment menuFragment = (MenuFragment) FragmentHelper.getFragment(this, R.id.menu_container);
        menuFragment.updateMenuList(type);
    }

    private void showAlert(String msg) {
        Utility.showAlert(this, msg);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
       // hideAdContainer();
    }

    @Override
    public void onDrawerClosed(View drawerView) {
       // showAdContainer();
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}

