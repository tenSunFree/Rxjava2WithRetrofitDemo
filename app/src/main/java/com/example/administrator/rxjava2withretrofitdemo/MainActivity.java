package com.example.administrator.rxjava2withretrofitdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private String username;
    private Disposable disposable;
    private GitHubRecycler recyclerAdapter = new GitHubRecycler(this);
    private RecyclerView recyclerView;
    private EditText editTextUsername;
    private Button buttonSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializationView();
        recyclerView.setAdapter(recyclerAdapter);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = editTextUsername.getText().toString();
                if (!TextUtils.isEmpty(username)) {
                    getStarredRepos(username);
                }
            }
        });
    }

    private void initializationView() {
        recyclerView = findViewById(R.id.list_view_repos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        editTextUsername = findViewById(R.id.edit_text_username);
        buttonSearch = findViewById(R.id.button_search);
    }

    /**
     * 判斷如果disposable存在的話, 就將其取消订阅
     */
    @Override
    protected void onDestroy() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        super.onDestroy();
    }

    /** 到指定的網址去取得資料, 並切換到主線程 動態更新recyclerView */
    private void getStarredRepos(String username) {
        disposable = GitHubClient.getInstance().getStarredRepos(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<List<GitHubRepo>>() {
                            @Override
                            public void accept(List<GitHubRepo> gitHubRepos) throws Exception {
                                Log.i(TAG, "RxJava2: Response from server ...");
                                recyclerAdapter.setGitHubRepos(gitHubRepos);
                                KeybordUtil.closeKeybord(MainActivity.this);
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable t) throws Exception {
                                Log.i(TAG, "RxJava2, HTTP Error: " + t.getMessage());
                            }
                        }
                );
    }
}
