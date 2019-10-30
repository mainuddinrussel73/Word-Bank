package com.example.czgame.wordbank.ui.words;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service1 = new Intent(context, Mainservice.class);
        service1.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        context.startService(service1);
    }
}