package com.example.apaodevo.basura_juan.Fragment;

import android.os.Bundle;
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
import com.example.apaodevo.basura_juan.Models.BinModel;
import com.example.apaodevo.basura_juan.Models.UserModel;
import com.example.apaodevo.basura_juan.R;
import com.example.apaodevo.basura_juan.Services.BinListAdapter;
import com.example.apaodevo.basura_juan.Services.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.apaodevo.basura_juan.Activities.MapsActivity.globalData;

/**
 * Created by Brylle on 2/11/2018.
 */

public class UndeployedBinFragment extends Fragment {

    private RecyclerView recyclerView;
    private BinListAdapter binListAdapter;
    private List<BinModel> binList;
    public static UserModel userModel;
    private static String BIN_LIST_URL = "http://192.168.1.19/basura_juan/bin-list.php";
    public UndeployedBinFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View rootView =  inflater.inflate(R.layout.content_bin_list, container, false);
        userModel = UserModel.getInstance();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        showBinListItem();
        binListAdapter = new BinListAdapter(getActivity(), binList);
        recyclerView.setAdapter(binListAdapter);

        return rootView;
    }

      private void showBinListItem() {
        binList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BIN_LIST_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), ""+response.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("Recycler View Contents", response.toString());
                        List<BinModel> items = new Gson().fromJson(response.toString(), new TypeToken<List<BinModel>>() {

                        }.getType());
                        Log.d("Passed to RecyclerView", items.toString());
                        binList.clear();
                        binList.addAll(items);
                        binListAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Keys.TAG_USER_ID, String.valueOf(userModel.getUserId()));
                params.put(Keys.TAG_BIN_STATUS, "Undeployed");
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }
}

