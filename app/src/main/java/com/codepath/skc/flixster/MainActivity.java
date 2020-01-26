package com.codepath.skc.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.skc.flixster.adapters.MovieAdapter;
import com.codepath.skc.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import com.codepath.skc.flixster.R;
import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String NOW_PLAYING_URL="https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public  static final String TAG="MainActivity";
    List <Movie> movies;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rvMovies=findViewById(R.id.rvItems);
        movies=new ArrayList<>();
        //create adapter
       final MovieAdapter movieAdapter=new MovieAdapter(this,movies);

        //set the adapter on the recylcerview
        rvMovies.setAdapter(movieAdapter);

        //set a layout manager on the recyler view
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        AsyncHttpClient client=new AsyncHttpClient();
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                 Log.d(TAG,"onSucess");
                 JSONObject jsonObject=json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG,"Results"+results.toString());
                    movies.addAll(Movie.fromJsonArray(results));
                    movieAdapter.notifyDataSetChanged();
                    Log.i(TAG,"Movies"+movies.size());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG,"Hit json exception",e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG,"onFaliure");
            }
        });
    }
}
