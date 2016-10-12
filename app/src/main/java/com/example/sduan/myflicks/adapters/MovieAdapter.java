package com.example.sduan.myflicks.adapters;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sduan.myflicks.R;
import com.example.sduan.myflicks.models.Movie;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by sduan on 10/11/16.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieItemViewHolder> {
    private AppCompatActivity mActivity;
    private ArrayList<Movie> mMovieArray;

    public MovieAdapter(Activity activity, ArrayList<Movie> movies) {
        mActivity = (AppCompatActivity) activity;
        mMovieArray = movies;
    }

    @Override
    public MovieItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listed_movie, parent, false);
        MovieItemViewHolder viewHolder = new MovieItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieItemViewHolder holder, int position) {
        Movie movieInfo = mMovieArray.get(position);
        holder.title.setText(movieInfo.getOriginalTitle());
        holder.overview.setText(movieInfo.getOverview());

        Picasso.with(mActivity).load(movieInfo.getPosterPath()).into(holder.posterImage);
    }

    @Override
    public int getItemCount() {
        return mMovieArray.size();
    }

    public class MovieItemViewHolder extends RecyclerView.ViewHolder {

        ImageView posterImage;
        TextView title;
        TextView overview;

        public MovieItemViewHolder(View itemView) {
            super(itemView);
            posterImage = (ImageView) itemView.findViewById(R.id.ivPoster);
            title = (TextView) itemView.findViewById(R.id.tvMovieTitle);
            overview = (TextView) itemView.findViewById(R.id.tvMovieOverview);
        }
    }
}
