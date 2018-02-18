package com.example.apaodevo.basura_juan.Fragment;


/**
 * Created by Brylle on 2/10/2018.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.apaodevo.basura_juan.Configuration.Keys;
import com.example.apaodevo.basura_juan.Configuration.WebServiceUrl;
import com.example.apaodevo.basura_juan.Models.NotificationModel;
import com.example.apaodevo.basura_juan.R;
import com.example.apaodevo.basura_juan.Services.NotificationAdapter;
import com.example.apaodevo.basura_juan.Services.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by apaodevo on 2/7/2018.
 */

public class BinCapacityFragment extends Fragment {
    private static List<NotificationModel> notificationModelList;

    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    public BinCapacityFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_battery, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.notification_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        notificationModelList = new ArrayList<>();
        shownotificationModelListItem();
        initializeAdapter();



        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    private void initializeAdapter(){
        notificationAdapter = new NotificationAdapter(getContext(), notificationModelList);
        recyclerView.setAdapter(notificationAdapter);
    }


    private void shownotificationModelListItem() {
        notificationModelList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebServiceUrl.NOTIFICATION_URL,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.d("Recycler View Contents", response.toString());
                        List<NotificationModel> items = new Gson().fromJson(response.toString(), new TypeToken<List<NotificationModel>>() {

                        }.getType());
                        Log.d("Passed to RecyclerView", items.toString());
                        notificationModelList.clear();
                        notificationModelList.addAll(items);
                        notificationAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error Bin Capacity"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Keys.TAG_NOTIFICATION_CATEGORY, "Bin Capacity");


                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }



}
