package com.example.apaodevo.basura_juan.Activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.example.apaodevo.basura_juan.Services.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class LoginActivity extends AppCompatActivity {

    public static String LOGIN_URL = "http://basurajuan.x10host.com/login.php";
    protected String enteredUsername, enteredPassword;
    /*
    ** Declare login activity objects
    */
    private Button bregister, blogin;
    private EditText user, pass;
    private ProgressDialog pDialog;
    private String json_response;
    private String image_url, email, username, password, firstName, lastName, middleInitial; //Variables to store json response

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Cast objects
        blogin      = (Button) findViewById(R.id.button);
        bregister   = (Button) findViewById(R.id.button2);
        user  = (EditText) findViewById(R.id.editText);
        pass  = (EditText) findViewById(R.id.editText2);


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
                                            finish();
                                            hidepDialog();
                                        }catch(JSONException e){
                                            e.printStackTrace();
                                        }

                                    }
                                }, 1000);


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
                Toast.makeText(LoginActivity.this, "Could not get data from the server.", Toast.LENGTH_LONG).show();
                error.printStackTrace();
                hidepDialog();
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
