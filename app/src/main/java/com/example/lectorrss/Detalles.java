package com.example.lectorrss;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;



public class Detalles extends AppCompatActivity {

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);
        webView = (WebView) findViewById(R.id.webView);
        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString("Enlace");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webView.loadUrl(url);

    }
}