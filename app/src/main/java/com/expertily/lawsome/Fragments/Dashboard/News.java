package com.expertily.lawsome.Fragments.Dashboard;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.expertily.lawsome.R;

public class News extends Fragment {

    private View view;
    private Typeface regular;
    private Typeface bold;

    private TextView news_1_head;
    private TextView news_1_text;
    private TextView news_1_date;
    private TextView news_1_read;

    private TextView news_2_head;
    private TextView news_2_text;
    private TextView news_2_date;
    private TextView news_2_read;

    private TextView news_3_head;
    private TextView news_3_text;
    private TextView news_3_date;
    private TextView news_3_read;

    private TextView news_4_head;
    private TextView news_4_text;
    private TextView news_4_date;
    private TextView news_4_read;

    private TextView news_5_head;
    private TextView news_5_text;
    private TextView news_5_date;
    private TextView news_5_read;

    public News() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.news, container, false);

        regular = Typeface.createFromAsset(getContext().getAssets(), "fonts/regular.ttf");
        bold = Typeface.createFromAsset(getContext().getAssets(), "fonts/semibold.ttf");

        news_1_head = (TextView) view.findViewById(R.id.news_1_head);
        news_1_text = (TextView) view.findViewById(R.id.news_1_text);
        news_1_date = (TextView) view.findViewById(R.id.news_1_date);
        news_1_read = (TextView) view.findViewById(R.id.news_1_read);
        news_1_head.setTypeface(bold);
        news_1_date.setTypeface(regular);
        news_1_read.setTypeface(bold);

        news_2_head = (TextView) view.findViewById(R.id.news_2_head);
        news_2_text = (TextView) view.findViewById(R.id.news_2_text);
        news_2_date = (TextView) view.findViewById(R.id.news_2_date);
        news_2_read = (TextView) view.findViewById(R.id.news_2_read);
        news_2_head.setTypeface(bold);
        news_2_date.setTypeface(regular);
        news_2_read.setTypeface(bold);

        news_3_head = (TextView) view.findViewById(R.id.news_3_head);
        news_3_text = (TextView) view.findViewById(R.id.news_3_text);
        news_3_date = (TextView) view.findViewById(R.id.news_3_date);
        news_3_read = (TextView) view.findViewById(R.id.news_3_read);
        news_3_head.setTypeface(bold);
        news_3_date.setTypeface(regular);
        news_3_read.setTypeface(bold);

        news_4_head = (TextView) view.findViewById(R.id.news_4_head);
        news_4_text = (TextView) view.findViewById(R.id.news_4_text);
        news_4_date = (TextView) view.findViewById(R.id.news_4_date);
        news_4_read = (TextView) view.findViewById(R.id.news_4_read);
        news_4_head.setTypeface(bold);
        news_4_date.setTypeface(regular);
        news_4_read.setTypeface(bold);

        news_5_head = (TextView) view.findViewById(R.id.news_5_head);
        news_5_text = (TextView) view.findViewById(R.id.news_5_text);
        news_5_date = (TextView) view.findViewById(R.id.news_5_date);
        news_5_read = (TextView) view.findViewById(R.id.news_5_read);
        news_5_head.setTypeface(bold);
        news_5_date.setTypeface(regular);
        news_5_read.setTypeface(bold);

        return view;
    }

}
