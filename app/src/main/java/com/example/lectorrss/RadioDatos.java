package com.example.lectorrss;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RadioDatos extends AppCompatActivity {
    RecyclerView recyclerView;
    String radio="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_datos);

        Intent intent = getIntent();

        recyclerView = findViewById(R.id.recycleView);
        LectorRss lectorRss = new LectorRss(this, recyclerView);

        lectorRss.direccion = intent.getStringExtra("url");
        Log.d("Nombre", lectorRss.direccion);
        lectorRss.execute();
        radio = lectorRss.direccion;



        }



    }

