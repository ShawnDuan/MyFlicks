package com.example.sduan.myflicks.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sduan.myflicks.R;
import com.example.sduan.myflicks.TrailerFullScreenPlayActivity;
import com.example.sduan.myflicks.models.Movie;
import com.example.sduan.myflicks.MovieDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sduan on 10/11/16.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieItemViewHolder> {

    private final static String TAG = "MovieAdapter";

    private final static int MORE_POPULAR = 1;
    private final static int LESS_POPULAR = 2;

    private AppCompatActivity mActivity;
    private ArrayList<Movie> mMovieArray;

    public MovieAdapter(Activity activity, ArrayList<Movie> movies) {
        mActivity = (AppCompatActivity) activity;
        mMovieArray = movies;
    }

    @Override
    public int getItemViewType(int position) {
        return (mMovieArray.get(position).getVoteAverage() > 5.0f) ? MORE_POPULAR : LESS_POPULAR;
    }

    @Override
    public MovieItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == MORE_POPULAR) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listed_movie_simple, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listed_movie, parent, false);
        }
        MovieItemViewHolder viewHolder = new MovieItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieItemViewHolder holder, int position) {
        final Movie movieInfo = mMovieArray.get(position);
        final long movieId = movieInfo.getMovieId();
        final int viewType = holder.getItemViewType();

        String imagePath;
        if (mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && viewType == LESS_POPULAR) {
            imagePath = movieInfo.getPosterPath();
        } else {
            imagePath = movieInfo.getBackdropPath();
        }
        Picasso.with(mActivity).load(imagePath)
                .placeholder(R.drawable.placeholder)
                .into(holder.posterImage);
        holder.title.setText(movieInfo.getOriginalTitle());

        if (holder.getItemViewType() == LESS_POPULAR) {
            holder.overview.setText(movieInfo.getOverview());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "click on movie id: " + movieId);
                Intent intent;
                if (viewType == LESS_POPULAR) {
                    intent = new Intent(mActivity, MovieDetailActivity.class);
                } else {
                    intent = new Intent(mActivity, TrailerFullScreenPlayActivity.class);
                }
                intent.putExtra("movieId", movieId);
                mActivity.startActivityForResult(intent, 0);
            }
        });
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
