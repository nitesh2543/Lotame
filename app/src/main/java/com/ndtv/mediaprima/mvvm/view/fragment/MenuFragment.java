package com.ndtv.mediaprima.mvvm.view.fragment;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.ndtv.mediaprima.R;
import com.ndtv.mediaprima.common.ConfigManager;
import com.ndtv.mediaprima.common.ui.BaseFragment;
import com.ndtv.mediaprima.common.utils.ClickEvents;
import com.ndtv.mediaprima.common.utils.Constants;
import com.ndtv.mediaprima.common.utils.PreferenceManager;
import com.ndtv.mediaprima.common.utils.Utility;
import com.ndtv.mediaprima.databinding.FragmentMenuBinding;
import com.ndtv.mediaprima.mvvm.model.ConfigurationItem;
import com.ndtv.mediaprima.mvvm.view.adapter.MenuAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elaa on 8/29/2016.
 */
public class MenuFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private final String TAG = MenuFragment.class.getSimpleName();
    private FragmentMenuBinding binding;
    private ClickEvents.LeftMenuItemListener leftMenuItemListener;
    private List<ConfigurationItem> menuList = new ArrayList<>();
    private MenuAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        leftMenuItemListener = (ClickEvents.LeftMenuItemListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<ConfigurationItem> list = ConfigManager.getsInstance().getSections(getContext());
        menuList.addAll(list);
        Log.e(TAG, "onActivityCreated: " + PreferenceManager.getsInstance(getContext()).getAuthorizationToken());
        menuList.add(new ConfigurationItem("Recommend this App", "", "", "recommend", ""));
        if (!Utility.isLoggedIn(getActivity()))
            menuList.add(new ConfigurationItem("Login", "", "", "login", ""));
        else
            menuList.add(new ConfigurationItem("Login", "", "", "login", ""));
        adapter = new MenuAdapter(getContext(), menuList);
        binding.listView.setAdapter(adapter);
        binding.listView.setOnItemClickListener(this);
        if (menuList.size() > 0)
            binding.listView.performItemClick(binding.listView.getAdapter().getView(0, null, null),
                    0,
                    binding.listView.getAdapter().getItemId(0));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ConfigurationItem item = menuList.get(position);
        leftMenuItemListener.onLeftMenuItemClick(item, position, true);
        adapter.setSelectedPosition(position);
    }

    public void updateMenuList(String type) {
        switch (type) {
            case Constants.SectionType.LOGOUT:
                menuList.set(adapter.getSelectedPosition(), new ConfigurationItem("Login", "", "", "login", ""));
                break;
            case Constants.SectionType.LOGIN:
                menuList.set(adapter.getSelectedPosition(), new ConfigurationItem("Logout", "", "", "logout", ""));
                break;

        }
        binding.listView.setAdapter(adapter);
    }
}

