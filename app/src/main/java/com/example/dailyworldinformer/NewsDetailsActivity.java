package com.example.dailyworldinformer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.dailyworldinformer.Model.Article;
import com.example.dailyworldinformer.Model.FavouriteAritcle;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class NewsDetailsActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener{

    private ImageView imageView,fav,wl;
    private TextView date,time,title;
    private  boolean isHideToolbarView = false;
    private FrameLayout date_behavior;
    private LinearLayout titleAppbar;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private String mUrl,mImg,mTitle,mDate,mSource,mAuthor,description;
    private FloatingActionButton floatingActionButton;
    DatabaseReference databaseReference,databaseReference2;
    FirebaseAuth fAuth;
    int count=0,count2=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);


        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");
        appBarLayout = findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(this);


        floatingActionButton = (FloatingActionButton)findViewById(R.id.shareId);

        fAuth = FirebaseAuth.getInstance();

        date_behavior = findViewById(R.id.date_behaviour);
        imageView = findViewById(R.id.backdrop);
        wl = findViewById(R.id.wlId);
        fav = findViewById(R.id.favId);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        title = findViewById(R.id.title);

        Intent intent = getIntent();
        mUrl = intent.getStringExtra("url");
        mImg = intent.getStringExtra("img");
        mTitle = intent.getStringExtra("title");
        mDate = intent.getStringExtra("date");
        mSource = intent.getStringExtra("source");
        mAuthor = intent.getStringExtra("author");
        description = intent.getStringExtra("description");


        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(Utils.getRandomDrawbleColor());

        Glide.with(this)
                .load(mImg).apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
        date.setText(Utils.DateFormat(mDate));
        title.setText(mTitle);

        String author = null;
        if(mAuthor!=null || mAuthor!="")
        {
            mAuthor = " \u2022 "+mAuthor;
        }
        else{
            author = "";
        }

        time.setText(mSource + author + " \u2022 " +Utils.DateToTimeFormat(mDate));
        initWebView(mUrl);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v.getId() == R.id.shareId) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plan");
                        intent.putExtra(Intent.EXTRA_SUBJECT, mSource);
                        String body = mTitle + "\n" + mUrl + "\n" + "Share from the Daily World Informer App" + "\n";
                        intent.putExtra(Intent.EXTRA_TEXT, body);
                        startActivity(Intent.createChooser(intent, "Share With"));


                    } catch (Exception e) {
                        Toast.makeText(NewsDetailsActivity.this, "Sorry , cannot share", Toast.LENGTH_SHORT).show();
                    }
                }


            }

        });


        databaseReference = FirebaseDatabase.getInstance().getReference("favourite");
        String id = fAuth.getCurrentUser().getUid();

        databaseReference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds:snapshot.getChildren())
                {
                    FavouriteAritcle favouriteAritcle = ds.getValue(FavouriteAritcle.class);
                    if(favouriteAritcle.getUrl().equals(mUrl) && favouriteAritcle.getCount() == 1)
                    {
                        fav.setImageResource(R.drawable.ic_baseline_favorite_red_24);
                        count = 1;
                    }
                    else{
                        fav.setImageResource(R.drawable.ic_baseline_favorite_24);
                        count=0;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference2 = FirebaseDatabase.getInstance().getReference("watchLater");


        databaseReference2.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds:snapshot.getChildren())
                {
                    FavouriteAritcle favouriteAritcle = ds.getValue(FavouriteAritcle.class);
                    if(favouriteAritcle.getUrl().equals(mUrl) && favouriteAritcle.getCount() == 1)
                    {
                        wl.setImageResource(R.drawable.ic_baseline_watch_later_red_24);
                        count2 = 1;
                    }
                    else{
                        wl.setImageResource(R.drawable.ic_baseline_watch_later_24);
                        count2 = 0;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                switch (count){

                    case 0:
                        fav.setImageResource(R.drawable.ic_baseline_favorite_red_24);
                        String id1 = fAuth.getCurrentUser().getUid();
                        String key = databaseReference.push().getKey();

                        FavouriteAritcle favouriteAritcle = new FavouriteAritcle(key,id1,mUrl,mImg,mTitle,description,mSource,mDate,mAuthor,1);
                        databaseReference.child(id1).child(key).setValue(favouriteAritcle).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                count = 1;
                                Toast.makeText(NewsDetailsActivity.this,"Added to Favourite",Toast.LENGTH_SHORT).show();
                            }
                        });

                        break;
                    default:

                        fav.setImageResource(R.drawable.ic_baseline_favorite_24);
                        databaseReference = FirebaseDatabase.getInstance().getReference("favourite");
                        String id = fAuth.getCurrentUser().getUid();

                        List<FavouriteAritcle>favouriteAritcleList = new ArrayList<>();

                        databaseReference.child(id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                favouriteAritcleList.clear();

                                for(DataSnapshot ds : snapshot.getChildren())
                                {
                                    FavouriteAritcle favouriteAritcle = ds.getValue(FavouriteAritcle.class);

                                    if(favouriteAritcle.getUrl().equals(mUrl))
                                    {
                                       // Toast.makeText(NewsDetailsActivity.this,"url: "+favouriteAritcle.getUrl()+"\n"+mUrl,Toast.LENGTH_SHORT).show();
                                        favouriteAritcleList.add(favouriteAritcle);
                                    }

                                }


                                if(favouriteAritcleList.size() == 1)
                                {
                                    count = 0;
                                    databaseReference.child(id).child(favouriteAritcleList.get(0).getId()).removeValue();
                                    Toast.makeText(NewsDetailsActivity.this,"Remove from favourite",Toast.LENGTH_SHORT).show();
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        break;


                }



            }
        });

        wl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (count2){

                    case 0:
                        wl.setImageResource(R.drawable.ic_baseline_watch_later_red_24);
                        String id1 = fAuth.getCurrentUser().getUid();
                        String key = databaseReference2.push().getKey();

                        FavouriteAritcle favouriteAritcle = new FavouriteAritcle(key,id1,mUrl,mImg,mTitle,description,mSource,mDate,mAuthor,1);
                        databaseReference2.child(id1).child(key).setValue(favouriteAritcle).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                count2 = 1;
                                Toast.makeText(NewsDetailsActivity.this,"Added to watch later",Toast.LENGTH_SHORT).show();
                            }
                        });

                        break;
                    default:

                        wl.setImageResource(R.drawable.ic_baseline_watch_later_24);
                        databaseReference2 = FirebaseDatabase.getInstance().getReference("watchLater");
                        String id = fAuth.getCurrentUser().getUid();

                        List<FavouriteAritcle>favouriteAritcleList = new ArrayList<>();

                        databaseReference2.child(id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                favouriteAritcleList.clear();
                                for(DataSnapshot ds : snapshot.getChildren())
                                {
                                    FavouriteAritcle favouriteAritcle = ds.getValue(FavouriteAritcle.class);

                                    if(favouriteAritcle.getUrl().equals(mUrl))
                                    {
                                        // Toast.makeText(NewsDetailsActivity.this,"url: "+favouriteAritcle.getUrl()+"\n"+mUrl,Toast.LENGTH_SHORT).show();
                                        favouriteAritcleList.add(favouriteAritcle);
                                    }

                                }


                                if(favouriteAritcleList.size() == 1)
                                {
                                    count2 = 0;
                                    databaseReference2.child(id).child(favouriteAritcleList.get(0).getId()).removeValue();
                                    Toast.makeText(NewsDetailsActivity.this,"Remove from watch later",Toast.LENGTH_SHORT).show();
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        break;


                }

            }
        });


    }


    private void initWebView(String url)
    {
        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage  = (float) Math.abs(verticalOffset)/(float)maxScroll;

        if(percentage == 1f && isHideToolbarView)
        {
            date_behavior.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;
        }
       else if(percentage<1f && isHideToolbarView)
        {
            date_behavior.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        }
    }



}