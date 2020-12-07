package com.example.lectorrss;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

public class RadioDatos extends AppCompatActivity {
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_datos);
        Intent intent = getIntent();

        recyclerView = findViewById(R.id.recycleView);

        LectorRss lectorRss = new LectorRss(this, recyclerView);
        lectorRss.direccion = intent.getStringExtra("url");
        lectorRss.execute();
    }
}