package com.example.blooddonor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class select extends AppCompatActivity {
private Button don,rec;
private  TextView exi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        don=findViewById(R.id.donor);
        rec=findViewById(R.id.recei);
        exi=findViewById(R.id.Log);
        don.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(select.this,DonorA.class);
                startActivity(i);
            }
        });
        rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(select.this,Recpa.class);
                startActivity(i);
            }
        });
        exi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {   Intent i=new Intent(select.this,login.class);
                startActivity(i);

            }
        });
    }
}