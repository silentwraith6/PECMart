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
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class MyItems extends AppCompatActivity implements ItemsAdapter.ItemClicked{

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    TextView tvitemssale;
    EditText etsearch;
    List<items> allitems = new ArrayList<>();

    RecyclerView recyclerView;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_items);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);
        tvitemssale = findViewById(R.id.tvitemssale);
        etsearch = findViewById(R.id.etsearch);


        recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        showProgress(true);
        tvLoad.setText("Loading your items...please wait...");

        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        String whereclause = "ownerId = '"+ApplicationClass.user.getObjectId()+"'";
        queryBuilder.setPageSize(100);
        queryBuilder.setWhereClause(whereclause);
        Backendless.Data.of(items.class).find(queryBuilder, new AsyncCallback<List<items>>() {
            @Override
            public void handleResponse(List<items> response) {
                ApplicationClass.Item = response;
                for(int j=0;j<response.size();j++)
                    allitems.add(j,response.get(j));
                myAdapter = new ItemsAdapter(MyItems.this,ApplicationClass.Item);
                recyclerView.setAdapter(myAdapter);
                showProgress(false);
            }

            @Override
            public void handleFault(BackendlessFault fault) {

                Toast.makeText(MyItems.this, "Error! "+fault.getMessage(), Toast.LENGTH_SHORT).show();

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
    }

    @Override
    public void onItemClicked(int index) {

        Intent intent = new Intent(MyItems.this,my_item_request_list.class);
        intent.putExtra("index1",index);
        startActivityForResult(intent,1);

    }

    private void filter(String text)
    {
        List<items> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (items s : allitems) {
            //if the existing elements contains the search input
            if (s.getTitle().toLowerCase().contains(text.toLowerCase()) || s.getCategory().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        myAdapter = new ItemsAdapter(MyItems.this,filterdNames);
        recyclerView.setAdapter(myAdapter);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            myAdapter.notifyDataSetChanged();
            Intent intent = new Intent(MyItems.this,MyItems.class);
            startActivity(intent);
            MyItems.this.finish();
        }
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
