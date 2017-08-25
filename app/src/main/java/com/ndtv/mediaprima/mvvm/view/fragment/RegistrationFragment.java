package com.ndtv.mediaprima.mvvm.view.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioGroup;

import com.ndtv.mediaprima.R;
import com.ndtv.mediaprima.common.GoogleAnalyticsHelper;
import com.ndtv.mediaprima.common.ui.BaseFragment;
import com.ndtv.mediaprima.common.utils.Constants;
import com.ndtv.mediaprima.common.utils.Network;
import com.ndtv.mediaprima.common.utils.Utility;
import com.ndtv.mediaprima.databinding.FragmentRegistrationBinding;
import com.ndtv.mediaprima.exception.InvalidTypeException;
import com.ndtv.mediaprima.mvvm.APIService;
import com.ndtv.mediaprima.mvvm.ServiceGenerator;
import com.ndtv.mediaprima.mvvm.model.PostResponse;
import com.ndtv.mediaprima.mvvm.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegistrationFragment extends BaseFragment implements View.OnClickListener,
        View.OnTouchListener, Constants.DateListener, RadioGroup.OnCheckedChangeListener {

    private String TAG = RegistrationFragment.class.getSimpleName();
    private FragmentRegistrationBinding binding;
    private int year, day, month;
    private String gender;
    private ProgressDialog progressDialogue;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_registration, container, false);
        binding.dob.setOnTouchListener(this);
        binding.gender.setOnCheckedChangeListener(this);
        binding.btnRegister.setOnClickListener(this);
        binding.dob.setKeyListener(null);
        return binding.getRoot();
    }

    private void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.setTargetFragment(this, 0);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int DRAWABLE_LEFT = 0;
        final int DRAWABLE_TOP = 1;
        final int DRAWABLE_RIGHT = 2;
        final int DRAWABLE_BOTTOM = 3;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (event.getRawX() >= (binding.dob.getRight() - binding.dob.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                // your action here
                Utility.hideKeyboard(getContext());
                binding.dob.setText("");
                showTimePickerDialog();
                return true;
            }
        }
        return false;
    }

    @Override
    public void OnDateSelected(int year, int month, int dayOfMonth) {
        int correctMonth = month + 1;
        binding.dob.setText(dayOfMonth + "/" + correctMonth + "/" + year);
        day = dayOfMonth;
        this.month = correctMonth;
        this.year = year;
    }

    private void register() {

        if (!Network.isConnected(getActivity())) {
            Network.showAlert(getActivity());
            return;
        }

        User.Builder userBuilder = new User.Builder(binding.name.getText().toString(), binding.lastName.getText().toString(),
                binding.email.getText().toString(),
                binding.password.getText().toString(), binding.confirmPassword.getText().toString(),
                day, month, year, gender);
        APIService apiService = ServiceGenerator.createService(APIService.class);
        Call<PostResponse> call = apiService.register(Constants.CONTENT_TYPE, userBuilder.build());
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                progressDialogue.dismiss();
                switch (response.code()) {
                    case HttpsURLConnection.HTTP_OK:
                        Utility.showToast(getContext(), response.body().message);
                        getActivity().onBackPressed();
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.m:
                gender = "m";
                break;
            case R.id.f:
                gender = "f";
                break;
            default:
                throw new InvalidTypeException("Invalid gender type");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                progressDialogue = Utility.createProgressDialog(getActivity());
                progressDialogue.show();
                register();
                Utility.hideKeyboard(getContext());
                break;
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), this, year, hour, minute);
            datePickerDialog.getDatePicker().setMaxDate(new Date("Sat Dec 31 17:32:23 GMT+05:30 2016").getTime());

            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy");
            try {
                Date d = sdf.parse("Mon Mar 06 17:32:23 GMT+05:30 2016");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //Sat Dec 31 17:32:23 GMT+05:30 2016
            // Create a new instance of TimePickerDialog and return it
            return datePickerDialog;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Constants.DateListener targetFragment = (Constants.DateListener) getTargetFragment();
            targetFragment.OnDateSelected(year, month, dayOfMonth);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        GoogleAnalyticsHelper.screenView(getActivity(), Constants.DramaSangatScreenName.REGISTRATION_SCREEN);
    }
}
