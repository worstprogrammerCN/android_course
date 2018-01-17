package com.example.andrew.final_term;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.andrew.final_term.Service.DBService;
import com.example.andrew.final_term.Model.Info;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andrew on 2017/12/29.
 */

public class NoteContextActivity extends AppCompatActivity {
        private RecyclerView mRecyclerView;
        private LinearLayoutManager mLayoutManager;
        private MainActivityRecyclerViewAdapter mAdapter;
        private long lastModifiedTime;

        private int mode = EDITING;
        private static int EDITING = 1;
        private static int CREATING = 2;

        private ArrayList<String> images = new ArrayList<>();

        EditText inputContext;
        Button btnComplete;
        ImageView getBackButton;
        TextView tvDate;
        TextView tvClock;

//        ImageButton addTextButton;
        ImageButton addImageButton;
//        ImageButton checkButton;
        ImageButton deleteButton;
        ImageButton alarmButton;

        LinearLayout inputLayout;
        ScrollView input;


        TimePickerDialog timePickerDialog;
        Calendar cal = Calendar.getInstance();
        boolean isCalSet = false;

        ArrayList<CheckBox> checkBoxes;

        static final int IMAGE = 1;

        public ArrayList<Info> myData;

        // AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        //Image tempImage = new Image();
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.note_content);

            Intent intent = getIntent();
            inputContext = (EditText) findViewById(R.id.input_context);
            btnComplete = (Button)findViewById(R.id.btn_complete);
            getBackButton = (ImageView)findViewById(R.id.getBackButton);
            tvDate = (TextView)findViewById(R.id.date);
            tvClock = (TextView)findViewById(R.id.timeClock);

            // find imageButtons by id
//            addTextButton = (ImageButton) findViewById(R.id.addTextButton);
            addImageButton = (ImageButton) findViewById(R.id.addImageButton);
