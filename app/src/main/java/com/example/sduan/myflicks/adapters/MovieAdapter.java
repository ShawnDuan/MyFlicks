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
import com.example.sduan.myflicks.models.Movie;
import com.example.sduan.myflicks.models.MovieDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sduan on 10/11/16.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieItemViewHolder> {

    private final static String TAG = "MovieAdapter";

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

        Picasso.with(mActivity).load((mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) ?
                movieInfo.getPosterPath() : movieInfo.getBackdropPath())
                .placeholder(R.drawable.placeholder)
                .into(holder.posterImage);

        int targetHeight, targetWidth;
        if (mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            targetHeight = (int) (mActivity.getResources().getDisplayMetrics().heightPixels * 0.333);
            targetWidth = (int) (targetHeight * 0.75);
        } else {
            targetHeight = (int) (mActivity.getResources().getDisplayMetrics().heightPixels * 0.667);
            targetWidth = (int) (targetHeight * 1.5);
        }
        ViewGroup.LayoutParams params = holder.posterImage.getLayoutParams();
        params.height = targetHeight;
        params.width = targetWidth;
        holder.posterImage.setLayoutParams(params);
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Movie movie = mMovieArray.get(position);
                    long movieId = movie.getMovieId();
                    Log.d(TAG, "click on movie id: " + movieId);

                    Intent intent = new Intent(mActivity, MovieDetailActivity.class);
                    intent.putExtra("movieId", movieId);
                    mActivity.startActivityForResult(intent, 0);
                }
            });
        }
    }
}
