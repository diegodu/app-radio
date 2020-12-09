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

    public void button_area88(View view){
        String direccion = "https://www.ivoox.com/podcast-area-88_fg_f1250443_filtro_1.xml";
        Intent intent = new Intent(MainActivity.this,RadioDatos.class);
        intent.putExtra("url",direccion);
        startActivity(intent);
    }
    public void button_cafe88(View view){
        String direccion = "https://www.ivoox.com/podcast-cafe-88_fg_f1394031_filtro_1.xml";
        Intent intent = new Intent(MainActivity.this,RadioDatos.class);
        intent.putExtra("url",direccion);
        startActivity(intent);

    }
    public void button_deportes(View view){
        String direccion = "https://www.ivoox.com/podcast-deportivas_fg_f1854129_filtro_1.xml";
        Intent intent = new Intent(MainActivity.this,RadioDatos.class);
        intent.putExtra("url",direccion);
        startActivity(intent);

    }
    public void button_Radioactivo(View view){
        String direccion = "https://www.ivoox.com/podcast-playlist-radioactivo_fg_f1164337_filtro_1.xml";
        Intent intent = new Intent(MainActivity.this,RadioDatos.class);
        intent.putExtra("url",direccion);
        startActivity(intent);

    }
    public void button_Triple_Sentido(View view){
        String direccion = "https://www.ivoox.com/podcast-triple-sentido_fg_f1276087_filtro_1.xml";
        Intent intent = new Intent(MainActivity.this,RadioDatos.class);
        intent.putExtra("url",direccion);
        startActivity(intent);

    }
    public void button_Caida_Limpia(View view){
        String direccion = "https://www.ivoox.com/podcast-caida-limpia_fg_f1164371_filtro_1.xml";
        Intent intent = new Intent(MainActivity.this,RadioDatos.class);
        intent.putExtra("url",direccion);
        startActivity(intent);

    }
}
