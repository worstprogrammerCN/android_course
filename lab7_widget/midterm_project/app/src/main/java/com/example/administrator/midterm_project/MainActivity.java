package com.example.administrator.midterm_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name =  (TextView)findViewById(R.id.name);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textview = ((TextView)view);
                textview.setCursorVisible(true);
                textview.setFocusableInTouchMode(true);
                textview.setInputType(InputType.TYPE_CLASS_TEXT);
                textview.requestFocus(); //to trigger the soft input
            }
        });


    }
}
