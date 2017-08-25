package com.ndtv.mediaprima.common.utils;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;

/**
 * Created by Elaa on 8/27/2016.
 */
public class Constants {

    public static final String CONTAINER_ID = "gtm_mgfvlbd";
    public static final long TIMEOUT_FOR_CONTAINER_OPEN_MILLISECONDS = 2000;
    public static final String GIGYA_SECRET_KEY = "dn+jTgM1FfU0mmltZJVtxZjrr7WgBx3qoXxTlxkt/Pc=";
    public static final String CONTENT_TYPE = "application/json";


    public interface BundleKeys {
        String POSITION = "position";
        String FRAGMENT_TYPE = "Fragment_type";
        String VIDEO = "video";
        String BANNERS = "banners";
        String SEARCH = "search_result";
        String ACTION_BAR_TITLE = "action_bar_title";
        String URL = "url";
        String ARTIST = "Artists";
        String EMPTY = "";
        String PARCELABLE_OBJECT = "object";
        String CATEGORY = "category";
        String SHARE_IMAGE = "share";
        String CATEGORY_NAME = "name";
        String EMAIL = "email";
    }

    public interface SectionType {
        String HOME = "home";
        String DRAMA_LIST = "dramas";
        String ARTIST_LIST = "artists";
        String LIFESTYLE_LIST = "articles";
        String DRAMA_DETAIL = "drama";
        String ARTIST_DETAIL = "artist";
        String LIFESTYLE_DETAIL = "article";
        String ABOUT = "pages";
        String FULL_SCREEN_IMAGE = "full_screen_image";
        String PLAYER = "player";
        String GALLERY = "gallery";
        String RECOMMEND_APP = "recommend";
        //String REGISTER = "register";
        String LOGOUT = "logout";
        String LOGIN = "login";
    }

    public interface Category {
        String SYNOPSIS = "SINOPSIS";
        String TRIVIA = "TRIVIA";
        String ARTISTS = "Artis";
        String EPISODES = "Episod";

    }

    public interface ActionBarListener {
        void setActionBarTitle(Toolbar toolbar, String title);

        void setCollapsingTitle(CollapsingToolbarLayout collapsingToolbarLayout, Toolbar toolbar, String title);
    }

    public interface ActivityRequestCode {
        int LOGIN_ACTIVITY = 100;
    }

    /****************
     * For GA
     *****************/

    public interface DramaSangatScreenName {

        String ABOUT_US = "Polisi/Tentang Kami/Terma dan Syarat";
        String ARTIST_DETAIL = "ArtistDetailFragment";
        String ARTIST_HOME = "ArtistHomeFragment";
        String ARTIST_LIST = "Artis";
        String BANNER = "BannerFragment";
        String DRAMA_DETAIL = "DramaDetailFragment";
        String DRAMA_DETAIL_LIST = "DramaDetailListFragment";
        String DRAMA_DETAIL_SYNOPSIS = "DramaDetailSynopsisFragment";
        String DRAMA_HOME = "DramaHomeFragment";
        String DRAMA_LIST = "DramaListFragment";
        String DRAMA_SANGAT = "DramaSangatFragment";
        String FULL_SCREEN = "FullScreenFragment";
        String LIFESTYLE_DETAIL = "LifestyleDetailFragment";
        String LIFESTYLE_HOME = "LifestyleHomeFragment";
        String LIFESTYLE_LIST = "LifestyleListFragment";
        String SEARCH_LIST = "searchListFragment";
        String MAIN_PAGE = "Main Page";
        String APP_LAUNCH = "App Launch";
        String LOGIN_SCREEN = "Login Screen";
        String REGISTRATION_SCREEN = "Registration Screen";
        String FORGOT_PASSWORD = "Forgot Password Screen" ;
        String VERIFY_EMAIL_SCREEN = "Verify Email Screen" ;
    }

    public interface DramaSangatEventType {

        String OPEN_APP_EVENT = "Open App Event";
        String MAIN_PAGE = "MAin Page";
        String TENTANG_KAMI = "Tentang Kami";
        String POLISI = "Polisi";
        String TERMA_DAN_SYARAT = "Terma dan Syarat";
        String DRAMAS = "Dramas";
        String ROMANTIK = "Romantik";
        String AKSI = "Aksi";
        String KOMEDI = "Komedi";
        String FIKSYEN = "Fiksyen";
        String TELEMOVIE = "Telemovie";
    }

    public interface Lotame {
        int LOTAME_CLIENT_ID = 9877;
        String LOTAME_KEY = "key";
    }


    public interface AccountListener {
        void onRegistration(Bundle bundle);

        void onForgotPassword(Bundle bundle);

        void onVerifyPassword(Bundle bundle);

        void onVerificationSuccess(Bundle bundle);

        void onLogin();

        void onLoginWithFB();
    }

    public interface DateListener {
        void OnDateSelected(int year, int month, int dayOfMonth);
    }

}
