package com.mayuresh08.sociogrevier;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;


public class dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {




    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FirebaseUser currentUser;
    FirebaseAuth mAuthd;
    DatabaseReference users_ref;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.navigation_view);
        toolbar=findViewById(R.id.new_toolbar);


        mAuthd = FirebaseAuth.getInstance();
        currentUser = mAuthd.getCurrentUser();
        users_ref=FirebaseDatabase.getInstance().getReference().child("Users");


        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        updateNavHeader();
        navigationView.setCheckedItem(R.id.nav_home);

        Calendar calendar = Calendar.getInstance();
        String currentdate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        TextView textViewDate = findViewById(R.id.text_view_date);
        textViewDate.setText(currentdate);

    }


    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        currentUser=mAuthd.getCurrentUser();
        if(currentUser==null)
        {
            Intent intent6 = new Intent(dashboard.this, login.class);
            intent6.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent6);

        }
        else
        {
            final String current_user_id=mAuthd.getCurrentUser().getUid();
            users_ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if(!dataSnapshot.hasChild(current_user_id))
                    {
                        Intent pro_intent = new Intent(dashboard.this, register_profile.class);
                        pro_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(pro_intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.CAT_police:
                Intent intent = new Intent(dashboard.this, police.class);
                startActivity(intent);
                break;
            case R.id.CAT_hospital:
                Intent intent1 = new Intent(dashboard.this, hospital.class);
                startActivity(intent1);
                break;
            case R.id.CAT_covid:
                Intent intent2 = new Intent(dashboard.this, covid.class);
                startActivity(intent2);
                break;
            case R.id.CAT_blood:
                Intent intent3 = new Intent(dashboard.this, blood.class);
                startActivity(intent3);
                break;
            case R.id.CAT_cyber:
                Intent intent4 = new Intent(dashboard.this, sos.class);
                startActivity(intent4);
                break;
            case R.id.nav_profile:
                Intent intent5 = new Intent(dashboard.this, shownearby.class);
                startActivity(intent5);
                break;
            case R.id.nav_logout:
                mAuthd.signOut();
                Intent intent6 = new Intent(dashboard.this, login.class);
                startActivity(intent6);
                break;
            case R.id.nav_call:
                Intent intent67 = new Intent(dashboard.this, cyber.class);
                startActivity(intent67);
                break;
            case R.id.nav_share:
                Toast.makeText(dashboard.this,"Feature Yet to come ",Toast.LENGTH_SHORT).show();
                break;


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateNavHeader()
    {
        navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navEmail = headerView.findViewById(R.id.text_email);
        navEmail.setText(currentUser.getEmail());
    }

    public void open_police(View view) {
        Intent intent = new Intent(dashboard.this, police.class);
        startActivity(intent);
    }
    public void open_hosp(View view) {
        Intent intent1 = new Intent(dashboard.this, hospital.class);
        startActivity(intent1);
    }
    public void open_covid(View view) {
        Intent intent2 = new Intent(dashboard.this, covid.class);
        startActivity(intent2);
    }
    public void open_blood(View view) {
        Intent intent3 = new Intent(dashboard.this, blood.class);
        startActivity(intent3);
    }

    public void open_cyber(View view) {
        Intent intent4 = new Intent(dashboard.this, cyber.class);
        startActivity(intent4);
    }

    public void show_all_puts(View view) {

        Intent intent5 = new Intent(dashboard.this, categories_with_comp.class);
        startActivity(intent5);


    }
}
