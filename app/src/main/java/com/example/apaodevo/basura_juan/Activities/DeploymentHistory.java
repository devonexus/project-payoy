package com.example.apaodevo.basura_juan.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.apaodevo.basura_juan.Configuration.Keys;
import com.example.apaodevo.basura_juan.Models.DeploymentModel;
import com.example.apaodevo.basura_juan.R;
import com.example.apaodevo.basura_juan.Services.DeploymentHistoryAdapter;
import com.example.apaodevo.basura_juan.Services.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Brylle on 1/31/2018.
 */

public class DeploymentHistory extends NavigationDrawerActivity{
    private RecyclerView recyclerView;
    private DeploymentHistoryAdapter deploymentHistoryAdapter;
    private List<DeploymentModel> deploymentList;
    private CoordinatorLayout coordinatorLayout;
    private EditText deploymentSearch;
    private static String DEPLOYMENT_URL = "http://basurajuan.x10host.com/bin-deployment.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeDrawer();
        recyclerView = (RecyclerView) findViewById(R.id.deployment_recycler_view);
        deploymentSearch = (EditText) findViewById(R.id.search_bin_deployment);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.deployment_coordinator_layout);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        deploymentList = new ArrayList<>();

        initializeAdapter();
        showDeployedBinListItem(globalData.getUserid());
    }

    private void showDeployedBinListItem(final String uid){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DEPLOYMENT_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Recycler View Contents", response.toString());
                        List<DeploymentModel> items = new Gson().fromJson(response.toString(), new TypeToken<List<DeploymentModel>>() {

                        }.getType());
                        Log.d("Passed to RecyclerView", items.toString());
                        deploymentList.clear();
                        deploymentList.addAll(items);
                        deploymentHistoryAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Keys.TAG_USER_ID, uid);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }
    private void initializeAdapter() {
        deploymentHistoryAdapter = new DeploymentHistoryAdapter(DeploymentHistory.this, deploymentList);
        recyclerView.setAdapter(deploymentHistoryAdapter);
    }
    private void initializeDrawer(){
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_deployment_history, null, false);
        drawer.addView(contentView, 0);
        fab.setImageResource(R.drawable.floating_navigate_bin);
        fab.setVisibility(View.GONE);
    }
}
