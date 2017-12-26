package com.example.administrator.lab11_web;

import android.Manifest;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.administrator.lab11_web.adapter.CardAdapter;
import com.example.administrator.lab11_web.factory.GithubServiceFactory;
import com.example.administrator.lab11_web.model.Github;
import com.example.administrator.lab11_web.service.GitHubService;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private GitHubService service;

    ArrayList<Github> list;
    CardAdapter adapter;

    private EditText ed_name;
    private Button btn_clear;
    private Button btn_fetch;
    private RecyclerView rv_users;
    ProgressBar progress_bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findView();

        // rv_user RecyclerView
        list = new ArrayList<Github>();
        adapter = new CardAdapter(list);
        adapter.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MainActivity.this, ReposActivity.class);
                intent.putExtra("name", list.get(position).getLogin());
                startActivity(intent);// sth
            }

            @Override
            public void onLongClick(int position) {
                list.remove(position);
                adapter.updateData(list);
            }
        });
        rv_users.setAdapter(adapter);
        rv_users.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // btn_clear
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ed_name.setText("");
            }
        });

        // btn_fetch
        btn_fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name =  ed_name.getText().toString();
                getUser(name);
                startWaiting();
            }
        });
        service = GithubServiceFactory.getInstance().getGitHubService();
    }

    private void findView() {
        btn_clear = (Button)findViewById(R.id.btn_clear);
        btn_fetch =  (Button)findViewById(R.id.btn_fetch);
        ed_name = (EditText)findViewById(R.id.ed_name);
        rv_users =  (RecyclerView)findViewById(R.id.rv_users);
        progress_bar = (ProgressBar)findViewById(R.id.progress_bar);
    }

    private void getUser(String name) {
        Observable<Github> githubs = service.getUser(name);
        githubs.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Github>() {

                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onNext(Github github) {
                        Log.i("main", github.getBlog() + github.getLogin() + github.getId()); // string int string
                        list.add(github);
                        adapter.updateData(list);
                        stopWaiting();
                    }

                    @Override
                    public void onError(Throwable e) {}

                    @Override
                    public void onComplete() {}

                });
    }

    private void stopWaiting() {
        progress_bar.setVisibility(View.INVISIBLE);
        rv_users.setVisibility(View.VISIBLE);
    }

    private void startWaiting() {
        progress_bar.setVisibility(View.VISIBLE);
        rv_users.setVisibility(View.INVISIBLE);
    }
}
