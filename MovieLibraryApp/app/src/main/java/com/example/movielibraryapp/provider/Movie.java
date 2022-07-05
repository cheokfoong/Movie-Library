package com.example.movielibraryapp.provider;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity(tableName = "movieTable")
public class Movie implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "movieID")
    private int movieID;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "year")
    private String year;

    @ColumnInfo(name = "country")
    private String country;

    @ColumnInfo(name = "genre")
    private String genre;

    @ColumnInfo(name = "cost")
    private int cost;

    @ColumnInfo(name = "keyword")
    private String keyword;


    public Movie(String title, String year, String country, String genre, int cost, String keyword){
        this.title = title;
        this.year = year;
        this.country = country;
        this.genre = genre;
        this.cost = cost;
        this.keyword = keyword;
    }

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(@NonNull int movieID) {
        this.movieID = movieID;
    }


    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getCountry() {
        return country;
    }

    public String getGenre() {
        return genre;
    }

    public int getCost() {
        return cost;
    }

    public String getKeyword() {
        return keyword;
    }
}

