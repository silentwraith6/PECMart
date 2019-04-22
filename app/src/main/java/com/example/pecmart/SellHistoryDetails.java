package com.example.pecmart;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

public class SellHistoryDetails extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    TextView tvtitle,tvprice,tvdescription,tvitemdetails,tvbuyerdetails,tvbuyername,tvbuyerphone,tvbuyerbranch,tvbuyeremail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_history_details);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        showProgress(true);
        tvLoad.setText("Getting buyer details...please wait...");

        tvtitle=(TextView) findViewById(R.id.tvtitle);
        tvdescription=(TextView)findViewById(R.id.tvdescription);
        tvprice=(TextView)findViewById(R.id.tvprice);
        tvitemdetails = findViewById(R.id.tvitemdetails);
        tvbuyerdetails = findViewById(R.id.tvbuyerdetails);
        tvbuyername = findViewById(R.id.tvbuyername);
        tvbuyerphone = findViewById(R.id.tvbuyerphone);
        tvbuyerbranch = findViewById(R.id.tvbuyerbranch);
        tvbuyeremail = findViewById(R.id.tvbuyeremail);

        final int index = getIntent().getIntExtra("index",0);

        tvtitle.setText("Item Title : "+ApplicationClass.completeds.get(index).getItemTitle());
        tvdescription.setText("Item Description : "+ApplicationClass.completeds.get(index).getItemDescription());
        tvprice.setText("Item Price : Rs."+ApplicationClass.completeds.get(index).getItemPrice());

        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        String whereclause = "objectId1 = '"+ApplicationClass.completeds.get(index).getBuyerId()+"'";
        queryBuilder.setWhereClause(whereclause);
        Backendless.Persistence.of(user.class).find(queryBuilder, new AsyncCallback<List<user>>() {
            @Override
            public void handleResponse(List<user> response) {
                tvbuyername.setText("Buyer Name : "+response.get(0).getName());
                tvbuyerphone.setText("Buyer Phone : "+response.get(0).getPhone());
                tvbuyerbranch.setText("Buyer Branch : "+response.get(0).getBranch());
                tvbuyeremail.setText("Buyer Email : "+response.get(0).getEmailID());
                showProgress(false);
            }

            @Override
            public void handleFault(BackendlessFault fault) {

                showProgress(false);
                Toast.makeText(SellHistoryDetails.this, "Error! "+fault.getMessage(), Toast.LENGTH_SHORT).show();

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
