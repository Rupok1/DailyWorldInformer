package com.example.dailyworldinformer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.dailyworldinformer.Model.FavouriteAritcle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WatchLaterActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    DatabaseReference databaseReference;
    FirebaseAuth fAuth;
    List<FavouriteAritcle> favouriteAritcleList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_later);

        bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setSelectedItemId(R.id.watchLater);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.favourite:
                        startActivity(new Intent(getApplicationContext(),ListActivity.class));
                        overridePendingTransition(0,0);
                    case R.id.watchLater:
                        return true;
                    case R.id.logOut:
                        fAuth.signOut();
                        Toast.makeText(getApplicationContext(),"You are log off",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        return true;
                }
                return true;
            }
        });
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(WatchLaterActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        databaseReference = FirebaseDatabase.getInstance().getReference("watchLater");
        fAuth = FirebaseAuth.getInstance();
        LoadData();
    }

    private void LoadData() {
        String id = fAuth.getCurrentUser().getUid();

        databaseReference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                favouriteAritcleList.clear();

                for(DataSnapshot ds: snapshot.getChildren())
                {
                    FavouriteAritcle favouriteAritcle = ds.getValue(FavouriteAritcle.class);
                    favouriteAritcleList.add(favouriteAritcle);
                }

                MyFavouriteAdapter favouriteAdapter = new MyFavouriteAdapter(WatchLaterActivity.this,favouriteAritcleList);
                recyclerView.setAdapter(favouriteAdapter);
                favouriteAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(WatchLaterActivity.this,MainActivity.class));
        finish();
    }
}