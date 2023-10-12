package com.example.women_security_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class showlist extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    private List<petrolpump> movieList;
    double clati=0.0;
    double clongi =0.0;
    String name,opening;
    double rating;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showlist);

        recyclerView = findViewById(R.id.recyclerview);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        requestQueue = VolleySingleton.getmInstance(this).getRequestQueue();

        movieList = new ArrayList<>();

        Intent intent = getIntent();
        clati = intent.getDoubleExtra("latitude",0.0);
        clongi = intent.getDoubleExtra("longitude",0.0);
//        System.out.println("latitude.................."+clati);

        fetchMovies();
//        Toast.makeText(getApplicationContext(), String.valueOf(clati), Toast.LENGTH_SHORT).show();
        SharedPreferences preferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("lati", String.valueOf(clati));
        editor.putString("longi",String.valueOf(clongi));

        editor.apply();
    }

    private void fetchMovies() {

        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                "?location=" + clati + "," + clongi +
                "&radius=5000" +
                "&types="+ "police" +
                "&sensor=true" +
                "&key=" + getResources().getString(R.string.map_key);



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        name = object.getString("name");
                        opening = object.getString("vicinity");
                        rating = object.getDouble("rating");

                        petrolpump petrolpump = new petrolpump(name,opening,rating);
                        movieList.add(petrolpump);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                MoiveAdapter adapter = new MoiveAdapter(showlist.this , movieList);

                recyclerView.setAdapter(adapter);


            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
        });



        requestQueue.add(jsonObjectRequest);


    }


}