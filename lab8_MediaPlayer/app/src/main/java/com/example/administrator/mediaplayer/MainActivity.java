package com.example.administrator.mediaplayer;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;

import java.security.Permissions;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity{
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 666;
    boolean hasPermission = false;
    private ServiceConnection sc;
    private IBinder mBinder;
    private ImageView disk_image;
    private Button btn_pause_or_start;
    private Button btn_stop;
    private Button btn_quit;
    private SeekBar seekBar;
    private TextView timePast;
    private TextView timeLeft;
    private TextView status;

    private ObjectAnimator animator;
    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");

    private static int PAUSE_OR_START = 101;
    private static int STOP = 102;
    private static int QUIT = 103;
    private static int REFRESH =  104;
    private static int DRAG =  105;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyStoragePermission();
        findView();
        addOnClickListener();

        sc = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                Log.d("service", "connected");
                mBinder = iBinder;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                sc = null;
            }
        };

        Intent intent =  new Intent(this, PlayerService.class);
        startService(intent);
        bindService(intent, sc, Context.BIND_AUTO_CREATE);

        initAnimator();
        initSeekBar();
        startUI();
    }

    private void initAnimator() {
        animator = ObjectAnimator.ofFloat(disk_image,"rotation",0, 360);
        animator.setDuration(4000);
        animator.setRepeatCount(-1);
        animator.setInterpolator(new LinearInterpolator());
    }

    private void initSeekBar() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // nothing
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    int code = STOP;
                    Parcel data = Parcel.obtain();
                    data.writeInt(seekBar.getProgress());
                    Parcel reply = Parcel.obtain();
                    mBinder.transact(code, data, reply, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                updateProgress();
            }
        });
    }

    private void updateProgress() {
        seekBar.setProgress(PlayerService.mp.getCurrentPosition());
        seekBar.setMax(PlayerService.mp.getDuration());
        String timePastStr = time.format(PlayerService.mp.getCurrentPosition());
        String timeLeftStr = time.format(PlayerService.mp.getDuration() - PlayerService.mp.getCurrentPosition());
        timePast.setText(timePastStr);
        timeLeft.setText(timeLeftStr);
    }

    private void findView() {
        btn_pause_or_start = findViewById(R.id.btn_pause_or_start);
        btn_stop =  findViewById(R.id.btn_stop);
        btn_quit = findViewById(R.id.btn_quit);
        seekBar = findViewById(R.id.seekBar);
        timePast = findViewById(R.id.time_past);
        timeLeft = findViewById(R.id.time_left);
        status = findViewById(R.id.status);
        disk_image = findViewById(R.id.disk_image);
    }

    private void addOnClickListener() {
        btn_pause_or_start.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                pauseOrStart();
            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop();
            }
        });
        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quit();
            }
        });
    }

    // 播放或停止
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void pauseOrStart() {
        if (PlayerService.mp.isPlaying()) { // 更新状态栏文字
            status.setText("Paused");
            btn_pause_or_start.setText("START");
            animator.pause();
        } else {
            status.setText("Playing");
            btn_pause_or_start.setText("PAUSE");
            if (!animator.isStarted()){
                animator.start();
            }
            else
                animator.resume();
        }
        try {
            int code = PAUSE_OR_START;
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            mBinder.transact(code, data, reply, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void stop() {
        animator.cancel();
        disk_image.setRotation(0);
        status.setText("Stopped");
        try {
            int code = STOP;
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            mBinder.transact(code, data, reply, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void quit() {
        unbindService(sc);
        sc = null;
        try {
            MainActivity.this.finish();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startUI() {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 123) {
                    updateProgress();
                }
            }
        };
        final Thread thread = new Thread() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(1000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if ( sc != null && hasPermission == true ) {
                        handler.obtainMessage(123).sendToTarget();
                    } else {
                        break;
                    }
                };
            }
        };
        thread.start();
    }

    private void verifyStoragePermission() {
        try {
            int permission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE }, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            } else {
                hasPermission = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            hasPermission = true;
        } else {
            System.exit(0);
        }
        return;
    }
}
