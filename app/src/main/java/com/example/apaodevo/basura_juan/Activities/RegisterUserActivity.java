package com.example.apaodevo.basura_juan.Activities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.graphics.Color;



import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
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
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.apaodevo.basura_juan.Configuration.Keys;
import com.example.apaodevo.basura_juan.Configuration.WebServiceUrl;
import com.example.apaodevo.basura_juan.R;
import com.example.apaodevo.basura_juan.Services.CustomJSONRequest;
import com.example.apaodevo.basura_juan.Services.PathUtil;
import com.example.apaodevo.basura_juan.Services.VolleySingleton;
import com.kosalgeek.android.imagebase64encoder.ImageBase64;
import com.squareup.picasso.Picasso;
import com.valdesekamdem.library.mdtoast.MDToast;


import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class RegisterUserActivity extends AppCompatActivity {

    //Declare objects
    private TextView tv1;
    private ImageView img_user_profile;
    private Button bregister;
    private String firstname, lastname, minitial;
    private EditText etFname, etMinitial, etLname, etEmail, etUsername, etPassword;
    private TextInputLayout inputFname, inputLname, inputMinitial, inputUsername, inputPassword, inputEmail;
    private ProgressDialog pDialog;
    private TextView tvImageUserProfile;
    private Uri uri;
    private String displayName;
    private static int RESULT_LOAD_IMAGE = 1;
    private static String image_path;
    private static String encodedImage;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register_user);

        //inputUsername = (TextInputLayout) findViewById(R.id.input_layout_username);
        if (shouldAskPermissions()) {
            askPermissions();
        }
        //Toast.makeText(getApplicationContext(), ""+image_path, Toast.LENGTH_SHORT).show();
        //Cast objects
        bregister   = (Button) findViewById(R.id.btn_submit);
        tv1         = (TextView) findViewById(R.id.et_go_to_login);
        etFname     = (EditText) findViewById(R.id.et_fname);
        etLname     = (EditText) findViewById(R.id.et_lname);
        etMinitial  = (EditText) findViewById(R.id.et_minitial);
        etEmail     = (EditText) findViewById(R.id.et_email);
        etUsername  = (EditText) findViewById(R.id.et_uname);
        tvImageUserProfile = (TextView) findViewById(R.id.tvImgUserProfile);
        etPassword  = (EditText) findViewById(R.id.et_pword);


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Registering account...");
        pDialog.setCancelable(false);

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
                finish();
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


        /*
        ** Characters and specials characters are disabled
        */


        etFname.setFilters(new InputFilter[]{getEditTextFilter()});
        etLname.setFilters(new InputFilter[]{getEditTextFilter()});
        etFname.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        etMinitial.setFilters(new InputFilter[] {new InputFilter.LengthFilter(1), getEditTextFilter()});
        setCapitalizeTextWatcher(etFname);
        setCapitalizeTextWatcher(etLname);
        setCapitalizeTextWatcher(etMinitial);
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
                Picasso.with(this)
                        .load(uri)
                        .transform(new CropCircleTransformation())
                        .into(img_user_profile);
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

    private void registerUser(final String lastName, final String firstName, final String middleInitial, final String email, final String uName, final String pWord, final String imagePath, final String imageName){
        showpDialog();
        CustomJSONRequest customJSONRequest = new CustomJSONRequest(Request.Method.POST, WebServiceUrl.REGISTER_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    if(response.getString(Keys.TAG_SUCCESS).equals("0")){
                        etUsername.setError("Duplicate username");
                        requestFocus(etUsername);
                        hidepDialog();
                    }else if(response.getString(Keys.TAG_SUCCESS).equals("1")){
                        MDToast mdToast = MDToast.makeText(getApplicationContext(), "User successfully registered", MDToast.LENGTH_SHORT, MDToast.TYPE_INFO);
                        mdToast.show();
                        clearRegistrationActivity();
                        hidepDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                    /*final String server_response = response.getString(Keys.TAG_SUCCESS);
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
                                        MDToast mdToast = MDToast.makeText(getApplicationContext(), "User successfully registered", MDToast.LENGTH_SHORT, MDToast.TYPE_INFO);
                                        mdToast.show();
                                        clearRegistrationActivity();
                                        hidepDialog();

                                    }else if(server_response.equals("0")){
                                       etUsername.setError("Duplicate username");
                                        requestFocus(etUsername);
                                        hidepDialog();
                                    }
                                }
                            });
                        }
                    };
                    thread.start();*/


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MDToast mdToast = MDToast.makeText(getApplicationContext(), "Could not process data to server", MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING);
                mdToast.show();
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



    private boolean validateImagePath(){
        if(image_path == null){
            tvImageUserProfile.setError(getString(R.string.err_select_image));
            return false;
        } else{
            tvImageUserProfile.setError(null);
            return true;
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

        if(!validateImagePath()){
            return;
        }

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

        registerUser(lastName, firstName, middleInitial, userEmail, userUsername, userPassword, encodedImage, displayName);

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
    public static void setCapitalizeTextWatcher(final EditText editText) {
        final TextWatcher textWatcher = new TextWatcher() {

            int mStart = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mStart = start + count;
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Use WordUtils.capitalizeFully if you only want the first letter of each word to be capitalized
                String capitalizedText = WordUtils.capitalize(editText.getText().toString());
                if (!capitalizedText.equals(editText.getText().toString())) {
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            editText.setSelection(mStart);
                            editText.removeTextChangedListener(this);
                        }
                    });
                    editText.setText(capitalizedText);
                }
            }
        };

        editText.addTextChangedListener(textWatcher);
    }
    /*EditText should accept only alphabets and space */
    public static InputFilter getEditTextFilter() {
        return new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                boolean keepOriginal = true;
                StringBuilder sb = new StringBuilder(end - start);
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    if (isCharAllowed(c)) // put your condition here
                        sb.append(c);
                    else
                        keepOriginal = false;
                }
                if (keepOriginal)
                    return null;
                else {
                    if (source instanceof Spanned) {
                        SpannableString sp = new SpannableString(sb);
                        TextUtils.copySpansFrom((Spanned) source, start, sb.length(), null, sp, 0);
                        return sp;
                    } else {
                        return sb;
                    }
                }
            }

            private boolean isCharAllowed(char c) {
                Pattern ps = Pattern.compile("^[a-zA-Z ]+$");
                Matcher ms = ps.matcher(String.valueOf(c));
                return ms.matches();
            }
        };
    }
    private void clearRegistrationActivity(){
        etFname.setText("");
        etLname.setText("");
        etEmail.setText("");
        etPassword.setText("");


        etUsername.setText("");
        etMinitial.setText("");

        etUsername.setError(null);
        etPassword.setError(null);
        etEmail.setError(null);
        etMinitial.setError(null);
        etFname.setError(null);
        etLname.setError(null);
        img_user_profile.setImageResource(R.drawable.user_profile_placeholder);
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

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
