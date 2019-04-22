package com.example.pecmart;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.local.UserIdStorageFactory;

public class Login extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    EditText etmail,etpassword;
    Button btnlogin,btnregister;
    TextView tvreset;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        etmail=findViewById(R.id.etmail);
        etpassword=findViewById(R.id.etpassword);
        btnlogin=findViewById(R.id.btnlogin);
        btnregister=findViewById(R.id.btnregister);
        tvreset=findViewById(R.id.tvreset);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etmail.getText().toString().isEmpty() || etpassword.getText().toString().isEmpty())
                {
                    Toast.makeText(Login.this, "Please enter all the fields!", Toast.LENGTH_SHORT).show();
                }

                else {
                    String mail = etmail.getText().toString().trim();
                    String password = etpassword.getText().toString().trim();

                    showProgress(true);
                    tvLoad.setText("Logging you in...please be patient...");

                    Backendless.UserService.login(mail, password, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {

                            ApplicationClass.user = response;
                            Toast.makeText(Login.this, "Logged In Successfully!", Toast.LENGTH_SHORT).show();
                            showProgress(false);
                            startActivity(new Intent(Login.this,MainActivity.class));
                            Login.this.finish();

                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                            Toast.makeText(Login.this, "Error! "+fault.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);

                        }
                    }, true);
                }


            }
        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Login.this,Register.class));


            }
        });

        tvreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etmail.getText().toString().isEmpty())
                {
                    Toast.makeText(Login.this, "Please enter the email address!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String mail = etmail.getText().toString().trim();
                    showProgress(true);

                    Backendless.UserService.restorePassword(mail, new AsyncCallback<Void>() {
                        @Override
                        public void handleResponse(Void response) {

                            Toast.makeText(Login.this, "Reset instructions sent to the registered email!", Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                            Toast.makeText(Login.this, "Error! " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);

                        }
                    });
                }

            }
        });

        showProgress(true);
        tvLoad.setText("Checking login history...please be patient...");

        Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() {
            @Override
            public void handleResponse(Boolean response) {

                if(response)
                {
                    String userObjectId = UserIdStorageFactory.instance().getStorage().get();

                    tvLoad.setText("Logging you in...please wait...");
                    Backendless.Data.of(BackendlessUser.class).findById(userObjectId, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {

                            ApplicationClass.user = response;
                            startActivity(new Intent(Login.this,MainActivity.class));
                            Login.this.finish();

                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                            Toast.makeText(Login.this, "Error! "+fault.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);

                        }
                    });
                }
                else
                {
                    showProgress(false);
                }

            }

            @Override
            public void handleFault(BackendlessFault fault) {

                Toast.makeText(Login.this, "Error! " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                showProgress(false);

            }
        });

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
