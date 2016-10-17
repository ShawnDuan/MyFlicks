package com.example.sduan.myflicks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.sduan.myflicks.R;
import com.example.sduan.myflicks.Utils;
import com.example.sduan.myflicks.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by sduan on 10/15/16.
 */

public class MovieDetailActivity extends YouTubeBaseActivity {
    private final static String TAG = "MovieDetailActivity";

    private long mMovieId;
    private Movie mMovie;
    private String mTrailerPath;

    final AsyncHttpClient mClient = new AsyncHttpClient();
    final String GET_MOVIE_DETAIL_URL = "https://api.themoviedb.org/3/movie/%d?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    final String GET_TRAILER_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    private Toolbar mToolbar;
    private YouTubePlayerView mYouTubePlayerView;
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
        mYouTubePlayerView = (YouTubePlayerView) findViewById(R.id.videoPlayer);
        mTitle = (TextView) findViewById(R.id.tvDetailTitle);
        mReleaseDate = (TextView) findViewById(R.id.tvDetailDate);
        mRateBar = (RatingBar) findViewById(R.id.rbRate);
        mRateText = (TextView) findViewById(R.id.tvRate);
        mOverview = (TextView) findViewById(R.id.tvDetailOverview);
        mOverview.setMovementMethod(new ScrollingMovementMethod());

        mMovieId = getIntent().getExtras().getLong("movieId");
        String getDetailURL = String.format(GET_MOVIE_DETAIL_URL, mMovieId);
        mClient.get(getDetailURL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    mMovie = new Movie(response);
                    if (mTrailerPath != null && mTrailerPath.length() > 0) {
                        mMovie.setTrailerPath(mTrailerPath);
                    }
                    tryUpdateDetails();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        String getTrailerURL = String.format(GET_TRAILER_URL, mMovieId);
        mClient.get(getTrailerURL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                ArrayList<String> trailerPaths = new ArrayList<>();
                try {
                    JSONArray trailerJsonResults = response.getJSONArray("results");
                    for (int i = 0; i < trailerJsonResults.length(); i++) {
                        JSONObject object = trailerJsonResults.getJSONObject(i);
                        if (object.getString("site").equals("YouTube")) {
                            mTrailerPath = object.getString("key");
                            if (mMovie != null) {
                                mMovie.setTrailerPath(mTrailerPath);
                            }
                            return;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void tryUpdateDetails() {
        try {
            String title = mMovie.getOriginalTitle();
            mToolbar.setTitle(title);
            mTitle.setText(title);
            mReleaseDate.setText(String.format("Release Date: %s", Utils.dateToString(mMovie.getReleaseDate())));
            mRateBar.setRating((float) (mMovie.getVoteAverage() * 0.5));
            mRateText.setText(String.format("%d reviewed", mMovie.getVoteCount()));
            mOverview.setText(mMovie.getOverview());

            mYouTubePlayerView.initialize("AIzaSyCD-FcZQmd2Hb6JC5qwTpcmk43XdwXxOIs",
                    new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                            YouTubePlayer youTubePlayer, boolean b) {

                            // do any work here to cue video, play video, etc.
                            youTubePlayer.cueVideo(mMovie.getTrailerPath());
                        }
                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                            YouTubeInitializationResult youTubeInitializationResult) {

                        }
                    });

        } catch (Exception ex) {
            Log.d(TAG, "Not able to update yet.");
        }
    }
}
