package com.example.andrew.final_term;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.util.Random;

import com.example.andrew.final_term.Model.Info;
import com.example.andrew.final_term.Service.DBService;

public class MyNotificationReceiver extends BroadcastReceiver {
    public static String ON_ENTER_MAIN_ACTIVITY = "onEnterMainActivity";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == ON_ENTER_MAIN_ACTIVITY) {

            //  get info from lastModifiedTime
            Long lastModifiedTime = intent.getLongExtra("lastModifiedTime", 0L);
            Info info  = DBService.getService(context).getInfo(lastModifiedTime);

            int infoIndex = intent.getIntExtra("infoIndex", 0);
            String title = info.title;
            String simpleContext = info.simpleContext;
            Bitmap shopIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);

            //设置Notification的内容
            Notification.Builder notify = new Notification.Builder(context)
                    .setAutoCancel(true).setContentTitle(title + "即将到期").setContentText(simpleContext)
                    .setLargeIcon(shopIcon).setSmallIcon(R.mipmap.ic_launcher_round);

            //设置intent
            Intent intentProductDetail = new Intent(context, NoteContextActivity.class);
            intentProductDetail.putExtra("lastModifiedTime", lastModifiedTime);
            intentProductDetail.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            context.sendBroadcast(intentProductDetail);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, infoIndex, intentProductDetail, PendingIntent.FLAG_UPDATE_CURRENT);
            notify.setContentIntent(pendingIntent);

            NotificationManager manager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
            manager.notify(0, notify.build());
        }
    }
}
