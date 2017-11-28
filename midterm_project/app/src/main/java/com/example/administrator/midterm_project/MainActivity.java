package com.example.administrator.midterm_project;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView img;
    private TextView name;
    private TextView gender;
    private TextView birthdate;
    private TextView shili;
    private TextView hometown;
    private Button change;
    private Button delete;

    private int id;
    private String nameStr;
    private String genderStr;
    private String birthdateStr;
    private String shiliStr;
    private String hometownStr;

    private boolean changed;
    private boolean deleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();
        setOnClickListener();

        getPerson();
    }

    private void getPerson() {
        Bundle bundle = getIntent().getExtras();

        String img_path = bundle.getString("img") + ".png";
        img.setImageBitmap(BitmapFactory.decodeFile(img_path));

        id = bundle.getInt("id");
        nameStr = bundle.getString("name");
        genderStr = bundle.getString("gender");
        birthdateStr = bundle.getString("birthdate");
        shiliStr = bundle.getString("shili");
        hometownStr = bundle.getString("hometown");

        name.setText(nameStr);
        gender.setText(genderStr);
        birthdate.setText(birthdateStr);
        shili.setText(shiliStr);
        hometown.setText(hometownStr);
    }

    private void setOnClickListener() {
        name.setOnClickListener(this);
        gender.setOnClickListener(this);
        birthdate.setOnClickListener(this);
        shili.setOnClickListener(this);
        hometown.setOnClickListener(this);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent();
                if (nameStr != name.getText() || genderStr != gender.getText() || birthdateStr != birthdate.getText()
                        || shiliStr != shili.getText() || hometownStr != hometown.getText()) {
                    intent.putExtra("changed", true);

                    intent.putExtra("id", id);
                    intent.putExtra("name", name.getText());
                    intent.putExtra("gender", gender.getText());
                    intent.putExtra("birthdate", birthdate.getText());
                    intent.putExtra("shili", shili.getText());
                    intent.putExtra("hometown", hometown.getText());
                }
                Toast.makeText(MainActivity.this, "已成功修改", Toast.LENGTH_SHORT).show();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent();
                intent.putExtra("deleted", true);
                intent.putExtra("id", id);

                Toast.makeText(MainActivity.this, "已成功删除", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void findView() {
        img = (ImageView)findViewById(R.id.img);
        name =  (TextView)findViewById(R.id.name);
        gender = (TextView)findViewById(R.id.gender);
        birthdate = (TextView)findViewById(R.id.birthdate);
        shili = (TextView)findViewById(R.id.shili) ;
        hometown = (TextView)findViewById(R.id.hometown);
        change = (Button)findViewById(R.id.change);
        delete = (Button)findViewById(R.id.delete);
    }


    @Override
    public void onClick(View view) {
        TextView textview = ((TextView)view);
        textview.setCursorVisible(true);
        textview.setFocusableInTouchMode(true);
        textview.setInputType(InputType.TYPE_CLASS_TEXT);
        textview.requestFocus(); //to trigger the soft input
    }
}
