package com.expertily.lawsome.Extras;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.expertily.lawsome.API.models.responses.CasesRes;
import com.expertily.lawsome.API.services.ApiClient;
import com.expertily.lawsome.API.services.ApiInterface;
import com.expertily.lawsome.Authentication.Login;
import com.expertily.lawsome.Dashboard;
import com.expertily.lawsome.Storage.LocalStorage;
import com.expertily.lawsome.Storage.TinyDB;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Splash extends AppCompatActivity {

    private LocalStorage storage;
    private TinyDB db;
    private ApiInterface api;
    private ArrayList<String> c_names = new ArrayList<>();
    private ArrayList<String> c_typer = new ArrayList<>();
    private ArrayList<String> c_court = new ArrayList<>();
    private ArrayList<String> c_type = new ArrayList<>();
    private ArrayList<String> c_year = new ArrayList<>();
    private ArrayList<String> c_orc = new ArrayList<>();
    private ArrayList<String> c_number = new ArrayList<>();
    private ArrayList<String> c_filing = new ArrayList<>();
    private ArrayList<String> c_practice = new ArrayList<>();
    private ArrayList<String> c_client = new ArrayList<>();
    private ArrayList<String> cl_type = new ArrayList<>();
    private ArrayList<String> c_description = new ArrayList<>();
    private ArrayList<String> cl_email = new ArrayList<>();
    private ArrayList<String> cl_mobile = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new TinyDB(getApplicationContext());
        storage = new LocalStorage(getApplicationContext());
        api = ApiClient.getClient().create(ApiInterface.class);

        if (storage.dashboardIn()) {

            api.getCases(db.getString("phonenumber").substring(2)).enqueue(new Callback<List<CasesRes>>() {
                @Override
                public void onResponse(Call<List<CasesRes>> call, Response<List<CasesRes>> response) {

                    if (response.isSuccessful()) {

                        int count = response.body().size();

                        if (count > 0) {
                            db.putBoolean("hasCases", true);

                            for (int i = 0; i < count; i++) {

                                String a = ((response.body().get(i).getCaseName().isEmpty()) ? "N/A" : response.body().get(i).getCaseName());
                                String b = ((response.body().get(i).getCaseNature().isEmpty()) ? "N/A" : response.body().get(i).getCaseNature());
                                String c = ((response.body().get(i).getCaseCourt().isEmpty()) ? "N/A" : response.body().get(i).getCaseCourt());
                                String d = ((response.body().get(i).getCaseType().isEmpty()) ? "N/A" : response.body().get(i).getCaseType());
                                String e = ((response.body().get(i).getCaseYear().isEmpty()) ? "N/A" : response.body().get(i).getCaseYear());
                                String f = ((response.body().get(i).getCaseCounsel().isEmpty()) ? "N/A" : response.body().get(i).getCaseCounsel());
                                String g = ((response.body().get(i).getCaseNumber().isEmpty()) ? "N/A" : response.body().get(i).getCaseNumber());
                                String h = ((response.body().get(i).getCaseFilingDate().isEmpty()) ? "N/A" : response.body().get(i).getCaseFilingDate());
                                String j = ((response.body().get(i).getCasePracticeArea().isEmpty()) ? "N/A" : response.body().get(i).getCasePracticeArea());
                                String k = ((response.body().get(i).getCaseClient().isEmpty()) ? "N/A" : response.body().get(i).getCaseClient());
                                String l = ((response.body().get(i).getCaseClientType().isEmpty()) ? "N/A" : response.body().get(i).getCaseClientType());
                                String m = ((response.body().get(i).getCaseDescription().isEmpty()) ? "N/A" : response.body().get(i).getCaseDescription());
                                String n = ((response.body().get(i).getCaseClientEmail().isEmpty()) ? "N/A" : response.body().get(i).getCaseClientEmail());
                                String o = ((response.body().get(i).getCaseClientMobile().isEmpty()) ? "N/A" : response.body().get(i).getCaseClientMobile());

                                c_names.add(a);
                                c_typer.add(b);
                                c_court.add(c);
                                c_type.add(d);
                                c_year.add(e);
                                c_orc.add(f);
                                c_number.add(g);
                                c_filing.add(h);
                                c_practice.add(j);
                                c_client.add(k);
                                cl_type.add(l);
                                c_description.add(m);
                                cl_email.add(n);
                                cl_mobile.add(o);

                                db.putListString("case_names", c_names);
                                db.putListString("case_typers", c_typer);
                                db.putListString("court_names", c_court);
                                db.putListString("case_types", c_type);
                                db.putListString("case_years", c_year);
                                db.putListString("case_orcs", c_orc);
                                db.putListString("case_numbers", c_number);
                                db.putListString("case_dates", c_filing);
                                db.putListString("case_areas", c_practice);
                                db.putListString("client_names", c_client);
                                db.putListString("client_types", cl_type);
                                db.putListString("case_descriptions", c_description);
                                db.putListString("client_emails", cl_email);
                                db.putListString("client_mobiles", cl_mobile);
                            }
                        } else {
                            db.putBoolean("hasCases", false);
                        }

                        Intent intent = new Intent(Splash.this, Dashboard.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(Splash.this, "Unable to fetch cases", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Splash.this, Dashboard.class);
                        startActivity(intent);
                        finish();
                    }

                }

                @Override
                public void onFailure(Call<List<CasesRes>> call, Throwable t) {
                    Toast.makeText(Splash.this, "Unable to fetch cases", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Splash.this, Dashboard.class);
                    startActivity(intent);
                    finish();
                }
            });

        } else {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
        }
    }
}