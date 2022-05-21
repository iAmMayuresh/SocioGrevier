package com.mayuresh08.sociogrevier;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class covid extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button btlocation;
    TextView location_view;
    FusedLocationProviderClient fusedLocationProviderClient;

    ProgressDialog loading_covid;
    ImageButton img;
    EditText postdescription;
    Button upload;
    private static final int gallerypick=1;
    Uri ImageUri;
    String description_c;
    StorageReference covid_comp_ref;
    String saveCurrentDate,saveCurrentTime,postRandomName,downCovidUrl,current_user_id;

    DatabaseReference covid_user_compl,covid_post_ref;
    FirebaseAuth mAuth_c;


    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_covid);

        location_view=findViewById(R.id.text_location_c);
        btlocation=findViewById(R.id.button_location_c);

        loading_covid=new ProgressDialog(this);

        covid_comp_ref= FirebaseStorage.getInstance().getReference();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        btlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(covid.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                    getLocation();
                }
                else
                {
                    ActivityCompat.requestPermissions(covid.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);

                }
            }
        });


        covid_user_compl= FirebaseDatabase.getInstance().getReference().child("Users");
        covid_post_ref=FirebaseDatabase.getInstance().getReference().child("Covid_posts");

        mAuth_c=FirebaseAuth.getInstance();
        current_user_id=mAuth_c.getCurrentUser().getUid();


        img = (ImageButton) findViewById(R.id.covid_complaints);
        upload = (Button) findViewById(R.id.button_upload_c);
        postdescription=(EditText)findViewById(R.id.description_c_comp);



        drawerLayout = findViewById(R.id.drawer_layout_c);
        navigationView = findViewById(R.id.navigation_view_c);
        toolbar = findViewById(R.id.new_toolbar_c);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("COVID-19 RELATED COMPLAINTS");

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.CAT_covid);


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidatePostCovid();
            }
        });

    }

    private void ValidatePostCovid()
    {
        description_c=postdescription.getText().toString();
        if(ImageUri==null){
            Toast.makeText(covid.this,"Please upload image",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(description_c)){
            Toast.makeText(covid.this,"Please write description",Toast.LENGTH_SHORT).show();
        }
        else {
            loading_covid.setTitle("Add Covid Complaint");
            loading_covid.setMessage("Please wait....");
            loading_covid.show();
            loading_covid.setCanceledOnTouchOutside(true);
            StorageCovComtoFirebase();
        }
    }

    private void StorageCovComtoFirebase()
    {
        Calendar calForDate=Calendar.getInstance();
        SimpleDateFormat currentdate=new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate=currentdate.format(calForDate.getTime());

        Calendar calForTime=Calendar.getInstance();
        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm");
        saveCurrentTime=currentTime.format(calForTime.getTime());

        postRandomName=saveCurrentDate+saveCurrentTime;

        final StorageReference filepath=covid_comp_ref.child("Covid Complaints").child(ImageUri.getLastPathSegment() + postRandomName + ".jpg");

        filepath.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
            {
                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri){
                        Uri p_down_url=uri;
                        downCovidUrl=p_down_url.toString();
                        SavingCovidComplaintstoDatabase();
                    }
                });

            }
        });
    }

    private void SavingCovidComplaintstoDatabase() {

        covid_user_compl.child(current_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String userFullname=dataSnapshot.child("fullname").getValue().toString();
                    String loc=location_view.getText().toString();

                    HashMap hosp_post_map=new HashMap();
                    hosp_post_map.put("uid",current_user_id);
                    hosp_post_map.put("date",saveCurrentDate);
                    hosp_post_map.put("time",saveCurrentTime);
                    hosp_post_map.put("description",description_c);
                    hosp_post_map.put("covid_posts",downCovidUrl);
                    hosp_post_map.put("location",loc);
                    hosp_post_map.put("fullname",userFullname);

                    covid_post_ref.child(current_user_id + postRandomName).updateChildren(hosp_post_map)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {

                                    if(task.isSuccessful())
                                    {
                                        Intent intent=new Intent(covid.this,dashboard.class);
                                        startActivity(intent);
                                        Toast.makeText(covid.this,"Complaint Is uploaded Succesfully",Toast.LENGTH_SHORT).show();
                                        loading_covid.dismiss();

                                    }
                                    else
                                    {
                                        Toast.makeText(covid.this,"Error while Posting ",Toast.LENGTH_SHORT).show();
                                        loading_covid.dismiss();
                                    }
                                }
                            });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void OpenGallery()
    {
        Intent galleryIntent =new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,gallerypick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==gallerypick && resultCode==RESULT_OK && data!=null)
        {
            ImageUri=data.getData();
            img.setImageURI(ImageUri);

        }

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
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                Intent intent = new Intent(covid.this,dashboard.class);
                startActivity(intent);
                break;
            case R.id.CAT_police:
                Intent intent1 = new Intent(covid.this, police.class);
                startActivity(intent1);
                break;
            case R.id.CAT_hospital:
                Intent intent2 = new Intent(covid.this, hospital.class);
                startActivity(intent2);
                break;
            case R.id.CAT_covid:
                break;
            case R.id.CAT_blood:
                Intent intent3 = new Intent(covid.this, blood.class);
                startActivity(intent3);
                break;
            case R.id.CAT_cyber:
                Intent intent4 = new Intent(covid.this, sos.class);
                startActivity(intent4);
                break;
            case R.id.nav_profile:
                Intent intent5 = new Intent(covid.this, profile.class);
                startActivity(intent5);
                break;
            case R.id.nav_logout:
                mAuth_c.signOut();
                Intent intent6 = new Intent(covid.this, login.class);
                startActivity(intent6);
                break;
            case R.id.nav_call:
                Intent intent67 = new Intent(covid.this, cyber.class);
                startActivity(intent67);
                break;
            case R.id.nav_share:
                Toast.makeText(covid.this,"Feature Yet to come ",Toast.LENGTH_SHORT).show();
                break;


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getLocation()
    {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location=task.getResult();
                if(location != null){
                    Geocoder geocoder=new Geocoder(covid.this, Locale.getDefault());

                    try {
                        List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

                        location_view.setText(addresses.get(0).getAddressLine(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void showallcovidcomp(View view) {
        Intent intent5 = new Intent(covid.this, show_covid_complaints.class);
        startActivity(intent5);
    }
}



