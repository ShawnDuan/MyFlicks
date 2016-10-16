package com.example.sduan.myflicks.models;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.sduan.myflicks.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by sduan on 10/15/16.
 */

public class MovieDetailActivity extends AppCompatActivity {
    private final static String TAG = "MovieDetailActivity";


    private long mMovieId;
    private Movie mMovie;

    final AsyncHttpClient mClient = new AsyncHttpClient();
    final String GET_MOVIE_DETAIL_PRE_URL = "https://api.themoviedb.org/3/movie/";
    final String GET_MOVIE_DETAIL_POST_URL = "?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    private Toolbar mToolbar;
    private ImageView mPosterImage;
    private TextView mTitle;
    private TextView mReleaseDate;
    private RatingBar mRateBar;
    private TextView mRateText;
    private TextView mOverview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_detail);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mPosterImage = (ImageView) findViewById(R.id.imageView);
        mTitle = (TextView) findViewById(R.id.tvDetailTitle);
        mReleaseDate = (TextView) findViewById(R.id.tvDetailDate);
        mRateBar = (RatingBar) findViewById(R.id.rbRate);
        mRateText = (TextView) findViewById(R.id.tvRate);
        mOverview = (TextView) findViewById(R.id.tvDetailOverview);


        mMovieId = getIntent().getExtras().getLong("movieId");
        String getDetailURL = GET_MOVIE_DETAIL_PRE_URL + String.valueOf(mMovieId) + GET_MOVIE_DETAIL_POST_URL;
        mClient.get(getDetailURL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    mMovie = new Movie(response);
                    tryUpdateDetails();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = mPosterImage.getLayoutParams();
        params.height = (int) (getResources().getDisplayMetrics().widthPixels * 0.75);
        mPosterImage.setLayoutParams(params);
    }

    private void tryUpdateDetails() {
        try {
            Picasso.with(MovieDetailActivity.this).load(mMovie.getBackdropPath())
                    .placeholder(R.drawable.placeholder)
                    .into(mPosterImage);
            mToolbar.setTitle(mMovie.getOriginalTitle());
            mTitle.setText(mMovie.getOriginalTitle());
            mReleaseDate.setText(String.format("Release Date: %s", mMovie.getReleaseDate().toString()));
            mRateBar.setRating((float) (mMovie.getVoteAverage() * 0.5));
            mRateText.setText(String.format("%.1f/10", mMovie.getVoteAverage()));
            mOverview.setText(mMovie.getOverview());
        } catch (Exception ex) {
            Log.d(TAG, "Not able to update yet.");
        }
    }
}
