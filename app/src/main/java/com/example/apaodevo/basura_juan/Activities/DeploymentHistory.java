package com.example.apaodevo.basura_juan.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.apaodevo.basura_juan.Configuration.WebServiceUrl;
import com.example.apaodevo.basura_juan.Models.BinModel;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_deployment_history, null, false);
        drawer.addView(contentView, 0);
        fab.setVisibility(View.INVISIBLE);
        recyclerView = (RecyclerView) findViewById(R.id.deployment_recycler_view);
        deploymentSearch = (EditText) findViewById(R.id.search_bin_deployment);
        deploymentSearch.addTextChangedListener(new SearchBinTextWatcher(deploymentSearch));
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.deployment_coordinator_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        deploymentList = new ArrayList<>();
        initializeAdapter();
        showDeployedBinListItem(globalData.getUserid());
    }

    private void showDeployedBinListItem(final String uid){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebServiceUrl.DEPLOYMENT_URL,
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

    void filter(String s){
        List<DeploymentModel> temp = new ArrayList();
        for(DeploymentModel d: deploymentList){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getBinName().toString().contains(s) || d.getBinName().toLowerCase().contains(s) || d.getBinName().toUpperCase().contains(s)){
                temp.add(d);
            }
        }
        //update recyclerview
        deploymentHistoryAdapter.updateList(temp);
    }//This is used to filter the list....
    private class SearchBinTextWatcher implements TextWatcher {
        private View view;
        private SearchBinTextWatcher(View view) {
            this.view = view;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            filter(s.toString());
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        showDeployedBinListItem(globalData.getUserid());
    }
}
