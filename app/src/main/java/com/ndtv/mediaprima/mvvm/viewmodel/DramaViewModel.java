package com.ndtv.mediaprima.mvvm.viewmodel;

import com.ndtv.mediaprima.mvvm.APIService;
import com.ndtv.mediaprima.mvvm.ServiceGenerator;
import com.ndtv.mediaprima.mvvm.model.Results;

import retrofit2.Call;

/**
 * Created by Elaa on 8/30/2016.
 */
public class DramaViewModel extends ViewModel {

    private APIService dramaAPI;
    private Call<Results> call;
    private String url;

    public DramaViewModel(ResponseListener listener, String url) {
        super(listener);
        this.url = url;
        dramaAPI = ServiceGenerator.createService(APIService.class);
    }

    public void downloadDramas() {
        call = dramaAPI.downloadDramas(url);
        downloadFirstPage(call);
    }

    public void downloadNextPage(String nextUrl) {
        call = dramaAPI.downloadDramas(nextUrl);
        downloadNextPage(call);
    }

    public void refreshDramas() {
        isRefreshing.set(true);
        downloadDramas();
    }

    @Override
    protected void onDestroyView() {

    }
}
