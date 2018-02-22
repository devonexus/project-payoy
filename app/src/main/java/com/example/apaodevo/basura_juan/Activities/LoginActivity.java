package com.example.apaodevo.basura_juan.Activities;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
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
import com.example.apaodevo.basura_juan.Configuration.WebServiceUrl;
import com.example.apaodevo.basura_juan.Models.UserModel;
import com.example.apaodevo.basura_juan.R;
import com.example.apaodevo.basura_juan.Services.CustomJSONRequest;
import com.example.apaodevo.basura_juan.Services.GlobalData;
import com.example.apaodevo.basura_juan.Services.VolleySingleton;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class LoginActivity extends AppCompatActivity {



    protected String enteredUsername, enteredPassword;
    /*
    ** Declare login activity objects
    */
    private Button bregister, blogin;
    private EditText user, pass;
    private ProgressDialog pDialog;
    private String json_response;
    private String image_url, email, username, password, firstName, lastName, middleInitial; //Variables to store json response
    private int userId;
    public static UserModel userModel;
    private String cancellationTag = "TAG";
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /*Styling fonts*/

        //Cast objects
        blogin      = (Button) findViewById(R.id.btnNotifRead);
        bregister   = (Button) findViewById(R.id.button2);
        user  = (EditText) findViewById(R.id.editText);
        pass  = (EditText) findViewById(R.id.editText2);



        /*Initialize Progress Dialog*/
        pDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        pDialog.setTitle("Processing");
        pDialog.setMessage("Signing in...");
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);
        pDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                VolleySingleton.getInstance(getApplicationContext()).cancelPendingRequests(cancellationTag);
                Toast.makeText(getApplicationContext(), "Login request is cancelled.", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

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
                        } else {
                            if (enteredPassword.equals("")) {
                                pass.setError("Enter password");
                                requestFocus(pass);
                            } else {
                            /*AttemptLogin attemptLogin = new AttemptLogin();
                            attemptLogin.execute(enteredUsername, enteredPassword);*/
                                loginUser(enteredUsername, enteredPassword);
                            }
                        }
                    }
                }
        ); //Redirect to Home Activity otherwise remain

        pass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pass.setTransformationMethod(new PasswordTransformationMethod());
        userModel = UserModel.getInstance();
    }

    private void loginUser(final String uname , final String pword){

        showpDialog();
        CustomJSONRequest customJSONRequest = new CustomJSONRequest(Request.Method.POST, WebServiceUrl.LOGIN_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {

                        String response_success = null;
                        try {
                            response_success = response.getString(Keys.TAG_SUCCESS);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (response_success.equals("0")) {

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
                                            MDToast mdToast = MDToast.makeText(getApplicationContext(), "Invalid password", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                                            mdToast.show();
                                            requestFocus(pass);
                                            hidepDialog();
                                        }
                                    });
                                }
                            };
                            thread.start();

                        } else if (response_success.equals("1")) {
                            try {
                                json_response = response.getString(Keys.TAG_FULLNAME);
                                image_url = response.getString(Keys.TAG_IMAGE_URL);
                                email = response.getString(Keys.TAG_USER_EMAIL);
                                username = response.getString(Keys.TAG_USERNAME);
                                firstName = response.getString(Keys.TAG_FNAME);
                                lastName = response.getString(Keys.TAG_LNAME);
                                middleInitial = response.getString(Keys.TAG_MINITIAL);
                                password = response.getString(Keys.TAG_PWORD);
                                userId  = Integer.parseInt(response.getString(Keys.TAG_USER_ID));
                                final GlobalData globalData = (GlobalData) getApplicationContext();
                                globalData.setFullname(json_response);
                                globalData.setImageUrl(image_url);
                                globalData.setEmailAddress(email);
                                globalData.setUserid(String.valueOf(userId));
                                globalData.setUsername(username);
                                userModel.setUserId(userId);
                                globalData.setMiddleInitial(middleInitial);
                                globalData.setFirstname(firstName);
                                globalData.setLastname(lastName);
                                globalData.setPassword(pword);
                                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(i);
                                finish();
                                hidepDialog();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
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
                                            MDToast mdToast = MDToast.makeText(getApplicationContext(), "Invalid username and password", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                                            mdToast.show();
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
                Toast.makeText(LoginActivity.this, "Error: "+error.getMessage(), Toast.LENGTH_LONG).show();
                MDToast mdToast = MDToast.makeText(getApplicationContext(), "Could not get data from server", MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING);
                mdToast.show();
                error.printStackTrace();
                hidepDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Keys.TAG_USERNAME, uname);
                params.put(Keys.TAG_PASSWORD, pword);
                return params;
            }
        };
        customJSONRequest.setTag(cancellationTag);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        VolleySingleton.getInstance(getApplicationContext()).cancelPendingRequests(cancellationTag);
    }
}
