package com.example.andrew.final_term;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.example.andrew.final_term.Model.Info;
import com.example.andrew.final_term.Service.DBService;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    public static final String STATIC_SATISFACTION = "MyWidgetReceiver";
    public static final String APPWIDGET_UPDATE = "android.appwidget.action.APPWIDGET_UPDATE";

    private Info info;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

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
        if(intent.getAction().equals(STATIC_SATISFACTION)) { // 新建widget

            //  get info from lastModifiedTime
            Long lastModifiedTime = intent.getLongExtra("lastModifiedTime", 0L);
            info = DBService.getService(context).getInfo(lastModifiedTime);

            String title = info.title;
            String simpleContext = info.simpleContext;


            // 设置widget内容: 标题、正文概要
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            views.setTextViewText(R.id.widget_title, title);
            views.setTextViewText(R.id.appwidget_text, simpleContext);

            // 设置intent
            Intent intentProductDetail = new Intent(context, NoteContextActivity.class);
            intentProductDetail.putExtra("lastModifiedTime", lastModifiedTime);

            // 设置pendingIntent
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentProductDetail, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            // 更新widget
            ComponentName thisWidget = new ComponentName(context.getApplicationContext(), NewAppWidget.class);
            appWidgetManager.updateAppWidget(thisWidget, views);
        }
//        else if (intent.getAction().equals(APPWIDGET_UPDATE)) { // 更新widget
//            long lastModifiedTime = intent.getLongExtra("lastModifiedTime", 0L);
//            if (this.info != null && lastModifiedTime != 0L && info.lastModifiedTime == lastModifiedTime) {
//                info = DBService.getService(context).getInfo(lastModifiedTime); // update view
//
//                // 设置widget内容: 标题、正文概要
//                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
//                views.setTextViewText(R.id.widget_title, info.title);
//                views.setTextViewText(R.id.appwidget_text, info.simpleContext);
//
//                // 设置intent
//                Intent intentProductDetail = new Intent(context, NoteContextActivity.class);
//                intentProductDetail.putExtra("info", info);
//                intentProductDetail.putExtra("lastModifiedTime", info.lastModifiedTime);
//                intentProductDetail.putExtra("context", info.context);
//
//                // 设置pendingIntent
//                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentProductDetail, PendingIntent.FLAG_UPDATE_CURRENT);
//                views.setOnClickPendingIntent(R.id.widget, pendingIntent);
//
//                // 更新widget
//                ComponentName thisWidget = new ComponentName(context.getApplicationContext(), NewAppWidget.class);
//                appWidgetManager.updateAppWidget(thisWidget, views);
//            }
//        }
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

