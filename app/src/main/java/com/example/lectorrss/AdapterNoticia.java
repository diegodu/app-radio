package com.example.lectorrss;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;

import static androidx.core.app.ActivityCompat.startActivityForResult;


//Utilizamos una extencion de RecyclerView.Adapter la cual nos permitira mostrar un listado de elementos
//en este caso las tarjetas
public class AdapterNoticia extends RecyclerView.Adapter<AdapterNoticia.MyViewHolder> {
    ArrayList<Noticia> noticias;
    Context context;
    String url;
    String direccionCarga;
    int vari =1;
    private boolean bandera = true;
    private static final int PERMISSION_STORAGE_CODE = 1000;
    private PendingIntent pendingIntent;
    private static final String CHANNEL_ID = "NOTIFICACION";
    private static final int NOTIFICACION_ID = 0;

    private TextView carga;

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
        holder.textCarga.setText("0%");
        holder.textCarga2.setText(actual.mAudio);

        url = actual.mAudio;
        holder.btnAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  setPendingIntenet();
                //  createNotificacion();
                //   Uri uri = Uri.parse(url);
                //  Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                //  context.startActivity(intent);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ActivityCompat.checkSelfPermission((Activity)context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        ActivityCompat.requestPermissions((Activity) context,permissions,PERMISSION_STORAGE_CODE);
                        Log.d("APROBO","Aprobo la validacion1-------------------------------");
                    }
                    else{
                        //startDowloading(url, holder);
                        Log.d("APROBO","Aprobo la validacion2-------------------------------");
                        new DownloadFileFromURL().execute(holder);
                    }
                }else {
                    //startDowloading(url, holder);
                    Log.d("APROBO","Aprobo la validacion3-------------------------------");
                    new DownloadFileFromURL().execute(holder);
                }


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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    class DownloadFileFromURL extends AsyncTask<MyViewHolder, String, String> {

        MyViewHolder hol;
        String drc;
        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            Toast toast1 =
                    Toast.makeText(context.getApplicationContext(),
                            "Empieza descarga", Toast.LENGTH_SHORT);

            toast1.show();

        }

        /**
         * Downloading file in background thread
         * */

        @Override
        protected String doInBackground(MyViewHolder... f_url) {

            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), "MyRadio");

            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("App", "failed to create directory");
                }
            }


            hol = f_url[0];
            direccionCarga = (String) hol.textCarga2.getText();
            drc = direccionCarga;
            String nombreCancion = direccionCarga.substring(21,direccionCarga.length());
            Log.d("Direccion",nombreCancion);
            DownloadManager.Request request= new DownloadManager.Request(Uri.parse(direccionCarga));
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                    DownloadManager.Request.NETWORK_MOBILE);
            Log.d("LLEGA","LLEGO EL DATO AL LA CLASEEEE"+ direccionCarga);
            request.setTitle("Descargando");
            request.setTitle(nombreCancion);
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC,  "MyRadio/"+nombreCancion);
            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            long id = manager.enqueue(request);



            Timer myTimer = new Timer();
            TimerTask tk = new TimerTask() {
                @Override
                public void run() {


                    DownloadManager.Query q = new DownloadManager.Query();
                    q.setFilterById(id);
                    Cursor cursor = manager.query(q);
                    cursor.moveToFirst();

                    int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))/1024;
                    int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))/1024;
                    cursor.close();
                    if(bytes_downloaded == 0 && bytes_total == 0){
                        Log.d("roegfd","ssfffsfsfg");
                    }else{
                        int multi = Math.abs(bytes_downloaded) * 100;
                        int dl_progress = multi / bytes_total;

                        Log.d("Bytes descraga", "byres"+bytes_downloaded);
                        Log.d("Bytes total", "byres"+bytes_total);
                        Log.d("Division", "byres"+dl_progress);

                        Log.e("DESCARGAs", "Tiempo empleado : "+ dl_progress);

                        hol.progress_bar.setProgress(dl_progress);

                        if(bytes_downloaded == bytes_total){

                            dl_progress = 100;
                            hol.progress_bar.setProgress(0);

                            Log.e("NOTAf", "Se esta descargando "+bytes_downloaded+":"+bytes_total+"Tiempo empleado -------------------: "+ dl_progress);
                            myTimer.cancel();


                        }

                        publishProgress(""+Integer.toString(dl_progress));


                    }



                }

            };

            myTimer.schedule(tk, 0, 100);




            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage

            Log.e("PPP", "LLego el porcentaje : -----------------------------------"+progress[0]);
            hol.textCarga.setText(progress[0]+"%");
            hol.btnAudio.setEnabled(false);
            if(progress[0].endsWith("100")){
                Toast toast1 =
                        Toast.makeText(context.getApplicationContext(),
                                "Descarga Completada", Toast.LENGTH_SHORT);

                toast1.show();
                hol.btnAudio.setEnabled(true);
                hol.textCarga.setText("0%");
                try {
                    openFolder();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
          //  file_url = url;
          //  hol.textCarga.setText(file_url);




        }
        public void openFolder() throws IOException {

            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
           // intent.setAction(Integer.)
            Uri uri = Uri.parse(Environment.getExternalStorageDirectory() + "/"+ Environment.DIRECTORY_MUSIC + "/MyRadio");
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri);
        //    intent.setType("mp3");
            Log.d("Ruta", String.valueOf(uri));

          // intent.setDataAndType(uri, "audio/mp3");

            context.startActivity(intent);
            String rutaArchivo = Environment.getExternalStorageDirectory() + "/"+ Environment.DIRECTORY_MUSIC + "/MyRadio/"+direccionCarga;
            MediaPlayer mediaPlayer = new  MediaPlayer();
            mediaPlayer.setDataSource(rutaArchivo);
            mediaPlayer.prepare();
            mediaPlayer.start();
         //   context.startActivity(Intent.createChooser(intent, "Open folder"));


        }

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////


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
        private TextView textCurrentTime, textTotalDuracion, textCarga, textCarga2;
        private SeekBar playerSeekBar;
        private MediaPlayer mediaPlayer;
        private Handler handler = new Handler();
        private ProgressBar progress_bar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitulo = (TextView) itemView.findViewById(R.id.txtTitulo);
            mDescripcion = (TextView) itemView.findViewById(R.id.textDescripcion);
            mDescripcion.setMovementMethod(new ScrollingMovementMethod());
            mFecha = (TextView) itemView.findViewById(R.id.textFecha);
            //   mDuracion = (TextView) itemView.findViewById(R.id.textDuracion);
            mImagen = (ImageView) itemView.findViewById(R.id.imageView3);

            btnAudio = (Button) itemView.findViewById(R.id.btn_audio);

            ImagePlayPause = (ImageView) itemView.findViewById(R.id.img_btnPlayPause);
            textCurrentTime = (TextView) itemView.findViewById(R.id.textCurrentTime);
            textTotalDuracion = (TextView) itemView.findViewById(R.id.textTotalDuracion);
            playerSeekBar = (SeekBar) itemView.findViewById(R.id.playerSeekBar);
            mediaPlayer = new MediaPlayer();

            progress_bar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
            textCarga = (TextView) itemView.findViewById(R.id.textCarga);
            textCarga2 = (TextView) itemView.findViewById(R.id.textCarga2);
            carga = textCarga;


            playerSeekBar.setMax(100);


            ImagePlayPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mediaPlayer.isPlaying() && bandera == false){
                        Log.d("Audio", "pause");
                        setPendingIntenet();
                        cancelNotificacion();

                        handler.removeCallbacks(updater);
                        mediaPlayer.pause();

                        ImagePlayPause.setImageResource(R.drawable.ic_play);
                        bandera = true;
                        Log.d("BANDERA", "Valor del boleano PAUSAR :"+ bandera);
                    }else{
                        if(bandera == true) {
                            Log.d("Audio", "play");
                            createNotificacionChannel();
                            createNotificacion();
                            mediaPlayer.start();
                            ImagePlayPause.setImageResource(R.drawable.ic_pause);
                            updateSeekBar();
                            bandera = false;
                            Log.d("BANDERA", "Valor del boleano REPRODUCIR :"+ bandera);
                        }
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

                private void cancelNotificacion(){
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(NOTIFICACION_ID);

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
                Log.d("Prepare","musica");
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