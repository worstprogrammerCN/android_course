package com.example.administrator.shoppinglist;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {

    private static final String STATIC_SATISFACTION = "MyWidgetReceiver";
    private static final String DYNAMIC_SATISFACTION = "MyDynamicWidgetReceiver";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_defaultText);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
//        views.setTextViewText(R.id.widgetText, widgetText);
        views.setTextViewText(R.id.widgetText, widgetText);
        views.setImageViewResource(R.id.widgetImg, R.drawable.shoplist);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
    
    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        Bundle bundle = intent.getExtras();
        if (intent.getAction().equals(STATIC_SATISFACTION)) {
            Product product = (Product) bundle.getParcelable("product");
            int productIndex = bundle.getInt("productIndex");

            String name = product.getName();
            String price = product.getPrice();
            int img = product.getImgResource();

            // widget 内容改为 "xxx仅售$price!"
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
            views.setImageViewResource(R.id.widgetImg, img);
            views.setTextViewText(R.id.widgetText, name + "仅售" + price + "!");

            // 设置intent
            Intent intentProductDetail = new Intent(context, product_detail.class);
            intentProductDetail.putExtra("product", product);
            intentProductDetail.putExtra("productIndex", productIndex);
            intentProductDetail.putExtra("isInProductList", true);

            // 设置pendingIntent
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentProductDetail, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            // 更新widget
            ComponentName thisWidget = new ComponentName(context.getApplicationContext(), AppWidget.class);
            appWidgetManager.updateAppWidget(thisWidget, views);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

