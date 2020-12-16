package com.example.lectorrss;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

    public class AdapterNoticia extends RecyclerView.Adapter<AdapterNoticia.MyViewHolder> {
    ArrayList<Noticia> noticias;
    Context context;






    public AdapterNoticia(ArrayList<Noticia> noticias, Context context) {
        this.noticias = noticias;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.item_noticia,parent,false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }




    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String url;
        Noticia actual=noticias.get(position);
        holder.mTitulo.setText(actual.getnTitulo());
        holder.mDescripcion.setText(corregirDescripcion(actual.getmDescripcion()));
        holder.mFecha.setText(actual.getmFechaPub());
       // holder.mDuracion.setText(actual.getMduracion());




        url = actual.mAudio;

        holder.btnAudio.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("CREATION", "INGRESO AL BOTON CON EXITO------------- ");



            }




        });



        //////////////////////////////////////////////////////////////////////




        Picasso.with(context).load(actual.getmImagen()).into(holder.mImagen);
        holder.mImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Detalles.class);
                intent.putExtra("Enlace",actual.getmEnlace());
                context.startActivity(intent);
            }
        });


    }










    private String corregirDescripcion(String s) {
        String descripcionOriginal= s;
        String separador="Contenido";
        String[] partes=descripcionOriginal.split(separador);
        String devolver=partes[0];
        return devolver;
    }

    @Override
    public int getItemCount() {
        return noticias.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTitulo, mDescripcion, mFecha, mDuracion;
        ImageView mImagen;
        Button btnAudio;


        Button play;
        

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitulo = (TextView) itemView.findViewById(R.id.txtTitulo);
            mDescripcion = (TextView) itemView.findViewById(R.id.textDescripcion);
            mDescripcion.setMovementMethod(new ScrollingMovementMethod());
            mFecha = (TextView) itemView.findViewById(R.id.textFecha);
        //    mDuracion = (TextView) itemView.findViewById(R.id.textDuracion);
            mImagen = (ImageView) itemView.findViewById(R.id.imageView3);

            btnAudio = (Button) itemView.findViewById(R.id.btn_audio);




        }
    }



}
