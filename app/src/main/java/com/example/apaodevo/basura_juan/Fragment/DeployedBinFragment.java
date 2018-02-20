package com.example.apaodevo.basura_juan.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.apaodevo.basura_juan.Configuration.Keys;
import com.example.apaodevo.basura_juan.Configuration.WebServiceUrl;
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

/**
 * Created by Brylle on 2/11/2018.
 */

public class DeployedBinFragment extends Fragment{
    private RecyclerView recyclerView;
    private BinListAdapter binListAdapter;
    private List<BinModel> binList;
    public static UserModel userModel;
    private TextView tvLabel;
    private EditText etBinSearch;
    private ImageView imageLabel;
    public DeployedBinFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView  =  inflater.inflate(R.layout.content_bin_list, container, false);
        userModel      = UserModel.getInstance();
        recyclerView   = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        tvLabel        = (TextView) rootView.findViewById(R.id.tvMarker);
        etBinSearch    = (EditText) rootView.findViewById(R.id.search_bin);
        imageLabel     = (ImageView) rootView.findViewById(R.id.image_list_bins);
        etBinSearch.addTextChangedListener(new SearchBinTextWatcher(etBinSearch));
        Toast.makeText(getContext(), "Goes to Deploy bin fragment", Toast.LENGTH_SHORT).show();
        binList = new ArrayList<>();
        showBinListItem();
        binListAdapter = new BinListAdapter(getActivity(), binList);
        recyclerView.setAdapter(binListAdapter);
        return rootView;
    }
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

    private void showBinListItem() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebServiceUrl.BIN_LIST_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(), "Data goes here: "+response.toString(), Toast.LENGTH_SHORT).show();
                        /*JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);

                            if (jsonArray.length() == 0) {
                                etBinSearch.setVisibility(View.INVISIBLE);
                                imageLabel.setImageResource(R.drawable.deploy_list);
                                tvLabel.setText("No deployed bins");
                            } else {*/
                                  Log.d("Recycler View Contents", response.toString());
                                    List<BinModel> items = new Gson().fromJson(response.toString(), new TypeToken<List<BinModel>>() {

                                    }.getType());
                                    Log.d("Passed to RecyclerView", items.toString());
                                    binList.clear();
                                    binList.addAll(items);
                                    binListAdapter.notifyDataSetChanged();
                                    imageLabel.setVisibility(View.INVISIBLE);
                                    tvLabel.setVisibility(View.INVISIBLE);
                            //}
                       /* }
                        catch (JSONException e) {
                            e.printStackTrace();

                        }*/
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Eror: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Keys.TAG_USER_ID, String.valueOf(userModel.getUserId()));
                params.put(Keys.TAG_BIN_STATUS, "Deployed");
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }
    void filter(String s){
        List<BinModel> temp = new ArrayList();
        for(BinModel d: binList){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getBinName().toString().contains(s) || d.getBinName().toLowerCase().contains(s) || d.getBinName().toUpperCase().contains(s)){
                temp.add(d);
            }
        }
        //update recyclerview
        binListAdapter.updateList(temp);
    }//This is used to filter the list....

}
