package com.ndtv.mediaprima.mvvm.view.fragment;


import android.content.Context;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ndtv.mediaprima.R;
import com.ndtv.mediaprima.common.ui.BaseFragment;
import com.ndtv.mediaprima.common.utils.Constants;
import com.ndtv.mediaprima.common.utils.Network;
import com.ndtv.mediaprima.databinding.FragmentOoyalaVideoPlayerBinding;
import com.ooyala.android.OoyalaPlayer;
import com.ooyala.android.PlayerDomain;
import com.ooyala.android.ui.OoyalaPlayerLayoutController;

import java.util.Observable;
import java.util.Observer;

/**
 * A simple {@link Fragment} subclass.
 */
public class OoyalaVideoPlayerFragment extends BaseFragment implements Observer {
    private String EMBED;
    private String PCODE = "M4bmUyOotTFYCkwakYlyd24w6YZb";
    private String DOMAIN = "http://www.ooyala.com";

    private OoyalaPlayerLayoutController playerLayoutController;
    private OoyalaPlayer player;
    private ObservableBoolean isPortrait = new ObservableBoolean(true);
    private Constants.ActionBarListener actionBarListener;
    private FragmentOoyalaVideoPlayerBinding binding;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        actionBarListener = (Constants.ActionBarListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EMBED = getArguments().getString(Constants.BundleKeys.URL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ooyala_video_player, container, false);
        player = new OoyalaPlayer(PCODE, new PlayerDomain(DOMAIN));
        playerLayoutController = new OoyalaPlayerLayoutController(binding.playerLayout, player);
        binding.setIsPortrait(isPortrait);
        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!Network.isConnected(getActivity())) {
            Network.showAlert(getActivity());
            return;
        }
        if (getActivity() == null)
            return;
        actionBarListener.setActionBarTitle(binding.toolbar, getArguments().getString(Constants.BundleKeys.ACTION_BAR_TITLE));
        player = playerLayoutController.getPlayer();
        player.addObserver(this);

        if (player.setEmbedCode(EMBED)) {
            player.play();
        }
        playerLayoutController.setFullscreenButtonShowing(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        player.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        player.play();
    }

    @Override
    public void update(Observable observable, Object data) {

    }

    @Override
    public void onDestroy() {
        super.onDestroyView();
        player.suspend();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        isPortrait.set((newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) ? false : true);

    }
}