package com.example.andrew.final_term;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.andrew.final_term.Service.DBService;
import com.example.andrew.final_term.Model.Info;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MainActivityRecyclerViewAdapter mAdapter;
    public ArrayList<Info> myData;
    public FloatingActionButton fab;
    //Image tempImage = new Image();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        //Log.e("TAG", ((Integer) myData.size()).toString());
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recyclerview);
        fab = (FloatingActionButton)findViewById(R.id.fab);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MainActivityRecyclerViewAdapter(myData);
        mRecyclerView.setAdapter(mAdapter);

        ImageView search = (ImageView) findViewById(R.id.searchButton);
        final EditText searchText = (EditText) findViewById(R.id.searchBarText);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = searchText.getText().toString();
                ArrayList<Info> temp = new ArrayList<>();
                if(!text.isEmpty()) {
                    for(Info i : myData) {
                        String title = i.title;
                        if(title.contains(text)) {
                            temp.add(i);
                        }
                    }
                }
                mAdapter.setData(temp);
                mAdapter.notifyDataSetChanged();
            }
        });

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0) {
                    mAdapter.setData(myData);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        mAdapter.setOnItemClickListener(new MainActivityRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, NoteContextActivity.class);
                intent.putExtra("lastModifiedTime", myData.get(position).lastModifiedTime);
                startActivity(intent);

            }
        });

        mAdapter.setOnItemLongClickListener(new MainActivityRecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final int position) {
                AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
                ab.setTitle("删除项目");
                ab.setMessage("确定要删除该项目吗？");
                ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(myData.get(position).lastModifiedTime);
                        myData.remove(position);
                        mAdapter.notifyDataSetChanged();
                    }
                });
                ab.setNegativeButton("取消", null);
                ab.show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NoteContextActivity.class);
                startActivity(intent);
            }
        });
    }

    public void delete(long lastModifiedTime) {
        DBService.getService(MainActivity.this).deleteInfo(lastModifiedTime);

        // intent
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("lastModifiedTime", lastModifiedTime);
        Long lt = lastModifiedTime;

        // poendingIntent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), lt.intValue(), intent, 0);


        // cancel the alarm
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }


    @Override
    protected void onStart() {
        init();
        mAdapter.setData(myData);
        mAdapter.notifyDataSetChanged();
        
        if (mAdapter.getItemCount() > 0) {
            int randomIndex = new Random().nextInt(mAdapter.getItemCount());
            Info info = mAdapter.getInfo(randomIndex);

            Intent intent = new Intent(NewAppWidget.STATIC_SATISFACTION);
            intent.putExtra("lastModifiedTime", info.lastModifiedTime);
            sendBroadcast(intent);

            Intent intent1 = new Intent(MyNotificationReceiver.ON_ENTER_MAIN_ACTIVITY);
            intent1.putExtra("lastModifiedTime", info.lastModifiedTime);
            sendBroadcast(intent1);
        }
        super.onStart();
    }

    public void init() {
        myData = DBService.getService(MainActivity.this).getAllInfo();
    }

}
