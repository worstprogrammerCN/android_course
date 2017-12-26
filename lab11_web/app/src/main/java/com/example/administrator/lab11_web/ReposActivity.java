package com.example.administrator.lab11_web;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import com.example.administrator.lab11_web.factory.GithubServiceFactory;
import com.example.administrator.lab11_web.model.Github;
import com.example.administrator.lab11_web.model.Repo;
import com.example.administrator.lab11_web.service.GitHubService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ReposActivity extends AppCompatActivity {
    GitHubService gitHubService;
    String name;
    ArrayList<Map<String, String>> data_repos;

    ProgressBar progress_bar;
    ListView lv_repos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos);

        findView();
        Intent intent = getIntent();
        name = intent.getStringExtra("name");

        gitHubService = GithubServiceFactory.getInstance().getGitHubService();

        startWaiting();
        getRepos(name);
    }

    private void findView() {
        progress_bar = (ProgressBar)findViewById(R.id.progress_bar);
        lv_repos = (ListView)findViewById(R.id.lv_repos);
    }

    private void getRepos(String user) {
        Observable<ArrayList<Repo>> githubs = gitHubService.getRepos(user);
        githubs.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<Repo>>() {

                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onNext(ArrayList<Repo> repos) {
                        data_repos = new ArrayList<>();
                        for (Repo r : repos) {
                            Map<String, String> m = new HashMap<String, String>();
                            m.put("name", r.getName());
                            m.put("language", r.getLanguage());
                            m.put("description", r.getDescription());
                            data_repos.add(m);
                        }

                        Integer size = repos.size();
                        lv_repos.setAdapter(new SimpleAdapter(ReposActivity.this, data_repos, R.layout.list_view_item_repo,
                                new String[]{ "name", "language", "description" },
                                new int[]{ R.id.tv_repo_name, R.id.tv_repo_lang, R.id.tv_repo_des }));
                        Log.i("repos activity", size.toString() + " " + repos.get(0).getLanguage()); // string int string
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
        lv_repos.setVisibility(View.VISIBLE);
    }

    private void startWaiting() {
        progress_bar.setVisibility(View.VISIBLE);
        lv_repos.setVisibility(View.INVISIBLE);
    }
}
