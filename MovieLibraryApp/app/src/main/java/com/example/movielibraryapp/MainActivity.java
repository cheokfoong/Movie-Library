package com.example.movielibraryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import java.util.*;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.movielibraryapp.provider.Movie;
import com.example.movielibraryapp.provider.MovieViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String currGenre;
    ArrayList<String> myList = new ArrayList<String>();
    //ArrayList<Movie> data = new ArrayList<>();
    ArrayAdapter myAdapter;
    DrawerLayout drawer;

    private MovieViewModel mMovieViewModel;
    MyRecycleViewAdapter adapter;

    DatabaseReference myRef;

    View myFrame;
    int x_down;
    int y_down;
    TextView title;
    TextView year;
    TextView country;
    TextView genre;
    TextView cost;
    TextView keyword;
    GestureDetector gestureDetector;
    ScaleGestureDetector scaleGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);
        Log.i("lab3","onCreate");


        mMovieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        adapter = new MyRecycleViewAdapter(mMovieViewModel);
        mMovieViewModel.getAllMovies().observe(this, newData -> {
            adapter.setData(newData);
            adapter.notifyDataSetChanged();
            //tv.setText(newData.size() + "");
        });

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS,Manifest.permission.READ_SMS},0);

        IntentFilter intentFilter = new IntentFilter("SMS_FILTER");
        registerReceiver(myReceiver,intentFilter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //To allow open & close navigation bar
        drawer = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //To allow actions for menu item in the navigation bar
        NavigationView navigationView = findViewById(R.id.nv);
        navigationView.setNavigationItemSelectedListener(new MyNavigationListener());

        //List of movies to be added in here
        ListView listView = findViewById(R.id.listView);
        myAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,myList);
        listView.setAdapter(myAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Database/Movie");

        gestureDetector = new GestureDetector(this, new MyGestureDetector());
        scaleGestureDetector = new ScaleGestureDetector(this, new MyScaleGestureDetector());

        myFrame = findViewById(R.id.frameLayout);
        myFrame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                int action = motionEvent.getActionMasked();
//
//                switch(action){
//                    case MotionEvent.ACTION_DOWN:
//                        x_down = (int)motionEvent.getX();
//                        y_down = (int)motionEvent.getY();
//                        return true;
////                    case MotionEvent.ACTION_MOVE:
////                        return true;
//                    case MotionEvent.ACTION_UP:
//                        cost = findViewById(R.id.Cost);
//
//                        //touches top left
//                        if(motionEvent.getY() < 30 && motionEvent.getX() < 30){
//                            int costValue = Integer.parseInt(cost.getText().toString());
//                            if (costValue >=50) {
//                                cost.setText(Integer.toString(costValue - 50));
//                            }
//                        }
//
//                        //touches top right
//                        if(motionEvent.getY() < 30 && motionEvent.getX() > myFrame.getWidth() - 30){
//                            int costValue = Integer.parseInt(cost.getText().toString());
//                            cost.setText(Integer.toString(costValue+50));
//                        }
//
//                        //move horizontally
//                        if(Math.abs(y_down-motionEvent.getY())<40){
//                            //move from left to right
//                            if(motionEvent.getX()-x_down>150)
//                            {
//                                addNewMovie(view);
//                                Toast.makeText(getApplicationContext(),"swipe horizontally",Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                        //move vertically
//                        else if (Math.abs(x_down-motionEvent.getX())<40){
//                            //move from top to bottom
//                            if(motionEvent.getY()-y_down>150)
//                            {
//                                resetField(view);
//                                Toast.makeText(getApplicationContext(),"swipe vertically",Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                        return true;
//                }

                gestureDetector.onTouchEvent(motionEvent);
                scaleGestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });

    }

    class MyScaleGestureDetector extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            keyword = findViewById(R.id.Keyword);
            String lowerKeyword = keyword.getText().toString().toLowerCase();
            keyword.setText(lowerKeyword);
            return super.onScale(detector);
        }
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener{
        @Override
        public void onLongPress(MotionEvent e) {
            clearField((ViewGroup) findViewById(R.id.layoutId));
            super.onLongPress(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            year = findViewById(R.id.Year);
            if (year.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(), "Year value is empty", Toast.LENGTH_SHORT).show();
            }
            else {
                if(distanceX ==0){
                    keyword = findViewById(R.id.Keyword);
                    String upperKeyword = keyword.getText().toString().toUpperCase();
                    keyword.setText(upperKeyword);
                }
                int yearValue = Integer.parseInt(year.getText().toString());
                year.setText(Integer.toString((int) (yearValue - distanceX)));

            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            moveTaskToBack(true);
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            cost = findViewById(R.id.Cost);
            if (cost.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Cost value is empty", Toast.LENGTH_SHORT).show();
            } else {
                int costValue = Integer.parseInt(cost.getText().toString());
                cost.setText(Integer.toString(costValue + 150));
            }
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            title = findViewById(R.id.Title);
            year = findViewById(R.id.Year);
            country = findViewById(R.id.Country);
            genre = findViewById(R.id.Genre);
            cost = findViewById(R.id.Cost);
            keyword = findViewById(R.id.Keyword);

            title.setText("Terminator");
            year.setText("2001");
            country.setText("USA");
            genre.setText("thriller");
            cost.setText("120");
            keyword.setText("18+");
            return super.onDoubleTap(e);
        }
    }

    class MyNavigationListener implements NavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // get the id of the selected item
            int id = item.getItemId();

            if (id == R.id.addMovie) {
                TextView movieName = findViewById(R.id.Title);
                TextView movieYear = findViewById(R.id.Year);
                myList.add(movieName.getText().toString() + " | " + movieYear.getText().toString());
                myAdapter.notifyDataSetChanged();
            } else if (id == R.id.rmLastMovie) {
                myList.remove(myList.size()-1);
                myAdapter.notifyDataSetChanged();
            } else if (id == R.id.rmAllMovie){
                myList.clear();
                mMovieViewModel.deleteAll();
                myRef.removeValue();
                myAdapter.notifyDataSetChanged();

            } else if (id == R.id.close) {
                finish();
            } else if (id == R.id.allMovie){
                goToNext(item);
            }
            // close the drawer
            drawer.closeDrawers();
            // tell the OS
            return true;
        }
    }

    public void goToNext(MenuItem item){
        Intent myIntent = new Intent(this,MainActivity2.class);
        startActivity(myIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // get the id of the selected item
        int id = item.getItemId();

        if (id == R.id.clField) {
            ViewGroup group = findViewById(R.id.layoutId);
            for (int i = 0, count = group.getChildCount(); i < count; ++i) {
                View view = group.getChildAt(i);
                if (view instanceof EditText) {
                    ((EditText)view).setText("");
                }
            }
        } else if (id == R.id.totalMovies) {
            int count = myList.size();
            Toast myToast = Toast.makeText(this,"Total movie(s): " + count, Toast.LENGTH_SHORT);
            myToast.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("lab3","onStart");

        SharedPreferences myData = getSharedPreferences("file1",0);
        ViewGroup layout = findViewById(R.id.layoutId);
        int index = 1;
        for (int i = 0, count = layout.getChildCount(); i < count; ++i) {
            View child = layout.getChildAt(i);
            if (child instanceof EditText){
                String currText = myData.getString("key" + index, "");
                ((EditText) child).setText(currText);
                index ++;
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("lab3","onResume");
        System.out.println(currGenre);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("lab3","onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("lab3","onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("lab3","onDestroy");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("lab3","onSaveInstanceState");

        // Change genre to lowercase when data is saved
        EditText genre = findViewById(R.id.Genre);
        currGenre = genre.getText().toString().toLowerCase();
        outState.putString("key0", currGenre);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("lab3","onRestoreInstanceState");

        // Change title to uppercase when data is restored
        EditText title = findViewById(R.id.Title);
        String currTitle = title.getText().toString().toUpperCase();
        title.setText(currTitle);

        // retrieve genre from saved bundle
        EditText genre = findViewById(R.id.Genre);
        genre.setText(savedInstanceState.getString("key0"));
    }

//    public void addNewMovie(View view){
//        TextView movieName = findViewById(R.id.Title);
//        TextView movieYear = findViewById(R.id.Year);
//
//        Toast myToast = Toast.makeText(this,"Movie -" + movieName.getText() + "- has been added", Toast.LENGTH_SHORT);
//        myToast.show();
//
//        SharedPreferences myData = getSharedPreferences("file1", 0);
//        SharedPreferences.Editor myEditor = myData.edit();
//
//        ViewGroup layout = findViewById(R.id.layoutId);
//        int index = 1;
//        for (int i = 0, count = layout.getChildCount(); i < count; ++i) {
//            View child = layout.getChildAt(i);
//            if (child instanceof EditText){
//                String currText = ((EditText) child).getText().toString();
//                myEditor.putString("key" + index, currText);
//                index ++;
//            }
//        }
//
//        myList.add(movieName.getText().toString() + " | " + movieYear.getText().toString());
//        myAdapter.notifyDataSetChanged();
//
//        myEditor.commit();
//
//    }

    public void addNewMovie(View view){
        TextView movieName = findViewById(R.id.Title);
        TextView movieYear = findViewById(R.id.Year);
        TextView movieCountry = findViewById(R.id.Country);
        TextView movieGenre = findViewById(R.id.Genre);
        TextView movieCost = findViewById(R.id.Cost);
        TextView movieKeyword = findViewById(R.id.Keyword);

        Toast myToast = Toast.makeText(this,"Movie -" + movieName.getText() + "- has been added", Toast.LENGTH_SHORT);
        myToast.show();

        myList.add(movieName.getText().toString() + " | " + movieYear.getText().toString());
        myAdapter.notifyDataSetChanged();

        Movie movie = new Movie(movieName.getText().toString(),movieYear.getText().toString(),movieCountry.getText().toString(),
                movieGenre.getText().toString(),Integer.parseInt(movieCost.getText().toString()),movieKeyword.getText().toString());

        mMovieViewModel.insert(movie);
        myRef.push().setValue(movie);

    }

    public void doubleCost(View view){
        EditText costText = findViewById(R.id.Cost);
        if (costText.getText().length() > 0){
            int cost = Integer.parseInt(costText.getText().toString());
            costText.setText(Integer.toString(cost*2));}
    }

    private void clearField(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText)view).setText("");
            }
        }
    }

    public void resetField(View view){
        clearField((ViewGroup) findViewById(R.id.layoutId));
    }

    public void clearShared(View view){
        SharedPreferences myData = getSharedPreferences("file1", 0);
        SharedPreferences.Editor myEditor = myData.edit();

        myEditor.clear().commit();
    }

    public void loadCost(View view){
        SharedPreferences myData = getSharedPreferences("file1", 0);
        String costLoad = myData.getString("key4", "");

        int cost = Integer.parseInt(costLoad)*2;
        EditText costText = findViewById(R.id.Cost);
        costText.setText(Integer.toString(cost));

        SharedPreferences.Editor myEditor = myData.edit();
        myEditor.putString("key4", String.valueOf(cost)).apply();
    }

    BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            TextView title = findViewById(R.id.Title);
            TextView year = findViewById(R.id.Year);
            TextView country = findViewById(R.id.Country);
            TextView genre = findViewById(R.id.Genre);
            TextView cost = findViewById(R.id.Cost);
            TextView keyword = findViewById(R.id.Keyword);

            int totalCost = Integer.parseInt(intent.getStringExtra("Cost")) + Integer.parseInt(intent.getStringExtra("Hidden Fee"));

            title.setText(intent.getStringExtra("Title"));
            year.setText(intent.getStringExtra("Year"));
            country.setText(intent.getStringExtra("Country"));
            genre.setText(intent.getStringExtra("Genre"));
            cost.setText(Integer.toString(totalCost));
            keyword.setText(intent.getStringExtra("Keyword"));
        }
    };
}