package com.example.lectorrss;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import java.util.logging.LogRecord;

public class AdapterNoticia extends RecyclerView.Adapter<AdapterNoticia.MyViewHolder> {
    ArrayList<Noticia> noticias;
    Context context;
    String url;
    private PendingIntent pendingIntent;
    private static final String CHANNEL_ID = "NOTIFICACION";
    private static final int NOTIFICACION_ID = 0;



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



        Noticia actual=noticias.get(position);
        holder.mTitulo.setText(actual.getnTitulo());
        holder.mDescripcion.setText(corregirDescripcion(actual.getmDescripcion()));
        holder.mFecha.setText(actual.getmFechaPub());
       // holder.mDuracion.setText(actual.getMduracion());

        url = actual.mAudio;
        holder.btnAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  setPendingIntenet();
              //  createNotificacion();
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }



        });

        Picasso.with(context).load(actual.getmImagen()).into(holder.mImagen);
        holder.mImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Detalles.class);
                intent.putExtra("Enlace",actual.getmEnlace());
                context.startActivity(intent);
            }
        });
        holder.prepareMediPLayer(url);


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
        Button btnAudio, downloadAudio;
        private ImageView ImagePlayPause;
        private TextView textCurrentTime, textTotalDuracion;
        private SeekBar playerSeekBar;
        private MediaPlayer mediaPlayer;
        private Handler handler = new Handler();




        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitulo = (TextView) itemView.findViewById(R.id.txtTitulo);
            mDescripcion = (TextView) itemView.findViewById(R.id.textDescripcion);
            mDescripcion.setMovementMethod(new ScrollingMovementMethod());
            mFecha = (TextView) itemView.findViewById(R.id.textFecha);
        //    mDuracion = (TextView) itemView.findViewById(R.id.textDuracion);
            mImagen = (ImageView) itemView.findViewById(R.id.imageView3);

            btnAudio = (Button) itemView.findViewById(R.id.btn_audio);

            ImagePlayPause = (ImageView) itemView.findViewById(R.id.img_btnPlayPause);
            textCurrentTime = (TextView) itemView.findViewById(R.id.textCurrentTime);
            textTotalDuracion = (TextView) itemView.findViewById(R.id.textTotalDuracion);
            playerSeekBar = (SeekBar) itemView.findViewById(R.id.playerSeekBar);
            mediaPlayer = new MediaPlayer();
            downloadAudio = (Button) itemView.findViewById(R.id.downloadAudio);

            playerSeekBar.setMax(100);

            ImagePlayPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mediaPlayer.isPlaying()){
                        setPendingIntenet();
                        createNotificacionChannel();
                        createNotificacion();

                        handler.removeCallbacks(updater);
                        mediaPlayer.pause();

                        ImagePlayPause.setImageResource(R.drawable.ic_play);
                    }else{
                        createNotificacionChannel();
                        createNotificacion();
                        mediaPlayer.start();
                        ImagePlayPause.setImageResource(R.drawable.ic_pause);
                        updateSeekBar();

                    }
                }

                private void setPendingIntenet() {
                    Intent intent = new Intent(String.valueOf(this));
                    TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
                    taskStackBuilder.addParentStack(context.getClass());
                    taskStackBuilder.addNextIntent(intent);
                    pendingIntent = taskStackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);

                }


                private void createNotificacionChannel() {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        CharSequence name = "Notificacion";
                        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name , NotificationManager.IMPORTANCE_DEFAULT);
                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.createNotificationChannel(notificationChannel);

                    }
                }

                private void createNotificacion() {
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), CHANNEL_ID);
                    builder.setSmallIcon(R.drawable.ic_baseline_music_note_24);
                    builder.setContentTitle("Notificacion Musica");
                    builder.setContentText("Music");
                    builder.setColor(Color.BLUE);
                    builder.setDefaults(Notification.DEFAULT_SOUND);
                    builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context.getApplicationContext());
                    notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());
                }
            });
            prepareMediPLayer("");
            playerSeekBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.d("df","vfdfffffff");
                    SeekBar seekBar = (SeekBar) v;
                    int playPosition  = (mediaPlayer.getDuration()/100) * seekBar.getProgress();
                    mediaPlayer.seekTo(playPosition);
                    textCurrentTime.setText(milisecondToTime(mediaPlayer.getCurrentPosition()));

                    return false;
                }
            });
            mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    playerSeekBar.setSecondaryProgress(percent);
                }
            });



        }
        public void prepareMediPLayer(String url){
            try {

                mediaPlayer.setDataSource(url);
                mediaPlayer.prepare();
                textTotalDuracion.setText(milisecondToTime(mediaPlayer.getDuration()));

            }catch (Exception e){

            }
        }
        private Runnable updater = new Runnable() {
            @Override
            public void run() {
                updateSeekBar();
                long currentDuration = mediaPlayer.getCurrentPosition();
                textCurrentTime.setText(milisecondToTime(currentDuration));

            }
        };
        private void updateSeekBar(){
            if(mediaPlayer.isPlaying()){
                playerSeekBar.setProgress((int)(((float) mediaPlayer.getCurrentPosition()/mediaPlayer.getDuration())*100));
                handler.postDelayed(updater,1000);
            }

        }
        private String milisecondToTime(long milliSeconds) {
            String timeString = "";
            String secondString = "";
            int hours = (int)(milliSeconds / (1000 * 60 * 60));
            int minutes = (int)(milliSeconds % (1000 * 60 *60)) / (1000 * 60);
            int second = (int)((milliSeconds % (1000 * 60 * 60)) % (1000 * 60)/1000);

            if(hours > 0){
                timeString = hours + ":";
            }
            if(second < 10){
                secondString = "0" + second;
            }else{
                secondString = "" + second;
            }
            timeString = timeString + minutes + ":" + secondString;
            return timeString;
        }
    }

}
