package com.ndtv.mediaprima.mvvm.view.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ndtv.mediaprima.R;
import com.ndtv.mediaprima.common.GoogleAnalyticsHelper;
import com.ndtv.mediaprima.common.ui.BaseFragment;
import com.ndtv.mediaprima.common.utils.Constants;
import com.ndtv.mediaprima.common.utils.Network;
import com.ndtv.mediaprima.common.utils.Utility;
import com.ndtv.mediaprima.databinding.FragmentForgotPasswordBinding;
import com.ndtv.mediaprima.mvvm.APIService;
import com.ndtv.mediaprima.mvvm.ServiceGenerator;
import com.ndtv.mediaprima.mvvm.model.PostResponse;
import com.ndtv.mediaprima.mvvm.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ForgotPasswordFragment extends BaseFragment implements View.OnClickListener {

    private FragmentForgotPasswordBinding binding;
    private Constants.AccountListener accountListener;
    private ProgressDialog progressDialogue;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        accountListener = (Constants.AccountListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_password, container, false);
        binding.btnProceed.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_proceed:
                progressDialogue = Utility.createProgressDialog(getActivity());
                progressDialogue.show();
                resetPassword();
                Utility.hideKeyboard(getContext());
                break;
        }
    }

    private void resetPassword() {

        if (!Network.isConnected(getActivity())) {
            Network.showAlert(getActivity());
            return;
        }
        User.Builder userBuilder = new User.Builder(null, null, binding.email.getText().toString(), null, null, 0, 0, 0, null);
        APIService apiService = ServiceGenerator.createService(APIService.class);
        Call<PostResponse> call = apiService.resetPassword(Constants.CONTENT_TYPE, userBuilder.build());
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                progressDialogue.dismiss();

                switch (response.code()) {
                    case HttpsURLConnection.HTTP_OK:
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.BundleKeys.EMAIL, binding.email.getText().toString());
                        Utility.showAlert(getContext(), response.body().message);
                        accountListener.onVerifyPassword(bundle);
                        break;
                    default:
                        try {
                            JSONObject obj = new JSONObject(response.errorBody().string());
                            Utility.showAlert(getContext(), obj.get("error").toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                progressDialogue.dismiss();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        GoogleAnalyticsHelper.screenView(getActivity(), Constants.DramaSangatScreenName.FORGOT_PASSWORD);
    }
}
