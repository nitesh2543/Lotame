package com.ndtv.mediaprima.mvvm;

import com.ndtv.mediaprima.mvvm.model.Configuration;
import com.ndtv.mediaprima.mvvm.model.DramaSangat;
import com.ndtv.mediaprima.mvvm.model.LoginCredential;
import com.ndtv.mediaprima.mvvm.model.PostResponse;
import com.ndtv.mediaprima.mvvm.model.Result;
import com.ndtv.mediaprima.mvvm.model.Results;
import com.ndtv.mediaprima.mvvm.model.Search;
import com.ndtv.mediaprima.mvvm.model.User;
import com.ndtv.mediaprima.mvvm.model.VerifyPassword;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by Elaa on 8/27/2016.
 */
public interface APIService {

    //production:-   http://www.dramasangat.my/api/android/v1/configurations
    //testing:-  http://ec2-54-254-131-19.ap-southeast-1.compute.amazonaws.com/api/android/v1/configurations


    interface HeaderKeys {
        String CONTENT_TYPE = "Content-Type";
        String AUTHORIZATION = "Authorization";
    }

    @GET("http://www.dramasangat.my/api/android/v1/configurations")
    Call<Configuration> downloadConfiguration();

    @GET
    Call<DramaSangat> downloadHomePage(@Url String url);

    @GET
    Call<Results> downloadDramas(@Url String url);

    @GET
    Call<Result> downloadDramaDetail(@Url String url);

    @GET
    Call<Search> downloadSearchList(@Url String url);

    @GET
    Call<Result> downloadAbout(@Url String url);

    @POST("http://ec2-54-254-131-19.ap-southeast-1.compute.amazonaws.com/api/ios/v1/app-users/login")
    Call<PostResponse> login(@Body LoginCredential loginCredential);

    @POST("http://ec2-54-254-131-19.ap-southeast-1.compute.amazonaws.com/api/ios/v1/app-users/logout")
    Call<PostResponse> logout(@Header(HeaderKeys.AUTHORIZATION) String authorizationToken);

    @POST("http://ec2-54-254-131-19.ap-southeast-1.compute.amazonaws.com/api/ios/v1/app-users/register")
    Call<PostResponse> register(@Header(HeaderKeys.CONTENT_TYPE) String contentType, @Body User user);

    @POST("http://ec2-54-254-131-19.ap-southeast-1.compute.amazonaws.com/api/ios/v1/app-users/forgot-password")
    Call<PostResponse> resetPassword(@Header(HeaderKeys.CONTENT_TYPE) String contentType, @Body User user);

    @POST("http://ec2-54-254-131-19.ap-southeast-1.compute.amazonaws.com/api/ios/v1/app-users/verify-password")
    Call<PostResponse> verifyPassword(@Header(HeaderKeys.CONTENT_TYPE) String contentType, @Body VerifyPassword verifyPassword);
}
