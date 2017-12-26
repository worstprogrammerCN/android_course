package com.example.administrator.lab11_web.service;

import com.example.administrator.lab11_web.model.Github;
import com.example.administrator.lab11_web.model.Repo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2017/12/12.
 */

public interface GitHubService {
    @GET("users/{user}")
    Observable<Github> getUser(@Path("user") String user);

    @GET("users/{user}/repos")
    Observable<ArrayList<Repo>> getRepos(@Path("user") String user);
}
