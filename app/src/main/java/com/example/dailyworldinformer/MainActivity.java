package com.example.dailyworldinformer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.dailyworldinformer.Api.ApiClient;
import com.example.dailyworldinformer.Api.ApiInterface;
import com.example.dailyworldinformer.Model.Article;
import com.example.dailyworldinformer.Model.News;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    public static final String API_KEY = "4276ae0d0ff04588975e320806c708f0";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article>articleList = new ArrayList<>();
    private MyAdapter adapter;
    private FloatingActionButton optionBtn;


    private SearchView searchView;
    private SwipeRefreshLayout refreshLayout;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchId);
        optionBtn = findViewById(R.id.optionBtn);

        refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(MainActivity.this);
        refreshLayout.setColorSchemeResources(R.color.purple_200);

        fAuth = FirebaseAuth.getInstance();

        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        onLoadingSwipeRefresh("");

        if(searchView!=null)
        {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if(query.length()>2)
                    {
                        onLoadingSwipeRefresh(query);
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }




        optionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(fAuth.getCurrentUser() == null)
                {
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(MainActivity.this, ListActivity.class));
                    finish();
                }


            }
        });



    }

    public void LoadJson(final String keyword){

        refreshLayout.setRefreshing(true);
        ApiInterface apiInterface = ApiClient.getAPiClient().create(ApiInterface.class);

        String country = Utils.getCountry();
        String language = Locale.getDefault().getLanguage();
        Call<News> call;
        if(keyword.length()>0)
        {
            call = apiInterface.getNewsSearch(keyword,language,"publishedAt",API_KEY);
        }
        else{
            call = apiInterface.getNews(country,API_KEY);
        }
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if(response.isSuccessful() && response.body().getArticles()!=null)
                {
                    if(!articleList.isEmpty())
                    {
                        articleList.clear();
                    }
                    articleList = response.body().getArticles();
                    adapter = new MyAdapter(articleList,MainActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    initListener();
                    refreshLayout.setRefreshing(false);
                }else{
                    refreshLayout.setRefreshing(false);
                    Toast.makeText(MainActivity.this,"No Result",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {

            }
        });


    }

    private void initListener()
    {


        adapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if(fAuth.getCurrentUser() == null) {

                    startActivity(new Intent(MainActivity.this, LoginActivity.class));

                }
                else{
                    Intent intent = new Intent(MainActivity.this,NewsDetailsActivity.class);

                    Article article = articleList.get(position);
                    intent.putExtra("url",article.getUrl());
                    intent.putExtra("title",article.getTitle());
                    intent.putExtra("img",article.getUrlToImage());
                    intent.putExtra("date",article.getPublishedAt());
                    intent.putExtra("source",article.getSource().getName());
                    intent.putExtra("author",article.getAuthor());
                    intent.putExtra("description",article.getDescription());
                    startActivity(intent);
                }

            }
        });
    }


    @Override
    public void onRefresh() {
        LoadJson("");
    }

    private  void onLoadingSwipeRefresh(final String keyword)
    {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                LoadJson(keyword);
            }
        });
    }

    @Override
    public void onBackPressed() {
       this.finish();
    }
}