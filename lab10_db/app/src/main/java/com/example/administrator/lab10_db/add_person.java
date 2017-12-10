package com.example.administrator.lab10_db;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

public class add_person extends AppCompatActivity {
    public final String DB = "app.db";
    EditText edit_name;
    EditText edit_birthday;
    EditText edit_gift;
    Button btn_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        btn_add = (Button)findViewById(R.id.btn_add);
        edit_name =  (EditText)findViewById(R.id.edit_name);
        edit_birthday = (EditText)findViewById(R.id.edit_birthday);
        edit_gift = (EditText)findViewById(R.id.edit_gift);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edit_name.getText().toString();
                String birthday = edit_birthday.getText().toString();
                String gift = edit_gift.getText().toString();

                if (name.equals("")){
                    Toast.makeText(add_person.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                myHelper helper = getHelper();
                boolean isDup = helper.findIfDuplicate(name);
                if (isDup) {
                    Toast.makeText(add_person.this, "用户名重复", Toast.LENGTH_SHORT).show();
                } else {
                    EventBus.getDefault().post(new MessageEvent(new Person(name, birthday, gift)));
                    finish();
                }

            }
        });
    }
    private myHelper getHelper() {
        return new myHelper(this, DB, null, 1);
    }

}
