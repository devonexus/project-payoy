package com.example.apaodevo.basura_juan.Activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.apaodevo.basura_juan.Configuration.Keys;
import com.example.apaodevo.basura_juan.Models.BinModel;
import com.example.apaodevo.basura_juan.R;
import com.example.apaodevo.basura_juan.Services.BinListAdapter;

import com.example.apaodevo.basura_juan.Services.CustomJSONRequest;
import com.example.apaodevo.basura_juan.Services.VolleySingleton;
import com.example.apaodevo.basura_juan.Utils.RecyclerItemTouchHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by apaodevo on 12/5/2017.
 */



public class BinListActivity extends NavigationDrawerActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{


    /*Declare variables here*/
    private RecyclerView recyclerView;
    private BinListAdapter binListAdapter;
    private List<BinModel> binList;
    private CoordinatorLayout coordinatorLayout;
    private EditText binSearch;
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
        fab.setVisibility(View.INVISIBLE); // Hide floating action button


        /*
        ** Cast objects here
        */
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        binSearch = (EditText) findViewById(R.id.search_bin);

        //Change as per key press
        binSearch.addTextChangedListener(new SearchBinTextWatcher(binSearch));




        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binList = new ArrayList<>();

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


        //Initialize bin list adapter
        initializeAdapter();

        //Retrieve bin list item
        showBinListItem();


    }

    /* private void initializeData(){
         binList = new ArrayList<>();
         binList.add(new BinModel("Emma Wilson", "23 years old", "1"));
         binList.add(new BinModel("Lavery Maiss", "25 years old", "2"));
         binList.add(new BinModel("Lillie Watts", "35 years old", "3"));

     }*/
    private void showBinListItem() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, BIN_LIST_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Toast.makeText(getApplicationContext(), ""+response.toString(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "Could not get data from server.", Toast.LENGTH_SHORT).show();
                Log.d(Keys.TAG_ERRORS, error.getMessage());
                error.printStackTrace();
            }
        });
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);


    }

    /**
     * Store array yo adapter
     * Set custom adapter for listview
     */

    private void initializeAdapter() {
        binListAdapter = new BinListAdapter(BinListActivity.this, binList);
        recyclerView.setAdapter(binListAdapter);
    }


    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof BinListAdapter.MyViewHolder) {
            // get the removed bin id to display it in snack bar
            final String binId = binList.get(viewHolder.getAdapterPosition()).getBinId();
            String binName = binList.get(viewHolder.getAdapterPosition()).getBinName();
            // backup of removed item for undo purpose
            final BinModel deletedBin = binList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();


            // remove the item from recycler view
            binListAdapter.removeItem(viewHolder.getAdapterPosition());


            CustomJSONRequest customJSONRequest = new CustomJSONRequest(Request.Method.POST, BIN_LIST_URL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(Keys.TAG_BIN_ID, binId);
                    return params;
                }
            };//Remove items from the database
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(customJSONRequest);
            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, binName + " was removed from list!", Snackbar.LENGTH_LONG);
            snackbar.setAction("DELETED", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    binListAdapter.restoreItem(deletedBin, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.GREEN);
            snackbar.show();
        }
    }




    //Triggering function for keypress
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

    void filter(String s){
        List<BinModel> temp = new ArrayList();
        for(BinModel d: binList){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getBinName().contains(s)){
                temp.add(d);
            }
        }
        //update recyclerview
        binListAdapter.updateList(temp);
    }//This is used to filter the list....

}
