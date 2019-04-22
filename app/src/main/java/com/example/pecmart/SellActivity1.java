package com.example.pecmart;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;

import java.io.IOException;

public class SellActivity1 extends AppCompatActivity {

   // private static final int PICK_MUL_IMAGE = 1;
    private static final int REQUEST_CODE = 1;
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    EditText ettitle,etdescription,etprice;
    TextView tventer;
    Spinner spinnercategory;
    Button btnupload;

    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell1);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        ettitle = findViewById(R.id.ettitle);
        etdescription = findViewById(R.id.etdescription);
        etprice = findViewById(R.id.etprice);
        tventer = findViewById(R.id.tventer);
        spinnercategory = findViewById(R.id.spinnercategory);
        btnupload = findViewById(R.id.btnupload);

        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ettitle.getText().toString().isEmpty() ||
                        etdescription.getText().toString().isEmpty() ||
                        etprice.getText().toString().isEmpty() ||
                        spinnercategory.getSelectedItem().toString().isEmpty())
                {
                    Toast.makeText(SellActivity1.this, "First fill all the fields!", Toast.LENGTH_SHORT).show();
                }
                else {

                    String title = ettitle.getText().toString().trim();
                    String category = spinnercategory.getSelectedItem().toString();
                    String price = etprice.getText().toString().trim();
                    //Float price = Float.parseFloat(sell_price);
                    String description = etdescription.getText().toString().trim();

                    items item = new items();
                    item.setTitle(title);
                    item.setCategory(category);
                    item.setDescription(description);
                    item.setPrice(price);
                    item.setOwnerId(ApplicationClass.user.getObjectId());
                    item.getUseremail(ApplicationClass.user.getEmail());

                    showProgress(true);
                    tvLoad.setText("Uploading the details...please wait...");

                    Backendless.Persistence.save(item, new AsyncCallback<items>() {
                        @Override
                        public void handleResponse(items response) {

                            //Toast.makeText(SellActivity1.this, "Item details successfully uploaded!", Toast.LENGTH_SHORT).show();
                            showProgress(false);
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"),REQUEST_CODE);
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                            showProgress(false);
                            Toast.makeText(SellActivity1.this, "Error! "+ fault.getMessage(), Toast.LENGTH_SHORT).show();

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode!=RESULT_CANCELED && requestCode==REQUEST_CODE && data !=null)
        {
            String title = ettitle.getText().toString().trim();
            String usermail = ApplicationClass.user.getEmail();
            Uri imageUri = data.getData();
            Bitmap photo = null;
            try {
                photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            showProgress(true);
            tvLoad.setText("Uploading photo...please wait...");

            Backendless.Files.Android.upload(photo, Bitmap.CompressFormat.PNG, 100, title + ".png", usermail, new AsyncCallback<BackendlessFile>() {
                @Override
                public void handleResponse(BackendlessFile response) {

                    showProgress(false);
                    Toast.makeText(SellActivity1.this, "Item image and details successfully uploaded!", Toast.LENGTH_SHORT).show();
                    ettitle.setText("");
                    etprice.setText("");
                    etdescription.setText("");

                }

                @Override
                public void handleFault(BackendlessFault fault) {

                    Toast.makeText(SellActivity1.this, "Error! "+fault.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

}

