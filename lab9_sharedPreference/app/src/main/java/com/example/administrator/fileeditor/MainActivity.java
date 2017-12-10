package com.example.administrator.fileeditor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText pw;
    EditText new_pw;
    EditText confirm_pw;
    Button btn_ok;
    Button btn_clear;

    final String PREFERENCE_NAME = "fileEditor";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isPasswordSet()) {
            setContentView(R.layout.login);
            findView();
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String pw_str = pw.getText().toString();
                    String saved_pw_str =  getSavedPassword();
                    Boolean isCorrect = pw_str.equals(saved_pw_str);

                    if (isCorrect) {
                        Intent intent = new Intent(MainActivity.this, EditFile.class); // 跳转
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "password does not match", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } else {
            setContentView(R.layout.activity_main);
            findView();
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String pw_str = new_pw.getText().toString();
                    String confirm_pw_str = confirm_pw.getText().toString();
                    boolean isEmpty = pw_str.equals("");
                    boolean isSame = confirm_pw_str.equals(pw_str);


                    if (isEmpty) {
                        Toast.makeText(MainActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                    } else if (!isSame) {
                        Toast.makeText(MainActivity.this, "Password not same", Toast.LENGTH_SHORT).show();
                    } else {
                        savePassword(pw_str);
                        Toast.makeText(MainActivity.this, "password has been set", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            btn_clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new_pw.setText("");
                    confirm_pw.setText("");
                }
            });
        }


    }

    private void savePassword(String pw) {
        SharedPreferences sp = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("password", pw);
        editor.putBoolean("isPasswordSet", true);
        editor.commit();
    }

    private Boolean isPasswordSet(){
        SharedPreferences sp = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        return sp.getBoolean("isPasswordSet", false);
    }

    private String getSavedPassword() {
        SharedPreferences sp = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        return sp.getString("password", "xx");
    }

    private void findView() {
        pw = findViewById(R.id.pw);
        new_pw = findViewById(R.id.new_pw);
        confirm_pw = findViewById(R.id.confirm_pw);
        btn_ok = findViewById(R.id.btn_ok);
        btn_clear = findViewById(R.id.btn_clear);
    }


}
