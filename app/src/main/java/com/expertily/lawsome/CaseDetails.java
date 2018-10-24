package com.expertily.lawsome;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.expertily.lawsome.Adapters.ViewPagerAdapter;
import com.expertily.lawsome.Fragments.OpenedCases.Actions;
import com.expertily.lawsome.Fragments.OpenedCases.BasicDetails;
import com.expertily.lawsome.Fragments.OpenedCases.Connect;
import com.expertily.lawsome.Fragments.OpenedCases.Notes;
import com.expertily.lawsome.Fragments.OpenedCases.Orders;
import com.expertily.lawsome.Fragments.OpenedCases.Tasks;

public class CaseDetails extends AppCompatActivity {

    private TextView headertext;
    private Typeface regular;
    private Typeface bold;
    private android.support.v7.widget.Toolbar toolbar;
    private ViewPager viewpager;
    private TabLayout tablayout;
    private AppBarLayout appbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.case_details);

        fragmentManager = getSupportFragmentManager();
        Bundle bundle = getIntent().getExtras();
        String title = bundle.getString("title");
        String pos = bundle.getString("position");
        regular = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");
        bold = Typeface.createFromAsset(getAssets(), "fonts/semibold.ttf");
        headertext = findViewById(R.id.headertext);
        headertext.setText("CASE: " + title);
        headertext.setTypeface(regular);
        toolbar = findViewById(R.id.bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        appbar = findViewById(R.id.appbar);
        viewpager = findViewById(R.id.viewpager);
        viewpager.setOffscreenPageLimit(4);
        tablayout = findViewById(R.id.tablayout);
        tablayout.setupWithViewPager(viewpager);
        drawerLayout = findViewById(R.id.drawer);
        setUpViewPager(viewpager);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Bundle frag = new Bundle();
        frag.putString("position", String.valueOf(pos));
        BasicDetails pass = new BasicDetails();
        pass.setArguments(frag);
    }

    public void setUpViewPager(ViewPager set) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BasicDetails(), "Details");
        adapter.addFragment(new Connect(), "Contact");
        adapter.addFragment(new Actions(), "Case History");
        adapter.addFragment(new Orders(), "Orders");
        adapter.addFragment(new Notes(), "Notes");
        adapter.addFragment(new Tasks(), "Tasks");
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
                Intent go = new Intent(CaseDetails.this, Dashboard.class);
                startActivity(go);
                finish();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
