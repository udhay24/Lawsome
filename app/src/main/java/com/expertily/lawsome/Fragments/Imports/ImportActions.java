package com.expertily.lawsome.Fragments.Imports;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.expertily.lawsome.Adapters.ActionsAdapter;
import com.expertily.lawsome.Adapters.CasesAdapter;
import com.expertily.lawsome.R;
import com.expertily.lawsome.Storage.TinyDB;

import java.util.ArrayList;

public class ImportActions extends Fragment {

    private View view;
    private LinearLayout nocases;
    private ScrollView allactions;
    private String casenumber;
    private String court;
    private RecyclerView actions_recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private ArrayList<String> importedaction = new ArrayList<>();
    private ArrayList<String> importedactiondate = new ArrayList<>();
    private TinyDB tinydb;

    public ImportActions() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.import_actions, container, false);

        tinydb = new TinyDB(getContext());
        nocases = view.findViewById(R.id.nocases);
        allactions = view.findViewById(R.id.allactions);
        actions_recycler = view.findViewById(R.id.actions);
        casenumber = tinydb.getString("importedcasenumber");
        court = tinydb.getString("importedcourtname");
        importedactiondate = tinydb.getListString("importedactiondate");
        importedaction = tinydb.getListString("importedaction");

        if (importedactiondate.size() != 0) {
            nocases.setVisibility(View.GONE);
            allactions.setVisibility(View.VISIBLE);
            loadActions();
        }
        return view;
    }

    private void loadActions() {
        manager = new LinearLayoutManager(getContext());
        actions_recycler.setLayoutManager(manager);
        adapter = new ActionsAdapter(getActivity(), importedactiondate, importedaction, casenumber, court);
        actions_recycler.setAdapter(adapter);
    }

}
