package com.expertily.lawsome.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.expertily.lawsome.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class ActionsAdapter extends RecyclerView.Adapter<ActionsAdapter.ViewHolder> {

    private Context context;
    private String casenumber;
    private String court;
    private ArrayList<String> importedactiondate;
    private ArrayList<String> importedaction;

    public ActionsAdapter(Context act, ArrayList<String> importedactiondate, ArrayList<String> importedaction, String casenumber, String court) {
        this.context = act;
        this.importedactiondate = importedactiondate;
        this.importedaction = importedaction;
        this.casenumber = casenumber;
        this.court = court;
    }


    @Override
    public ActionsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.action_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ActionsAdapter.ViewHolder viewHolder, int i) {

        Date date;
        Calendar calendar = GregorianCalendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.'SSS'Z'", Locale.getDefault());

        try {
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
            date = simpleDateFormat.parse(importedactiondate.get(i));
        } catch (ParseException e) {
            e.printStackTrace();
            date = new Date(0);
        }

        calendar.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        SimpleDateFormat smf = new SimpleDateFormat("MMM");
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        String[] c = court.split("/");
        String dayOfTheWeek = sdf.format(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String month = smf.format(calendar.getTime());
        int year = calendar.get(Calendar.YEAR);

        viewHolder.weekday.setText(dayOfTheWeek);
        viewHolder.day.setText(String.valueOf(day));
        viewHolder.month.setText(month);
        viewHolder.year.setText(String.valueOf(year));
        viewHolder.hearing.setText("Hearing: " + dateFormat.format(date));
        viewHolder.casenumber.setText(casenumber);
        viewHolder.court.setText(c[0]);
        viewHolder.action.setText(importedaction.get(i));
    }

    @Override
    public int getItemCount() {
        return importedactiondate.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView weekday;
        private TextView day;
        private TextView month;
        private TextView year;
        private TextView hearing;
        private TextView casenumber;
        private TextView court;
        private TextView action;

        public ViewHolder(View view) {
            super(view);
            weekday = view.findViewById(R.id.weekday);
            day = view.findViewById(R.id.day);
            month = view.findViewById(R.id.month);
            year = view.findViewById(R.id.year);
            hearing = view.findViewById(R.id.hearing);
            casenumber = view.findViewById(R.id.casenumber);
            court = view.findViewById(R.id.court);
            action = view.findViewById(R.id.action);
        }
    }

}