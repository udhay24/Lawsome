package com.expertily.lawsome.Authentication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.expertily.lawsome.API.models.responses.AuthRes;
import com.expertily.lawsome.API.models.responses.OTPRes;
import com.expertily.lawsome.API.models.responses.VerifyRes;
import com.expertily.lawsome.API.services.ApiClient;
import com.expertily.lawsome.API.services.ApiInterface;
import com.expertily.lawsome.API.services.OTPClient;
import com.expertily.lawsome.API.services.OTPInterface;
import com.expertily.lawsome.Dashboard;
import com.expertily.lawsome.Extras.ReadSMS;
import com.expertily.lawsome.Extras.SmsListener;
import com.expertily.lawsome.R;
import com.expertily.lawsome.Storage.LocalStorage;
import com.expertily.lawsome.Storage.TinyDB;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private String MSG91_AUTH_KEY = "171702A2hRweFdSW5b2d155b";
    private String MSG91_SENDER_ID = "EXPOTP";
    private String name;
    private String otpnumber;
    private String phonenumber;
    private ImageView nameimage;
    private ImageView verifyimage;
    private Typeface bold;
    private EditText mobilefield;
    private EditText otpfield;
    private EditText namefield;
    private TextView disclaimer;
    private TextView cc;
    private Button submit;
    private Button otpsubmit;
    private Button register;
    private ApiInterface api;
    private OTPInterface otp;
    private ProgressDialog loading;
    private LocalStorage storage;
    private TinyDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        db = new TinyDB(getApplicationContext());
        storage = new LocalStorage(getApplicationContext());
        bold = Typeface.createFromAsset(getAssets(), "fonts/semibold.ttf");

        mobilefield = findViewById(R.id.mobile);
        otpfield = findViewById(R.id.otp);
        disclaimer = findViewById(R.id.disclaimer);
        cc = findViewById(R.id.cc);
        submit = findViewById(R.id.button);
        otpsubmit = findViewById(R.id.otpsubmit);
        namefield = findViewById(R.id.namefield);
        register = findViewById(R.id.register);
        nameimage = findViewById(R.id.nameimage);
        verifyimage = findViewById(R.id.verifyimage);

        api = ApiClient.getClient().create(ApiInterface.class);
        otp = OTPClient.getClient().create(OTPInterface.class);
        submit.setOnClickListener(this);
        otpsubmit.setOnClickListener(this);
        register.setOnClickListener(this);

        namefield.setTypeface(bold);
        mobilefield.setTypeface(bold);
        otpfield.setTypeface(bold);
        disclaimer.setTypeface(bold);
        cc.setTypeface(bold);
        submit.setTypeface(bold);
        otpsubmit.setTypeface(bold);

        smsListener();
        permissions();
    }

    private void alert(String message) {
        Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();
    }

    private void stateOTPSent() {
        cc.setVisibility(View.GONE);
        nameimage.setVisibility(View.GONE);
        namefield.setVisibility(View.GONE);
        register.setVisibility(View.GONE);
        mobilefield.setVisibility(View.GONE);
        submit.setVisibility(View.GONE);
        verifyimage.setVisibility(View.VISIBLE);
        otpfield.setVisibility(View.VISIBLE);
        otpsubmit.setVisibility(View.VISIBLE);
    }

    private void stateNewUser() {
        nameimage.setVisibility(View.VISIBLE);
        namefield.setVisibility(View.VISIBLE);
        register.setVisibility(View.VISIBLE);
        cc.setVisibility(View.GONE);
        mobilefield.setVisibility(View.GONE);
        submit.setVisibility(View.GONE);
    }

    private void smsListener() {

        ReadSMS.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                messageText = messageText.replaceAll("\\D+", "");
                otpfield.setText(messageText);
                verifyOTP(messageText);
            }
        });

    }

    private void permissions() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 69);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.CALL_PHONE}, 69);
        }

    }

    private void attemptLogin() {

        loading = new ProgressDialog(Login.this, R.style.MyTheme);
        loading.setCancelable(false);
        loading.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        loading.show();

        api.attemptLogin(mobilefield.getText().toString()).enqueue(new Callback<AuthRes>() {
            @Override
            public void onResponse(Call<AuthRes> call, Response<AuthRes> response) {
                if (response.isSuccessful()) {

                    if (response.body().getStatus().equalsIgnoreCase("successful")) {

                        phonenumber = "91" + mobilefield.getText().toString();
                        name = response.body().getName();
                        sendOTP();

                    } else {
                        loading.dismiss();
                        stateNewUser();
                    }

                } else {
                    loading.dismiss();
                    alert("Some error occurred!");
                }
            }

            @Override
            public void onFailure(Call<AuthRes> call, Throwable t) {
                loading.dismiss();
                alert("Some error occurred!");
            }
        });

    }

    private void attemptRegister() {

        loading = new ProgressDialog(Login.this, R.style.MyTheme);
        loading.setCancelable(false);
        loading.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        loading.show();

        api.attemptRegister(namefield.getText().toString(), mobilefield.getText().toString()).enqueue(new Callback<AuthRes>() {
            @Override
            public void onResponse(Call<AuthRes> call, Response<AuthRes> response) {
                if (response.isSuccessful()) {
                    phonenumber = "91" + mobilefield.getText().toString();
                    name = namefield.getText().toString();
                    sendOTP();

                } else {
                    loading.dismiss();
                    alert("Some error occurred!");
                }
            }

            @Override
            public void onFailure(Call<AuthRes> call, Throwable t) {
                loading.dismiss();
                alert("Some error occurred!");
            }
        });

    }

    private void sendOTP() {

        int randomPIN = (int) (Math.random() * 9000) + 1000;
        otpnumber = "" + randomPIN;

        otp.sendOTP(MSG91_AUTH_KEY, phonenumber, "Your OTP is " + otpnumber, MSG91_SENDER_ID, otpnumber).enqueue(new Callback<OTPRes>() {
            @Override
            public void onResponse(Call<OTPRes> call, Response<OTPRes> response) {

                if (response.isSuccessful()) {
                    stateOTPSent();
                    loading.dismiss();

                } else {
                    loading.dismiss();
                    alert("Some error occurred!");
                }
            }

            @Override
            public void onFailure(Call<OTPRes> call, Throwable t) {
                loading.dismiss();
                alert("Some error occurred!");
            }
        });

    }

    private void verifyOTP(String otpnumber) {

        loading = new ProgressDialog(Login.this, R.style.MyTheme);
        loading.setCancelable(false);
        loading.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        loading.show();

        otp.verifyOTP(MSG91_AUTH_KEY, phonenumber, otpnumber).enqueue(new Callback<VerifyRes>() {
            @Override
            public void onResponse(Call<VerifyRes> call, Response<VerifyRes> response) {

                if (response.isSuccessful()) {

                    if (response.body().getMessage().equalsIgnoreCase("otp_verified")) {
                        loading.dismiss();
                        storage.loginSession(name, phonenumber);
                        db.putString("name", name);
                        db.putString("phonenumber", phonenumber);
                        db.putBoolean("refresh", true);
                        alert("OTP verified!");
                        Intent go = new Intent(Login.this, Dashboard.class);
                        startActivity(go);
                        finish();

                    } else {
                        loading.dismiss();
                        alert("OTP verification failed!");
                    }
                } else {
                    loading.dismiss();
                    alert("Some error occurred!");
                }
            }

            @Override
            public void onFailure(Call<VerifyRes> call, Throwable t) {
                loading.dismiss();
                alert("Some error occurred!");
            }
        });

    }

    @Override
    public void onClick(View view) {

        if (view == submit) {

            if (!mobilefield.getText().toString().isEmpty()) {

                if (mobilefield.getText().length() < 10) {
                    alert("Please enter a valid number");

                } else {
                    attemptLogin();
                }

            } else {
                alert("Please enter your number");
            }

        }

        if (view == otpsubmit) {

            if (!otpfield.getText().toString().isEmpty()) {

                if (otpfield.getText().length() < 4) {
                    alert("Please enter a valid OTP");

                } else {
                    verifyOTP(otpfield.getText().toString());
                }

            } else {
                alert("Please enter the OTP");
            }

        }

        if (view == register) {

            if (!namefield.getText().toString().isEmpty()) {

                if (namefield.getText().length() < 4) {
                    alert("Please enter full name");

                } else {
                    attemptRegister();
                }

            } else {
                alert("Please enter the OTP");
            }

        }
    }

}