//            checkButton = (ImageButton) findViewById(R.id.checkButton);
//            checkButton.setTag("NO");
            CheckBox initCB = (CheckBox) findViewById(R.id.initCB);
            deleteButton = (ImageButton) findViewById(R.id.deleteButton);
            alarmButton = (ImageButton) findViewById(R.id.alarmButton);

            inputLayout = (LinearLayout) findViewById(R.id.input);
            input = (ScrollView) findViewById(R.id.scrollInput);
            checkBoxes = new ArrayList<CheckBox>();
            checkBoxes.add(initCB);

            btnComplete.setVisibility(View.VISIBLE);

            lastModifiedTime = intent.getLongExtra("lastModifiedTime", 0L);
            mode = lastModifiedTime == 0 ? CREATING : EDITING;
            if (mode == EDITING) {
                Info info = DBService.getService(getApplicationContext()).getInfo(lastModifiedTime);
                String context = info.context;
                images = info.images;
                tvDate.setText(Info.convertLongToDateString(info.lastModifiedTime));
                tvClock.setText(Info.convetLongToTimeClockString(info.lastModifiedTime));

                inputContext.setText(context);
                addImages();
                // pop up a dialog if it's opened by alarm
                boolean isAlarm = intent.getBooleanExtra("isAlarm", false);
                if (isAlarm) {
                    AlertDialog.Builder ab = new AlertDialog.Builder(NoteContextActivity.this);
                    ab.setTitle(info.title);
                    ab.setMessage(info.simpleContext);
                    ab.setPositiveButton("知道了", null);
                    ab.show();
                }
            } else  if (mode == CREATING){
                lastModifiedTime = System.currentTimeMillis();
                tvDate.setText(Info.convertLongToDateString(System.currentTimeMillis()));
                tvClock.setText(Info.convetLongToTimeClockString(System.currentTimeMillis()));
                inputContext.setText("");
            }

            btnComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mode == EDITING) {
                        DBService.getService(NoteContextActivity.this).updateInfo(lastModifiedTime, inputContext.getText().toString(), images);
                        Log.i("noteacti", String.format("images size: %d", images.size()));
//                        Intent intent = new Intent(NewAppWidget.APPWIDGET_UPDATE);
//                        intent.putExtra("lastModifiedTime", lastModifiedTime);
//                        sendBroadcast(intent);
                    } else if (mode == CREATING){
                        DBService.getService(NoteContextActivity.this).addInfo(lastModifiedTime, inputContext.getText().toString(), images);
                    }
                    if(isCalSet) {
                        setAlarm(cal);
                    }
                    finish();
                }
            });

            addImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, IMAGE);
                    Log.e("input", "pic");
                }
            });

            alarmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDateTimePickerDialogSuccessively();
                }
            });



            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder ab = new AlertDialog.Builder(NoteContextActivity.this);
                    ab.setTitle("删除项目");
                    ab.setMessage("确定要删除该项目吗？");
                    ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            delete();
                        }
                    });
                    ab.setNegativeButton("取消", null);
                    ab.show();
                }
            });


            getBackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        }

    private void addImages() {
            for (String image : images) {
                Bitmap bitmap = decodeBase64(image);
                final ImageView imageView = new ImageView(getApplicationContext());
                imageView.setImageBitmap(bitmap);

                setImageView(imageView, image);


            }
        }

        private void setImageView(final ImageView imageView, final String myBase64Image) {

            // set size and margin for imageView
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(500,500);
            lp.setMargins(10,10,10,10);
            imageView.setLayoutParams(lp);

            // build dialog

            final AlertDialog.Builder ab = new AlertDialog.Builder(NoteContextActivity.this);
            ab.setTitle("删除图片");
            ab.setMessage("确定要删除该图片吗？");
            ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    inputLayout.removeView(imageView);
                    images.remove(myBase64Image);
                }
            });
            ab.setNegativeButton("取消", null);

            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ab.show();
                    return false;
                }
            });

            inputLayout.addView(imageView, lp);
        }

        public void delete() {
            DBService.getService(NoteContextActivity.this).deleteInfo(lastModifiedTime);
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

            finish();
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            //获取图片路径
            if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
                Uri selectedImage = data.getData();
                showImage(selectedImage);
            }
        }

        public void showImage(Uri imagePath) {
            final ImageView imageView = new ImageView(getApplicationContext());
            imageView.setImageURI(imagePath);

            // get bitmap from imageView
            Bitmap image = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            final String myBase64Image = encodeToBase64(image, Bitmap.CompressFormat.JPEG, 100);
            images.add(myBase64Image);

            setImageView(imageView, myBase64Image);
            Log.e("input", "pic");
        }


        // encode BitMap to String
        public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
        {
            ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
            image.compress(compressFormat, quality, byteArrayOS);
            return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
        }

        // decode String to BitMap
        public static Bitmap decodeBase64(String input)
        {
            byte[] decodedBytes = Base64.decode(input, 0);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        }


    private void openDateTimePickerDialogSuccessively() {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                // set private calendar
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                Toast.makeText(NoteContextActivity.this, String.format("date is set. %d年%d月%d日.", year, month + 1, dayOfMonth), Toast.LENGTH_SHORT).show();
                openTimePickerDialog(true);
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(NoteContextActivity.this, onDateSetListener, year, month, day);
        datePickerDialog.show();
    }

    private void openTimePickerDialog(boolean is24r) {
        Calendar calendar = Calendar.getInstance();


        timePickerDialog = new TimePickerDialog(NoteContextActivity.this,
                onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), is24r);
        timePickerDialog.setTitle("Set Alarm Time");
        timePickerDialog.show();

    }

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Toast.makeText(NoteContextActivity.this, String.format("time is set. %d点%d分.", hourOfDay,minute), Toast.LENGTH_SHORT).show();
            cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
            cal.set(Calendar.MINUTE, minute);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            isCalSet = true;
        }
    };

        private void setAlarm(Calendar targetCal) {

            // intent
            Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
            intent.putExtra("lastModifiedTime", lastModifiedTime);
            Long lt = lastModifiedTime;

            // poendingIntent
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    getBaseContext(), lt.intValue(), intent, 0);


            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),
                    pendingIntent);

        }

}
