package com.expertily.lawsome.Fragments.OpenedCases;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.expertily.lawsome.API.models.responses.AuthRes;
import com.expertily.lawsome.API.models.responses.NotesRes;
import com.expertily.lawsome.API.services.ApiClient;
import com.expertily.lawsome.API.services.ApiInterface;
import com.expertily.lawsome.Adapters.NoteAdapter;
import com.expertily.lawsome.Authentication.Login;
import com.expertily.lawsome.R;
import com.expertily.lawsome.Storage.TinyDB;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Notes extends Fragment implements View.OnClickListener {

    private View view;
    private int count = 0;
    private boolean loading = false;
    private LinearLayout nonotes;
    private LinearLayout allnotes;
    private ImageView selectphoto;
    private ImageView addnote;
    private EditText enternote;
    private ApiInterface api;
    private TinyDB db;
    private RecyclerView notes_recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private ArrayList<String> ids = new ArrayList<>();
    private ArrayList<String> texts = new ArrayList<>();
    private ArrayList<String> timestamps = new ArrayList<>();
    private ProgressDialog dialog;

    public Notes() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.notes, container, false);
        api = ApiClient.getClient().create(ApiInterface.class);
        db = new TinyDB(view.getContext());

        nonotes = view.findViewById(R.id.nonotes);
        allnotes = view.findViewById(R.id.allnotes);
        addnote = view.findViewById(R.id.addnote);
        selectphoto = view.findViewById(R.id.selectphoto);
        enternote = view.findViewById(R.id.enternote);
        notes_recycler = view.findViewById(R.id.notes);

        addnote.setOnClickListener(this);
        selectphoto.setOnClickListener(this);

        loading = true;
        getNotes();
        return view;
    }

    private void alert(String message) {
        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void getNotes() {

        api.getNotes(db.getString("phonenumber").substring(2), db.getString("currentcasenumber")).enqueue(new Callback<List<NotesRes>>() {
            @Override
            public void onResponse(Call<List<NotesRes>> call, Response<List<NotesRes>> response) {

                if (response.isSuccessful()) {
                    loading = false;
                    count = response.body().size();
                    if (count > 0) {
                        nonotes.setVisibility(View.GONE);
                        allnotes.setVisibility(View.VISIBLE);

                        for (int i = 0; i < response.body().size(); i++) {
                            int id = i + 1;
                            ids.add("Note " + id);
                            texts.add(response.body().get(i).getText());
                            timestamps.add(response.body().get(i).getNoteTimestamp());
                        }
                        loadNotes();
                    }


                } else {
                    alert("Error retrieving case notes");
                }
            }

            @Override
            public void onFailure(Call<List<NotesRes>> call, Throwable t) {
                alert("Error retrieving case notes");
            }
        });

    }

    private void loadNotes() {

        loading = false;

        if (adapter != null) {
            adapter.notifyDataSetChanged();

        } else {
            manager = new LinearLayoutManager(getContext());
            notes_recycler.setLayoutManager(manager);
            adapter = new NoteAdapter(getActivity(), ids, texts, timestamps);
            notes_recycler.setAdapter(adapter);
        }

    }

    private void addNote() {

        dialog = new ProgressDialog(view.getContext(), R.style.MyTheme);
        dialog.setCancelable(false);
        dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        dialog.show();

        final String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        api.addNote(db.getString("phonenumber").substring(2), enternote.getText().toString(), "", db.getString("currentcasenumber"), date).enqueue(new Callback<AuthRes>() {
            @Override
            public void onResponse(Call<AuthRes> call, Response<AuthRes> response) {

                if (response.isSuccessful()) {

                    if (response.body().getStatus().equalsIgnoreCase("success")) {

                        dialog.dismiss();

                        if (count > 0) {
                            int num = count + 1;
                            ids.add("Note " + num);
                            texts.add(enternote.getText().toString());
                            timestamps.add(date);
                            loadNotes();
                            enternote.getText().clear();
                        } else {
                            nonotes.setVisibility(View.GONE);
                            allnotes.setVisibility(View.VISIBLE);
                            int num = count + 1;
                            ids.add("Note " + num);
                            texts.add(enternote.getText().toString());
                            timestamps.add(date);
                            loadNotes();
                            enternote.getText().clear();
                        }

                    } else {
                        dialog.dismiss();
                        alert("Error adding case note. Try again later.");
                    }

                } else {
                    dialog.dismiss();
                    alert("Error adding case note. Try again later.");
                }

            }

            @Override
            public void onFailure(Call<AuthRes> call, Throwable t) {
                dialog.dismiss();
                alert("Error adding case note. Try again later.");
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v == addnote) {
            if (loading) {
                alert("Please wait till we get your notes");
            } else {
                if (enternote.getText().toString().isEmpty()) {
                    alert("Can't post empty notes");
                } else {
                    addNote();
                }
            }
        } else if (v == selectphoto) {
            alert("Photo Notes: Coming Soon");
        }
    }

}
