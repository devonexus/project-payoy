package com.example.apaodevo.basura_juan.Activities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;

import android.database.Cursor;
import android.graphics.Color;

import android.graphics.drawable.GradientDrawable;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.OpenableColumns;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apaodevo.basura_juan.Configuration.Keys;
import com.example.apaodevo.basura_juan.R;
import com.example.apaodevo.basura_juan.Services.JSONParser;
import com.example.apaodevo.basura_juan.Services.PathUtil;
import com.kosalgeek.android.imagebase64encoder.ImageBase64;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class RegisterUserActivity extends AppCompatActivity {


    //Declare objects
    private TextView tv1;
    private ImageView img_user_profile;
    private Button bregister;
    private String firstname, lastname, minitial;
    private EditText etFname, etMinitial, etLname, etEmail, etUsername, etPassword;
    private TextInputLayout inputFname, inputLname, inputMinitial, inputUsername, inputPassword, inputEmail;
    private ProgressDialog pDialog;
    public static String REGISTER_URL = "http://132.223.41.121/registration.php"; //WEB Service URL
    private Uri uri;
    private String displayName;

    private int success; //JSON Result
    private JSONParser jsonParser = new JSONParser(); //JSON Parser Class to make HTTP Request...
    private static int RESULT_LOAD_IMAGE = 1;
    //Post data variables
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static String image_path;
    private static String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        //inputUsername = (TextInputLayout) findViewById(R.id.input_layout_username);
        if (shouldAskPermissions()) {
            askPermissions();
        }
        //Cast objects
        bregister   = (Button) findViewById(R.id.btn_submit);
        tv1         = (TextView) findViewById(R.id.et_go_to_login);
        etFname     = (EditText) findViewById(R.id.et_fname);
        etLname     = (EditText) findViewById(R.id.et_lname);
        etMinitial  = (EditText) findViewById(R.id.et_minitial);
        etEmail     = (EditText) findViewById(R.id.et_email);
        etUsername  = (EditText) findViewById(R.id.et_uname);
        etPassword  = (EditText) findViewById(R.id.et_pword);

        GradientDrawable sd = (GradientDrawable) bregister.getBackground();
        sd.setColor(Color.rgb(199, 0, 57));



        inputFname      = (TextInputLayout) findViewById(R.id.input_layout_fname);
        inputLname      = (TextInputLayout) findViewById(R.id.input_layout_lname);
        inputMinitial   = (TextInputLayout) findViewById(R.id.input_layout_minitial);
        inputUsername   = (TextInputLayout) findViewById(R.id.input_layout_uname);
        inputPassword   = (TextInputLayout) findViewById(R.id.input_layout_pword);
        inputEmail      = (TextInputLayout) findViewById(R.id.input_layout_email);
        img_user_profile = (ImageView) findViewById(R.id.imgUserProfile);

        //Change as per key press
        etUsername.addTextChangedListener(new MyTextWatcher(etUsername));
        etPassword.addTextChangedListener(new MyTextWatcher(etPassword));
        etEmail.addTextChangedListener(new MyTextWatcher(etEmail));

        //Set text to underline and color blue
        SpannableString content = new SpannableString(tv1.getText().toString());
        ClickableSpan click = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));

            }
        };
        content.setSpan(click, 25, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv1.setMovementMethod(LinkMovementMethod.getInstance());
        tv1.setHighlightColor(Color.TRANSPARENT);
        tv1.setText(content);


        img_user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getImageFromAlbum();
            }
        });



        bregister.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        submitForm();


                    }
                }
        );  //Register user



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE) {

                uri = data.getData();
                image_path = PathUtil.getPathFromURI(RegisterUserActivity.this, uri);
                File f = new File(uri.getPath());



                displayName = null;
                Picasso.with(this).load(uri).into(img_user_profile);
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
    //Submit form
    private void submitForm() {
        String firstName        = etFname.getText().toString();
        String lastName         = etLname.getText().toString();
        String middleInitial    = etMinitial.getText().toString();
        String userEmail        = etEmail.getText().toString();
        String userUsername     = etUsername.getText().toString();
        String userPassword     = etPassword.getText().toString();

        if(!validateFirstname()){
            return;
        }
        if(!validateMinitial()){
            return;
        }
        if(!validateLastname()){
            return;
        }
        if (!validateEmail()) {
            return;
        }

        if(!validateUsername()){
            return;
        }
        if(!validatePasssword()){
            return;
        }

        try {
            encodedImage = ImageBase64
                    .with(getApplicationContext())
                    .requestSize(60, 60)
                    .encodeFile(image_path);
        } catch (FileNotFoundException e) {
            Log.d("Error:", e.getMessage());
            e.printStackTrace();
        }


        //Toast.makeText(this, encodedImage+"", Toast.LENGTH_LONG).show();
//        Toast.makeText(this, encodedImage+"", Toast.LENGTH_LONG).show();
        AttemptRegister ar = new AttemptRegister();

        ar.execute(lastName, firstName, middleInitial, userEmail, userUsername, userPassword, encodedImage, displayName);
//        ar.execute(lastName, firstName, middleInitial, userEmail, userUsername, userPassword, encodedImage, displayName);
         /*  ar.execute(encodedImage, displayName);*/
    }



    private boolean validatePasssword(){
        if(etPassword.getText().toString().trim().isEmpty()){
            etPassword.setError(getString(R.string.err_msg_password));
            requestFocus(etPassword);
            return false;
        }
        else {
            etPassword.setError(null);
        }
        return true;
    }   //Validate password

    private boolean validateUsername() {
        if (etUsername.getText().toString().trim().isEmpty()) {
            etUsername.setError(getString(R.string.err_msg_name));
            requestFocus(etUsername);
            return false;
        } else {
            etUsername.setError(null);
        }

        return true;
    }   //Validate username
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private boolean validateFirstname() {
        firstname = etFname.getText().toString().trim();

        if (firstname.isEmpty()) {
            etFname.setError(getString(R.string.err_msg_fname));
            requestFocus(etFname);
            return false;
        } else {
            etFname.setError(null);
        }

        return true;
    }  //Validate firstname


    private boolean validateLastname() {
        lastname = etLname.getText().toString().trim();

        if (lastname.isEmpty()) {
            etLname.setError(getString(R.string.err_msg_lname));
            requestFocus(etLname);
            return false;
        } else {
            etLname.setError(null);
        }

        return true;
    }//Validate lastname


    private boolean validateMinitial() {
        minitial = etMinitial.getText().toString().trim();

        if (minitial.isEmpty() && minitial.length() == 0) {
            etMinitial.setError(getString(R.string.err_msg_minitial));
            requestFocus(etMinitial);
            return false;
        } else {
            etMinitial.setError(null);
        }

        return true;
    } //Validate middle initial


    private boolean validateEmail() {
        String email = etEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            etEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            etEmail.setError(null);
        }

        return true;
    }   //Validate email format


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }  //Focus

    //Triggering function for keypress
    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()) {
                case R.id.et_uname:
                    validateUsername();
                    break;
                case R.id.et_email:
                    validateEmail();
                    break;
                case R.id.et_pword:
                    validatePasssword();
                    break;
                case R.id.et_fname:
                    validateFirstname();
                    break;
                case R.id.et_minitial:
                    validateMinitial();
                    break;
                case R.id.et_lname:
                    validateLastname();
                    break;
            }
        }


    }

    private void clearRegistrationActivity(){
        etFname.setText(null);
        etLname.setText(null);
        etEmail.setText(null);
        etEmail.setText(null);
        etUsername.setText(null);
        etPassword.setText(null);
        etUsername.setError(null);
        etPassword.setError(null);
        img_user_profile.setImageResource(android.R.color.transparent);
    }
    //Background Worker for Login
    private class AttemptRegister extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(RegisterUserActivity.this);
            pDialog.setMessage("Registering accounts...");

            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... args) {


            String last_name        = args[0];
            String first_name       = args[1];
            String middle_initial   = args[2];
            String user_email       = args[3];
            String user_username    = args[4];
            String user_password    = args[5];
            String user_image       = args[6];
            String user_image_name  = args[7];
            try {
                //Put the argument to the array list.
                ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair(Keys.TAG_LASTNAME, last_name));
                params.add(new BasicNameValuePair(Keys.TAG_FIRSTNAME, first_name));
                params.add(new BasicNameValuePair(Keys.TAG_MIDDLE_INITIAL, middle_initial));
                params.add(new BasicNameValuePair(Keys.TAG_EMAIL, user_email));
                params.add(new BasicNameValuePair(Keys.TAG_USERNAME, user_username));
                params.add(new BasicNameValuePair(Keys.TAG_PASSWORD, user_password));
                params.add(new BasicNameValuePair(Keys.TAG_USER_IMAGE, user_image));
                params.add(new BasicNameValuePair(Keys.TAG_USER_IMAGE_NAME, user_image_name));
                //Send post data request to web server through the web service
                JSONObject json = jsonParser.makeHttpRequest(REGISTER_URL, "POST",	params);
                Log.d("request!", "starting"+json.toString());
                Log.d("Login attempt", json.toString());

                //Fetch json response from web service
                success = json.getInt(TAG_SUCCESS);
                if(success == 0){
                    //Dismiss progress dialog and show alert dialog.
                    Thread thread = new Thread() {

                        @Override
                        public void run() {

                            // Block this thread for 4 seconds.
                            try {
                                Thread.sleep(4000);
                            } catch (InterruptedException e) {
                            }

                            // After sleep finished blocking, create a Runnable to run on the UI Thread.
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    etUsername.setError("Username already exists");
                                    pDialog.dismiss();
                                }
                            });

                        }

                    };
                    thread.start();
                    return json.getString(TAG_MESSAGE);

                } else{

                    Thread thread = new Thread() {

                        @Override
                        public void run() {

                            // Block this thread for 4 seconds.
                            try {
                                Thread.sleep(4000);
                            } catch (InterruptedException e) {
                            }

                            // After sleep finished blocking, create a Runnable to run on the UI Thread.
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),
                                            "Successfully registered",
                                            Toast.LENGTH_LONG).show();
                                    clearRegistrationActivity();
                                    pDialog.dismiss();
                                }
                            });

                        }

                    };
                    thread.start();
                    return json.getString(TAG_MESSAGE);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return  null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
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
}
