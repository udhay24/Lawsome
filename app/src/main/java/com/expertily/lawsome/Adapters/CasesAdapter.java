package com.expertily.lawsome.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.expertily.lawsome.R;

import java.util.ArrayList;

public class CasesAdapter extends RecyclerView.Adapter<CasesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> cases;
    private ArrayList<String> courts;
    private ArrayList<String> lawyers;
    private ArrayList<String> clients;
    private Typeface regular;
    private Typeface bold;

    public CasesAdapter(Context act, ArrayList<String> cases, ArrayList<String> courts, ArrayList<String> lawyers, ArrayList<String> clients) {
        this.context = act;
        this.cases = cases;
        this.courts = courts;
        this.lawyers = lawyers;
        this.clients = clients;
    }


    @Override
    public CasesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.case_row, viewGroup, false);
        regular = Typeface.createFromAsset(viewGroup.getContext().getAssets(), "fonts/regular.ttf");
        bold = Typeface.createFromAsset(viewGroup.getContext().getAssets(), "fonts/semibold.ttf");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CasesAdapter.ViewHolder viewHolder, int i) {
        String[] c = courts.get(i).split("/");
        viewHolder.name.setText(cases.get(i));
        viewHolder.court.setText(c[0]);
        viewHolder.lawyer.setText(lawyers.get(i));
        viewHolder.client.setText(clients.get(i));
    }

    @Override
    public int getItemCount() {
        return courts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView court;
        private TextView lawyer;
        private TextView client;

        public ViewHolder(View view) {

            super(view);
            name = (TextView) view.findViewById(R.id.case_name);
            court = (TextView) view.findViewById(R.id.court);
            lawyer = (TextView) view.findViewById(R.id.lawyer);
            client = (TextView) view.findViewById(R.id.client);
        }
    }

}