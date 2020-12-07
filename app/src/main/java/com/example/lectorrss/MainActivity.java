package com.example.lectorrss;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectorrss.LectorRss;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void button_Pagin(View view){
        String direccion = "https://www.ivoox.com/podcast-tacones-braguetas_fg_f1167230_filtro_1.xml";
        Intent intent = new Intent(MainActivity.this,RadioDatos.class);
        intent.putExtra("url",direccion);
        startActivity(intent);

    }
}