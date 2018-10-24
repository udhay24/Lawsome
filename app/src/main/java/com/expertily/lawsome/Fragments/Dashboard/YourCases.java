package com.expertily.lawsome.Fragments.Dashboard;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.expertily.lawsome.API.models.responses.AuthRes;
import com.expertily.lawsome.API.models.responses.CasesRes;
import com.expertily.lawsome.API.models.responses.HighCourtRes;
import com.expertily.lawsome.API.services.ApiClient;
import com.expertily.lawsome.API.services.ApiInterface;
import com.expertily.lawsome.Adapters.CasesAdapter;
import com.expertily.lawsome.CaseDetails;
import com.expertily.lawsome.Dashboard;
import com.expertily.lawsome.Extras.Splash;
import com.expertily.lawsome.R;
import com.expertily.lawsome.Storage.LocalStorage;
import com.expertily.lawsome.Storage.TinyDB;
import com.fourmob.datetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YourCases extends Fragment implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    private Boolean enterdate = false;
    private View view;
    private Typeface regular;
    private Typeface bold;
    private LocalStorage storage;
    private LinearLayout nocases;
    private LinearLayout allcases;
    private TextView nocasestext;
    private TinyDB tinydb;
    private Button addcase;
    private Dialog dialog;
    private String case_name;
    private String case_typer;
    private String court_name;
    private String case_type;
    private String case_year;
    private String onrecord_counsel;
    private String case_number;
    private String filing_date;
    private String practice_area;
    private String client_name;
    private String client_type;
    private String case_description;
    private String client_email = "";
    private String client_mobile = "";
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
    private ArrayList<String> cl_email = new ArrayList<>();
    private ArrayList<String> cl_mobile = new ArrayList<>();
    private ArrayAdapter<String> spinnerAdapter;
    private List<String> list;
    private RecyclerView cases_recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private ApiInterface api;
    private ProgressDialog loading;

    public YourCases() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.your_cases, container, false);

        regular = Typeface.createFromAsset(getContext().getAssets(), "fonts/regular.ttf");
        bold = Typeface.createFromAsset(getContext().getAssets(), "fonts/semibold.ttf");

        String[] case_types = new String[]{
                "Select Case Type"
        };

        api = ApiClient.getClient().create(ApiInterface.class);
        tinydb = new TinyDB(getContext());
        storage = new LocalStorage(getContext());
        nocases = (LinearLayout) view.findViewById(R.id.nocases);
        allcases = (LinearLayout) view.findViewById(R.id.allcases);
        nocasestext = (TextView) view.findViewById(R.id.nocasestext);
        addcase = (Button) view.findViewById(R.id.addcase);
        addcase.setOnClickListener(this);
        nocasestext.setTypeface(bold);
        cases_recycler = (RecyclerView) view.findViewById(R.id.cases);
        list = new ArrayList<>(Arrays.asList(case_types));
        spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        c_names = tinydb.getListString("case_names");
        c_typer = tinydb.getListString("case_typers");
        c_court = tinydb.getListString("court_names");
        c_type = tinydb.getListString("case_types");
        c_year = tinydb.getListString("case_years");
        c_orc = tinydb.getListString("case_orcs");
        c_numbers = tinydb.getListString("case_numbers");
        c_filing = tinydb.getListString("case_dates");
        c_practice = tinydb.getListString("case_areas");
        c_client = tinydb.getListString("client_names");
        cl_type = tinydb.getListString("client_types");
        c_description = tinydb.getListString("case_descriptions");
        cl_email = tinydb.getListString("client_emails");
        cl_mobile = tinydb.getListString("client_mobiles");

        if (tinydb.getBoolean("refresh")) {
            refreshCases();
        } else {
            caseCheck();
        }

        return view;
    }

    private void showAlert(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void caseCheck() {
        if (tinydb.getBoolean("hasCases")) {
            nocases.setVisibility(View.GONE);
            allcases.setVisibility(View.VISIBLE);

            if (adapter != null) {
                adapter.notifyDataSetChanged();
            } else {

                manager = new LinearLayoutManager(getContext());
                cases_recycler.setLayoutManager(manager);
                adapter = new CasesAdapter(getActivity(), c_names, c_court, c_orc, c_client);
                cases_recycler.setAdapter(adapter);

                cases_recycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                    GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            return true;
                        }
                    });

                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                        View child = rv.findChildViewUnder(e.getX(), e.getY());
                        if (child != null && gestureDetector.onTouchEvent(e)) {
                            int position = rv.getChildAdapterPosition(child);

                            Intent go = new Intent(getContext(), CaseDetails.class);
                            go.putExtra("title", c_numbers.get(position));
                            tinydb.putString("currentcasenumber", c_numbers.get(position));
                            tinydb.putString("currentclientemail", c_numbers.get(position));
                            tinydb.putString("currentclientmobile", c_numbers.get(position));
                            tinydb.putInt("casedetails_position", position);
                            startActivity(go);
                        }
                        return false;
                    }

                    @Override
                    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                    }

                    @Override
                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                    }
                });

            }

        }
    }

    private void refreshCases() {

        api.getCases(tinydb.getString("phonenumber").substring(2)).enqueue(new Callback<List<CasesRes>>() {
            @Override
            public void onResponse(Call<List<CasesRes>> call, Response<List<CasesRes>> response) {

                if (response.isSuccessful()) {

                    int count = response.body().size();

                    if (count > 0) {
                        tinydb.putBoolean("hasCases", true);
                        tinydb.putBoolean("refresh", false);
                        c_names.clear();
                        c_typer.clear();
                        c_court.clear();
                        c_type.clear();
                        c_year.clear();
                        c_orc.clear();
                        c_numbers.clear();
                        c_filing.clear();
                        c_practice.clear();
                        c_client.clear();
                        cl_type.clear();
                        c_description.clear();
                        cl_email.clear();
                        cl_mobile.clear();

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
                            c_numbers.add(g);
                            c_filing.add(h);
                            c_practice.add(j);
                            c_client.add(k);
                            cl_type.add(l);
                            c_description.add(m);
                            cl_email.add(n);
                            cl_mobile.add(o);

                            tinydb.putListString("case_names", c_names);
                            tinydb.putListString("case_typers", c_typer);
                            tinydb.putListString("court_names", c_court);
                            tinydb.putListString("case_types", c_type);
                            tinydb.putListString("case_years", c_year);
                            tinydb.putListString("case_orcs", c_orc);
                            tinydb.putListString("case_numbers", c_numbers);
                            tinydb.putListString("case_dates", c_filing);
                            tinydb.putListString("case_areas", c_practice);
                            tinydb.putListString("client_names", c_client);
                            tinydb.putListString("client_types", cl_type);
                            tinydb.putListString("case_descriptions", c_description);
                            tinydb.putListString("client_emails", cl_email);
                            tinydb.putListString("client_mobiles", cl_mobile);

                            caseCheck();
                        }
                    } else {
                        tinydb.putBoolean("hasCases", false);
                    }

                } else {
                    Toast.makeText(getContext(), "Unable to fetch cases", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<List<CasesRes>> call, Throwable t) {
                Toast.makeText(getContext(), "Unable to fetch cases", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getCaseTypes() {

        api.getHighCourt().enqueue(new Callback<List<HighCourtRes>>() {
            @Override
            public void onResponse(Call<List<HighCourtRes>> call, Response<List<HighCourtRes>> response) {

                if (response.isSuccessful()) {

                    for (int i = 0; i < response.body().size(); i++) {
                        spinnerAdapter.add(response.body().get(i).getReference());
                    }

                    spinnerAdapter.notifyDataSetChanged();

                } else {
                    showAlert("Fetching failed");
                }

            }

            @Override
            public void onFailure(Call<List<HighCourtRes>> call, Throwable t) {
                showAlert("Fetching failed");
            }
        });

    }

    public void showDialog() {

        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog = new Dialog(getContext());
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.addcase);
        dialog.show();
        final LinearLayout datepicker = dialog.findViewById(R.id.datepicker);
        Button addcase = dialog.findViewById(R.id.addcase);
        final EditText casename = dialog.findViewById(R.id.casename);
        final EditText casenumber = dialog.findViewById(R.id.casenumber);
        final EditText description = dialog.findViewById(R.id.description);
        final RadioGroup casetyper = dialog.findViewById(R.id.casetyper);
        final RadioGroup clienttype = dialog.findViewById(R.id.clienttype);
        final Spinner court = dialog.findViewById(R.id.court);
        final Spinner casetype = dialog.findViewById(R.id.casetype);
        final Spinner year = dialog.findViewById(R.id.caseyear);
        final Spinner practicearea = dialog.findViewById(R.id.practicearea);
        final Spinner client = dialog.findViewById(R.id.client);
        final Spinner onrecordcounsel = dialog.findViewById(R.id.onrecord);
        final TextView filingdate = (TextView) dialog.findViewById(R.id.filingdate);

        casetype.setAdapter(spinnerAdapter);
        getCaseTypes();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(final DialogInterface arg0) {
                caseCheck();
            }
        });

        datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.setVibrate(false);
                datePickerDialog.setYearRange(1985, 2018);
                datePickerDialog.setCloseOnSingleTapDay(false);
                datePickerDialog.show(getActivity().getSupportFragmentManager(), "datepicker");
            }
        });

        addcase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //CASENAME

                if (casename.getText().toString().equalsIgnoreCase("")) {
                    showAlert("Please Enter The Case Name");
                    return;
                } else {
                    case_name = casename.getText().toString().trim();
                }


                //CASE TYPER

                if (casetyper.getCheckedRadioButtonId() == -1) {
                    showAlert("Please Select Litigation/Non-Litigation");
                    return;
                } else {
                    int selectedId = casetyper.getCheckedRadioButtonId();
                    RadioButton selected = (RadioButton) dialog.findViewById(selectedId);
                    case_typer = selected.getText().toString();
                }


                //COURT NAME

                if (court.getSelectedItem().toString().equalsIgnoreCase("Select Court")) {
                    showAlert("Please Select Court");
                    return;
                } else {
                    if (court.getSelectedItem().toString().contains("District")) {
                        court_name = court.getSelectedItem().toString() + "/" + "southwest-1";
                    } else if (court.getSelectedItem().toString().contains("High")) {
                        court_name = court.getSelectedItem().toString() + "/" + "delhi";
                    } else if (court.getSelectedItem().toString().contains("Supreme")) {
                        court_name = court.getSelectedItem().toString() + "/" + "supreme";
                    }
                }


                //CASE TYPE

                if (casetype.getSelectedItem().toString().equalsIgnoreCase("Select Case Type")) {
                    showAlert("Please Select Case Type");
                    return;
                } else {
                    case_type = casetype.getSelectedItem().toString();
                }


                //CASE YEAR

                if (year.getSelectedItem().toString().equalsIgnoreCase("Case Year")) {
                    showAlert("Please Select Case Year");
                    return;
                } else {
                    case_year = year.getSelectedItem().toString();
                }


                //ONRECORD COUNSEL

                if (onrecordcounsel.getSelectedItem().toString().equalsIgnoreCase("On Record Counsel")) {
                    showAlert("Please Select On Record Counsel");
                    return;
                } else {
                    onrecord_counsel = onrecordcounsel.getSelectedItem().toString();
                }


                //CASE NUMBER

                if (casenumber.getText().toString().equalsIgnoreCase("")) {
                    showAlert("Please Enter Case Number");
                    return;
                } else {
                    case_number = casenumber.getText().toString();
                }


                //FILING DATE

                if (!enterdate) {
                    showAlert("Please Select Case Filing Date");
                    return;
                } else {
                    filing_date = filingdate.getText().toString().trim();
                }


                //PRACTICE AREA

                if (practicearea.getSelectedItem().toString().equalsIgnoreCase("Practice Area")) {
                    showAlert("Please Select Practice Area");
                    return;
                } else {
                    practice_area = practicearea.getSelectedItem().toString();
                }


                //CLIENT

                if (client.getSelectedItem().toString().equalsIgnoreCase("Client(s)")) {
                    showAlert("Please Select Client");
                    return;
                } else {
                    client_name = client.getSelectedItem().toString();
                }


                //CLIENT TYPE

                if (clienttype.getCheckedRadioButtonId() == -1) {
                    showAlert("Please Select Client Type");
                    return;
                } else {
                    int selectedId = clienttype.getCheckedRadioButtonId();
                    RadioButton selected = (RadioButton) dialog.findViewById(selectedId);
                    client_type = selected.getText().toString();
                }


                //CASE DESCRIPTION

                if (description.getText().toString().equalsIgnoreCase("")) {
                    showAlert("Please Enter The Case Description");
                    return;
                } else {
                    case_description = description.getText().toString().trim();
                }


                loading = new ProgressDialog(getContext(), R.style.MyTheme);
                loading.setCancelable(false);
                loading.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                loading.show();

                api.addCase(tinydb.getString("phonenumber").substring(2), case_name, case_typer, court_name, case_type, case_year, onrecord_counsel, case_type + "/" + case_number + "/" + case_year, filing_date, practice_area, client_name, client_type, case_description, client_email, client_mobile).enqueue(new Callback<AuthRes>() {
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
                                c_numbers.add(case_type + "/" + case_number + "/" + case_year);
                                c_filing.add(filing_date);
                                c_practice.add(practice_area);
                                c_client.add(client_name);
                                cl_type.add(client_type);
                                c_description.add(case_description);
                                cl_email.add(client_email);
                                cl_mobile.add(client_mobile);

                                tinydb.putListString("case_names", c_names);
                                tinydb.putListString("case_typers", c_typer);
                                tinydb.putListString("court_names", c_court);
                                tinydb.putListString("case_types", c_type);
                                tinydb.putListString("case_years", c_year);
                                tinydb.putListString("case_orcs", c_orc);
                                tinydb.putListString("case_numbers", c_numbers);
                                tinydb.putListString("case_dates", c_filing);
                                tinydb.putListString("case_areas", c_practice);
                                tinydb.putListString("client_names", c_client);
                                tinydb.putListString("client_types", cl_type);
                                tinydb.putListString("case_descriptions", c_description);
                                tinydb.putListString("client_emails", cl_email);
                                tinydb.putListString("client_mobiles", cl_mobile);
                                dialog.dismiss();
                                tinydb.putBoolean("hasCases", true);
                                caseCheck();
                                loading.dismiss();
                                showAlert("Case Added");

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
        });

    }

    @Override
    public void onClick(View view) {
        if (view == addcase) {
            showDialog();
        }
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        TextView filingdate = dialog.findViewById(R.id.filingdate);
        filingdate.setText(day + "-" + (month + 1) + "-" + year);
        enterdate = true;
    }

}
