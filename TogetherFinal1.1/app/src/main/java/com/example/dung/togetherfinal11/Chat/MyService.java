package com.example.dung.togetherfinal11.Chat;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.dung.togetherfinal11.Config.Config;

/*
 * Created by dung on 9/5/2016.
 */
public class MyService extends Service {

    public static ConnectivityManager cm;
    public static TogetherXMPP xmpp;
    public static boolean ServerchatCreated = false;
    String text = "";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder<MyService>(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        xmpp = TogetherXMPP.getInstance(MyService.this, Config.Server, Config.Username_Openfile, Config.Plainpassword,Config.TeamID);
        xmpp.connect();
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags,
                              final int startId) {
        return Service.START_NOT_STICKY;
    }

    @Override
    public boolean onUnbind(final Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        xmpp.disconnect();
    }

}
