package com.example.administrator.rxjava2withretrofitdemo;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 網址的後半段部分, 具體定義想連到哪個網址去取得資料
 */
public interface GitHubService {

    @GET("users/{user}/starred")
    io.reactivex.Observable<List<GitHubRepo>> getStarredRepositories(@Path("user") String userName);
}
