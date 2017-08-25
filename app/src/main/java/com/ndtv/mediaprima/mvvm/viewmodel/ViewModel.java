package com.ndtv.mediaprima.mvvm.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.util.Log;
import android.view.View;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Elaa on 8/27/2016.
 */
public abstract class ViewModel {

    public final ObservableInt isLoading;
    public final ObservableBoolean isRefreshing;
    public final ObservableBoolean isNextPageLoading;
    private ResponseListener responseListener;
    private DataListener dataListener;

    protected abstract void onDestroyView();

    public ViewModel() {
        isLoading = new ObservableInt(View.VISIBLE);
        isRefreshing = new ObservableBoolean(false);
        isNextPageLoading = new ObservableBoolean(false);
    }

    public ViewModel(ResponseListener listener) {
        this();
        responseListener = listener;
    }

    protected void setListener(ResponseListener listener) {
        responseListener = listener;
    }

    public interface ResponseListener<T> {
        void onResponse(T response);

        void onNextResponse(T response);

        void onNextError();

        void onError();

    }

    protected <T> void downloadFirstPage(Call<T> call) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, retrofit2.Response<T> response) {
                if (response.body() == null) {
                    responseListener.onError();
                    return;
                }
                isRefreshing.set(false);
                isLoading.set(View.GONE);
                if (response.isSuccessful()) {
                    responseListener.onResponse(response.body());
                } else {
                    responseListener.onError();
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                isRefreshing.set(false);
                isLoading.set(View.GONE);
                responseListener.onError();
                Log.e("Error", "onFailure: " + t.getMessage());
            }
        });
    }


    protected <T> void downloadNextPage(Call<T> call) {
        isNextPageLoading.set(true);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, retrofit2.Response<T> response) {
                isNextPageLoading.set(false);
                if (response.isSuccessful()) {
                    responseListener.onNextResponse(response.body());
                } else {
                    responseListener.onNextError();
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                isNextPageLoading.set(false);
                responseListener.onNextError();
            }
        });
    }

    public ViewModel(DataListener listener) {
        this();
        dataListener = listener;
    }

    public interface DataListener<T> {
        void onResponse(T response);

        void onError();
    }

    protected <T> void downloadData(Call<T> call) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, retrofit2.Response<T> response) {
                isRefreshing.set(false);
                isLoading.set(View.GONE);
                if (response.isSuccessful()) {
                    dataListener.onResponse(response.body());
                } else {
                    dataListener.onError();
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                isRefreshing.set(false);
                isLoading.set(View.GONE);
                dataListener.onError();
            }
        });
    }
}
