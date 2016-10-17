package com.example.sduan.myflicks;

import android.os.Bundle;

import com.example.sduan.myflicks.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by sduan on 10/16/16.
 */

public class TrailerFullScreenPlayActivity extends YouTubeBaseActivity {
    private static final String TAG = "TrailerFullScreenPlayAct";

    private long mMovieId;
    private String mTrailerPath;

    final AsyncHttpClient mClient = new AsyncHttpClient();
    final String GET_TRAILER_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    private YouTubePlayerView mYouTubePlayerView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_full_screen_play);

        mYouTubePlayerView = (YouTubePlayerView) findViewById(R.id.fullScreenPlayer);

        mMovieId = getIntent().getExtras().getLong("movieId");

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

                            mYouTubePlayerView.initialize("AIzaSyCD-FcZQmd2Hb6JC5qwTpcmk43XdwXxOIs",
                                    new YouTubePlayer.OnInitializedListener() {
                                        @Override
                                        public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                                            YouTubePlayer youTubePlayer, boolean b) {

                                            // do any work here to cue video, play video, etc.
                                            if (mTrailerPath != null) {
                                                youTubePlayer.loadVideo(mTrailerPath);
                                            }
                                        }
                                        @Override
                                        public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                                            YouTubeInitializationResult youTubeInitializationResult) {

                                        }
                                    });
                            return;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }


}
