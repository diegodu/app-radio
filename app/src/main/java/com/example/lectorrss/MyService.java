package com.example.lectorrss;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyService extends Service {
    private MediaPlayer mediaPlayer;
    private  AdapterNoticia adapterNoticia;
    public String url;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("gf","hollaaaaaa");
        url = adapterNoticia.url;
        mediaPlayer = MediaPlayer.create(this, Uri.parse(url));
        mediaPlayer.start();
        return mediaPlayer.getCurrentPosition();


    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }
}
