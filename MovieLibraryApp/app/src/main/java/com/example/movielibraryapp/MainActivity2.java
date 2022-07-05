package com.example.movielibraryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.movielibraryapp.provider.Movie;
import com.example.movielibraryapp.provider.MovieViewModel;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MyRecycleViewAdapter adapter;

    private MovieViewModel mMovieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

//        Intent intent = getIntent();
//        Bundle bundle = intent.getBundleExtra("bundle");
//        data = (ArrayList<Movie>) bundle.getSerializable("movies");


        recyclerView = findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(this);  //A RecyclerView.LayoutManager implementation which provides similar functionality to ListView.
        recyclerView.setLayoutManager(layoutManager);   // Also StaggeredGridLayoutManager and GridLayoutManager or a custom Layout manager



        mMovieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        adapter = new MyRecycleViewAdapter(mMovieViewModel);
        recyclerView.setAdapter(adapter);

        mMovieViewModel.getAllMovies().observe(this, newData -> {
            adapter.setData(newData);
            adapter.notifyDataSetChanged();
        });

    }

    public void backButton(View view){
        finish();
    }

}
