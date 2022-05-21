package com.mayuresh08.sociogrevier;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class register extends AppCompatActivity {

    TextInputLayout t1,t2,t4;
    TextView textView3;
    ProgressBar bar;

    FirebaseAuth mAuthr;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        t1=(TextInputLayout)findViewById(R.id.reg_email);
        t2=(TextInputLayout)findViewById(R.id.reg_pass);
        t4=(TextInputLayout)findViewById(R.id.reg_pass_c);
        bar=(ProgressBar)findViewById(R.id.progressbar);

        textView3 = findViewById(R.id.textView3);
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(register.this, login.class);
                startActivity(intent);
            }
        });
    }



    public void Registerhere(View view) {

        bar.setVisibility(view.VISIBLE);
        final String email = t1.getEditText().getText().toString();
        final String password = t2.getEditText().getText().toString();
        final String conf_pass = t4.getEditText().getText().toString();
        mAuthr = FirebaseAuth.getInstance();

        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password))
        {
            t1.setError("Email is Required !");
            t2.setError("Password is Required !");
            bar.setVisibility(View.INVISIBLE);
            return;
        }

        if (TextUtils.isEmpty(email)) {
            t1.setError("Email is Required !");
            bar.setVisibility(View.INVISIBLE);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            t2.setError("Password is Required !");
            bar.setVisibility(View.INVISIBLE);
            return;
        }

        if (password.length() < 6) {
            t2.setError("Password too short !");
            bar.setVisibility(View.INVISIBLE);
            return;
        }
        if (password.equals(conf_pass))
        {

            mAuthr.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful())
                            {
                                SendUserToRegProfile();
                                Toast.makeText(register.this,"Authenticated Successfully ",Toast.LENGTH_SHORT).show();


                            }
                            else
                            {
                                bar.setVisibility(View.INVISIBLE);
                                String mesg= task.getException().getMessage();
                                Toast.makeText(register.this,"Error occured "+mesg,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }

        else
        {
            t4.setError("password is unmatched !");
            bar.setVisibility(View.INVISIBLE);
            return;

        }
    }

    private void SendUserToRegProfile()
    {

        Intent setup=new Intent(register.this,register_profile.class);
        setup.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setup);
        finish();
    }


}
