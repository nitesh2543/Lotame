package com.ndtv.mediaprima.mvvm.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ndtv.mediaprima.R;
import com.ndtv.mediaprima.common.ui.BaseActivity;
import com.ndtv.mediaprima.common.utils.Constants;
import com.ndtv.mediaprima.common.utils.FragmentHelper;
import com.ndtv.mediaprima.mvvm.view.fragment.ForgotPasswordFragment;
import com.ndtv.mediaprima.mvvm.view.fragment.LoginFragment;
import com.ndtv.mediaprima.mvvm.view.fragment.RegistrationFragment;
import com.ndtv.mediaprima.mvvm.view.fragment.VerifyEmailFragment;

public class LoginActivity extends BaseActivity implements Constants.AccountListener {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FragmentHelper.addFragment(this, R.id.account_container, new LoginFragment());
    }

    @Override
    public void onRegistration(Bundle bundle) {
        FragmentHelper.replaceAndAddFragment(this, R.id.account_container, new RegistrationFragment());
    }

    @Override
    public void onForgotPassword(Bundle bundle) {
        FragmentHelper.replaceAndAddFragment(this, R.id.account_container, new ForgotPasswordFragment());
    }

    @Override
    public void onVerifyPassword(Bundle bundle) {
        Fragment fragment = new VerifyEmailFragment();
        fragment.setArguments(bundle);
        FragmentHelper.replaceAndAddFragment(this, R.id.account_container, fragment);
    }

    @Override
    public void onVerificationSuccess(Bundle bundle) {
        FragmentHelper.replaceFragment(this, R.id.account_container, new LoginFragment());
    }

    @Override
    public void onLogin() {
        setResult(Activity.RESULT_OK);
        this.finish();
    }

    @Override
    public void onLoginWithFB() {
        setResult(Activity.RESULT_FIRST_USER);
        this.finish();
    }
}