package com.example.apaodevo.basura_juan.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.android.volley.toolbox.StringRequest;
import com.example.apaodevo.basura_juan.Configuration.Keys;
import com.example.apaodevo.basura_juan.Fragment.BatteryFragment;
import com.example.apaodevo.basura_juan.Fragment.BinCapacityFragment;
import com.example.apaodevo.basura_juan.Fragment.DeployedBinFragment;
import com.example.apaodevo.basura_juan.Fragment.UndeployedBinFragment;
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

public class BinListActivity extends NavigationDrawerActivity{

    /*Declare variables here*/

    private CoordinatorLayout coordinatorLayout;
    private EditText binSearch;
    //private static final String BIN_LIST_URL = "http://172.17.152.98/bin-list.php";

    private String binId;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_bin_list);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_bin_list, null, false);
        drawer.addView(contentView, 0);
        fab.setVisibility(View.INVISIBLE); // Hide floating action


        //Cast objects here

        /*recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        binSearch = (EditText) findViewById(R.id.search_bin);

        //Change as per key press
        binSearch.addTextChangedListener(new SearchBinTextWatcher(binSearch));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //binList = new ArrayList<>();

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);*/

        //Initialize bin list adapter
        //initializeAdapter();
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        setupViewPager(viewPager);
        tabLayout.setTabTextColors(getColor(R.color.colorBlack), getColor(R.color.colorBlack));
        tabLayout.setupWithViewPager(viewPager);
        //setupTabIcons();
        //Retrieve bin list item
        //showBinListItem();

    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new DeployedBinFragment(),  "Deployed Bins");
        adapter.addFragment(new UndeployedBinFragment(), "Undeployed Bins");
        viewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    /*private void showBinListItem() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BIN_LIST_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
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
                params.put(Keys.TAG_USER_ID, globalData.getUserid());
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }*/

    /**
     * Store array yo adapter
     * Set custom adapter for listview
     */

    /*private void initializeAdapter() {
        binListAdapter = new BinListAdapter(BinListActivity.this, binList);
        recyclerView.setAdapter(binListAdapter);
    }*/



    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */
   /* @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof BinListAdapter.MyViewHolder) {
            // get the removed bin id to display it in snack bar
            binId = binList.get(viewHolder.getAdapterPosition()).getBinId();
            final String binName = binList.get(viewHolder.getAdapterPosition()).getBinName();
            // backup of removed item for undo purpose
            final BinModel deletedBin = binList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();


            final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
            builder.setCancelable(true);
            builder.setTitle("Delete Bin");
            builder.setMessage("Are you sure you want to delete bin?");
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    //binListAdapter.restoreItem(deletedBin, deletedIndex);
                    onResume();
                }
            });
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
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
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, binName + " was removed from list!", Snackbar.LENGTH_LONG);
                    snackbar.setAction("DELETED", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    snackbar.setActionTextColor(Color.GREEN);
                    snackbar.show();
                }
            });
            builder.show();


        }


            // remove the item from recycler view

    }*/
    public void showMessage(String title,String Message){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


               /* Thread thread = new Thread() {

                    @Override
                    public void run() {

                        // Block this thread for 4 seconds.al
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // After sleep finished blocking, create a Runnable to run on the UI Thread.
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                intent.putExtra("LOGIN_STATUS", false);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK); // To clean up all activities
                                startActivity(intent);
                                finish();
                                hidepDialog();
                            }
                        });
                    }

                };
                thread.start();*/
            }

        });

        builder.show();
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

          /*  filter(s.toString());*/
        }
    }

    /*void filter(String s){
        List<BinModel> temp = new ArrayList();
        for(BinModel d: binList){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getBinName().toLowerCase().contains(s)){
                temp.add(d);
            }
        }
        //update recyclerview
        binListAdapter.updateList(temp);
    }//This is used to filter the list....*/

    @Override
    protected void onResume() {
        super.onResume();
        //showBinListItem();
    }
}
