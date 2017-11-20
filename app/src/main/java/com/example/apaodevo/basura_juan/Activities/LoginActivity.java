package com.example.apaodevo.basura_juan.Activities;


import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.apaodevo.basura_juan.Configuration.Keys;
import com.example.apaodevo.basura_juan.R;
import com.example.apaodevo.basura_juan.Services.CustomJSONRequest;
import com.example.apaodevo.basura_juan.Services.GlobalData;
import com.example.apaodevo.basura_juan.Services.JSONParser;
import com.example.apaodevo.basura_juan.Services.VolleySingleton;


public class LoginActivity extends AppCompatActivity {

    //Declare objects static String json_response;
    private Button bregister, blogin;
    private EditText user, pass;
    private ProgressDialog pDialog;
    protected String enteredUsername, enteredPassword;
    private String json_response;
    private JSONParser jsonParser = new JSONParser();


    //JSON Responses
    private int success;
    private String image_url, email, response, username, password, firstName, lastName, middleInitial;
    public static String LOGIN_URL = "http://132.223.41.121/login.php";
//    public static String LOGIN_URL = "http://basurajuan.x10host.com/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Cast objects
        blogin      = (Button) findViewById(R.id.button);
        bregister   = (Button) findViewById(R.id.button2);
        user  = (EditText) findViewById(R.id.editText);
        pass  = (EditText) findViewById(R.id.editText2);

        //Set runtime color for buttons
        GradientDrawable sd = (GradientDrawable) blogin.getBackground();
        sd.setColor(Color.rgb(0, 191, 255));

        GradientDrawable sd1 = (GradientDrawable) bregister.getBackground();
        sd1.setColor(Color.rgb(199, 0, 57));

        GradientDrawable sd2 = (GradientDrawable) user.getBackground();
        sd2.setColor(Color.WHITE);

        GradientDrawable sd3 = (GradientDrawable) pass.getBackground();
        sd3.setColor(Color.WHITE);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Signing in, Please wait...");
        pDialog.setCancelable(false);

        bregister.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), RegisterUserActivity.class));

                    }
                }
        );//Redirects to user registration activity
        blogin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        enteredUsername = user.getText().toString().trim();
                        enteredPassword = pass.getText().toString().trim();

                        if (enteredUsername.equals("") && enteredPassword.equals("")) {
                            user.setError("Enter username");
                            pass.setError("Enter password");
                            requestFocus(user);
                        } else if(enteredUsername.equals("")){
                            //showMessage("Error", "Input username.");
                            user.setError("Enter username");
                            requestFocus(user);
                        } else if(enteredPassword.equals("")){
                            pass.setError("Enter password");
                            requestFocus(pass);
                        }else {
                            /*AttemptLogin attemptLogin = new AttemptLogin();
                            attemptLogin.execute(enteredUsername, enteredPassword);*/
                            loginUser(enteredUsername, enteredPassword);
                        }

                    }
                }
        ); //Redirect to Home Activity otherwise remain

    }

    private void loginUser(final String uname , final String pword){
        showpDialog();
        CustomJSONRequest customJSONRequest = new CustomJSONRequest(Request.Method.POST, LOGIN_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {

                        String response_success = null;
                        try {
                            response_success = response.getString(Keys.TAG_SUCCESS);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(response_success.equals("0")){

                            Thread thread = new Thread() {

                                @Override
                                public void run() {

                                    // Block this thread for 4 seconds.al
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    // After sleep finished blocking, create a Runnable to run on the UI Thread.
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            pass.setError("Invalid password");
                                            hidepDialog();
                                        }
                                    });

                                }

                            };
                            thread.start();

                        }else if(response_success.equals("1")){

                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        try {
                                            json_response = response.getString(Keys.TAG_FULLNAME);
                                            image_url = response.getString(Keys.TAG_IMAGE_URL);
                                            email = response.getString(Keys.TAG_USER_EMAIL);
                                            username = response.getString(Keys.TAG_USERNAME);
                                            firstName = response.getString(Keys.TAG_FNAME);
                                            lastName = response.getString(Keys.TAG_LNAME);
                                            middleInitial = response.getString(Keys.TAG_MINITIAL);
                                            password = response.getString(Keys.TAG_PWORD);

                                            final GlobalData globalData = (GlobalData) getApplicationContext();
                                            globalData.setSomeVariable(json_response);
                                            globalData.setImageUrl(image_url);
                                            globalData.setEmailAddress(email);

                                            globalData.setUsername(username);
                                            globalData.setMiddleInitial(middleInitial);
                                            globalData.setFirstname(firstName);
                                            globalData.setLastname(lastName);
                                            globalData.setPassword(pword);
                                            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                            startActivity(i);
                                            hidepDialog();
                                        }catch(JSONException e){
                                            e.printStackTrace();
                                        }

                                    }
                                }, 1500);


                        }else{
                            Thread thread = new Thread() {

                                @Override
                                public void run() {

                                    // Block this thread for 4 seconds.al
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    // After sleep finished blocking, create a Runnable to run on the UI Thread.
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            user.setError("Invalid username");
                                            pass.setError("Invalid password");
                                            hidepDialog();
                                        }
                                    });

                                }

                            };
                            thread.start();

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
                params.put(Keys.TAG_USERNAME, uname);
                params.put(Keys.TAG_PASSWORD, pword);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(customJSONRequest);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }//Focus the cursor to where the error originated
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }//Display progress dialog

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }//Dismiss progressDialog
}
