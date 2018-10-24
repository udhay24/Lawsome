package com.expertily.lawsome.Fragments.Imports;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.expertily.lawsome.API.models.responses.DistrictCourtsRes;
import com.expertily.lawsome.API.models.responses.DistrictsRes;
import com.expertily.lawsome.API.models.responses.ImportDistrictRes;
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

public class DistrictCourt extends Fragment implements View.OnClickListener {

    private View view;
    private ApiInterface api;
    private ProgressDialog loading;
    private Spinner select;
    private Spinner court;
    private Spinner casetypes;
    private Spinner caseyear;
    private EditText casenumber;
    private Button fetch;
    private ArrayAdapter<String> districtAdapter;
    private ArrayAdapter<String> courtsAdapter;
    private ArrayAdapter<String> casesAdapter;
    private List<String> all_districts;
    private List<String> all_courts;
    private List<String> all_cases;
    private ArrayList<String> courtreferences = new ArrayList<>();
    private ArrayList<String> casereferences = new ArrayList<>();
    private ArrayList<String> importedaction = new ArrayList<>();
    private ArrayList<String> importedactiondate = new ArrayList<>();
    private Response<List<DistrictsRes>> json_districts;
    private Response<DistrictCourtsRes> json_all;
    private TinyDB db;

    public DistrictCourt() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.district_court, container, false);
        db = new TinyDB(view.getContext());

        String[] districts = new String[]{
                "Select District"
        };

        String[] courts = new String[]{
                "Select Court"
        };

        String[] cases = new String[]{
                "Select Case Type"
        };

        view = inflater.inflate(R.layout.district_court, container, false);
        api = ApiClient.getClient().create(ApiInterface.class);
        select = view.findViewById(R.id.select);
        court = view.findViewById(R.id.court);
        casetypes = view.findViewById(R.id.casetypes);
        caseyear = view.findViewById(R.id.caseyear);
        casenumber = view.findViewById(R.id.casenumber);
        fetch = view.findViewById(R.id.fetch);

        select.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                courtsAdapter.clear();
                casesAdapter.clear();
                courtsAdapter.add("Select Court");
                casesAdapter.add("Select Case Type");
                casetypes.setSelection(0);
                court.setSelection(0);
                return false;
            }
        });

        court.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                casesAdapter.clear();
                casesAdapter.add("Select Case Type");
                casetypes.setSelection(0);
                return false;
            }
        });

        fetch.setOnClickListener(this);
        all_districts = new ArrayList<>(Arrays.asList(districts));
        districtAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, all_districts);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        select.setAdapter(districtAdapter);

        all_courts = new ArrayList<>(Arrays.asList(courts));
        courtsAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, all_courts);
        courtsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        court.setAdapter(courtsAdapter);

        all_cases = new ArrayList<>(Arrays.asList(cases));
        casesAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, all_cases);
        casesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        casetypes.setAdapter(casesAdapter);

        getDistricts();
        return view;
    }

    private void showAlert(String message) {
        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void getDistricts() {

        api.getDistrictCourt().enqueue(new Callback<List<DistrictsRes>>() {
            @Override
            public void onResponse(Call<List<DistrictsRes>> call, Response<List<DistrictsRes>> response) {

                if (response.isSuccessful()) {
                    json_districts = response;
                    for (int i = 0; i < response.body().size(); i++) {
                        districtAdapter.add(response.body().get(i).getName());
                    }
                    getDistrictCourts();
                    districtAdapter.notifyDataSetChanged();

                } else {
                    showAlert("District court fetching failed");
                }

            }

            @Override
            public void onFailure(Call<List<DistrictsRes>> call, Throwable t) {
                showAlert("District court fetching failed");
            }
        });

    }

    private void getDistrictCourts() {

        select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String selected = select.getSelectedItem().toString();

                for (int i = 0; i < json_districts.body().size(); i++) {

                    if (json_districts.body().get(i).getName().equalsIgnoreCase(selected)) {

                        loading = new ProgressDialog(view.getContext(), R.style.MyTheme);
                        loading.setCancelable(false);
                        loading.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                        loading.show();

                        courtsAdapter.clear();
                        courtsAdapter.add("Select Court");
                        courtsAdapter.notifyDataSetChanged();
                        court.setSelection(0);

                        api.getDistrictCourts(json_districts.body().get(i).getId()).enqueue(new Callback<DistrictCourtsRes>() {
                            @Override
                            public void onResponse(Call<DistrictCourtsRes> call, Response<DistrictCourtsRes> response) {
                                if (response.isSuccessful()) {
                                    loading.dismiss();
                                    json_all = response;

                                    for (int i = 0; i < response.body().getDistrictCourts().size(); i++) {
                                        courtreferences.add(response.body().getDistrictCourts().get(i).getReferenceName());
                                        courtsAdapter.add(response.body().getDistrictCourts().get(i).getName());
                                    }

                                    courtsAdapter.notifyDataSetChanged();
                                    getCaseTypes();

                                } else {
                                    loading.dismiss();
                                    court.setSelection(0);
                                    showAlert("Error retrieving court type");
                                }

                            }

                            @Override
                            public void onFailure(Call<DistrictCourtsRes> call, Throwable t) {
                                loading.dismiss();
                                court.setSelection(0);
                                showAlert("Error retrieving court type");
                            }
                        });

                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

    }

    private void getCaseTypes() {

        court.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (!court.getSelectedItem().toString().equalsIgnoreCase("Select Court")) {

                    for (int i = 0; i < json_all.body().getDistrictCourts().size(); i++) {

                        casesAdapter.clear();
                        casereferences.clear();
                        casesAdapter.add("Select Case Type");

                        for (int j = 0; j < json_all.body().getDistrictCourts().get(court.getSelectedItemPosition() - 1).getCaseTypes().size(); j++) {
                            casereferences.add(json_all.body().getDistrictCourts().get(court.getSelectedItemPosition() - 1).getCaseTypes().get(j).getReferenceName());
                            casesAdapter.add(json_all.body().getDistrictCourts().get(court.getSelectedItemPosition() - 1).getCaseTypes().get(j).getName());
                        }
                        casesAdapter.notifyDataSetChanged();

                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

    }

    private void importCase() {

        loading = new ProgressDialog(getContext(), R.style.MyTheme);
        loading.setCancelable(false);
        loading.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        loading.show();

        api.importDistrictCase("DistrictCourt", courtreferences.get(court.getSelectedItemPosition() - 1), casereferences.get(casetypes.getSelectedItemPosition() - 1), casenumber.getText().toString(), caseyear.getSelectedItem().toString()).enqueue(new Callback<ImportDistrictRes>() {
            @Override
            public void onResponse(Call<ImportDistrictRes> call, Response<ImportDistrictRes> response) {

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
                        db.putString("importedcourtname", "District Court of Delhi/"+courtreferences.get(court.getSelectedItemPosition() - 1));
                        db.putString("importedcasetype", casetypes.getSelectedItem().toString());
                        db.putString("importedparty", response.body().getParty());
                        db.putString("importedstatus", response.body().getStatus());
                        db.putString("importedpetitioner", response.body().getpAdv().get(0).getName());
                        db.putString("importedrespondant", response.body().getrAdv().get(0).getName());
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
            public void onFailure(Call<ImportDistrictRes> call, Throwable t) {
                loading.dismiss();

                if (t.toString().contains("Socket")) {
                    showAlert("Failed to contact. Please try again later.");
                } else {
                    showAlert("No case found!");
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view == fetch) {
            if (select.getSelectedItem().toString().equalsIgnoreCase("Select District")) {
                showAlert("Please select a district");
                return;
            } else if (court.getSelectedItem().toString().equalsIgnoreCase("Select Court")) {
                showAlert("Please select a court");
                return;
            } else if (casetypes.getSelectedItem().toString().equalsIgnoreCase("Select Case Type")) {
                showAlert("Please select a case type");
                return;
            } else if (caseyear.getSelectedItem().toString().equalsIgnoreCase("Case Year")) {
                showAlert("Please select case year");
                return;
            } else if (casenumber.getText().toString().isEmpty()) {
                showAlert("Please enter case number");
            } else {
                importCase();
            }
        } else if (view == court) {
            casetypes.setSelection(0);
        }
    }

}
