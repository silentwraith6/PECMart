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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyRequests extends AppCompatActivity implements ItemsAdapter.ItemClicked{

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    TextView tvrequests;
    EditText etsearch;

    List<requests> myrequests = new ArrayList<>();
    List<items> myrequesteditems = new ArrayList<>();
    int x = 0;

    RecyclerView recyclerView;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);
        tvrequests = findViewById(R.id.tvrequests);
        etsearch = findViewById(R.id.etsearch);

        recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        showProgress(true);
        tvLoad.setText("Loading Items...please wait...");


        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setPageSize(100);
        BackendlessUser y = ApplicationClass.user;
        String whereclause = "buyerId = '"+y.getObjectId()+"'";
        queryBuilder.setWhereClause(whereclause);
        Backendless.Persistence.of(requests.class).find(queryBuilder, new AsyncCallback<List<requests>>() {
            @Override
            public void handleResponse(List<requests> response) {

                for(int j=0;j<response.size();j++)
                    myrequests.add(response.get(j));
                x = response.size();

                for(int i = 0; i<myrequests.size(); i++) {
                    DataQueryBuilder queryBuilder1 = DataQueryBuilder.create();
                    queryBuilder1.setPageSize(100);
                    String whereclause1="objectId = '"+myrequests.get(i).getItemId()+"'";
                    queryBuilder1.setWhereClause(whereclause1);
                    Backendless.Persistence.of(items.class).find(queryBuilder1, new AsyncCallback<List<items>>() {
                        @Override
                        public void handleResponse(List<items> response) {
                            for(int j=0;j<response.size();j++)
                                myrequesteditems.add(response.get(j));

                            ApplicationClass.Item = myrequesteditems;
                            myAdapter = new ItemsAdapter(MyRequests.this, myrequesteditems);
                            recyclerView.setAdapter(myAdapter);
                            showProgress(false);
                        }


                        @Override
                        public void handleFault(BackendlessFault fault) {

                            Toast.makeText(MyRequests.this, "Error! "+fault.getMessage(), Toast.LENGTH_SHORT).show();

                        }


                    });

                }

//                if(!(myrequesteditems.isEmpty())) {
//                    myAdapter = new ItemsAdapter(MyRequests.this, myrequesteditems);
//                    recyclerView.setAdapter(myAdapter);
//                }

            }

            @Override
            public void handleFault(BackendlessFault fault) {

                Toast.makeText(MyRequests.this, "Error! "+fault.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        etsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());
            }
        });



        //Log.e("dataaaaaa",myrequests.get(0).getBuyerId());

//        for(int i = 0; i<myrequests.size(); i++) {
//            DataQueryBuilder queryBuilder1 = DataQueryBuilder.create();
//            queryBuilder1.setPageSize(100);
//            Log.e("sexxxxxxxxx",myrequests.get(i).getItemId());
//            whereclause="objectId = '"+myrequests.get(i).getItemId()+"'";
//            queryBuilder1.setWhereClause(whereclause);
//            Backendless.Persistence.of(items.class).find(queryBuilder1, new AsyncCallback<List<items>>() {
//                @Override
//                public void handleResponse(List<items> response) {
//                    Log.e("data",response.get(0).getDescription());
//                    for(int j=0;j<response.size();j++)
//                        myrequesteditems.add(response.get(j));
//                    Toast.makeText(MyRequests.this, "my requests "+myrequesteditems.size(), Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void handleFault(BackendlessFault fault) {
//
//                    Toast.makeText(MyRequests.this, "Error! "+fault.getMessage(), Toast.LENGTH_SHORT).show();
//
//                }
//            });
//        }


        //Toast.makeText(this, "Items "+myrequesteditems.size(), Toast.LENGTH_SHORT).show();

//        if(!(myrequesteditems.isEmpty())) {
//            myAdapter = new ItemsAdapter(MyRequests.this, myrequesteditems);
//            recyclerView.setAdapter(myAdapter);
//        }

//        else if(myrequesteditems.isEmpty())
//        {
//            Toast.makeText(this, "sad", Toast.LENGTH_SHORT).show();
//        }

    }

    private void filter(String text)
    {
        List<items> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (items s : myrequesteditems) {
            //if the existing elements contains the search input
            if (s.getTitle().toLowerCase().contains(text.toLowerCase()) || s.getCategory().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        myAdapter = new ItemsAdapter(MyRequests.this,filterdNames);
        recyclerView.setAdapter(myAdapter);
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

        Intent intent = new Intent(MyRequests.this,Buy2.class);
        intent.putExtra("index",index);
        startActivityForResult(intent,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 )
        {
            myAdapter.notifyDataSetChanged();
            Intent intent = new Intent(MyRequests.this,MyRequests.class);
            startActivity(intent);
            MyRequests.this.finish();
        }
    }
}
