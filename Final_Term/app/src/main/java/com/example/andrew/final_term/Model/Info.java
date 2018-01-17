package com.example.andrew.final_term.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by Andrew on 2017/12/24.
 */

public class Info implements Parcelable {
    public String previewImage;
    public long lastModifiedTime;
    public String title;
    public String context;
    public String time;
    public String simpleContext;
    public ArrayList<String> images;

    private static int maxTitleLen = 8;
    private static int maxSimpleContextLen = 12;

    public Info(String image, long lastModifiedTime,String context, ArrayList<String> images){
        this.previewImage = image;
        this.lastModifiedTime = lastModifiedTime;
        this.title = getTitleFromContext(context);
        this.context = context;
        this.simpleContext = getSimpleContext(context);
        this.time = convertLongToDateString(lastModifiedTime);
        this.images = images;
    }

    protected Info(Parcel in) {
        previewImage = in.readString();
        lastModifiedTime = in.readLong();
        title = in.readString();
        context = in.readString();
        time = in.readString();
        simpleContext = in.readString();
        images = in.readArrayList(null);
    }

    public static final Creator<Info> CREATOR = new Creator<Info>() {
        @Override
        public Info createFromParcel(Parcel in) {
            return new Info(in);
        }

        @Override
        public Info[] newArray(int size) {
            return new Info[size];
        }
    };

    public static String convertLongToDateString(long lastModifiedTime) {
        Date d = new Date(lastModifiedTime);
        DateFormat format = DateFormat.getDateInstance();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(lastModifiedTime);
        return String.format("%s年%s月%s日", cal.get(Calendar.YEAR),  cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
    }

    public static String convetLongToTimeClockString(long lastModifiedTime) {
        Date d = new Date(lastModifiedTime);
        DateFormat format = DateFormat.getDateInstance();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(lastModifiedTime);

        int clock = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        return String.format("%d:%d", clock, minute);
    }

    public static String getTitleFromContext(String context) {
        int titleLen = context.indexOf('\n');
        if (titleLen > maxTitleLen) {
            return context.substring(0, maxTitleLen) + "...";
        } else if (titleLen >= 0 && titleLen <= maxTitleLen) {
            return context.substring(0, titleLen);
        } else if (titleLen == -1 && context.length() > maxTitleLen){
            return context.substring(0, maxTitleLen) + "...";
        } else { // titleLen == -1 && len <= maxTitleLen || titleLen <= maxTitleLen
            return context.substring(0);
        }
    }

    public static String getSimpleContext(String context) {
        int titleIndex = context.indexOf('\n');

        if (titleIndex == -1)
            return "";

        String remain = context.substring(titleIndex + 1);
        Scanner scanner = new Scanner(remain);
        while (scanner.hasNextLine()) {
            String line =  scanner.nextLine();
            String trim = line.trim();
            boolean allSpace = trim.equals("");
            if (allSpace)
                continue;
            else
                return line.substring(0, Math.min(maxSimpleContextLen, trim.length()));
        }
        return "";
    }

    public String getPreviewImage() {
        return previewImage;
    }

    public String getTitle() {
        return title;
    }

    public String getContext() {
        return context;
    }

    public String getTime() {
        return time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(previewImage);
        parcel.writeLong(lastModifiedTime);
        parcel.writeString(title);
        parcel.writeString(context);
        parcel.writeString(time);
        parcel.writeString(simpleContext);
        parcel.writeList(images);
    }
}
