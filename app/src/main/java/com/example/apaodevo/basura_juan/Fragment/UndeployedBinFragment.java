package com.example.apaodevo.basura_juan.Fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import com.example.apaodevo.basura_juan.Services.CustomJSONRequest;
import com.example.apaodevo.basura_juan.Services.VolleySingleton;
import com.example.apaodevo.basura_juan.Utils.RecyclerItemTouchHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UndeployedBinFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{
    private String            binId;
    private RecyclerView      recyclerView;
    private EditText etBinSearch;
    private BinListAdapter    binListAdapter;
    private List<BinModel>    binList;
    public static UserModel   userModel;
    private CoordinatorLayout coordinatorLayout;
    private TextView tvLabel;
    private ImageView imageLabel;
    private boolean shouldRefreshOnResume = false;

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
        tvLabel         = (TextView) rootView.findViewById(R.id.tvMarker);
        coordinatorLayout =  (CoordinatorLayout) rootView.findViewById(R.id.coordinator_undeployed_fragment);
        imageLabel = (ImageView) rootView.findViewById(R.id.image_list_bins);
        etBinSearch = (EditText) rootView.findViewById(R.id.search_bin);
        etBinSearch.addTextChangedListener(new SearchBinTextWatcher(etBinSearch));
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        showBinListItem();

        binListAdapter = new BinListAdapter(getActivity(), binList);
        recyclerView.setAdapter(binListAdapter);
        return rootView;
    }

      private void showBinListItem() {
        binList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebServiceUrl.BIN_LIST_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);
                            if (jsonArray.length() == 0){
                                etBinSearch.setVisibility(View.INVISIBLE);
                                imageLabel.setImageResource(R.drawable.deploy_list);
                                tvLabel.setText("No undeployed bins");
                            }else{
                                Log.d("Recycler View Contents", response.toString());
                                List<BinModel> items = new Gson().fromJson(response.toString(), new TypeToken<List<BinModel>>() {

                                }.getType());
                                Log.d("Passed to RecyclerView", items.toString());
                                binList.clear();
                                binList.addAll(items);
                                binListAdapter.notifyDataSetChanged();
                                etBinSearch.setVisibility(View.VISIBLE);
                                tvLabel.setVisibility(View.INVISIBLE);
                                imageLabel.setVisibility(View.INVISIBLE);
                            }

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

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
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof BinListAdapter.MyViewHolder) {
            // get the removed bin id to display it in snack bar
            binId = binList.get(viewHolder.getAdapterPosition()).getBinId();
            final String binName = binList.get(viewHolder.getAdapterPosition()).getBinName();
            // backup of removed item for undo purpose
            final BinModel deletedBin = binList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
            builder.setCancelable(true);
            builder.setTitle("Delete Bin");
            builder.setMessage("Are you sure you want to delete bin?");
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    showBinListItem();
                    //binListAdapter.restoreItem(deletedBin, deletedIndex);
                }
            });
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    binListAdapter.removeItem(viewHolder.getAdapterPosition());
                    CustomJSONRequest customJSONRequest = new CustomJSONRequest(Request.Method.POST, WebServiceUrl.BIN_LIST_URL, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if(response.getString(Keys.TAG_BIN_UNDEPLOYED).equals("0")){
                                            imageLabel.setVisibility(View.VISIBLE);
                                            tvLabel.setVisibility(View.VISIBLE);
                                            etBinSearch.setVisibility(View.INVISIBLE);
                                            imageLabel.setImageResource(R.drawable.deploy_list);
                                            tvLabel.setText("No undeployed bins");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
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
                            params.put(Keys.TAG_USER_ID, String.valueOf(userModel.getUserId()));
                            return params;
                        }
                    };//Remove items from the database
                    VolleySingleton.getInstance(getActivity()).addToRequestQueue(customJSONRequest);
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
    }


}

