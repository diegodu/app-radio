package com.example.lectorrss;


import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectorrss.LectorRss;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




    }



    public void LanzarNotificacion(View v){
        Intent intent = new Intent(this, RadioDatos.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(RadioDatos.class);
        stackBuilder.addNextIntent(intent);
        pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);

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
    public void caida_limpia(View view){
        String direccion = "https://www.ivoox.com/podcast-caida-limpia_fg_f1164371_filtro_1.xml";
        Intent intent = new Intent(MainActivity.this,RadioDatos.class);
        intent.putExtra("url",direccion);
        startActivity(intent);
    }
    public void fanaticos_camp(View view){
        String direccion = "https://www.ivoox.com/podcast-fanaticos-campeones_fg_f1166031_filtro_1.xml";
        Intent intent = new Intent(MainActivity.this,RadioDatos.class);
        intent.putExtra("url",direccion);
        startActivity(intent);
    }

    public void Triple_Sentido(View view){
        String direccion = "https://www.ivoox.com/podcast-triple-sentido_fg_f1276087_filtro_1.xml";
        Intent intent = new Intent(MainActivity.this,RadioDatos.class);
        intent.putExtra("url",direccion);
        startActivity(intent);
    }

    public void Playlist_Radioactivo(View view){
        String direccion = "https://www.ivoox.com/podcast-playlist-radioactivo_fg_f1164337_filtro_1.xml";
        Intent intent = new Intent(MainActivity.this,RadioDatos.class);
        intent.putExtra("url",direccion);
        startActivity(intent);
    }
    public void TaconesyBraguetas(View view){
        String direccion = "https://www.ivoox.com/podcast-tacones-braguetas_fg_f1167230_filtro_1.xml";
        Intent intent = new Intent(MainActivity.this,RadioDatos.class);
        intent.putExtra("url",direccion);
        startActivity(intent);
    }


    public void EntrevistasFM88(View view){
        String direccion = "https://www.ivoox.com/podcast-entrevistas-fm88_fg_f1250444_filtro_1.xml";
        Intent intent = new Intent(MainActivity.this,RadioDatos.class);
        intent.putExtra("url",direccion);
        startActivity(intent);
    }
    public void Personalisimo(View view){
        String direccion = "https://www.ivoox.com/podcast-personalisimo_fg_f1167227_filtro_1.xml";
        Intent intent = new Intent(MainActivity.this,RadioDatos.class);
        intent.putExtra("url",direccion);
        startActivity(intent);
    }


    public void Enlasmejoresmanos(View view){
        String direccion = "https://www.ivoox.com/podcast-en-mejores-manos_fg_f1174942_filtro_1.xml";
        Intent intent = new Intent(MainActivity.this,RadioDatos.class);
        intent.putExtra("url",direccion);
        startActivity(intent);
    }




}
