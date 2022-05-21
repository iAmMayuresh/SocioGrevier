package com.mayuresh08.sociogrevier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class register_profile extends AppCompatActivity {

    private TextInputLayout p_username,p_fullname,p_phn;
    private Button save_profile;

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    String currentUserid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_profile);

        p_username=(TextInputLayout)findViewById(R.id.profile_username);
        p_fullname=(TextInputLayout)findViewById(R.id.profile_fulname);
        p_phn=(TextInputLayout)findViewById(R.id.profile_phone);
        save_profile=(Button)findViewById(R.id.save_button);

        mAuth=FirebaseAuth.getInstance();
        currentUserid=mAuth.getCurrentUser().getUid();
        userRef= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserid);


        save_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveAccount_profileinfo();
            }
        });



    }



    private void SaveAccount_profileinfo()
    {
        String username=p_username.getEditText().getText().toString();
        String fullname=p_fullname.getEditText().getText().toString();
        String phoneNumber=p_phn.getEditText().getText().toString();

        if(TextUtils.isEmpty(username))
        {
           p_username.setError("username is Required !");
            return;
        }
        if(TextUtils.isEmpty(fullname))
        {
            p_fullname.setError("Full name is Required !");
            return;
        }
        if (phoneNumber.length() < 10 | phoneNumber.length()>10)
        {
            p_phn.setError("Invalid Phone Number !");
            return;
        }
        else {

            HashMap userMap=new HashMap();
            userMap.put("username",username);
            userMap.put("fullname",fullname);
            userMap.put("phoneNumber",phoneNumber);
            userMap.put("status","Developer");
            userMap.put("gender","none");
            userMap.put("dob","none");
            userMap.put("relationshipStatus","none");
            userRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if(task.isSuccessful())
                    {
                        sendUserToDahboard();
                        Toast.makeText(register_profile.this,"your Account is Created",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        String mesg= task.getException().getMessage();
                        Toast.makeText(register_profile.this,"Error Occured "+mesg,Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void sendUserToDahboard() {

        Intent intent6 = new Intent(register_profile.this, dashboard.class);
        intent6.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent6);
        finish();
    }
}
