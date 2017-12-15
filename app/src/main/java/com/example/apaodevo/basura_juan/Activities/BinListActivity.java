package com.example.apaodevo.basura_juan.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.apaodevo.basura_juan.Models.BinModel;
import com.example.apaodevo.basura_juan.R;
import com.example.apaodevo.basura_juan.Services.BinListAdapter;
import com.example.apaodevo.basura_juan.Services.CustomJSONRequest;
import com.example.apaodevo.basura_juan.Services.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by apaodevo on 12/5/2017.
 */



public class BinListActivity extends NavigationDrawerActivity{


     /*Declare variables here*/
     private RecyclerView recyclerView;
     private BinListAdapter binListAdapter;
     private List<BinModel> binList;


     private static final String BIN_LIST_URL = "http://132.223.41.121/bin-list.php";
//     private static String BIN_LIST_URL = "http://basurajuan.x10host.com/bin-list.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_bin_list);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_bin_list, null, false);
        drawer.addView(contentView, 0);

        recyclerView            = (RecyclerView) findViewById(R.id.recycler_view);

        /*LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);*/

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        //
        showBinListItem();
        initializeAdapter();

    }
   /* private void initializeData(){
        binList = new ArrayList<>();
        binList.add(new BinModel("Emma Wilson", "23 years old", "1"));
        binList.add(new BinModel("Lavery Maiss", "25 years old", "2"));
        binList.add(new BinModel("Lillie Watts", "35 years old", "3"));

    }*/
    private void showBinListItem(){
        CustomJSONRequest customJSONRequest = new CustomJSONRequest(Request.Method.POST,BIN_LIST_URL,null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        List<BinModel> list = new Gson().fromJson(response.toString(),
                                new TypeToken<List<BinModel>>(){

                            }.getType());
                        binList.clear();
                        binList.addAll(list);
                        binListAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(customJSONRequest);


    }
    private void initializeAdapter(){
        binListAdapter = new BinListAdapter(BinListActivity.this, binList);
        recyclerView.setAdapter(binListAdapter);
    }

}
