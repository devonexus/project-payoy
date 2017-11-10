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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.apaodevo.basura_juan.R;
import com.example.apaodevo.basura_juan.Services.GlobalData;
import com.example.apaodevo.basura_juan.Services.JSONParser;


public class LoginActivity extends AppCompatActivity {

    //Declare objects

    public static String json_response;
    private Button bregister, blogin;
    private EditText user, pass;
    private ProgressDialog pDialog;
    protected String enteredUsername, enteredPassword;
    JSONParser jsonParser = new JSONParser();
    GlobalData globalData = new GlobalData();
    private int success;
    public static String LOGIN_URL = "http://132.223.41.121/login.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_PASSWORD = "password";
    private static final String TAG_FULLNAME = "fullname";


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


        //Redirect to register UI
        bregister.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), RegisterUserActivity.class));

                    }
                }
        );
        blogin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        enteredUsername = user.getText().toString();
                        enteredPassword = pass.getText().toString();

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
            // TODO Auto-generated method stub



            //Declare variable to store post data arguments
            String name= args[0];
            String password = args[1];

            try {

                //Put the argument to the array list.
                ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair(TAG_USERNAME, name));
                params.add(new BasicNameValuePair(TAG_PASSWORD, password));


                //Send post data request to web server through the web service
                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST",	params);
                Log.d("request!", "starting");


                Log.d("Login attempt", json.toString());

                //Fetch json response from web service
                success = json.getInt(TAG_SUCCESS);

                 //Check json response and perform based on conditions met
                 if (success == 0){


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



                     Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                     return json.getString(TAG_MESSAGE);


                } else if(success == 2){
                     new Timer().schedule(new TimerTask() {
                         @Override
                         public void run() {
                             pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                 @Override
                                 public void onCancel(DialogInterface dialog) {
                                     //showMessage("ERROR", "Incorrect username and password");
                                     user.setError("Invalid username");
                                     pass.setError("Invalid password");
                                 }
                             });
                             pDialog.cancel();


                         }
                     }, 3000);



                     Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                     return json.getString(TAG_MESSAGE);
                 }
                else {
                    Log.d("Login Successful!", json.toString());
                     json_response = json.getString(TAG_FULLNAME);
                    //Delay intent to prolong progress dialog
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {

                            globalData.setSomeVariable(json_response);
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            // run AsyncTask here.
                            pDialog.dismiss();

                            // set


                        }
                    }, 3000);


                    return json.getString(TAG_MESSAGE);

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
    }
    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        builder.show();
    }
}
