package com.example.appbcontentresolver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView tV;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tV=findViewById(R.id.numOfMovie);
        uri= Uri.parse("content://fit2081.app.CHEOK_FOONG/movieTable");
        Cursor result= getContentResolver().query(uri,null,null,null);
        tV.setText(result.getCount()+"");

    }

    public void addMovie(View view){
        ContentValues values= new ContentValues();
        values.put("title","Spiderman");
        values.put("year","2012");
        values.put("country","USA");
        values.put("genre","action");
        values.put("cost","120");
        values.put("keyword","exciting");
        Uri uri2= getContentResolver().insert(uri,values);
        Toast.makeText(this,uri2.toString(),Toast.LENGTH_LONG).show();
    }

    public void deleteMovie(View view){
        TextView cost = findViewById(R.id.costValue);
        int costValue = Integer.parseInt((cost.getText().toString()));
        String selection = "cost<" + costValue;
        String[] selectionArgs = null;
        int result= getContentResolver().delete(uri,selection,selectionArgs);
        Toast.makeText(this,"Number of movies deleted: " + result,Toast.LENGTH_LONG).show();
    }

    public void updateCost(View view){
        ContentValues values = new ContentValues();
        values.put("cost","200");
        String selection = "cost<?";
        String[] selectionArgs = {"50"};
        int result= getContentResolver().update(uri,values,selection,selectionArgs);
        Toast.makeText(this,"Number of movies updated: " + result,Toast.LENGTH_LONG).show();
    }
}