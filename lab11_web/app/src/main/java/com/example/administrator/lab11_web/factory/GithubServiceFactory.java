package com.example.administrator.lab11_web.factory;

import com.example.administrator.lab11_web.service.GitHubService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/12/13.
 */

public class GithubServiceFactory {
    private static final GithubServiceFactory ourInstance = new GithubServiceFactory();
    private GitHubService gitHubService;
    public static GithubServiceFactory getInstance() {
        return ourInstance;
    }

    private GithubServiceFactory() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        gitHubService = retrofit.create(GitHubService.class);
    }

    public GitHubService getGitHubService() {
        return gitHubService;
    }
}
