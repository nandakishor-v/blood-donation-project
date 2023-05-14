package com.example.blooddonor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {
    private Button bt;
private TextInputEditText logeml,logpass;
private TextView fgpass;
private Button login;
private ProgressDialog ldr;
private FirebaseAuth mauth;
private FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mauth=FirebaseAuth.getInstance();
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=mauth.getCurrentUser();
                if(user!=null)
                {Intent i=new Intent(login.this,MainActivity.class);
                    startActivity(i);
                    finish();}
            }
        };
        setContentView(R.layout.activity_login);
        logeml=findViewById(R.id.logeml);
        logpass=findViewById(R.id.logpass);
        fgpass=findViewById(R.id.fgtpass);
        login=findViewById(R.id.logrecei);
        ldr=new ProgressDialog(this);

        bt=findViewById(R.id.LogB);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(login.this,select.class);
                startActivity(i);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email=logeml.getText().toString().trim();
             final String pass=logpass.getText().toString().trim();
             if(TextUtils.isEmpty(email))
             {logeml.setError("Email is Empty");

             }
                if(TextUtils.isEmpty(pass))
                {logpass.setError("Password is Empty");

                }
                else{ldr.setMessage("Logging In");
                    ldr.setCanceledOnTouchOutside(false);
                    ldr.show();
                mauth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(login.this,"Login Successful",Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(login.this,MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else{
                            Toast.makeText(login.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                        }
                    ldr.dismiss();}
                });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mauth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
    mauth.removeAuthStateListener(authStateListener);
    }
}