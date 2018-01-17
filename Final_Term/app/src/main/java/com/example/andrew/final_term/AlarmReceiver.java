package com.example.andrew.final_term;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Long lt = intent.getLongExtra("lastModifiedTime", 0L);

        // go to NoteContextActivity
        Intent intent1 = new Intent(context, NoteContextActivity.class);
        intent1.putExtra("lastModifiedTime", lt);
        intent1.putExtra("isAlarm", true);
        context.startActivity(intent1);
    }
}
