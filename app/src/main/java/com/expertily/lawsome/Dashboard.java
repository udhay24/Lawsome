package com.expertily.lawsome;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.expertily.lawsome.Adapters.ViewPagerAdapter;
import com.expertily.lawsome.Extras.CustomTypefaceSpan;
import com.expertily.lawsome.Fragments.Dashboard.News;
import com.expertily.lawsome.Fragments.Dashboard.YourCases;
import com.expertily.lawsome.Storage.LocalStorage;
import com.expertily.lawsome.Storage.TinyDB;

public class Dashboard extends AppCompatActivity {

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
    private LocalStorage storage;
    private TinyDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        db = new TinyDB(getApplicationContext());
        storage = new LocalStorage(getApplicationContext());
        regular = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");
        bold = Typeface.createFromAsset(getAssets(), "fonts/semibold.ttf");
        headertext = (TextView) findViewById(R.id.headertext);
        headertext.setText(db.getString("name"));
        headertext.setTypeface(regular);
        toolbar = findViewById(R.id.bar);
        appbar = findViewById(R.id.appbar);
        viewpager =  findViewById(R.id.viewpager);
        tablayout = findViewById(R.id.tablayout);
        tablayout.setupWithViewPager(viewpager);
        drawerLayout = findViewById(R.id.drawer);
        setUpViewPager(viewpager);
        navigationDrawer();

    }

    private void showAlert(String message) {
        Toast.makeText(Dashboard.this, message, Toast.LENGTH_SHORT).show();
    }

    public void setUpViewPager(ViewPager set) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new News(), "News");
        adapter.addFragment(new YourCases(), "Your Cases");
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

    private void navigationDrawer() {
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();

                switch (id) {
                    case R.id.importcase:
                        Intent go = new Intent(Dashboard.this,  ImportCase.class);
                        startActivity(go);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.billing:
                        showAlert("Coming Soon!");
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.exit:
                        storage.logout();
                        finish();
                        Toast.makeText(Dashboard.this, "See you again. Be Lawsome!", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

        View header = navigationView.getHeaderView(0);
        Menu m = navigationView.getMenu();
        TextView name = (TextView) header.findViewById(R.id.name);
        name.setText(getResources().getString(R.string.welcome));
        name.setTypeface(regular);

        for (int i = 0; i < m.size(); i++) {

            MenuItem mi = m.getItem(i);
            SpannableString s = new SpannableString(mi.getTitle());
            s.setSpan(new CustomTypefaceSpan("", bold), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mi.setTitle(s);
        }

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.draweropen, R.string.drawerclose) {
            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

}
