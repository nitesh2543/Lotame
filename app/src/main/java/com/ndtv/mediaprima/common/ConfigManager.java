package com.ndtv.mediaprima.common;

import android.content.Context;

import com.ndtv.mediaprima.mvvm.APIService;
import com.ndtv.mediaprima.mvvm.ServiceGenerator;
import com.ndtv.mediaprima.mvvm.model.Configuration;
import com.ndtv.mediaprima.mvvm.model.ConfigurationItem;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ELAA on 19-09-2016.
 */
public class ConfigManager {

    private static ConfigManager sInstance;
    private Configuration configuration;
    private APIService apiService;

    public interface ConfigListener {
        void onResponse(Configuration configuration);

        void onError();
    }

    private ConfigManager() {
        apiService = ServiceGenerator.createService(APIService.class);
    }

    public static ConfigManager getsInstance() {
        if (sInstance == null)
            synchronized (ConfigManager.class) {
                if (sInstance == null)
                    sInstance = new ConfigManager();
            }
        return sInstance;
    }

    public void downloadConfig(final ConfigListener listener, Context ctx){
       Call<Configuration> call = apiService.downloadConfiguration();
        call.enqueue(new Callback<Configuration>() {
            @Override
            public void onResponse(Call<Configuration> call, Response<Configuration> response) {
                if(response.isSuccessful()){
                    configuration = response.body();
                    listener.onResponse(configuration);
                }else {
                    listener.onError();
                }
            }

            @Override
            public void onFailure(Call<Configuration> call, Throwable t) {
                listener.onError();
            }
        });
    }

    public List<ConfigurationItem> getSections(Context ctx) {
        if (configuration != null)
            return configuration.list;
        return Collections.EMPTY_LIST;
    }

    public String getSearchUrl(Context context){
        if(configuration != null)
            return configuration.apis.search;
        return null;
    }

}
