package com.example.administrator.fileeditor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditFile extends AppCompatActivity {
    EditText title;
    EditText content;
    Button btn_save;
    Button btn_load;
    Button btn_clear;
    Button btn_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_file);

        findView();

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title_str = title.getText().toString();
                String content_str = content.getText().toString();
                try {
                    FileOutputStream outputStream = openFileOutput(title_str, MODE_PRIVATE);
                    outputStream.write(content_str.getBytes());
                    Toast.makeText(EditFile.this, "save successfully", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title_str = title.getText().toString();
                try {
                    FileInputStream inputStream = openFileInput(title_str);
                    byte []content_bytes = new byte[inputStream.available()];
                    inputStream.read(content_bytes);
                    content.setText(new String(content_bytes));
                    Toast.makeText(EditFile.this, "load successfully", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(EditFile.this, "load failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content.setText("");
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title_str = title.getText().toString();
                deleteFile(title_str);
                Toast.makeText(EditFile.this, "delete successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void findView() {
        title = findViewById(R.id.article_title);
        content = findViewById(R.id.article_content);
        btn_save = findViewById(R.id.btn_save);
        btn_load = findViewById(R.id.btn_load);
        btn_clear = findViewById(R.id.btn_clear);
        btn_delete = findViewById(R.id.btn_delete);
    }

    @Override
    public void onBackPressed() {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);

        startActivity(home);
    }
}
