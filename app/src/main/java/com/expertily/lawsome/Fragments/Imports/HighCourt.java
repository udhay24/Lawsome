package com.expertily.lawsome.Fragments.Imports;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.expertily.lawsome.API.models.responses.HighCourtRes;
import com.expertily.lawsome.API.models.responses.ImportRes;
import com.expertily.lawsome.API.services.ApiClient;
import com.expertily.lawsome.API.services.ApiInterface;
import com.expertily.lawsome.ImportedCase;
import com.expertily.lawsome.R;
import com.expertily.lawsome.Storage.TinyDB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HighCourt extends Fragment implements View.OnClickListener {

    private View view;
    private ApiInterface api;
    private ProgressDialog loading;
    private Spinner casetypes;
    private Spinner caseyear;
    private EditText casenumber;
    private Button fetch;
    private ArrayList<String> references;
    private ArrayAdapter<String> spinnerAdapter;
    private ArrayList<String> importedaction = new ArrayList<>();
    private ArrayList<String> importedactiondate = new ArrayList<>();
    private List<String> list;
    private TinyDB db;


    public HighCourt() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String[] case_types = new String[]{
                "Select Case Type"
        };

        view = inflater.inflate(R.layout.high_court, container, false);
        db = new TinyDB(view.getContext());
        api = ApiClient.getClient().create(ApiInterface.class);

        casetypes = view.findViewById(R.id.casetypes);
        caseyear = view.findViewById(R.id.caseyear);
        casenumber = view.findViewById(R.id.casenumber);
        fetch = view.findViewById(R.id.fetch);
        references = new ArrayList<>();

        fetch.setOnClickListener(this);
        list = new ArrayList<>(Arrays.asList(case_types));
        spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        casetypes.setAdapter(spinnerAdapter);

        getCaseTypes();

        return view;
    }

    private void showAlert(String message) {
        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void getCaseTypes() {

        api.getHighCourt().enqueue(new Callback<List<HighCourtRes>>() {
            @Override
            public void onResponse(Call<List<HighCourtRes>> call, Response<List<HighCourtRes>> response) {

                if (response.isSuccessful()) {

                    for (int i = 0; i < response.body().size(); i++) {
                        references.add(response.body().get(i).getReference());
                        spinnerAdapter.add(response.body().get(i).getName());
                    }

                    spinnerAdapter.notifyDataSetChanged();

                } else {
                    showAlert("High court fetching failed");
                }

            }

            @Override
            public void onFailure(Call<List<HighCourtRes>> call, Throwable t) {
                showAlert("High court fetching failed");
            }
        });

    }

    private void importCase() {

        loading = new ProgressDialog(getContext(), R.style.MyTheme);
        loading.setCancelable(false);
        loading.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        loading.show();

        api.importCase("HighCourt", "delhi", references.get(casetypes.getSelectedItemPosition()-1), casenumber.getText().toString(), caseyear.getSelectedItem().toString()).enqueue(new Callback<ImportRes>() {
            @Override
            public void onResponse(Call<ImportRes> call, Response<ImportRes> response) {

                if (response.isSuccessful()) {
                    loading.dismiss();
                    if (response.body().getMessage() == null) {
                        for (int i=0; i<response.body().getActions().size(); i++) {
                            if (response.body().getActions().get(i).getAction().contains("Listed")) {
                                importedaction.add(response.body().getActions().get(i).getAction());
                                importedactiondate.add(response.body().getActions().get(i).getDate());
                            }
                        }

                        Intent intent = new Intent(getContext(), ImportedCase.class);
                        intent.putExtra("casenumber", response.body().getCaseNumber());
                        db.putString("importedcasenumber", response.body().getCaseNumber());
                        db.putString("importedcourtname", "High Court of Delhi/delhi");
                        db.putString("importedcasetype", casetypes.getSelectedItem().toString());
                        db.putString("importedparty", response.body().getParty());
                        db.putString("importedstatus", response.body().getStatus());
                        db.putString("importedpetitioner", response.body().getpAdv());
                        db.putString("importedrespondant", response.body().getrAdv());
                        db.putString("importedyear", caseyear.getSelectedItem().toString());
                        db.putString("importednum", casenumber.getText().toString());
                        db.putListString("importedactiondate", importedactiondate);
                        db.putListString("importedaction", importedaction);
                        startActivity(intent);
                    } else {
                        showAlert("Error contacting server!");
                    }

                } else {
                    loading.dismiss();
                    showAlert("Fetching failed!");
                }

            }

            @Override
            public void onFailure(Call<ImportRes> call, Throwable t) {
                loading.dismiss();
                showAlert("No case found!");
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view == fetch) {
            if (casetypes.getSelectedItem().toString().equalsIgnoreCase("Select Case Type")) {
                showAlert("Please select case type");
                return;
            } else if (caseyear.getSelectedItem().toString().equalsIgnoreCase("Case Year")) {
                showAlert("Please select case year");
                return;
            } else if (casenumber.getText().toString().isEmpty()) {
                showAlert("Please enter case number");
            } else {
                importCase();
            }
        }
    }

}
