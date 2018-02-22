package com.example.apaodevo.basura_juan.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.example.apaodevo.basura_juan.Configuration.Keys;
import com.example.apaodevo.basura_juan.Configuration.WebServiceUrl;
import com.example.apaodevo.basura_juan.R;
import com.example.apaodevo.basura_juan.Services.CustomJSONRequest;
import com.example.apaodevo.basura_juan.Services.GlobalData;
import com.example.apaodevo.basura_juan.Services.VolleySingleton;
import com.example.apaodevo.basura_juan.Utils.Refresher;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterBin extends NavigationDrawerActivity implements View.OnClickListener{
    //public static String BIN_REG_URL = "http://192.168.43.163/bin-registration.php";
    private EditText  etBinName;
    private Button btn_register_bin;
    private ProgressDialog pDialog;
    private String ip_address, bin_name;
    private String logged_in_user_id = ""; //Id of the current user
    private String selectedBinId = "";
    /*private String ip_address, bin_name;*/ /* Variables  to store post data*/
    GlobalData globalData;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set up navigation drawer
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_register_bin, null, false);
        drawer.addView(contentView, 0);
        fab.setImageResource(R.drawable.floating_action_register_bin);
        fab.setVisibility(View.GONE);
        castObjects();

        globalData = (GlobalData) getApplicationContext();
        logged_in_user_id = globalData.getUserid();
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        bundle      = getIntent().getExtras();
        if(bundle != null){
            if(bundle.get(Keys.TAG_BIN_UPDATE).toString().equals("Update")){
                setTitle(getString(R.string.edit_bin));
                etBinName.setText(bundle.get(Keys.TAG_BIN_NAME).toString());
                selectedBinId = bundle.get(Keys.TAG_BIN_ID).toString();
                btn_register_bin.setText(getString(R.string.update_bin));
            }
        }

        btn_register_bin.setOnClickListener(this);

        final FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setBackgroundDrawable(R.drawable.floating_action_register_bin)
                .build();
        int blueSubActionButtonSize;
        blueSubActionButtonSize = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_size);

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        FrameLayout.LayoutParams ContentParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        itemBuilder.setLayoutParams(ContentParams);
        // Set custom layout params
        FrameLayout.LayoutParams conParams = new FrameLayout.LayoutParams(blueSubActionButtonSize,blueSubActionButtonSize);
        itemBuilder.setLayoutParams(conParams);
        // repeat many times:
        ImageView itemIcon1 = new ImageView(this);
        itemIcon1.setImageResource(R.drawable.bin_location_icon);

        ImageView itemIcon2 = new ImageView(this);
        itemIcon2.setImageResource(R.drawable.deploy);

        ImageView itemIcon3 = new ImageView(this);
        itemIcon3.setImageResource(R.drawable.floating_navigate_bin);

        final SubActionButton sabLocateBin = itemBuilder
                .setContentView(itemIcon1)
                .build();
        final SubActionButton sabDeployBin = itemBuilder.setContentView(itemIcon2).build();
        final SubActionButton sabNavigateBin = itemBuilder.setContentView(itemIcon3).build();
        //attach the sub buttons to the main button
        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(sabLocateBin)
                .addSubActionView(sabDeployBin)
                .addSubActionView(sabNavigateBin)
                .attachTo(actionButton)
                .build();

        sabNavigateBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DeviceList.btSocket == null) {
                    startActivity(new Intent(getApplicationContext(), DeviceList.class));
                }
                else
                {
                    startActivity(new Intent(getApplicationContext(), NavigateBin.class));
                }
            }
        });

        sabLocateBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            }
        });
        sabDeployBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DeviceList.btSocket != null)
                {
                    globalData.msg("Please disconnect currently connected bin");
                }
                else {
                    startActivity(new Intent(getApplicationContext(), DeployBinActivity.class));
                }
            }
        });

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                actionButton.setVisibility(View.INVISIBLE);
                sabLocateBin.setVisibility(View.INVISIBLE);
                sabNavigateBin.setVisibility(View.INVISIBLE);
                sabDeployBin.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onDrawerOpened(View drawerView) {

            }
            @Override
            public void onDrawerClosed(View drawerView) {
                actionButton.setVisibility(View.VISIBLE);
                sabLocateBin.setVisibility(View.VISIBLE);
                sabNavigateBin.setVisibility(View.VISIBLE);
                sabDeployBin.setVisibility(View.VISIBLE);
            }
            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }

    public void castObjects(){
        etBinName   = (EditText) findViewById(R.id.et_bin_name);
        btn_register_bin    = (Button) findViewById(R.id.btn_reg_bin);
    }

    private void createBin() {
        pDialog.setMessage("Creating bin...");
        showpDialog();
        CustomJSONRequest request = new CustomJSONRequest(Request.Method.POST, WebServiceUrl.BIN_REG_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    final String server_response = response.getString(Keys.TAG_SUCCESS);
                    Thread thread = new Thread() {
                            @Override
                            public void run() {
                                // Block this thread for 4 seconds.al
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                // After sleep finished blocking, create a Runnable to run on the UI Thread.
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(server_response.equals("1")){
                                            //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                                            MDToast mdToast = MDToast.makeText(getApplicationContext(), "Bin successfully registered", MDToast.LENGTH_SHORT, MDToast.TYPE_INFO);
                                            mdToast.show();
                                            etBinName.setText("");
                                            hidepDialog();
                                        }else if(server_response.equals("0")){
                                            etBinName.setError(getString(R.string.err_bin_name));
                                            hidepDialog();
                                        }
                                    }
                                });
                            }
                        };
                        thread.start();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error.", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
                hidepDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Keys.TAG_BIN_NAME, etBinName.getText().toString());
                params.put(Keys.TAG_USER_ID, logged_in_user_id);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private boolean validateBinName() {
        bin_name = etBinName.getText().toString().trim();

        if (bin_name.isEmpty()) {
            etBinName.setError(getString(R.string.err_msg_bin_name));
            requestFocus(etBinName);
            return false;
        } else {
            etBinName.setError(null);
        }

        return true;
    }//Validate ip_address
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }  //Focus

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_reg_bin:
                if(btn_register_bin.getText().toString().equals("Register Bin")){

                    if(!validateBinName()){
                        return;
                    }
                    createBin();
                }else if (btn_register_bin.getText().toString().equals(getString(R.string.update_bin))){
                    updateBin(selectedBinId, etBinName.getText().toString());
                                    }
            break;
        }
    }

    private void updateBin(final String binId, final String binName){
        showpDialog();
        pDialog.setMessage("Updating bin");
        CustomJSONRequest customJSONRequest = new CustomJSONRequest(Request.Method.POST, WebServiceUrl.BIN_REG_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            final String server_response = response.getString(Keys.TAG_SUCCESS);
                            Thread thread = new Thread() {
                                @Override
                                 public void run() {
                                    // Block this thread for 4 seconds.al
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    // After sleep finished blocking, create a Runnable to run on the UI Thread.
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(server_response.equals("1")){
                                                MDToast mdToast = MDToast.makeText(getApplicationContext(), "Bin successfully updated", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
                                                mdToast.show();
                                                VolleySingleton.getInstance(getApplicationContext()).cancelPendingRequests("TAG");
                                                etBinName.setText("");
                                                hidepDialog();
                                                finish();
                                                startActivity(new Intent(getApplicationContext(), BinListActivity.class));

                                            }else if(server_response.equals("0")){
                                                    etBinName.setError(getString(R.string.err_bin_name));
                                                    hidepDialog();
                                            }
                                        }
                                    });
                                }
                            };
                            thread.start();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                hidepDialog();
                VolleySingleton.getInstance(getApplicationContext()).cancelPendingRequests("TAG");
                Log.d(Keys.TAG_ERRORS, error.getMessage());
                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Keys.TAG_BIN_ID, binId);
                params.put(Keys.TAG_BIN_NAME, binName);
                params.put(Keys.TAG_USER_ID, globalData.getUserid());
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(customJSONRequest);
        // showing snack bar with Undo opt
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
