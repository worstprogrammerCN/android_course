package com.example.administrator.myapplication;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    TextInputLayout numberLayout;
    TextInputLayout passwordLayout;
    RadioButton r1;
    RadioButton r2;
    Button signIn;
    Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final CharSequence[] items = {"拍摄", "从相册选择"};

        ImageView img = (ImageView) findViewById(R.id.img);
        numberLayout = (TextInputLayout)findViewById(R.id.numberLayout);
        passwordLayout = (TextInputLayout)findViewById(R.id.passwordLayout);
        r1 = (RadioButton)findViewById(R.id.radio1);
        r2 = (RadioButton)findViewById(R.id.radio2);
        signIn = (Button)findViewById(R.id.signIn);
        signUp = (Button)findViewById(R.id.signUp);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                .setTitle("请选择图片")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0)
                            Toast.makeText(MainActivity.this, "您选择了[拍摄]", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(MainActivity.this, "您选择了[从相册选择]", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "您选择了[取消]", Toast.LENGTH_SHORT).show();
                    }
                }).create().show();
            }
        });

        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "您选择了学生", Snackbar.LENGTH_SHORT)
                        .setAction("确定 ", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .show();
            }
        });

        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "您选择了教职工", Snackbar.LENGTH_SHORT)
                        .setAction("确定 ", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .show();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numberStr = numberLayout.getEditText().getText().toString();
                String passwordStr = passwordLayout.getEditText().getText().toString();
                Boolean isNumberEmpty = numberStr.equals("");
                Boolean isPasswordEmpty = passwordStr.equals("");
                Boolean isCorrect = numberStr.equals("123456") && passwordStr.equals("6666");
                if (isNumberEmpty) {
                    numberLayout.setError("学号不能为空");
                    numberLayout.setErrorEnabled(true);
                }else if (isPasswordEmpty) {
                    passwordLayout.setError("密码不能为空");
                    passwordLayout.setErrorEnabled(true);
                }else if (!isCorrect) {
                    Snackbar.make(view, "学号或密码错误", Snackbar.LENGTH_SHORT)
                            .setAction("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            }).show();
                }else {
                    Snackbar.make(view, "登陆成功", Snackbar.LENGTH_SHORT)
                            .setAction("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            }).show();
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isStudentChecked = r1.isChecked();
                Boolean isTeacherChecked = r2.isChecked();
                if (isStudentChecked) {
                    Snackbar.make(view, "学生注册功能尚未启用", Snackbar.LENGTH_SHORT)
                            .setAction("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            }).show();
                } else if (isTeacherChecked) {
                    Snackbar.make(view, "教职工功能尚未启用", Snackbar.LENGTH_SHORT)
                            .setAction("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            }).show();
                }
            }
        });

    }
}
