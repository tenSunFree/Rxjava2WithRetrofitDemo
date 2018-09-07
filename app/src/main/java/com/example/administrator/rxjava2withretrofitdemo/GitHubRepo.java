package com.example.administrator.rxjava2withretrofitdemo;

/**
 * 想要抓哪些資料下來, 為那些資料做相關設定
 */
public class GitHubRepo {

    public final int id, stargazersCount;
    public final String name, htmlUrl, description, language;

    public GitHubRepo(int id, String name, String htmlUrl, String description, String language, int stargazersCount) {
        this.id = id;
        this.name = name;
        this.htmlUrl = htmlUrl;
        this.description = description;
        this.language = language;
        this.stargazersCount = stargazersCount;
    }
}
