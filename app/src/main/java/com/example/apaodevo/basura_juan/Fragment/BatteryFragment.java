package com.example.apaodevo.basura_juan.Fragment;

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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.apaodevo.basura_juan.Models.NotificationModel;
import com.example.apaodevo.basura_juan.R;
import com.example.apaodevo.basura_juan.Services.NotificationAdapter;
import com.example.apaodevo.basura_juan.Services.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apaodevo on 2/7/2018.
 */

public class BatteryFragment extends Fragment {
    private static List<NotificationModel> notificationModelList;
    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private static String NOTIFICATION_URL = "http://132.223.41.121/notification-list.php";
    public BatteryFragment() {
        // Required empty public constructor
    }
    public static BatteryFragment newInstance() {
        BatteryFragment fragment = new BatteryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_battery, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.notification_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        notificationModelList = new ArrayList<>();
        initializeAdapter();
        shownotificationModelListItem();

    }
    private void initializeAdapter(){
        notificationAdapter = new NotificationAdapter(getContext(), notificationModelList);
        recyclerView.setAdapter(notificationAdapter);
    }
    private void shownotificationModelListItem() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NOTIFICATION_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(), "Data: "+response.toString(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "Sample "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

    /*private void showUnreadNotifications {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BIN_LIST_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Recycler View Contents", response.toString());
                        List<BinModel> items = new Gson().fromJson(response.toString(), new TypeToken<List<BinModel>>() {

                        }.getType());
                        Log.d("Passed to RecyclerView", items.toString());
                        notificationModelList.clear();
                        notificationModelList.addAll(items);
                        notificationModelListAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Keys.TAG_USER_ID, globalData.getUserid());
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }*/
}
