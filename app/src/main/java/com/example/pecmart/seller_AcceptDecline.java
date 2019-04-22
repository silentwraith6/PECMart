package com.example.pecmart;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.ClipData;
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

import java.util.ArrayList;
import java.util.List;

public class seller_AcceptDecline extends AppCompatActivity {

    TextView tvbuyerdetails,tvbuyername,tvbuyerphone,tvbuyerbranch,tvbuyeremail,tvbid,tvsuccessful;
    Button btnaccept,btndecline,btnsuccess,btnfail;
    EditText etfinalprice;
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    requests request = new requests();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller__accept_decline);

        tvbuyerdetails = findViewById(R.id.tvbuyerdetails);
        tvbuyername = findViewById(R.id.tvbuyername);
        tvbuyerbranch = findViewById(R.id.tvbuyerbranch);
        tvbuyerphone = findViewById(R.id.tvbuyerphone);
        tvbuyeremail = findViewById(R.id.tvbuyeremail);
        tvbid = findViewById(R.id.tvbid);
        tvsuccessful = findViewById(R.id.tvsuccessful);
        btnaccept = findViewById(R.id.btnaccept);
        btndecline = findViewById(R.id.btndecline);
        btnsuccess = findViewById(R.id.btnsuccess);
        btnfail = findViewById(R.id.btnfail);
        etfinalprice = findViewById(R.id.etfinalprice);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        tvsuccessful.setVisibility(View.GONE);
        btnsuccess.setVisibility(View.GONE);
        btnfail.setVisibility(View.GONE);
        etfinalprice.setVisibility(View.GONE);

        final int index2 = getIntent().getIntExtra("index2",0);
        final int index1 = getIntent().getIntExtra("index1",0);

        showProgress(true);
        tvLoad.setText("Loading buyer details...please wait...");


        tvbuyername.setText("Buyer Name : "+ApplicationClass.Users.get(index2).getName());
        tvbuyerbranch.setText("Buyer Branch : "+ApplicationClass.Users.get(index2).getBranch());
        tvbuyerphone.setText("Buyer Phone : "+ApplicationClass.Users.get(index2).getPhone());
        tvbuyeremail.setText("Buyer Email : "+ApplicationClass.Users.get(index2).getEmailID());

        String itemid = ApplicationClass.Item.get(index1).getObjectId();


        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setPageSize(100);
        String whereclause = "itemId = '"+itemid+"' AND buyerId = '"+ApplicationClass.Users.get(index2).getObjectId1()+"'";
        queryBuilder.setWhereClause(whereclause);

        Backendless.Persistence.of(requests.class).find(queryBuilder, new AsyncCallback<List<requests>>() {
            @Override
            public void handleResponse(List<requests> response)
            {
                tvbid.setText("Offered Price : Rs."+response.get(0).getBid());
                request = response.get(0);
                if(request.getStatus().equalsIgnoreCase("accepted"))
                {
                    Toast.makeText(seller_AcceptDecline.this, "You have already accepted the request", Toast.LENGTH_SHORT).show();
                    btnaccept.setVisibility(View.GONE);
                    btndecline.setVisibility(View.GONE);
                    tvsuccessful.setVisibility(View.VISIBLE);
                    btnsuccess.setVisibility(View.VISIBLE);
                    btnfail.setVisibility(View.VISIBLE);
                    etfinalprice.setVisibility(View.VISIBLE);
                }

                showProgress(false);
            }

            @Override
            public void handleFault(BackendlessFault fault) {

                Toast.makeText(seller_AcceptDecline.this, "Error! "+fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        btndecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgress(true);
                tvLoad.setText("Declining the request...please wait...");
                request.setStatus("declined");
                Backendless.Persistence.of(requests.class).save(request, new AsyncCallback<requests>() {
                    @Override
                    public void handleResponse(requests response) {
                        Intent intent = new Intent(seller_AcceptDecline.this,my_item_request_list.class);
                        intent.putExtra("index1",index1);
                        startActivity(intent);
                        seller_AcceptDecline.this.finish();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                        showProgress(false);
                        Toast.makeText(seller_AcceptDecline.this, "Error! "+fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgress(true);
                tvLoad.setText("Making your details visible to buyer...please wait...");
                request.setStatus("accepted");
                Backendless.Persistence.of(requests.class).save(request, new AsyncCallback<requests>() {
                    @Override
                    public void handleResponse(requests response) {
                        Toast.makeText(seller_AcceptDecline.this, "Request accepted successfully !", Toast.LENGTH_SHORT).show();
                        btnaccept.setVisibility(View.GONE);
                        btndecline.setVisibility(View.GONE);
                        tvsuccessful.setVisibility(View.VISIBLE);
                        btnsuccess.setVisibility(View.VISIBLE);
                        btnfail.setVisibility(View.VISIBLE);
                        etfinalprice.setVisibility(View.VISIBLE);

                        showProgress(false);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                        showProgress(false);
                        Toast.makeText(seller_AcceptDecline.this, "Error! "+fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnsuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etfinalprice.getText().toString().isEmpty())
                {
                    Toast.makeText(seller_AcceptDecline.this, "Please enter the final price first!", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    showProgress(true);
                    tvLoad.setText("Updating transaction history...please wait...");
                    Completed transaction = new Completed();
                    transaction.setBuyerId(ApplicationClass.Users.get(index2).getObjectId1());
                    transaction.setSellerId(ApplicationClass.user.getObjectId());
                    transaction.setItemTitle(ApplicationClass.Item.get(index1).getTitle());
                    transaction.setItemDescription(ApplicationClass.Item.get(index1).getDescription());
                    transaction.setItemPrice(etfinalprice.getText().toString().trim());
                    transaction.setItemId(ApplicationClass.Item.get(index1).getObjectId());

                    Backendless.Persistence.of(Completed.class).save(transaction, new AsyncCallback<Completed>() {
                        @Override
                        public void handleResponse(Completed response) {

                            String whereclause = "objectId = '"+response.getItemId()+"'";
                            DataQueryBuilder queryBuilder2 = DataQueryBuilder.create();
                            queryBuilder2.setWhereClause(whereclause);
                            tvLoad.setText("Deleting the item...please wait...");
                            Backendless.Persistence.of(items.class).find(queryBuilder2, new AsyncCallback<List<items>>() {
                                @Override
                                public void handleResponse(List<items> response) {

                                    items item = new items();
                                    item = response.get(0);

                                    Backendless.Persistence.of(items.class).remove(item, new AsyncCallback<Long>() {
                                        @Override
                                        public void handleResponse(Long response) {

                                            tvLoad.setText("Deleting all the requests...please wait...");

                                            List <requests> allrequests = new ArrayList<>();
                                            allrequests = ApplicationClass.Request;
                                            for(int i=0;i<ApplicationClass.Request.size();i++)
                                            {
                                                Backendless.Persistence.of(requests.class).remove(allrequests.get(i), new AsyncCallback<Long>() {
                                                    @Override
                                                    public void handleResponse(Long response) {

                                                    }

                                                    @Override
                                                    public void handleFault(BackendlessFault fault) {
                                                        showProgress(false);
                                                        Toast.makeText(seller_AcceptDecline.this, "Error! "+fault.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            seller_AcceptDecline.this.finish();
                                        }

                                        @Override
                                        public void handleFault(BackendlessFault fault) {

                                            showProgress(false);
                                            Toast.makeText(seller_AcceptDecline.this, "Error! "+fault.getMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    showProgress(false);
                                    Toast.makeText(seller_AcceptDecline.this, "Error! "+fault.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });

                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                            showProgress(false);
                            Toast.makeText(seller_AcceptDecline.this, "Error! "+fault.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }


            }
        });

        btnfail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgress(true);
                tvLoad.setText("Hiding your details from the buyer...please wait...");
                request.setStatus("declined");
                Backendless.Persistence.of(requests.class).save(request, new AsyncCallback<requests>() {
                    @Override
                    public void handleResponse(requests response) {
                        Intent intent = new Intent(seller_AcceptDecline.this,my_item_request_list.class);
                        intent.putExtra("index1",index1);
                        startActivity(intent);
                        seller_AcceptDecline.this.finish();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                        showProgress(false);
                        Toast.makeText(seller_AcceptDecline.this, "Error! "+fault.getMessage(), Toast.LENGTH_SHORT).show();
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
