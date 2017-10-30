package com.example.administrator.shoppinglist;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DynamicReceiver extends BroadcastReceiver {
    private static final String DYNAMICATION = "myDynamicFilter";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DYNAMICATION)) {
            String name = intent.getStringExtra("name");
            int productIndex = intent.getIntExtra("productIndex", 0);
            int imgResource =  intent.getIntExtra("imgResource", R.drawable.kindle);

            Bitmap shopIcon = BitmapFactory.decodeResource(context.getResources(), imgResource);

            Notification.Builder notify = new Notification.Builder(context)
                    .setAutoCancel(true).setContentTitle("马上下单").setContentText(name + "已添加到购物车")
                    .setLargeIcon(shopIcon).setSmallIcon(imgResource);

            Intent notifyIntent = new Intent(context, MainActivity.class);
            notifyIntent.putExtra("isShopList", true);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, productIndex, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notify.setContentIntent(pendingIntent);

            NotificationManager manager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
            manager.notify(0, notify.build());

            context.unregisterReceiver(DataBus.dynamicReceiver);
        }
    }
}
