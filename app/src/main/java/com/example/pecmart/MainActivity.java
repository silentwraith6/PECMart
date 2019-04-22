package com.example.pecmart;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class MainActivity extends AppCompatActivity {

    Button btnsell,btnbuy;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnsell = findViewById(R.id.btnsell);
        btnbuy = findViewById(R.id.btnbuy);
        toolbar=findViewById(R.id.toolBar);


        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch(menuItem.getItemId())
                {
                    case R.id.nav_account:
                        startActivity(new Intent(MainActivity.this,MyAccount.class));
                        return true;

                    case R.id.nav_sale:
                        startActivity(new Intent(MainActivity.this,MyItems.class));
                        return true;

                    case R.id.nav_request:
                        startActivity(new Intent(MainActivity.this,MyRequests.class));
                        return true;


                    case R.id.nav_sellhistory:
                        startActivity(new Intent(MainActivity.this,SellingHistory.class));
                        return true;

                    case R.id.nav_buyhistory:
                        startActivity(new Intent(MainActivity.this,BuyingHistory.class));
                        return true;

                    case R.id.nav_logout:
                        Toast.makeText(MainActivity.this, "Logging you out!", Toast.LENGTH_SHORT).show();
                        Backendless.UserService.logout(new AsyncCallback<Void>() {
                            @Override
                            public void handleResponse(Void response) {
                                startActivity(new Intent(MainActivity.this,Login.class));
                                MainActivity.this.finish();

                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(MainActivity.this, "Error! "+fault.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                        return true;
                }
                return false;
            }
        });


        btnsell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,SellActivity1.class));

            }
        });

        btnbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,BuyActivity1.class));

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
