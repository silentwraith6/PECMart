package com.example.pecmart;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

public class SellingHistory extends AppCompatActivity implements HistoryAdapter.ItemClicked{

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    RecyclerView recyclerView;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;

    TextView tvsolditems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling_history);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        tvsolditems = findViewById(R.id.tvsolditems);

        recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);

        showProgress(true);
        tvLoad.setText("Getting your sold items...please wait...");

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setPageSize(100);
        String whereclause = "sellerId = '"+ApplicationClass.user.getObjectId()+"'";
        queryBuilder.setWhereClause(whereclause);
        Backendless.Persistence.of(Completed.class).find(queryBuilder, new AsyncCallback<List<Completed>>() {
            @Override
            public void handleResponse(List<Completed> response) {
                if(response.size()==0) {
                    SellingHistory.this.finish();
                    Toast.makeText(SellingHistory.this, "You have not sold anything till now !", Toast.LENGTH_SHORT).show();
                }

                else {
                    ApplicationClass.completeds = response;
                    myAdapter = new HistoryAdapter(SellingHistory.this, ApplicationClass.completeds);
                    recyclerView.setAdapter(myAdapter);
                    showProgress(false);
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {

                showProgress(false);
                Toast.makeText(SellingHistory.this, "Error! "+fault.getMessage(), Toast.LENGTH_SHORT).show();

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

    @Override
    public void onItemClicked(int index) {
        Intent intent = new Intent(SellingHistory.this,SellHistoryDetails.class);
        intent.putExtra("index",index);
        startActivity(intent);
    }
}
