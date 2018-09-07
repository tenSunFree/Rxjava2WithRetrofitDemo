package com.example.administrator.rxjava2withretrofitdemo;

import android.support.annotation.NonNull;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class GitHubClient {

    private static final String GITHUB_BASE_URL = "https://api.github.com/";
    private static GitHubClient instance;
    private GitHubService gitHubService;

    private GitHubClient() {
        final Gson gson = new GsonBuilder()                                                         // setFieldNamingPolicy(), 如果是輸出emailAddress字段, 會變成{"email_address":"ikidou@example.com"}
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GITHUB_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))   // 為了進行线程切换
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        gitHubService = retrofit.create(GitHubService.class);
    }

    /** 实现单例模式 */
    public static GitHubClient getInstance() {
        if (instance == null) {
            instance = new GitHubClient();
        }
        return instance;
    }

    /** 取得完整網址的資料 */
    public io.reactivex.Observable<List<GitHubRepo>> getStarredRepos(@NonNull String userName) {
        return gitHubService.getStarredRepositories(userName);
    }
}


