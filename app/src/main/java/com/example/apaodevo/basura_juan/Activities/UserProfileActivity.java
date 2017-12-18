package com.example.apaodevo.basura_juan.Activities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.apaodevo.basura_juan.Configuration.Keys;
import com.example.apaodevo.basura_juan.R;
import com.example.apaodevo.basura_juan.Services.CustomJSONRequest;
import com.example.apaodevo.basura_juan.Services.GlobalData;
import com.example.apaodevo.basura_juan.Services.PathUtil;
import com.example.apaodevo.basura_juan.Services.VolleySingleton;
import com.kosalgeek.android.imagebase64encoder.ImageBase64;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by apaodevo on 11/16/2017.
 */


public class UserProfileActivity extends NavigationDrawerActivity{
    private GlobalData globalData;
    private TextView etlname, etfname, etminitial, etuname, etpword, etemail;
    private Button btn_update;
    private ImageView imageView;
    private Uri uri;
    public static String UPDATE_USER_URL= "http://132.223.41.121/update-user.php"; //WEB Service URL
    private static int RESULT_LOAD_IMAGE = 1;
    private String image_path, displayName, encodedImage;
    private static String lastName, firstName, middleInitial, email, userName, passWord, imageUrl; /*Store user profile data*/
    private TextInputLayout inputFname, inputLname, inputMinitial, inputUsername, inputPassword, inputEmail;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set up navigation drawer
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_user_profile, null, false);
        drawer.addView(contentView, 0);
        fab.setVisibility(View.INVISIBLE);
        //castObjects();
        if (shouldAskPermissions()) {
            askPermissions();
        }
        globalData = (GlobalData) getApplicationContext();
        btn_update  = (Button) findViewById(R.id.btn_update);
        etlname     = (EditText) findViewById(R.id.etLname);
        etfname     = (EditText) findViewById(R.id.etFname);
        etuname     = (EditText) findViewById(R.id.etUname);
        etemail     = (EditText) findViewById(R.id.etEmail);
        etminitial  = (EditText) findViewById(R.id.etMinitial);
        etpword     = (EditText) findViewById(R.id.etPword);
        imageView   = (ImageView) findViewById(R.id.img_login_user);
        inputFname      = (TextInputLayout) findViewById(R.id.input_fname);
        inputLname      = (TextInputLayout) findViewById(R.id.input_lname);
        inputMinitial   = (TextInputLayout) findViewById(R.id.input_minitial);
        inputUsername   = (TextInputLayout) findViewById(R.id.input_uname);
        inputPassword   = (TextInputLayout) findViewById(R.id.input_pword);
        inputEmail      = (TextInputLayout) findViewById(R.id.input_email);
        GradientDrawable sd = (GradientDrawable) btn_update.getBackground();
        sd.setColor(Color.rgb(199, 0, 57));

        /* Get data from loggedin user*/
        lastName        = globalData.getLastname();
        firstName       = globalData.getFirstname();
        middleInitial   = globalData.getMinitial();
        email           = globalData.getEmailAddress();
        userName        = globalData.getUsername();
        passWord        = globalData.getPassword();
        imageUrl        = globalData.getImageUrl();

        etlname.setText(lastName);
        etfname.setText(firstName);
        etminitial.setText(middleInitial);
        etemail.setText(email);
        etuname.setText(userName);
        etpword.setText(passWord);

        Picasso.with(this).load(imageUrl)
                .transform(new CropCircleTransformation())
                .into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromAlbum();
            }
        });

        /* This updates the user profile*/


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Updating user profile...");
        pDialog.setCancelable(false);



        btn_update.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       /* && etfname.equals(firstName) && etminitial.equals(middleInitial) &&
                                etemail.equals(email) && etuname.equals(userName) && etpword.equals(passWord)*/
                        if(etlname.getText().toString().equals(lastName) && etfname.getText().toString().equals(firstName)){
                            Toast.makeText(getApplicationContext(), "No changes made.", Toast.LENGTH_SHORT).show();
                        }else{
                            try {
                                encodedImage = ImageBase64
                                        .with(getApplicationContext())
                                        .requestSize(60, 60)
                                        .encodeFile(image_path);
                            } catch (FileNotFoundException e) {
                                Log.d("Error:", e.getMessage());
                                e.printStackTrace();
                            }
                            updateUserProfile(etlname.getText().toString(),
                                                etfname.getText().toString(),
                                                etminitial.getText().toString(),
                                                etemail.getText().toString(),
                                                etuname.getText().toString(),
                                                etpword.getText().toString(),
                                                encodedImage, displayName);


                           // Toast.makeText(getApplicationContext(), "You have updated"+image_path, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );//Check changes for user profile, update else no changes made
    }

    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE) {

                uri = data.getData();
                image_path = PathUtil.getPathFromURI(this, uri);
                File f = new File(uri.getPath());



                displayName = null;
                Picasso.with(this).load(uri).into(imageView);
                if (uri.toString().startsWith("content://")) {
                    Cursor cursor = null;
                    try {
                        cursor = this.getContentResolver().query(uri, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        }
                    } finally {
                        cursor.close();
                    }
                } else if (uri.toString().startsWith("file://")) {
                    displayName = f.getName();
                }

            }

        }


    }

    private void getImageFromAlbum(){
        try{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, RESULT_LOAD_IMAGE);

        }catch(Exception e){
            Log.i("Error", e.toString());
        }
    }
    private void updateUserProfile(final String lastName, final String firstName, final String middleInitial, final String email, final String uName, final String pWord, final String imagePath, final String imageName){
        showpDialog();
        final CustomJSONRequest customJSONRequest = new CustomJSONRequest(Request.Method.POST, UPDATE_USER_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                String server_response;
                try {
                    server_response = response.getString(Keys.TAG_SUCCESS);

                    if(server_response.equals("0")){
                        Thread thread = new Thread() {
                            @Override
                            public void run() {
                                // Block this thread for 1.5 seconds.
                                try {
                                    Thread.sleep(1500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                // After sleep finished blocking, create a Runnable to run on the UI Thread.
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        etuname.setError("Username does not exist");
                                        hidepDialog();
                                    }
                                });

                            }

                        };
                        thread.start();
                    } else if(server_response.equals("1")){
                        Thread thread = new Thread() {

                            @Override
                            public void run() {

                                // Block this thread for 1.5 seconds.
                                try {
                                    Thread.sleep(1500);
                                } catch (InterruptedException e) {
                                }

                                // After sleep finished blocking, create a Runnable to run on the UI Thread.
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try{
                                        Toast.makeText(getApplicationContext(),
                                                "User successfully updated",
                                                Toast.LENGTH_LONG).show();
                                                etfname.setText(response.getString(Keys.TAG_FNAME));
                                                etlname.setText(response.getString(Keys.TAG_LNAME));
                                                etminitial.setText(response.getString(Keys.TAG_MINITIAL));
                                                etemail.setText(response.getString(Keys.TAG_USER_EMAIL));
                                                etpword.setText(response.getString(Keys.TAG_PWORD));
                                                etuname.setText(response.getString(Keys.TAG_USERNAME));
                                                Picasso.with(getApplicationContext())
                                                    .load(response.getString(Keys.TAG_IMAGE_URL))
                                                    .transform(new CropCircleTransformation())
                                                    .into(imageView);

                                                /*Update data for new users*/
                                                globalData.setUsername(response.getString(Keys.TAG_USERNAME));
                                                globalData.setPassword(response.getString(Keys.TAG_PWORD));
                                                globalData.setFirstname(response.getString(Keys.TAG_FNAME));
                                                globalData.setLastname(response.getString(Keys.TAG_LNAME));
                                                globalData.setEmailAddress(response.getString(Keys.TAG_USER_EMAIL));
                                                globalData.setMiddleInitial(response.getString(Keys.TAG_MINITIAL));
                                                globalData.setImageUrl(response.getString(Keys.TAG_IMAGE_URL));
                                        hidepDialog();
                                        }catch (JSONException e){
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            }

                        };
                        thread.start();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserProfileActivity.this, "Could not get data from server.", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
                hidepDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Keys.TAG_LASTNAME, lastName);
                params.put(Keys.TAG_FIRSTNAME, firstName);
                params.put(Keys.TAG_MIDDLE_INITIAL, middleInitial);
                params.put(Keys.TAG_EMAIL, email);
                params.put(Keys.TAG_USERNAME, uName);
                params.put(Keys.TAG_PASSWORD, pWord);
                params.put(Keys.TAG_USER_IMAGE, imagePath);
                params.put(Keys.TAG_USER_IMAGE_NAME, imageName);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(customJSONRequest);
    }
    
    private void clearUserProfileActivity(){
        etfname.setText("");
        etlname.setText("");
        etemail.setText("");
        etpword.setText("");


        etuname.setText("");
        etminitial.setText("");

        etuname.setError(null);
        etpword.setError(null);
        etemail.setError(null);
        etminitial.setError(null);
        etfname.setError(null);
        etlname.setError(null);
        imageView.setImageResource(android.R.color.transparent);
    }
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
