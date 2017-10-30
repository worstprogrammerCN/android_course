package com.example.administrator.shoppinglist;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class WidgetReceiver extends BroadcastReceiver {
    private static final String WIDGET_SATISFACTION = "MyDynamicWidgetReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(WIDGET_SATISFACTION)) {
            String name = intent.getStringExtra("name");
            int img = intent.getIntExtra("imgResource", 0);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);
            // 改变 widget为 "xxx以添加到购物车!"
            remoteViews.setTextViewText(R.id.widgetText, name + "已添加到购物车!");
            remoteViews.setImageViewResource(R.id.widgetImg, img);

            // 设置intent
            Intent intentMainActivity = new Intent(context, MainActivity.class);
            intentMainActivity.putExtra("isShopList", true);

            // 设置pendingIntent
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentMainActivity, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widget, pendingIntent);

            // 更新widget视图
            AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context.getApplicationContext(),
                    AppWidget.class), remoteViews);

            // 取消动态广播
            context.unregisterReceiver(DataBus.widgetReceiver);
        }
    }
}
