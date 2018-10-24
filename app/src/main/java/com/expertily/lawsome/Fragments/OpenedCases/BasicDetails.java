package com.expertily.lawsome.Fragments.OpenedCases;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.expertily.lawsome.API.models.responses.HighCourtRes;
import com.expertily.lawsome.API.models.responses.SupremeCourtRes;
import com.expertily.lawsome.API.services.ApiClient;
import com.expertily.lawsome.API.services.ApiInterface;
import com.expertily.lawsome.R;
import com.expertily.lawsome.Storage.TinyDB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BasicDetails extends Fragment {

    private int pos;
    private View view;
    private EditText casename;
    private EditText description;
    private RadioGroup casetyper;
    private RadioButton lit;
    private RadioButton nonlit;
    private RadioGroup clienttype;
    private Spinner court;
    private Spinner casetype;
    private TextView casenumber;
    private Spinner year;
    private Spinner practicearea;
    private Spinner client;
    private Spinner onrecordcounsel;
    private TextView filingdate;
    private ArrayList<String> c_names = new ArrayList<>();
    private ArrayList<String> c_typer = new ArrayList<>();
    private ArrayList<String> c_court = new ArrayList<>();
    private ArrayList<String> c_type = new ArrayList<>();
    private ArrayList<String> c_year = new ArrayList<>();
    private ArrayList<String> c_orc = new ArrayList<>();
    private ArrayList<String> c_numbers = new ArrayList<>();
    private ArrayList<String> c_filing = new ArrayList<>();
    private ArrayList<String> c_practice = new ArrayList<>();
    private ArrayList<String> c_client = new ArrayList<>();
    private ArrayList<String> cl_type = new ArrayList<>();
    private ArrayList<String> c_description = new ArrayList<>();
    private ArrayAdapter<String> spinnerAdapter;
    private List<String> list;
    private TinyDB db;
    private ApiInterface api;

    public BasicDetails() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.basic_details, container, false);

        String[] case_types = new String[]{
                "Select Case Type"
        };

        db = new TinyDB(getContext());
        api = ApiClient.getClient().create(ApiInterface.class);
        pos = db.getInt("casedetails_position");

        c_names = db.getListString("case_names");
        c_typer = db.getListString("case_typers");
        c_court = db.getListString("court_names");
        c_type = db.getListString("case_types");
        c_year = db.getListString("case_years");
        c_orc = db.getListString("case_orcs");
        c_numbers = db.getListString("case_numbers");
        c_filing = db.getListString("case_dates");
        c_practice = db.getListString("case_areas");
        c_client = db.getListString("client_names");
        cl_type = db.getListString("client_types");
        c_description = db.getListString("case_descriptions");

        casename = view.findViewById(R.id.casename);
        description = view.findViewById(R.id.description);
        casetyper = view.findViewById(R.id.casetyper);
        lit = view.findViewById(R.id.choice1);
        nonlit = view.findViewById(R.id.choice2);
        clienttype = view.findViewById(R.id.clienttype);
        court = view.findViewById(R.id.court);
        casetype = view.findViewById(R.id.casetype);
        year = view.findViewById(R.id.caseyear);
        casenumber = view.findViewById(R.id.casenumber);
        practicearea = view.findViewById(R.id.practicearea);
        client = view.findViewById(R.id.client);
        onrecordcounsel = view.findViewById(R.id.onrecord);
        filingdate = view.findViewById(R.id.filingdate);

        list = new ArrayList<>(Arrays.asList(case_types));
        spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        casetype.setAdapter(spinnerAdapter);
        casename.setEnabled(false);
        casetyper.setEnabled(false);
        court.setEnabled(false);
        casetype.setEnabled(false);
        year.setEnabled(false);
        onrecordcounsel.setEnabled(false);
        casenumber.setText("Case Number: " + c_numbers.get(pos));
        filingdate.setEnabled(false);
        practicearea.setEnabled(false);
        client.setEnabled(false);
        clienttype.setEnabled(false);
        description.setEnabled(false);

        casename.setText(c_names.get(pos));
        if (c_typer.get(pos).equalsIgnoreCase("Litigation")) {
            lit.setSelected(true);
        } else {
            nonlit.setSelected(true);
        }
        String[] c = c_court.get(pos).split("/");
        court.setSelection(getIndex(court, c[0]));
        loadTypes(c_court.get(pos));
        year.setSelection(getIndex(year, c_year.get(pos)));
        onrecordcounsel.setSelection(2);
        filingdate.setText(c_filing.get(pos));
        practicearea.setSelection(2);
        client.setSelection(2);
        //clienttype.setEnabled(false);
        description.setText(c_description.get(pos));

        return view;
    }

    private void showAlert(String message) {
        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private int getIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void loadTypes(String thecourt) {

        if (thecourt.contains("District")) {

            api.getRawDistrictTypes().enqueue(new Callback<List<HighCourtRes>>() {
                @Override
                public void onResponse(Call<List<HighCourtRes>> call, Response<List<HighCourtRes>> response) {

                    if (response.isSuccessful()) {

                        for (int i = 0; i < response.body().size(); i++) {
                            spinnerAdapter.add(response.body().get(i).getName());
                        }
                        spinnerAdapter.notifyDataSetChanged();
                        casetype.setSelection(getIndex(casetype, c_type.get(pos)));

                    } else {
                        showAlert("Case loading failed. Please try again.");
                    }

                }

                @Override
                public void onFailure(Call<List<HighCourtRes>> call, Throwable t) {
                    showAlert("Case loading failed. Please try again.");
                }
            });

        }

        if (thecourt.contains("High")) {

            api.getHighCourt().enqueue(new Callback<List<HighCourtRes>>() {
                @Override
                public void onResponse(Call<List<HighCourtRes>> call, Response<List<HighCourtRes>> response) {

                    if (response.isSuccessful()) {

                        for (int i = 0; i < response.body().size(); i++) {
                            spinnerAdapter.add(response.body().get(i).getName());
                        }
                        spinnerAdapter.notifyDataSetChanged();
                        casetype.setSelection(getIndex(casetype, c_type.get(pos)));

                    } else {
                        showAlert("Case loading failed. Please try again.");
                    }

                }

                @Override
                public void onFailure(Call<List<HighCourtRes>> call, Throwable t) {
                    showAlert("Case loading failed. Please try again.");
                }
            });

        }

        if (thecourt.contains("Supreme")) {

            api.getSupremeCourts().enqueue(new Callback<List<SupremeCourtRes>>() {
                @Override
                public void onResponse(Call<List<SupremeCourtRes>> call, Response<List<SupremeCourtRes>> response) {

                    if (response.isSuccessful()) {

                        for (int i = 0; i < response.body().size(); i++) {
                            spinnerAdapter.add(response.body().get(i).getName());
                        }
                        spinnerAdapter.notifyDataSetChanged();
                        casetype.setSelection(getIndex(casetype, c_type.get(pos)));

                    } else {
                        showAlert("Case loading failed. Please try again.");
                    }

                }

                @Override
                public void onFailure(Call<List<SupremeCourtRes>> call, Throwable t) {
                    showAlert("Case loading failed. Please try again.");
                }
            });

        }

    }

}