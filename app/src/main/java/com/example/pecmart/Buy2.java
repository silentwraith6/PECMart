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
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

public class Buy2 extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    requests e = new requests();

    requests request = new requests();



    TextView tvtitle,tvcategory,tvdescription,tvprice,tvsellername,tvsellerbranch,tvsellerphone,tvselleremail,tvitemdetails,tvsellerdetails,tvoffer,tvdecline;
    Button btnreqseller,btnyes,btnno;
    EditText etoffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy2);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);


        tvtitle=(TextView) findViewById(R.id.tvtitle);
        tvcategory=(TextView)findViewById(R.id.tvcategory);
        tvdescription=(TextView)findViewById(R.id.tvdescription);
        tvprice=(TextView)findViewById(R.id.tvprice);
        tvsellerbranch=(TextView)findViewById(R.id.tvsellerbranch);
        tvsellername=findViewById(R.id.tvsellername);
        tvsellerphone=findViewById(R.id.tvsellerphone);
        tvselleremail=findViewById(R.id.tvselleremail);
        tvitemdetails=findViewById(R.id.tvitemdetails);
        tvsellerdetails=findViewById(R.id.tvsellerdetails);
        btnreqseller=findViewById(R.id.btnreqseller);
        etoffer=findViewById(R.id.etoffer);
        tvoffer = findViewById(R.id.tvoffer);
        tvdecline = findViewById(R.id.tvdecline);
        btnyes = findViewById(R.id.btnyes);
        btnno = findViewById(R.id.btnno);


        tvsellerdetails.setVisibility(View.GONE);
        tvsellerphone.setVisibility(View.GONE);
        tvsellerbranch.setVisibility(View.GONE);
        tvselleremail.setVisibility(View.GONE);
        tvsellername.setVisibility(View.GONE);
        tvoffer.setVisibility(View.GONE);
        tvdecline.setVisibility(View.GONE);
        btnyes.setVisibility(View.GONE);
        btnno.setVisibility(View.GONE);

