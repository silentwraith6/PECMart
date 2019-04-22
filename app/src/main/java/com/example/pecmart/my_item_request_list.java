package com.example.pecmart;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.List;

public class my_item_request_list extends AppCompatActivity implements request_userAdapter.ItemClicked {


    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    TextView tvpeople;

    List<requests> allrequests = new ArrayList<>();
    List<user> allusers = new ArrayList<>();

    RecyclerView recyclerView;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;
    int index1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_item_request_list);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);
        tvpeople = findViewById(R.id.tvpeople);

        recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final int index = getIntent().getIntExtra("index1",0);
        index1 = index;


        showProgress(true);
        tvLoad.setText("Loading all the requests...please wait...");

        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setPageSize(100);
        String whereclause = "itemId = '"+ApplicationClass.Item.get(index).getObjectId()+"'";
        //Toast.makeText(this, ""+ApplicationClass.Item.get(index).getObjectId(), Toast.LENGTH_SHORT).show();
        queryBuilder.setWhereClause(whereclause);
        Backendless.Persistence.of(requests.class).find(queryBuilder, new AsyncCallback<List<requests>>() {
            @Override
            public void handleResponse(List<requests> response) {

                for(int j=0;j<response.size();j++)
                {
                    if(response.get(j).getStatus().equalsIgnoreCase("pending") || response.get(j).getStatus().equalsIgnoreCase("accepted")) {
                        allrequests.add(response.get(j));
                    }
                }

                if(allrequests.size()==0)
                {
                    showProgress(false);
                    Toast.makeText(my_item_request_list.this, "No requests placed till now!", Toast.LENGTH_SHORT).show();
                    my_item_request_list.this.finish();
                }

                else {

                    ApplicationClass.Request = response;

                    for (int i = 0; i < allrequests.size(); i++) {
                        DataQueryBuilder queryBuilder1 = DataQueryBuilder.create();
                        queryBuilder1.setPageSize(100);
                        String whereclause1 = "objectId1 = '" + allrequests.get(i).getBuyerId() + "'";
                        queryBuilder1.setWhereClause(whereclause1);

                        Backendless.Persistence.of(user.class).find(queryBuilder1, new AsyncCallback<List<user>>() {
                            @Override
                            public void handleResponse(List<user> response) {


                                //ApplicationClass.Users = response;

                                for (int j = 0; j < response.size(); j++) {
                                    allusers.add(response.get(j));
                                }


                                ApplicationClass.Users = allusers;

                                myAdapter = new request_userAdapter(my_item_request_list.this, allusers);
                                recyclerView.setAdapter(myAdapter);
                                showProgress(false);
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {

                                Toast.makeText(my_item_request_list.this, "Error! " + fault.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                    ApplicationClass.Users = allusers;
                }

            }

            @Override
            public void handleFault(BackendlessFault fault) {

                Toast.makeText(my_item_request_list.this, "Error! "+fault.getMessage(), Toast.LENGTH_SHORT).show();

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

        Intent intent = new Intent(my_item_request_list.this,seller_AcceptDecline.class);
        intent.putExtra("index2",index);
        intent.putExtra("index1",index1);
        startActivityForResult(intent,2);
        my_item_request_list.this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2)
        {
            myAdapter.notifyDataSetChanged();
            Intent intent = new Intent(my_item_request_list.this,my_item_request_list.class);
            startActivity(intent);
            my_item_request_list.this.finish();
        }
    }
}
