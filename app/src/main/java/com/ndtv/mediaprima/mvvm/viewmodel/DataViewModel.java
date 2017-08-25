package com.ndtv.mediaprima.mvvm.viewmodel;

import com.ndtv.mediaprima.mvvm.APIService;
import com.ndtv.mediaprima.mvvm.ServiceGenerator;
import com.ndtv.mediaprima.mvvm.model.DramaSangat;
import com.ndtv.mediaprima.mvvm.model.Result;
import com.ndtv.mediaprima.mvvm.model.Results;
import com.ndtv.mediaprima.mvvm.model.Search;

import retrofit2.Call;

/**
 * Created by ELAA on 21-09-2016.
 */
public class DataViewModel extends ViewModel {

    private APIService apiService;
    private Call<Result> aboutCall;
    private Call<Search> searchCall;
    private Call<Results> responseCall;
    private Call<Results> lifestyleCall;
    private Call<DramaSangat> homeCall;
    private Call<Result> dramaDetailCall;
    private String url;

    public DataViewModel(DataListener listener, String url) {
        super(listener);
        this.url = url;
        apiService = ServiceGenerator.createService(APIService.class);
    }

    public void downloadAboutData() {
        aboutCall = apiService.downloadAbout(url);
        downloadData(aboutCall);
    }

    public void downloadSearchData() {
        searchCall = apiService.downloadSearchList(url);
        downloadData(searchCall);
    }

    public void downloadData() {
        responseCall = apiService.downloadDramas(url);
        downloadData(responseCall);
    }

    public void downloadLifestyleData() {
        lifestyleCall = apiService.downloadDramas(url);
        downloadData(lifestyleCall);
    }

    public void downloadHomeData() {
        homeCall = apiService.downloadHomePage(url);
        downloadData(homeCall);
    }

    public void downloadDramaDetail() {
        dramaDetailCall = apiService.downloadDramaDetail(url);
        downloadData(dramaDetailCall);
    }

    @Override
    protected void onDestroyView() {

    }

}