//        e.setStatus("");
//        e.setBid("");
//        e.setBuyerId("");
//        e.setSellerId("");
//        e.setItemId("");
//
//        Backendless.Persistence.save(e, new AsyncCallback<requests>() {
//            @Override
//            public void handleResponse(requests response) {
//
//            }
//
//            @Override
//            public void handleFault(BackendlessFault fault) {
//                Toast.makeText(Buy2.this, "Error! "+fault.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });


        final int index = getIntent().getIntExtra("index",0);

        showProgress(true);
        tvLoad.setText("Checking request status...please wait...");


        final DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setPageSize(100);
        String wherecluase= "itemId = '"+ApplicationClass.Item.get(index).getObjectId()+"' AND buyerId = '"+ApplicationClass.user.getObjectId()+"'";
        queryBuilder.setWhereClause(wherecluase);
        Backendless.Data.of(requests.class).find(queryBuilder, new AsyncCallback<List<requests>>() {
            @Override
            public void handleResponse(List<requests> response) {



                if(!response.isEmpty())
                {
                    tvoffer.setText("Offered Price : Rs."+response.get(0).getBid());
                    request = response.get(0);

                    if(response.get(0).getStatus().equalsIgnoreCase("pending"))
                    {
                        etoffer.setVisibility(View.GONE);
                        btnreqseller.setVisibility(View.GONE);
                        tvoffer.setVisibility(View.VISIBLE);
                        Toast.makeText(Buy2.this, "You have already generated a request for this item!", Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    }

                    if(response.get(0).getStatus().equalsIgnoreCase("accepted"))
                    {
                        String sellerid = response.get(0).getSellerId();
                        DataQueryBuilder queryBuilder1 = DataQueryBuilder.create();
                        String whereclause1 = "objectId1 = '"+sellerid+"'";
                        queryBuilder1.setWhereClause(whereclause1);
                        Backendless.Persistence.of(user.class).find(queryBuilder1, new AsyncCallback<List<user>>() {
                            @Override
                            public void handleResponse(List<user> response) {
                                tvsellername.setText("Name : "+response.get(0).getName());
                                tvsellerbranch.setText("Branch : "+response.get(0).getBranch());
                                tvsellerphone.setText("Contact : "+response.get(0).getPhone());
                                tvselleremail.setText("Email ID : "+response.get(0).getEmailID());

                                tvsellerdetails.setVisibility(View.VISIBLE);
                                tvsellerphone.setVisibility(View.VISIBLE);
                                tvsellerbranch.setVisibility(View.VISIBLE);
                                tvselleremail.setVisibility(View.VISIBLE);
                                tvsellername.setVisibility(View.VISIBLE);
                                tvoffer.setVisibility(View.VISIBLE);
                                etoffer.setVisibility(View.GONE);
                                btnreqseller.setVisibility(View.GONE);

                                showProgress(false);
                                Toast.makeText(Buy2.this, "Your request has been accepted", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {

                                showProgress(false);
                                Toast.makeText(Buy2.this, "Error! "+fault.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                    if(response.get(0).getStatus().equalsIgnoreCase("declined"))
                    {
                        showProgress(false);
                        etoffer.setVisibility(View.GONE);
                        btnreqseller.setVisibility(View.GONE);
                        tvoffer.setVisibility(View.VISIBLE);
                        tvdecline.setVisibility(View.VISIBLE);
                        btnyes.setVisibility(View.VISIBLE);
                        btnno.setVisibility(View.VISIBLE);

                        btnyes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                showProgress(true);
                                tvLoad.setText("Loading...please wait...");
                                Backendless.Persistence.of(requests.class).remove(request, new AsyncCallback<Long>() {
                                    @Override
                                    public void handleResponse(Long response) {
                                        Intent intent = new Intent(Buy2.this,Buy2.class);
                                        intent.putExtra("index",index);
                                        startActivity(intent);
                                        Buy2.this.finish();
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {

                                        Toast.makeText(Buy2.this, "Error! "+fault.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });

                        btnno.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                showProgress(true);
                                tvLoad.setText("Loading...please wait...");
                                Backendless.Persistence.of(requests.class).remove(request, new AsyncCallback<Long>() {
                                    @Override
                                    public void handleResponse(Long response) {
                                        Buy2.this.finish();
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {

                                        Toast.makeText(Buy2.this, "Error! "+fault.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }

                }

                else
                {
                    showProgress(false);
                }

            }

            @Override
            public void handleFault(BackendlessFault fault) {

                Toast.makeText(Buy2.this, "Error! "+fault.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        tvtitle.setText("Title : "+ApplicationClass.Item.get(index).getTitle());
        tvcategory.setText("Category : "+ApplicationClass.Item.get(index).getCategory());
        tvdescription.setText("Details : "+ApplicationClass.Item.get(index).getDescription());
        tvprice.setText("Price : Rs."+ApplicationClass.Item.get(index).getPrice());



        btnreqseller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etoffer.getText().toString().isEmpty())
                {
                    Toast.makeText(Buy2.this, "Enter the offer first!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String itemId,sellerId,buyerId,bid,status;
                    itemId = ApplicationClass.Item.get(index).getObjectId();
                    sellerId = ApplicationClass.Item.get(index).getOwnerId();
                    buyerId = ApplicationClass.user.getObjectId();
                    bid = etoffer.getText().toString().trim();
                    status = "pending";

                    requests request = new requests();

                    request.setItemId(itemId);
                    request.setSellerId(sellerId);
                    request.setBuyerId(buyerId);
                    request.setBid(bid);
                    request.setStatus(status);

                    showProgress(true);
                    tvLoad.setText("Generating your request...please wait...");

                    Backendless.Persistence.save(request, new AsyncCallback<requests>() {
                        @Override
                        public void handleResponse(requests response) {
                            showProgress(false);
                            Toast.makeText(Buy2.this, "Requested Successfully! Check My Requests for further information", Toast.LENGTH_SHORT).show();
                            Buy2.this.finish();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                            showProgress(false);
                            Toast.makeText(Buy2.this, "Error! "+fault.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
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
