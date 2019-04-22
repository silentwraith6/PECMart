package com.example.pecmart;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

public class Register extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    EditText etname,etmail,etpassword,etrepassword,etphone;
    Spinner spinnerbranch;
    Button btnsubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        etname=findViewById(R.id.etname);
        etmail=findViewById(R.id.etmail);
        etpassword=findViewById(R.id.etpassword);
        etrepassword=findViewById(R.id.etrepassword);
        etphone=findViewById(R.id.etphone);
        spinnerbranch=(Spinner)findViewById(R.id.spinnerbranch);
        btnsubmit=findViewById(R.id.btnsubmit);

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etname.getText().toString().isEmpty() ||
                        etphone.getText().toString().isEmpty() ||
                        etmail.getText().toString().isEmpty() ||
                        etpassword.getText().toString().isEmpty() ||
                        etrepassword.getText().toString().isEmpty() ||
                        spinnerbranch.getSelectedItem().toString().isEmpty())
                {
                    Toast.makeText(Register.this, "Please enter all the fields first!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(etpassword.getText().toString().equals(etrepassword.getText().toString()))
                    {
                        final String name = etname.getText().toString().trim();
                        final String phone = etphone.getText().toString().trim();
                        final String mail = etmail.getText().toString().trim()+"@pec.edu.in";
                        String password = etpassword.getText().toString();
                        final String branch = spinnerbranch.getSelectedItem().toString();



                        BackendlessUser User = new BackendlessUser();
                        User.setEmail(mail);
                        User.setPassword(password);
                        User.setProperty("Name",name);
                        User.setProperty("Branch",branch);
                        User.setProperty("Phone",phone);

                        showProgress(true);
                        tvLoad.setText("Registering new user...please wait...");

                        Backendless.UserService.register(User, new AsyncCallback<BackendlessUser>() {
                            @Override
                            public void handleResponse(BackendlessUser response) {

                                ApplicationClass.user = response;

                                //Toast.makeText(Register.this, ""+ApplicationClass.user.getObjectId(), Toast.LENGTH_SHORT).show();

                                user User = new user();
                                User.setName(name);
                                User.setPhone(phone);
                                User.setBranch(branch);
                                User.setEmailID(mail);
                                User.setObjectId1(ApplicationClass.user.getObjectId());


                                Backendless.Persistence.save(User, new AsyncCallback<user>() {
                                    @Override
                                    public void handleResponse(user response) {

                                        showProgress(false);
                                        Toast.makeText(Register.this, "User successfully registered!", Toast.LENGTH_SHORT).show();
                                        Register.this.finish();

                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {

                                        Toast.makeText(Register.this, "Error! "+fault.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });



                            }

                            @Override
                            public void handleFault(BackendlessFault fault)
                            {
                                Toast.makeText(Register.this, "Error! " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                showProgress(false);
                            }
                        });

//
                    }

                    else
                    {
                        Toast.makeText(Register.this, "The entered passwords do not match!", Toast.LENGTH_SHORT).show();
                    }
                }
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
