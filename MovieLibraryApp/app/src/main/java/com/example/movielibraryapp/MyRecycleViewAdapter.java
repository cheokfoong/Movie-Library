package com.example.movielibraryapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movielibraryapp.provider.Movie;
import com.example.movielibraryapp.provider.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyRecycleViewAdapter extends RecyclerView.Adapter<MyRecycleViewAdapter.ViewHolder>{

    List<Movie> data = new ArrayList<>();

    MovieViewModel mMovieViewModel;

    MyRecycleViewAdapter(MovieViewModel mMovieViewModel){
        this.mMovieViewModel = mMovieViewModel;
    }

    public void setData(List<Movie> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MyRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false); //CardView inflated as RecyclerView list item
        ViewHolder viewHolder = new ViewHolder(v);
        Log.d("week6App","onCreateViewHolder");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecycleViewAdapter.ViewHolder holder, int position) {
        holder.itemTitle.setText(data.get(position).getTitle());
        holder.itemYear.setText(data.get(position).getYear());
        holder.itemCountry.setText(data.get(position).getCountry());
        holder.itemGenre.setText(data.get(position).getGenre());
        holder.itemCost.setText(data.get(position).getCost()+"");
        holder.itemKeyword.setText(data.get(position).getKeyword());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast myToast = Toast.makeText(view.getContext(),"No." + (holder.getAdapterPosition()+1) + " with Title: " + holder.itemTitle.getText() + " is selected.", Toast.LENGTH_SHORT);
                myToast.show();

                String thisYear = String.valueOf(holder.itemYear.getText());
                mMovieViewModel.deleteMovieByYear(thisYear);
            }
        });
        Log.d("week6App","onBindViewHolder");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemTitle;
        public TextView itemYear;
        public TextView itemCountry;
        public TextView itemGenre;
        public TextView itemCost;
        public TextView itemKeyword;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.card_info1);
            itemYear = itemView.findViewById(R.id.card_info2);
            itemCountry = itemView.findViewById(R.id.card_info3);
            itemGenre = itemView.findViewById(R.id.card_info4);
            itemCost = itemView.findViewById(R.id.card_info5);
            itemKeyword = itemView.findViewById(R.id.card_info6);
        }
    }
}
