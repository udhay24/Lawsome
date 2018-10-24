package com.expertily.lawsome;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class WebView extends AppCompatActivity {

    private String pdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sheet_order);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Toast.makeText(WebView.this, "Loading order", Toast.LENGTH_SHORT).show();
        Bundle bundle = getIntent().getExtras();
        pdf = bundle.getString("pdf");
        android.webkit.WebView webview = findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.download_pdf:
                Toast.makeText(WebView.this, "Tap the 3-dots to download", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(pdf)));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.webview_download, menu);
        return true;
    }

}
