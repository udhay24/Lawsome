package com.expertily.lawsome.Fragments.OpenedCases;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.expertily.lawsome.API.models.responses.AuthRes;
import com.expertily.lawsome.API.services.ApiClient;
import com.expertily.lawsome.API.services.ApiInterface;
import com.expertily.lawsome.R;
import com.expertily.lawsome.Storage.TinyDB;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Connect extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final Collection<String> SCOPE = Arrays.asList("https://www.googleapis.com/auth/userinfo.profile;https://www.googleapis.com/auth/userinfo.email;https://www.googleapis.com/auth/gmail.modify".split(";"));
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private View view;
    private int pos;
    private String gmail;
    private String email;
    private String mobile;
    private LinearLayout buttonpanel1;
    private LinearLayout buttonpanel2;
    private FrameLayout setemailparent;
    private FrameLayout setmobileparent;
    private TextView emailavailability;
    private TextView mobileavailability;
    private TextView emailarea;
    private TextView callarea;
    private EditText enteremail;
    private EditText entermobile;
    private ImageView setemail;
    private ImageView setmobile;
    private Button sendemail;
    private Button placecall;
    private ArrayList<String> c_numbers = new ArrayList<>();
    private ArrayList<String> cl_email = new ArrayList<>();
    private ArrayList<String> cl_mobile = new ArrayList<>();
    private ApiInterface api;
    private TinyDB db;
    private ProgressDialog loading;
    private GoogleSignInOptions gso;
    private GoogleApiClient gapiclient;
    private GoogleSignInAccount account;
    private GoogleSignInClient gclient;
    private OptionalPendingResult<GoogleSignInResult> googletoken;
    private GoogleSignInResult result;
    private Intent gsignin;

    public Connect() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.connect, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        api = ApiClient.getClient().create(ApiInterface.class);
        db = new TinyDB(view.getContext());
        googleSDK();

        googletoken = Auth.GoogleSignInApi.silentSignIn(gapiclient);
        account = GoogleSignIn.getLastSignedInAccount(view.getContext());
        buttonpanel1 = view.findViewById(R.id.buttonpanel1);
        buttonpanel2 = view.findViewById(R.id.buttonpanel2);
        setemailparent = view.findViewById(R.id.setemailparent);
        setmobileparent = view.findViewById(R.id.setmobileparent);
        emailavailability = view.findViewById(R.id.emailavailability);
        mobileavailability = view.findViewById(R.id.mobileavailability);
        emailarea = view.findViewById(R.id.emailarea);
        callarea = view.findViewById(R.id.callarea);
        enteremail = view.findViewById(R.id.enteremail);
        entermobile = view.findViewById(R.id.entermobile);
        setemail = view.findViewById(R.id.setemail);
        setmobile = view.findViewById(R.id.setmobile);
        sendemail = view.findViewById(R.id.sendemail);
        placecall = view.findViewById(R.id.placecall);

        setemail.setOnClickListener(this);
        setmobile.setOnClickListener(this);
        sendemail.setOnClickListener(this);
        placecall.setOnClickListener(this);

        pos = db.getInt("casedetails_position");
        c_numbers = db.getListString("case_numbers");
        cl_email = db.getListString("client_emails");
        cl_mobile = db.getListString("client_mobiles");
        email = cl_email.get(pos);
        mobile = cl_mobile.get(pos);

        check();
        return view;
    }

    private void showAlert(String message) {
        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean checkCallingPermission() {
        String permission = Manifest.permission.CALL_PHONE;
        int res = getContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    private void googleSDK() {

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gapiclient = new GoogleApiClient.Builder(view.getContext())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        gclient = GoogleSignIn.getClient(view.getContext(), gso);

    }

    private void check() {
        if (!email.equalsIgnoreCase("N/A")) {
            setemailparent.setVisibility(View.GONE);
            buttonpanel1.setVisibility(View.VISIBLE);
            emailavailability.setText("Available");
            emailarea.setText("Email Address: " + email);
            emailavailability.setTextColor(Color.parseColor("#4CAF50"));
        }
        if (!mobile.equalsIgnoreCase("N/A")) {
            setmobileparent.setVisibility(View.GONE);
            buttonpanel2.setVisibility(View.VISIBLE);
            mobileavailability.setText("Available");
            callarea.setText("Mobile Number: " + mobile);
            mobileavailability.setTextColor(Color.parseColor("#4CAF50"));
        }
    }

    private void updateEmail() {

        loading = new ProgressDialog(view.getContext(), R.style.MyTheme);
        loading.setCancelable(false);
        loading.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        loading.show();

        api.updateEmail(db.getString("phonenumber").substring(2), enteremail.getText().toString(), c_numbers.get(pos)).enqueue(new Callback<AuthRes>() {
            @Override
            public void onResponse(Call<AuthRes> call, Response<AuthRes> response) {

                if (response.isSuccessful()) {

                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        loading.dismiss();
                        showAlert("Email updated successfully");
                        email = enteremail.getText().toString();
                        check();
                        db.putBoolean("refresh", true);
                    } else {
                        loading.dismiss();
                        showAlert("Email update failed");
                    }

                } else {
                    loading.dismiss();
                    showAlert("Email update failed");
                }

            }

            @Override
            public void onFailure(Call<AuthRes> call, Throwable t) {
                loading.dismiss();
                showAlert("Email update failed");
            }
        });

    }

    private void updateMobile() {

        loading = new ProgressDialog(view.getContext(), R.style.MyTheme);
        loading.setCancelable(false);
        loading.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        loading.show();

        api.updateMobile(db.getString("phonenumber").substring(2), entermobile.getText().toString(), c_numbers.get(pos)).enqueue(new Callback<AuthRes>() {
            @Override
            public void onResponse(Call<AuthRes> call, Response<AuthRes> response) {

                if (response.isSuccessful()) {

                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        loading.dismiss();
                        showAlert("Mobile updated successfully");
                        mobile = entermobile.getText().toString();
                        check();
                        db.putBoolean("refresh", true);
                    } else {
                        loading.dismiss();
                        showAlert("Email update failed");
                    }

                } else {
                    loading.dismiss();
                    showAlert("Email update failed");
                }

            }

            @Override
            public void onFailure(Call<AuthRes> call, Throwable t) {
                loading.dismiss();
                showAlert("Email update failed");
            }
        });

    }

    private void googleDetails(GoogleSignInResult result) {

        if (result.isSuccess()) {
            account = result.getSignInAccount();
            gmail = account.getEmail();
            db.putString("gmail", gmail);
        } else {
            Toast.makeText(view.getContext(), "Login failed", Toast.LENGTH_SHORT).show();
        }

    }

    public static MimeMessage createEmail(String to, String from, String subject, String bodyText) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);
        return email;
    }

    public static Message createMessageWithEmail(MimeMessage emailContent) throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

    public static Message sendMessage(Gmail service, String userId, MimeMessage emailContent) throws MessagingException, IOException {
        Message message = createMessageWithEmail(emailContent);
        message = service.users().messages().send(userId, message).execute();
        System.out.println("Message id: " + message.getId());
        System.out.println(message.toPrettyString());
        return message;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(view.getContext(), "Bad Connection. Try again later.", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0601) {
            result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            googleDetails(result);
        }

    }

    @Override
    public void onClick(View v) {
        if (v == sendemail) {

            if (account != null) {

            }
            else {
                gsignin = Auth.GoogleSignInApi.getSignInIntent(gapiclient);
                startActivityForResult(gsignin, 0601);
            }

        } else if (v == placecall) {
            if (checkCallingPermission()) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile));
                startActivity(intent);
            } else {
                if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.CALL_PHONE}, 69);
                }
            }

        } else if (v == setemail) {
            if (!enteremail.getText().toString().isEmpty() && isEmailValid(enteremail.getText().toString())) {
                updateEmail();
            } else {
                showAlert("Enter valid email address");
            }
        } else if (v == setmobile) {
            if (!entermobile.getText().toString().isEmpty() && entermobile.getText().toString().length() == 10) {
                updateMobile();
            } else {
                showAlert("Enter valid mobile number");
            }
        }
    }

}
