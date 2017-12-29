package com.example.apaodevo.basura_juan.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.apaodevo.basura_juan.Models.BinModel;
import com.example.apaodevo.basura_juan.R;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;


public class DeployBinActivity extends NavigationDrawerActivity{

    private static String BIN_REG_URL = "http://132.223.41.121/bin-registration.php";
    private Button btnDeploy;
    private Spinner dropdown;
    private ArrayList<String> binNames= new ArrayList<>();
    ArrayList<BinModel> world;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set up navigation drawer
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_deploy_bin, null, false);
        drawer.addView(contentView, 0);
    
        //get the spinner from the xml.
        dropdown = (Spinner)findViewById(R.id.spinner1);
        btnDeploy   = (Button) findViewById(R.id.btn_deploy);
        //create a list of items for the spinner.
        //String[] items = new String[]{"Bin 1", "Bin 2", "Bin 3"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        //dropdown.setAdapter(adapter);
        fab.setImageResource(R.drawable.deploy);
        fab.setVisibility(View.GONE);
        //loadBinNames();

        final FloatingActionButton actionButton = new FloatingActionButton.Builder(this)

                .setBackgroundDrawable(R.drawable.deploy)
                .build();
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        // repeat many times:
        ImageView itemIcon1 = new ImageView(this);
        itemIcon1.setImageResource(R.drawable.floating_navigate_bin);

        ImageView itemIcon2 = new ImageView(this);
        itemIcon2.setImageResource(R.drawable.bin_location_icon);

        ImageView itemIcon3 = new ImageView(this);
        itemIcon3.setImageResource(R.drawable.floating_action_register_bin);

        final SubActionButton sabNavigateBin = itemBuilder
                .setContentView(itemIcon1)
                .build();
        final SubActionButton sabLocateBin = itemBuilder.setContentView(itemIcon2).build();
        final SubActionButton sabRegisterBin = itemBuilder.setContentView(itemIcon3).build();

        //attach the sub buttons to the main button


        final FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(sabNavigateBin)
                .addSubActionView(sabLocateBin)
                .addSubActionView(sabRegisterBin)
                .attachTo(actionButton)
                .build();

        sabNavigateBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NavigateBin.class));
            }
        });

        sabLocateBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BinLocationActivity.class));
            }
        });
        sabRegisterBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterBin.class));
            }
        });
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                actionButton.setVisibility(View.INVISIBLE);
                sabLocateBin.setVisibility(View.INVISIBLE);
                sabNavigateBin.setVisibility(View.INVISIBLE);
                sabRegisterBin.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onDrawerOpened(View drawerView) {


            }

            @Override
            public void onDrawerClosed(View drawerView) {
                actionButton.setVisibility(View.VISIBLE);
                sabLocateBin.setVisibility(View.VISIBLE);
                sabNavigateBin.setVisibility(View.VISIBLE);
                sabRegisterBin.setVisibility(View.VISIBLE);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    /*private void loadBinNames() {

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, BIN_REG_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        String json = "";

                        for(int i=0; i<response.length(); i++){

                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                //binNames.add(jsonObject.getString(Keys.TAG_BIN_NAME));
                              *//*  BinModel binModel = new BinModel();
                                binModel.setBinName(jsonObject.optString(Keys.TAG_BIN_NAME));
                                world.add(binModel);*//*
                                //json = jsonObject.getString(Keys.TAG_BIN_NAME);
                                binNames.add(jsonObject.getString(Keys.TAG_BIN_NAME));
                                Log.d("XXX", jsonObject.getString(Keys.TAG_BIN_NAME));
                                //binNames.add(jsonObject.optString(Keys.TAG_BIN_NAME));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(DeployBinActivity.this, android.R.layout.simple_spinner_dropdown_item, binNames);
                        dropdown.setAdapter(adapter);
                        //dropdown.setAdapter(new ArrayAdapter<String>(DeployBinActivity.this, android.R.layout.simple_spinner_dropdown_item, binNames));


                       *//* Log.d("Recycler View Contents", response.toString());
                        List<BinModel> items = new Gson().fromJson(response.toString(), new TypeToken<List<BinModel>>() {

                        }.getType());*//*

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


    }*/
}
