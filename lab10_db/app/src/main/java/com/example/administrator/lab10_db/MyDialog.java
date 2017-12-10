package com.example.administrator.lab10_db;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/12/9.
 */

public class MyDialog extends AlertDialog {
    //定义回调事件，用于dialog的点击事件
    public interface OnCustomDialogListener{
        public void back(String name);
    }

    private Person person;
    private OnCustomDialogListener customDialogListener;
    TextView tv_name;
    EditText ed_birthday;
    EditText ed_gift;

    public MyDialog(@NonNull Context context, Person person, OnCustomDialogListener customDialogListener) {
        super(context);
        this.person = person;
        this.customDialogListener = customDialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);

        tv_name = (TextView)findViewById(R.id.tv_name);
        ed_birthday = (EditText)findViewById(R.id.ed_birthday);
        ed_gift = (EditText)findViewById(R.id.ed_gift);

        tv_name.setText(person.getName());
        ed_birthday.setText(person.getBirthday());
        ed_gift.setText(person.getGift());

    }

}
