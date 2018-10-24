package com.expertily.lawsome.Fragments.OpenedCases;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.expertily.lawsome.API.models.responses.ImportDistrictRes;
import com.expertily.lawsome.API.models.responses.ImportRes;
import com.expertily.lawsome.API.services.ApiClient;
import com.expertily.lawsome.API.services.ApiInterface;
import com.expertily.lawsome.Adapters.ActionsAdapter;
import com.expertily.lawsome.Adapters.OrdersAdapter;
import com.expertily.lawsome.CaseDetails;
import com.expertily.lawsome.R;
import com.expertily.lawsome.Storage.TinyDB;
import com.flipboard.bottomsheet.BottomSheetLayout;

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

public class Orders extends Fragment {

    private int pos;
    private String[] parts1;
    private String[] parts2;
    private String thecourt;
    private String courttype;
    private String type;
    private String number;
    private String year;
    private View view;
    private LinearLayout noorders;
    private ScrollView allorders;
    private RecyclerView orders_recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private ArrayList<String> c_court = new ArrayList<>();
    private ArrayList<String> c_numbers = new ArrayList<>();
    private ArrayList<String> orderids = new ArrayList<>();
    private ArrayList<String> orderdates = new ArrayList<>();
    private ArrayList<String> orderlinks = new ArrayList<>();
    private TinyDB db;
    private ApiInterface api;

    public Orders() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_orders, container, false);
        api = ApiClient.getClient().create(ApiInterface.class);
        db = new TinyDB(getContext());
        noorders = view.findViewById(R.id.noorders);
        allorders = view.findViewById(R.id.allorders);
        orders_recycler = view.findViewById(R.id.orders);

        pos = db.getInt("casedetails_position");
        c_court = db.getListString("court_names");
        c_numbers = db.getListString("case_numbers");

        getOrders();
        return view;
    }

    private void showAlert(String message) {
        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
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

    private void getOrders() {

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
        orders_recycler.setLayoutManager(manager);
        adapter = new OrdersAdapter(getActivity(), orderids, orderdates);
        orders_recycler.setAdapter(adapter);

        orders_recycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
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

                    Intent go = new Intent(getContext(), com.expertily.lawsome.WebView.class);
                    go.putExtra("pdf", orderlinks.get(position));
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

    private void disctrictActions() {

        api.importDistrictCase(thecourt, courttype, type, number, year).enqueue(new Callback<ImportDistrictRes>() {
            @Override
            public void onResponse(Call<ImportDistrictRes> call, Response<ImportDistrictRes> response) {

                if (response.isSuccessful()) {
                    if (response.body().getMessage() == null) {

                        for (int i = 0; i < response.body().getDailyOrders().size(); i++) {
                            String id = String.valueOf(i + 1);
                            orderids.add(id);
                            orderdates.add(parseDate(response.body().getDailyOrders().get(i).getDate()));
                            orderlinks.add(response.body().getDailyOrders().get(i).getCloudLink());
                        }

                        db.putListString("importedorderids", orderids);
                        db.putListString("importedorderdates", orderdates);
                        db.putListString("importedorderlinks", orderlinks);

                        orderids = db.getListString("importedorderids");
                        orderdates = db.getListString("importedorderdates");
                        orderlinks = db.getListString("importedorderlinks");

                        if (orderids.size() != 0) {
                            noorders.setVisibility(View.GONE);
                            allorders.setVisibility(View.VISIBLE);
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
                        for (int i = 0; i < response.body().getDailyOrders().size(); i++) {
                            String id = String.valueOf(i + 1);
                            orderids.add(id);
                            orderdates.add(parseDate(response.body().getDailyOrders().get(i).getDate()));
                            orderlinks.add(response.body().getDailyOrders().get(i).getCloudLink());
                        }

                        db.putListString("importedorderids", orderids);
                        db.putListString("importedorderdates", orderdates);
                        db.putListString("importedorderlinks", orderlinks);

                        orderids = db.getListString("importedorderids");
                        orderdates = db.getListString("importedorderdates");
                        orderlinks = db.getListString("importedorderlinks");

                        if (orderids.size() != 0) {
                            noorders.setVisibility(View.GONE);
                            allorders.setVisibility(View.VISIBLE);
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
                        for (int i = 0; i < response.body().getDailyOrders().size(); i++) {
                            String id = String.valueOf(i + 1);
                            orderids.add(id);
                            orderdates.add(parseDate(response.body().getDailyOrders().get(i).getDate()));
                            orderlinks.add(response.body().getDailyOrders().get(i).getCloudLink());
                        }

                        db.putListString("importedorderids", orderids);
                        db.putListString("importedorderdates", orderdates);
                        db.putListString("importedorderlinks", orderlinks);

                        orderids = db.getListString("importedorderids");
                        orderdates = db.getListString("importedorderdates");
                        orderlinks = db.getListString("importedorderlinks");

                        if (orderids.size() != 0) {
                            noorders.setVisibility(View.GONE);
                            allorders.setVisibility(View.VISIBLE);
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
