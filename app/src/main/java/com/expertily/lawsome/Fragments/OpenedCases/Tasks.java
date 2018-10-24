package com.expertily.lawsome.Fragments.OpenedCases;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.expertily.lawsome.API.models.responses.ImportDistrictRes;
import com.expertily.lawsome.API.models.responses.ImportRes;
import com.expertily.lawsome.API.services.ApiClient;
import com.expertily.lawsome.API.services.ApiInterface;
import com.expertily.lawsome.Adapters.ActionsAdapter;
import com.expertily.lawsome.Authentication.Login;
import com.expertily.lawsome.R;
import com.expertily.lawsome.Storage.TinyDB;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Tasks extends Fragment implements View.OnClickListener {

    private int pos;
    private Date parseddate;
    private String[] parts1;
    private String[] parts2;
    private String thecourt;
    private String courttype;
    private String type;
    private String number;
    private String year;
    private View view;
    private Button todo;
    private Button appointment;
    private Button hearing;
    private LinearLayout todos;
    private LinearLayout appointments;
    private LinearLayout hearings;
    private LinearLayout nohearings;
    private LinearLayout allhearings;
    private RecyclerView actions_recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private ArrayList<String> c_court = new ArrayList<>();
    private ArrayList<String> c_numbers = new ArrayList<>();
    private ArrayList<String> importedaction = new ArrayList<>();
    private ArrayList<String> importedactiondate = new ArrayList<>();
    private TinyDB db;
    private ApiInterface api;

    public Tasks() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tasks, container, false);
        api = ApiClient.getClient().create(ApiInterface.class);
        db = new TinyDB(getContext());
        todo = view.findViewById(R.id.todo);
        appointment = view.findViewById(R.id.appointment);
        hearing = view.findViewById(R.id.hearing);
        todos = view.findViewById(R.id.todos);
        appointments = view.findViewById(R.id.appointments);
        hearings = view.findViewById(R.id.hearings);
        nohearings = view.findViewById(R.id.nohearings);
        allhearings = view.findViewById(R.id.allhearings);
        actions_recycler = view.findViewById(R.id.orders);
        pos = db.getInt("casedetails_position");
        c_court = db.getListString("court_names");
        c_numbers = db.getListString("case_numbers");

        todo.setOnClickListener(this);
        appointment.setOnClickListener(this);
        hearing.setOnClickListener(this);

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

    private void stateShowTasks() {
        todos.setVisibility(View.VISIBLE);
        appointments.setVisibility(View.GONE);
        hearings.setVisibility(View.GONE);
    }

    private void stateShowAppointments() {
        todos.setVisibility(View.GONE);
        appointments.setVisibility(View.VISIBLE);
        hearings.setVisibility(View.GONE);
    }

    private void stateShowHearings() {
        todos.setVisibility(View.GONE);
        appointments.setVisibility(View.GONE);
        hearings.setVisibility(View.VISIBLE);
    }

    private void loadActions() {
        manager = new LinearLayoutManager(getContext());
        actions_recycler.setLayoutManager(manager);
        adapter = new ActionsAdapter(getActivity(), importedactiondate, importedaction, c_numbers.get(pos), parts1[0]);
        actions_recycler.setAdapter(adapter);
    }

    private String parseDate(String d) {
        Date date;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.'SSS'Z'", Locale.getDefault());

        try {
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
            date = simpleDateFormat.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
            date = new Date(0);
        }

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return String.valueOf(df.format(date));
    }

    private void disctrictActions() {

        api.importDistrictCase(thecourt, courttype, type, number, year).enqueue(new Callback<ImportDistrictRes>() {
            @Override
            public void onResponse(Call<ImportDistrictRes> call, Response<ImportDistrictRes> response) {

                if (response.isSuccessful()) {
                    if (response.body().getMessage() == null) {

                        for (int i = 0; i < response.body().getActions().size(); i++) {
                            if (response.body().getActions().get(i).getAction().contains("Listed")) {
                                if (response.body().getActions().get(i).getAction().contains("Listed")) {
                                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                    Date date = new Date();
                                    try {
                                        parseddate = dateFormat.parse(parseDate(response.body().getActions().get(i).getDate()));
                                    } catch (ParseException e) {
                                        showAlert(e.toString());
                                    }

                                    if (date.compareTo(parseddate) <= 0) {
                                        importedaction.add(response.body().getActions().get(i).getAction());
                                        importedactiondate.add(response.body().getActions().get(i).getDate());
                                    }
                                }
                            }
                        }

                        db.putListString("importedactiondate", importedactiondate);
                        db.putListString("importedaction", importedaction);

                        importedactiondate = db.getListString("importedactiondate");
                        importedaction = db.getListString("importedaction");

                        if (importedactiondate.size() != 0) {
                            nohearings.setVisibility(View.GONE);
                            allhearings.setVisibility(View.VISIBLE);
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
                                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                Date date = new Date();
                                try {
                                    parseddate = dateFormat.parse(parseDate(response.body().getActions().get(i).getDate()));
                                } catch (ParseException e) {
                                    showAlert(e.toString());
                                }

                                if (date.compareTo(parseddate) <= 0) {
                                    importedaction.add(response.body().getActions().get(i).getAction());
                                    importedactiondate.add(response.body().getActions().get(i).getDate());
                                }
                            }
                        }

                        db.putListString("importedactiondate", importedactiondate);
                        db.putListString("importedaction", importedaction);

                        importedactiondate = db.getListString("importedactiondate");
                        importedaction = db.getListString("importedaction");

                        if (importedactiondate.size() != 0) {
                            nohearings.setVisibility(View.GONE);
                            allhearings.setVisibility(View.VISIBLE);
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
                                if (response.body().getActions().get(i).getAction().contains("Listed")) {
                                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                    Date date = new Date();
                                    try {
                                        parseddate = dateFormat.parse(parseDate(response.body().getActions().get(i).getDate()));
                                    } catch (ParseException e) {
                                        showAlert(e.toString());
                                    }

                                    if (date.compareTo(parseddate) <= 0) {
                                        importedaction.add(response.body().getActions().get(i).getAction());
                                        importedactiondate.add(response.body().getActions().get(i).getDate());
                                    }
                                }
                            }
                        }

                        db.putListString("importedactiondate", importedactiondate);
                        db.putListString("importedaction", importedaction);

                        importedactiondate = db.getListString("importedactiondate");
                        importedaction = db.getListString("importedaction");

                        if (importedactiondate.size() != 0) {
                            nohearings.setVisibility(View.GONE);
                            allhearings.setVisibility(View.VISIBLE);
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

    @Override
    public void onClick(View v) {
        if (v == todo) {
            showAlert("Coming Soon");
        } else if (v == appointment) {
            showAlert("Coming Soon");
        } else if (v == hearing) {

        }
    }

}
