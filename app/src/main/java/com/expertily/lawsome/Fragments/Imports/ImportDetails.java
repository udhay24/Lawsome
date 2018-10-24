package com.expertily.lawsome.Fragments.Imports;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.expertily.lawsome.API.models.responses.AuthRes;
import com.expertily.lawsome.API.services.ApiClient;
import com.expertily.lawsome.API.services.ApiInterface;
import com.expertily.lawsome.Dashboard;
import com.expertily.lawsome.ImportCase;
import com.expertily.lawsome.R;
import com.expertily.lawsome.Storage.TinyDB;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImportDetails extends Fragment implements View.OnClickListener {

    private View view;
    private String[] parties;
    private String case_name;
    private String case_typer;
    private String court_name;
    private String case_type;
    private String case_year;
    private String onrecord_counsel;
    private String number;
    private String case_number;
    private String filing_date;
    private String practice_area;
    private String client_name;
    private String case_description;
    private String c_num;
    private String c_party;
    private String c_status;
    private String pAdv;
    private String rAdv;
    private String year;
    private String client_type;
    private TextView casenumber;
    private TextView party;
    private TextView status;
    private TextView petitioner;
    private TextView respondant;
    private Typeface regular;
    private Typeface bold;
    private Button importcase;
    private RadioGroup clienttype;
    private ProgressDialog loading;
    private ApiInterface api;
    private TinyDB tinydb;
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

    public ImportDetails() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_details, container, false);
        tinydb = new TinyDB(view.getContext());
        api = ApiClient.getClient().create(ApiInterface.class);
        bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/semibold.ttf");
        casenumber = view.findViewById(R.id.casenumber);
        party = view.findViewById(R.id.party);
        status = view.findViewById(R.id.status);
        petitioner = view.findViewById(R.id.petetioner);
        respondant = view.findViewById(R.id.respondant);
        importcase = view.findViewById(R.id.importcase);
        importcase.setOnClickListener(this);
        clienttype = view.findViewById(R.id.clienttype);

        c_names = tinydb.getListString("case_names");
        c_typer = tinydb.getListString("case_typers");
        c_court = tinydb.getListString("court_names");
        c_type = tinydb.getListString("case_types");
        c_year = tinydb.getListString("case_years");
        c_orc = tinydb.getListString("case_orcs");
        c_orc = tinydb.getListString("case_numbers");
        c_filing = tinydb.getListString("case_dates");
        c_practice = tinydb.getListString("case_areas");
        c_client = tinydb.getListString("client_names");
        cl_type = tinydb.getListString("client_types");
        c_description = tinydb.getListString("case_descriptions");

        casenumber.setTypeface(bold);
        party.setTypeface(bold);
        status.setTypeface(bold);
        petitioner.setTypeface(bold);
        respondant.setTypeface(bold);

        c_num = tinydb.getString("importedcasenumber");
        court_name = tinydb.getString("importedcourtname");
        case_type = tinydb.getString("importedcasetype");
        c_party = tinydb.getString("importedparty");
        c_status = tinydb.getString("importedstatus");
        pAdv = tinydb.getString("importedpetitioner");
        rAdv = tinydb.getString("importedrespondant");
        year = tinydb.getString("importedyear");
        number = tinydb.getString("importednum");

        if (c_num == null) {
            c_num = "N/A";
        }

        if (c_party == null) {
            c_party = "N/A";
            client_name = "N/A";
        }

        if (c_status == null) {
            c_status = "N/A";
        }

        if (pAdv == null) {
            pAdv = "N/A";
        }

        if (rAdv == null) {
            rAdv = "N/A";
        }

        casenumber.setText(c_num);
        party.setText(c_party);
        status.setText(c_status);
        petitioner.setText(pAdv);
        respondant.setText(rAdv);
        String[] splitter = c_num.split("/");
        if (c_party.contains("Vs.")) {
            parties = c_party.split("Vs.");
        } else {
            parties = c_party.split("Vs");
        }
        case_name = c_party;
        case_typer = "Litigation";
        case_year = year;
        onrecord_counsel = tinydb.getString("name");
        case_number = splitter[0] + "/" + number + "/" + year;
        filing_date = "";
        practice_area = "";
        case_description = "";
        return view;
    }

    private void showAlert(String message) {
        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
    }


    private void importCase() {

        loading = new ProgressDialog(view.getContext(), R.style.MyTheme);
        loading.setCancelable(false);
        loading.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        loading.show();

        api.addCase(tinydb.getString("phonenumber").substring(2), c_party, case_typer, court_name, case_type, case_year, onrecord_counsel, case_number, filing_date, practice_area, client_name, client_type, case_description, "", "").enqueue(new Callback<AuthRes>() {
            @Override
            public void onResponse(Call<AuthRes> call, Response<AuthRes> response) {

                if (response.isSuccessful()) {

                    if (response.body().getStatus().equalsIgnoreCase("success")) {

                        c_names.add(case_name);
                        c_typer.add(case_typer);
                        c_court.add(court_name);
                        c_type.add(case_type);
                        c_year.add(case_year);
                        c_orc.add(onrecord_counsel);
                        c_number.add(case_number);
                        c_filing.add(filing_date);
                        c_practice.add(practice_area);
                        c_client.add(client_name);
                        cl_type.add(client_type);
                        c_description.add(case_description);

                        tinydb.putListString("case_names", c_names);
                        tinydb.putListString("case_typers", c_typer);
                        tinydb.putListString("court_names", c_court);
                        tinydb.putListString("case_types", c_type);
                        tinydb.putListString("case_years", c_year);
                        tinydb.putListString("case_orcs", c_orc);
                        tinydb.putListString("case_numbers", c_number);
                        tinydb.putListString("case_dates", c_filing);
                        tinydb.putListString("case_areas", c_practice);
                        tinydb.putListString("client_names", c_client);
                        tinydb.putListString("client_types", cl_type);
                        tinydb.putListString("case_descriptions", c_description);
                        tinydb.putBoolean("hasCases", true);
                        tinydb.putBoolean("refresh", true);
                        loading.dismiss();
                        showAlert("Case Imported");

                        Intent go = new Intent(view.getContext(), Dashboard.class);
                        go.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(go);

                    } else {
                        loading.dismiss();
                        showAlert("Error adding case");
                    }

                } else {
                    loading.dismiss();
                    showAlert("Error adding case");
                }
            }

            @Override
            public void onFailure(Call<AuthRes> call, Throwable t) {
                loading.dismiss();
                showAlert("Error adding case");
            }
        });


    }

    @Override
    public void onClick(View v) {
        if (v == importcase) {
            if (clienttype.getCheckedRadioButtonId() == -1) {
                Toast.makeText(view.getContext(), "Please Select Client Type", Toast.LENGTH_LONG).show();
                return;
            } else {
                int selectedId = clienttype.getCheckedRadioButtonId();
                RadioButton selected = view.findViewById(selectedId);
                client_type = selected.getText().toString();
                if (client_type.equalsIgnoreCase("Petitioner")) {
                    if (c_party.equalsIgnoreCase("N/A")) {
                        client_name = "N/A";
                    } else {
                        client_name = parties[0];
                    }
                } else if (client_type.equalsIgnoreCase("Respondant")) {
                    if (c_party.equalsIgnoreCase("N/A")) {
                        client_name = "N/A";
                    } else {
                        client_name = parties[1];
                    }
                }
                importCase();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent go = new Intent(view.getContext(), ImportCase.class);
                startActivity(go);
                getActivity().finish();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
