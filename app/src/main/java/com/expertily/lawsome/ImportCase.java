package com.expertily.lawsome;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.expertily.lawsome.Adapters.ViewPagerAdapter;
import com.expertily.lawsome.Fragments.Imports.DistrictCourt;
import com.expertily.lawsome.Fragments.Imports.HighCourt;
import com.expertily.lawsome.Fragments.Imports.SupremeCourt;

public class ImportCase extends AppCompatActivity {

    private TextView headertext;
    private Typeface regular;
    private Typeface bold;
    private android.support.v7.widget.Toolbar toolbar;
    private ViewPager viewpager;
    private TabLayout tablayout;
    private AppBarLayout appbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_case);

        bold = Typeface.createFromAsset(getAssets(), "fonts/semibold.ttf");
        headertext = findViewById(R.id.headertext);
        headertext.setText("Import Case: Delhi");
        headertext.setTypeface(regular);
        toolbar =  findViewById(R.id.bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        appbar = findViewById(R.id.appbar);
        viewpager = findViewById(R.id.viewpager);
        tablayout = findViewById(R.id.tablayout);
        tablayout.setupWithViewPager(viewpager);
        setUpViewPager(viewpager);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void setUpViewPager(ViewPager set) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DistrictCourt(), "District Court");
        adapter.addFragment(new HighCourt(), "High Court");
        adapter.addFragment(new SupremeCourt(), "Supreme Court");
        set.setAdapter(adapter);

        ViewGroup vg = (ViewGroup) tablayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(regular);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
