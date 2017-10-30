package com.example.administrator.shoppinglist;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == "onEnterMainActivity") {
            Product product = (Product) intent.getParcelableExtra("product");
            int productIndex = intent.getIntExtra("productIndex", 0);

            Bitmap shopIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.kindle);

            Notification.Builder notify = new Notification.Builder(context)
                    .setAutoCancel(true).setContentTitle("新商品热卖").setContentText(product.getName() + "仅售" + product.getPrice())
                    .setLargeIcon(shopIcon).setSmallIcon(R.mipmap.kindle);

            Intent newIntent = new Intent(context, product_detail.class);
            newIntent.putExtra("name", product.getName());
            newIntent.putExtra("price", product.getPrice());
            newIntent.putExtra("type", product.getType());
            newIntent.putExtra("information", product.getInformation());
            newIntent.putExtra("imgResource", product.getImgResource());
            newIntent.putExtra("productIndex", productIndex);
            newIntent.putExtra("isInProductList", true);
            // newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            context.sendBroadcast(newIntent);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, productIndex, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notify.setContentIntent(pendingIntent);

            NotificationManager manager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
            manager.notify(0, notify.build());
        }
    }
}
