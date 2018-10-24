package com.expertily.lawsome.Fragments.OpenedCases;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.expertily.lawsome.API.models.responses.ImportDistrictRes;
import com.expertily.lawsome.API.models.responses.ImportRes;
import com.expertily.lawsome.API.services.ApiClient;
import com.expertily.lawsome.API.services.ApiInterface;
import com.expertily.lawsome.Adapters.ActionsAdapter;
import com.expertily.lawsome.R;
import com.expertily.lawsome.Storage.TinyDB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Actions extends Fragment {

    private int pos;
    private String currentdate;
    private String[] parts1;
    private String[] parts2;
    private String thecourt;
    private String courttype;
    private String type;
    private String number;
    private String year;
    private View view;
    private LinearLayout nocases;
    private ScrollView allactions;
    private RecyclerView actions_recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private ArrayList<String> c_court = new ArrayList<>();
    private ArrayList<String> c_numbers = new ArrayList<>();
    private ArrayList<String> importedaction = new ArrayList<>();
    private ArrayList<String> importedactiondate = new ArrayList<>();
    private TinyDB db;
    private ApiInterface api;

    public Actions() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_actions, container, false);
        api = ApiClient.getClient().create(ApiInterface.class);
        db = new TinyDB(getContext());
        nocases = view.findViewById(R.id.nocases);
        allactions = view.findViewById(R.id.allactions);
        actions_recycler = view.findViewById(R.id.actions);
        pos = db.getInt("casedetails_position");
        c_court = db.getListString("court_names");
        c_numbers = db.getListString("case_numbers");

        getActions();

        return view;
    }

    private void showAlert(String message) {
        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void getActions() {

        parts1 = c_court.get(pos).split("/");
        parts2 = c_numbers.get(pos).split("/");
        courttype = parts1[1];
        type = parts2[0];
        number = parts2[1];
        year = parts2[2];

        if (c_court.get(pos).contains("District")) {
            thecourt = "DistrictCourt";
            disctrictActions();
        } else if (c_court.get(pos).contains("High")) {
            thecourt = "HighCourt";
            highAction();
        } else if (c_court.get(pos).contains("Supreme")) {
            thecourt = "SupremeCourt";
            supremeAction();
        }

    }

    private void loadActions() {
        manager = new LinearLayoutManager(getContext());
        actions_recycler.setLayoutManager(manager);
        adapter = new ActionsAdapter(getActivity(), importedactiondate, importedaction, c_numbers.get(pos), parts1[0]);
        actions_recycler.setAdapter(adapter);
    }

    private void disctrictActions() {

        api.importDistrictCase(thecourt, courttype, type, number, year).enqueue(new Callback<ImportDistrictRes>() {
            @Override
            public void onResponse(Call<ImportDistrictRes> call, Response<ImportDistrictRes> response) {

                if (response.isSuccessful()) {
                    if (response.body().getMessage() == null) {

                        for (int i = 0; i < response.body().getActions().size(); i++) {
                            if (response.body().getActions().get(i).getAction().contains("Listed")) {
                                importedaction.add(response.body().getActions().get(i).getAction());
                                importedactiondate.add(response.body().getActions().get(i).getDate());
                            }
                        }

                        db.putListString("importedactiondate", importedactiondate);
                        db.putListString("importedaction", importedaction);

                        importedactiondate = db.getListString("importedactiondate");
                        importedaction = db.getListString("importedaction");

                        if (importedactiondate.size() != 0) {
                            nocases.setVisibility(View.GONE);
                            allactions.setVisibility(View.VISIBLE);
                            loadActions();
                        }

                    } else {
                        showAlert("Error contacting server!");
                    }

                } else {
                    showAlert("Fetching failed!");
                }

            }

            @Override
            public void onFailure(Call<ImportDistrictRes> call, Throwable t) {
                if (t.toString().contains("Socket")) {
                    showAlert("Failed to contact. Please try again later.");
                } else {
                }
            }
        });

    }

    private void highAction() {

        api.importCase("HighCourt", "delhi", type, number, year).enqueue(new Callback<ImportRes>() {
            @Override
            public void onResponse(Call<ImportRes> call, Response<ImportRes> response) {

                if (response.isSuccessful()) {
                    if (response.body().getMessage() == null) {
                        for (int i = 0; i < response.body().getActions().size(); i++) {
                            if (response.body().getActions().get(i).getAction().contains("Listed")) {
                                importedaction.add(response.body().getActions().get(i).getAction());
                                importedactiondate.add(response.body().getActions().get(i).getDate());
                            }
                        }

                        db.putListString("importedactiondate", importedactiondate);
                        db.putListString("importedaction", importedaction);

                        importedactiondate = db.getListString("importedactiondate");
                        importedaction = db.getListString("importedaction");

                        if (importedactiondate.size() != 0) {
                            nocases.setVisibility(View.GONE);
                            allactions.setVisibility(View.VISIBLE);
                            loadActions();
                        }
                    } else {
                        showAlert("Error contacting server!");
                    }

                } else {
                    showAlert("Fetching failed!");
                }

            }

            @Override
            public void onFailure(Call<ImportRes> call, Throwable t) {
            }
        });

    }

    private void supremeAction() {

        api.importCase("SupremeCourt", "supremecourt", type, number, year).enqueue(new Callback<ImportRes>() {
            @Override
            public void onResponse(Call<ImportRes> call, Response<ImportRes> response) {

                if (response.isSuccessful()) {
                    if (response.body().getMessage() == null) {
                        for (int i = 0; i < response.body().getActions().size(); i++) {
                            if (response.body().getActions().get(i).getAction().contains("Listed")) {
                                importedaction.add(response.body().getActions().get(i).getAction());
                                importedactiondate.add(response.body().getActions().get(i).getDate());
                            }
                        }

                        db.putListString("importedactiondate", importedactiondate);
                        db.putListString("importedaction", importedaction);

                        importedactiondate = db.getListString("importedactiondate");
                        importedaction = db.getListString("importedaction");

                        if (importedactiondate.size() != 0) {
                            nocases.setVisibility(View.GONE);
                            allactions.setVisibility(View.VISIBLE);
                            loadActions();
                        }
                    } else {
                        showAlert("Error contacting server!");
                    }

                } else {
                    showAlert("Fetching failed!");
                }

            }

            @Override
            public void onFailure(Call<ImportRes> call, Throwable t) {
            }
        });

    }

}