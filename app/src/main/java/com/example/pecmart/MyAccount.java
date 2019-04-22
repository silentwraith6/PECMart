package com.example.pecmart;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;
import java.util.jar.Attributes;

public class MyAccount extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;


    TextView tvmyacc,tvname,tvuserid,tvmail,tvbranch,tvphone;
    Button btnchangepass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        tvmyacc=findViewById(R.id.tvmyacc);
        tvname=findViewById(R.id.tvname);
        tvuserid=findViewById(R.id.tvuserid);
        tvmail=findViewById(R.id.tvmail);
        tvbranch=findViewById(R.id.tvbranch);
        tvphone=findViewById(R.id.tvphone);
        btnchangepass=findViewById(R.id.btnchangepass);

        BackendlessUser x = ApplicationClass.user;
        final String name,phone,branch,email,userid;

        name = (String) x.getProperty("Name");
        phone = (String) x.getProperty("Phone");
        branch = (String) x.getProperty("Branch");
        email = x.getEmail();
        userid = x.getObjectId();



        tvname.setText("Name : "+name);
        tvbranch.setText("Branch : "+branch);
        tvphone.setText("Phone : "+phone);
        tvmail.setText("Email : "+email);
        tvuserid.setText("User ID : "+userid);

        btnchangepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email;
                showProgress(true);

                Backendless.UserService.restorePassword(mail, new AsyncCallback<Void>() {
                    @Override
                    public void handleResponse(Void response) {

                        Toast.makeText(MyAccount.this, "Reset instructions sent to the registered email!", Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                        Toast.makeText(MyAccount.this, "Error! " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                        showProgress(false);

                    }
                });
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
