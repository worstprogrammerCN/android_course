package com.example.administrator.mediaplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

public class PlayerService extends Service {
    public static MediaPlayer mp = new MediaPlayer();
    private IBinder mBinder =  new MyBinder();

    private static int PAUSE_OR_START = 101;
    private static int STOP = 102;
    private static int QUIT = 103;
    private static int REFRESH =  104;
    private static int DRAG =  105;

    public PlayerService() {
        try {
            mp.setDataSource(Environment.getExternalStorageDirectory() + "/data/melt.mp3");
            mp.prepare();
            mp.setLooping(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pauseOrStart() {
        if (mp.isPlaying()) {
            mp.pause();
        } else {
            mp.start();
        }
    }

    public void stop() {
        if (mp != null) {
            mp.stop();
            try {
                mp.prepare();
                mp.seekTo(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class MyBinder extends Binder {
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code){
                case 101:
                    pauseOrStart();
                    // play / pause
                    break;
                case 102:
                    // stop
                    stop();
                    break;
                case 103:
                    // quit
                    break;
                case 104:
                    // refresh
                    break;
                case 105:
                    // drag
                    int curProgress = data.readInt();
                    mp.seekTo(curProgress);
                    break;
            }
            return super.onTransact(code, data, reply, flags);
        }
        public PlayerService getService() {
            return PlayerService.this;
        }
    }
}
