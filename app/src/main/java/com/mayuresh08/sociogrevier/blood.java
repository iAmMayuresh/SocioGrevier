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

public class blood extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button btlocation;
    TextView location_view;
    FusedLocationProviderClient fusedLocationProviderClient;

    ProgressDialog loading_blood;
    ImageButton img;
    EditText postdescription;
    Button upload;
    private static final int gallerypick=1;
    Uri ImageUri;
    String description_b;
    StorageReference blood_comp_ref;
    String saveCurrentDate,saveCurrentTime,postRandomName,downBloodUrl,current_user_id;

    DatabaseReference blood_user_compl,blood_post_ref;
    FirebaseAuth mAuth_b;


    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_blood);

        location_view=findViewById(R.id.text_location_b);
        btlocation=findViewById(R.id.button_location_b);

        loading_blood=new ProgressDialog(this);

        blood_comp_ref= FirebaseStorage.getInstance().getReference();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        btlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(blood.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                    getLocation();
                }
                else
                {
                    ActivityCompat.requestPermissions(blood.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);

                }
            }
        });


        blood_user_compl= FirebaseDatabase.getInstance().getReference().child("Users");
        blood_post_ref=FirebaseDatabase.getInstance().getReference().child("Blood_posts");

        mAuth_b=FirebaseAuth.getInstance();
        current_user_id=mAuth_b.getCurrentUser().getUid();


        img = (ImageButton) findViewById(R.id.blood_complaints);
        upload = (Button) findViewById(R.id.button_upload_b);
        postdescription=(EditText)findViewById(R.id.description_b_comp);



        drawerLayout = findViewById(R.id.drawer_layout_b);
        navigationView = findViewById(R.id.navigation_view_b);
        toolbar = findViewById(R.id.new_toolbar_b);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("BLOOD BANKS RELATED COMPLAINTS");

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.CAT_blood);


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateBloodCovid();
            }
        });

    }

    private void ValidateBloodCovid()
    {
        description_b=postdescription.getText().toString();
        if(ImageUri==null){
            Toast.makeText(blood.this,"Please upload image",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(description_b)){
            Toast.makeText(blood.this,"Please write description",Toast.LENGTH_SHORT).show();
        }
        else {
            loading_blood.setTitle("Add Blood Complaint");
            loading_blood.setMessage("Please wait....");
            loading_blood.show();
            loading_blood.setCanceledOnTouchOutside(true);
            StorageBloodComtoFirebase();
        }
    }

    private void StorageBloodComtoFirebase()
    {
        Calendar calForDate=Calendar.getInstance();
        SimpleDateFormat currentdate=new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate=currentdate.format(calForDate.getTime());

        Calendar calForTime=Calendar.getInstance();
        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm");
        saveCurrentTime=currentTime.format(calForTime.getTime());

        postRandomName=saveCurrentDate+saveCurrentTime;

        final StorageReference filepath=blood_comp_ref.child("Blood Complaints").child(ImageUri.getLastPathSegment() + postRandomName + ".jpg");

        filepath.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
            {
                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri){
                        Uri p_down_url=uri;
                        downBloodUrl=p_down_url.toString();
                        Toast.makeText(blood.this,"Complaint Image Uploaded to storage ",Toast.LENGTH_SHORT).show();
                        SavingBloodComplaintstoDatabase();
                    }
                });

            }
        });
    }

    private void SavingBloodComplaintstoDatabase() {

        blood_user_compl.child(current_user_id).addValueEventListener(new ValueEventListener() {
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
                    hosp_post_map.put("description",description_b);
                    hosp_post_map.put("blood_posts",downBloodUrl);
                    hosp_post_map.put("location",loc);
                    hosp_post_map.put("fullname",userFullname);

                    blood_post_ref.child(current_user_id + postRandomName).updateChildren(hosp_post_map)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {

                                    if(task.isSuccessful())
                                    {
                                        Intent intent=new Intent(blood.this,dashboard.class);
                                        startActivity(intent);
                                        Toast.makeText(blood.this,"Post Is uploaded Succesfully",Toast.LENGTH_SHORT).show();
                                        loading_blood.dismiss();

                                    }
                                    else
                                    {
                                        Toast.makeText(blood.this,"Error while Posting ",Toast.LENGTH_SHORT).show();
                                        loading_blood.dismiss();
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
                Intent intent = new Intent(blood.this,dashboard.class);
                startActivity(intent);
                break;
            case R.id.CAT_police:
                Intent intent1 = new Intent(blood.this, police.class);
                startActivity(intent1);
                break;
            case R.id.CAT_hospital:
                Intent intent2 = new Intent(blood.this, hospital.class);
                startActivity(intent2);
                break;
            case R.id.CAT_covid:
                Intent intent3 = new Intent(blood.this, covid.class);
                startActivity(intent3);
                break;
            case R.id.CAT_blood:
                break;
            case R.id.CAT_cyber:
                Intent intent4 = new Intent(blood.this, sos.class);
                startActivity(intent4);
                break;
            case R.id.nav_profile:
                Intent intent5 = new Intent(blood.this, profile.class);
                startActivity(intent5);
                break;
            case R.id.nav_logout:
                mAuth_b.signOut();
                Intent intent6 = new Intent(blood.this, login.class);
                startActivity(intent6);
                break;
            case R.id.nav_call:
                Intent intent67 = new Intent(blood.this, cyber.class);
                startActivity(intent67);
                break;
            case R.id.nav_share:
                Toast.makeText(blood.this,"Feature Yet to come ",Toast.LENGTH_SHORT).show();
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
                    Geocoder geocoder=new Geocoder(blood.this, Locale.getDefault());

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


    public void showallBloodcomp(View view) {
        Intent intent5 = new Intent(blood.this, show_blood_complaints.class);
        startActivity(intent5);
    }
}
