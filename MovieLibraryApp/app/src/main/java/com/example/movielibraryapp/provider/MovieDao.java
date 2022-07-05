package com.example.movielibraryapp.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("select * from movieTable")
    LiveData<List<Movie>> getAllMovie();

    @Query("select * from movieTable where title=:title")
    List<Movie> getMovie(String title);

    @Insert
    void addMovie(Movie movie);

    @Query("delete from movieTable where title= :title")
    void deleteMovie(String title);

    @Query("delete FROM movieTable")
    void deleteAllMovies();

    @Query("delete from movieTable where year= :year")
    void deleteMovieByYear(String year);
}
