package com.example.apaodevo.basura_juan.Activities;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apaodevo.basura_juan.Configuration.Keys;
import com.example.apaodevo.basura_juan.R;
import com.example.apaodevo.basura_juan.Services.GlobalData;
import com.example.apaodevo.basura_juan.Services.JSONParser;


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
    private String image_url, email, response, username, pword, firstName, lastName, middleInitial;
    //public static String LOGIN_URL = "http://132.223.41.121/login.php";
    public static String LOGIN_URL = "http://basurajuan.x10host.com/login.php";

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
                            AttemptLogin attemptLogin = new AttemptLogin();
                            attemptLogin.execute(enteredUsername, enteredPassword);
                        }

                    }
                }
        ); //Redirect to Home Activity otherwise remain

    }


    //Background Worker for Login
    private class AttemptLogin extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Logging in ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            //Declare variable to store post data arguments
            String name= args[0];
            String password = args[1];

            try {

                //Put the argument to the array list.
                ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair(Keys.TAG_USERNAME, name));
                params.add(new BasicNameValuePair(Keys.TAG_PASSWORD, password));
                //Send post data request to web server through the web service
                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST",	params);
                Log.d("request!", "starting");


                Log.d("Login attempt", json.toString());
                if(json.toString() != null) {
                    //Fetch json response from web service
                    success = json.getInt(Keys.TAG_SUCCESS);

                    //Check json response and perform based on conditions met
                    if (success == 0) {


                        //Dismiss progress dialog and show alert dialog.
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        //showMessage("ERROR", "Invalid password");
                                        pass.setError("Invalid password");

                                    }
                                });
                                pDialog.cancel();


                            }
                        }, 3000);


                        Log.d("Login Failure!", json.getString(Keys.TAG_MESSAGE));
                        return json.getString(Keys.TAG_MESSAGE);


                    } else if (success == 2) {
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {

                                        user.setError("Invalid username");
                                        pass.setError("Invalid password");
                                    }
                                });
                                pDialog.cancel();


                            }
                        }, 3000);


                        Log.d("Login Failure!", json.getString(Keys.TAG_MESSAGE));
                        return json.getString(Keys.TAG_MESSAGE);
                    } else {
                        Log.d("Login Successful!", json.toString());
                        json_response = json.getString(Keys.TAG_FULLNAME);
                        image_url = json.getString(Keys.TAG_IMAGE_URL);
                        email = json.getString(Keys.TAG_USER_EMAIL);
                        response = json.getString(Keys.TAG_SUCCESS);
                        username = json.getString(Keys.TAG_USERNAME);
                        firstName = json.getString(Keys.TAG_FNAME);
                        lastName = json.getString(Keys.TAG_LNAME);
                        middleInitial = json.getString(Keys.TAG_MINITIAL);
                        pword = json.getString(Keys.TAG_PWORD);
                        Log.d("Login Successful!", image_url.toString());
                        final GlobalData globalData = (GlobalData) getApplicationContext();
                        globalData.setSomeVariable(json_response);
                        globalData.setImageUrl(image_url);
                        globalData.setEmailAddress(email);
                        globalData.setLoginStatus(response);
                        globalData.setUsername(username);
                        globalData.setMiddleInitial(middleInitial);
                        globalData.setFirstname(firstName);
                        globalData.setLastname(lastName);
                        globalData.setPassword(pword);
                        Thread thread = new Thread() {

                            @Override
                            public void run() {

                                // Block this thread for 4 seconds.al
                                try {
                                    Thread.sleep(4000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                // After sleep finished blocking, create a Runnable to run on the UI Thread.
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(i);
                                        //startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                        //finish();

                                        pDialog.dismiss();
                                    }
                                });

                            }

                        };
                        thread.start();

                        return json.getString(Keys.TAG_MESSAGE);

                    }
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Could get data from server!",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }//Focus the cursor to where the error originated

}
