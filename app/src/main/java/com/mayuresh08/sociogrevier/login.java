package com.mayuresh08.sociogrevier;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class login extends AppCompatActivity {


    TextInputLayout t1,t2;
    ProgressBar bar;
    FirebaseAuth mAuthl;
    TextView textView3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        textView3=findViewById(R.id.textView3);
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserTo_register_validation();
            }
        }); textView3= findViewById(R.id.textView3);


        t1=(TextInputLayout)findViewById(R.id.login_email);
        t2=(TextInputLayout)findViewById(R.id.login_password);
        bar=(ProgressBar)findViewById(R.id.login_progressbar);
        mAuthl=FirebaseAuth.getInstance();

}

    private void sendUserTo_register_validation() {

        Intent registerValidation =new Intent(login.this, register.class);
        startActivity(registerValidation);
    }


    @Override
    protected void onStart() {
        super.onStart();
        super.onStart();
        FirebaseUser currentUser=mAuthl.getCurrentUser();
        if(currentUser!=null)
        {
            Intent intent6 = new Intent(login.this, dashboard.class);
            startActivity(intent6);

        }
    }

    public void Loginhere(View view)
    {
        bar.setVisibility(view.VISIBLE);
        String email=t1.getEditText().getText().toString();
        String password=t2.getEditText().getText().toString();

        if(TextUtils.isEmpty(email) && TextUtils.isEmpty(password) )
    {
        t1.setError("Email is Required !");
        t2.setError("Password is required !");
        bar.setVisibility(View.INVISIBLE);
        return;
    }
        if(TextUtils.isEmpty(email) )
        {
            t1.setError("Email is Required !");
            bar.setVisibility(View.INVISIBLE);
            return;
        }
        if(TextUtils.isEmpty(password)){
            t2.setError("Password is Required !");
            bar.setVisibility(View.INVISIBLE);
            return;
        }
        if(password.length()<6)
        {
            t2.setError("Password must be greater than six letters");
            bar.setVisibility(View.INVISIBLE);
            return;
        }

        mAuthl.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            bar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(),"Login Successfully",Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(login.this,dashboard.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("email",mAuthl.getCurrentUser().getEmail());
                            intent.putExtra("email",mAuthl.getCurrentUser().getUid());
                            startActivity(intent);
                            finish();

                        } else
                         {
                             bar.setVisibility(View.INVISIBLE);
                             t1.getEditText().setText("");
                             t2.getEditText().setText("");
                             String message=task.getException().getMessage();
                             Toast.makeText(getApplicationContext(),"Error :"+message,Toast.LENGTH_SHORT).show();

                        }

                    }
                });


    }

    public void forget_password_click(View view) {
        final EditText resetMail=new EditText(view.getContext());
        AlertDialog.Builder passwordResetdilogue =new AlertDialog.Builder(view.getContext());
        passwordResetdilogue.setTitle("Reset Password");
        passwordResetdilogue.setMessage("Enter your registered Email");
        passwordResetdilogue.setView(resetMail);

        passwordResetdilogue.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String mail=resetMail.getText().toString();
                mAuthl.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(login.this,"Reset Link Sent to your Email",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(login.this,"Error ! Reset link not sent"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        passwordResetdilogue.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        passwordResetdilogue.create().show();

    }
}
