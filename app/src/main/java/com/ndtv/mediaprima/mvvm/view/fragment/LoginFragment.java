package com.ndtv.mediaprima.mvvm.view.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.gigya.socialize.GSObject;
import com.gigya.socialize.GSResponse;
import com.gigya.socialize.GSResponseListener;
import com.gigya.socialize.android.GSAPI;
import com.gigya.socialize.android.GSLoginRequest;
import com.gigya.socialize.android.GSSession;
import com.gigya.socialize.android.login.providers.FacebookProvider;
import com.ndtv.mediaprima.R;
import com.ndtv.mediaprima.common.GoogleAnalyticsHelper;
import com.ndtv.mediaprima.common.ui.BaseFragment;
import com.ndtv.mediaprima.common.utils.Constants;
import com.ndtv.mediaprima.common.utils.Network;
import com.ndtv.mediaprima.common.utils.PreferenceManager;
import com.ndtv.mediaprima.common.utils.Utility;
import com.ndtv.mediaprima.databinding.FragmentLoginBinding;
import com.ndtv.mediaprima.mvvm.APIService;
import com.ndtv.mediaprima.mvvm.ServiceGenerator;
import com.ndtv.mediaprima.mvvm.model.LoginCredential;
import com.ndtv.mediaprima.mvvm.model.PostResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends BaseFragment implements View.OnClickListener, GSResponseListener {

    private String TAG = LoginFragment.class.getSimpleName();

    private FragmentLoginBinding binding;
    private Constants.AccountListener accountListener;
    private CallbackManager callbackManager;
    private ProgressDialog progressDialogue;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        accountListener = (Constants.AccountListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        binding.fb.setOnClickListener(this);
        callbackManager = CallbackManager.Factory.create();
        binding.emailLoginBtn.setOnClickListener(this);
        binding.emailSignupBtn.setOnClickListener(this);
        binding.fb.setOnClickListener(this);
        binding.forgotPasswordBtn.setOnClickListener(this);
        //setPaddingForFBButton();
        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fb:
                progressDialogue = Utility.createProgressDialog(getActivity());
                progressDialogue.show();
                onLoginWithFB();
                Utility.hideKeyboard(getContext());
                break;
            case R.id.email_login_btn:
                progressDialogue = Utility.createProgressDialog(getActivity());
                progressDialogue.show();
                loginWithEmail();
                Utility.hideKeyboard(getContext());
                break;
            case R.id.email_signup_btn:
                accountListener.onRegistration(null);
                break;
            case R.id.forgot_password_btn:
                accountListener.onForgotPassword(null);
                break;
        }
    }

    private void setPaddingForFBButton() {
        binding.fb.setPadding(getResources().getDimensionPixelSize(
                R.dimen.com_facebook_loginview_padding_left),
                getResources().getDimensionPixelSize(
                        R.dimen.com_facebook_loginview_padding_top),
                getResources().getDimensionPixelSize(
                        R.dimen.com_facebook_loginview_padding_right),
                getResources().getDimensionPixelSize(
                        R.dimen.com_facebook_loginview_padding_bottom));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.fb.setElevation(4);
        }
    }


    private void loginWithEmail() {

        if (!Network.isConnected(getActivity())) {
            Network.showAlert(getActivity());
            return;
        }
        APIService apiService = ServiceGenerator.createService(APIService.class);
        Call<PostResponse> call = apiService.login(new LoginCredential(binding.inputLayoutEmail.getEditText().getText().toString(),
                binding.inputLayoutPassword.getEditText().getText().toString()));
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                progressDialogue.dismiss();
                switch (response.code()) {
                    case HttpsURLConnection.HTTP_OK:
                        Toast.makeText(getContext(), "User logged in successfully.", Toast.LENGTH_SHORT).show();
                        GSAPI.getInstance().setSession(new GSSession(response.headers().get("Gigya-Session-Token"),
                                response.headers().get("Gigya-Session-Secret")));
                        PreferenceManager.getsInstance(getContext()).setAuthorizationToken(response.headers().get("Authorization"));
                        accountListener.onLogin();
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
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    private void onLoginWithFB() {
        GSObject params = new GSObject();
        params.put("provider", "Facebook");
        if (!FacebookProvider.isLoggedIn()) {
            try {
                GSLoginRequest loginRequest = GSAPI.getInstance().login(getActivity(), params, this, true);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "onLoginWithFB: " + e.getMessage());
            }
        }
    }

    @Override
    public void onGSResponse(String s, GSResponse gsResponse, Object o) {
        progressDialogue.dismiss();
        if (gsResponse != null) {
            if (gsResponse.getResponseText().contains("facebook")) {
                accountListener.onLogin();
                Toast.makeText(getContext(), "User logged in successfully.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        GoogleAnalyticsHelper.screenView(getActivity(), Constants.DramaSangatScreenName.LOGIN_SCREEN);
    }
}
