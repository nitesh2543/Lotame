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
import com.ndtv.mediaprima.databinding.FragmentVerifyEmailBinding;
import com.ndtv.mediaprima.mvvm.APIService;
import com.ndtv.mediaprima.mvvm.ServiceGenerator;
import com.ndtv.mediaprima.mvvm.model.PostResponse;
import com.ndtv.mediaprima.mvvm.model.VerifyPassword;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VerifyEmailFragment extends BaseFragment implements View.OnClickListener {

    private FragmentVerifyEmailBinding binding;
    private Constants.AccountListener accountListener;
    private ProgressDialog progressDialogue;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        accountListener = (Constants.AccountListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_verify_email, container, false);
        binding.btnChangePassword.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_change_password:
                progressDialogue = Utility.createProgressDialog(getActivity());
                progressDialogue.show();
                verifyPassword();
                Utility.hideKeyboard(getContext());
                break;
        }
    }

    private void verifyPassword() {
        if (!Network.isConnected(getActivity())) {
            Network.showAlert(getActivity());
            return;
        }
        VerifyPassword verificationCredential = new VerifyPassword(getArguments().getString(Constants.BundleKeys.EMAIL),
                binding.verificationCode.getText().toString().trim()
                , binding.newPassword.getText().toString().trim(),
                binding.newPasswordConfirm.getText().toString().trim());
        APIService apiService = ServiceGenerator.createService(APIService.class);
        Call<PostResponse> call = apiService.verifyPassword("application/json", verificationCredential);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                progressDialogue.dismiss();

                switch (response.code()) {
                    case HttpsURLConnection.HTTP_OK:
                        Utility.showToast(getContext(), response.body().message);
                        accountListener.onVerificationSuccess(null);
                        break;
                    default:
                        try {
                            JSONObject obj = new JSONObject(response.errorBody().string());
                            Utility.showAlert(getContext(), obj.get("error").toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Utility.showAlert(getContext(), "Network issue please try after some time.");
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
        GoogleAnalyticsHelper.screenView(getActivity(), Constants.DramaSangatScreenName.VERIFY_EMAIL_SCREEN);
    }
}
