package com.example.blooddonor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {
private Toolbar toolbar;
private TextView type,name,email,phn,age,bgp;
private CircleImageView pfpim;
private Button bkbt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
toolbar=findViewById(R.id.toolbr);
bkbt=findViewById(R.id.bkbt);
setSupportActionBar(toolbar);
getSupportActionBar().setTitle("My Profile");
getSupportActionBar().setDisplayHomeAsUpEnabled(true);
getSupportActionBar().setDisplayShowHomeEnabled(true);
name=findViewById(R.id.pname);
email=findViewById(R.id.pemail);
phn=findViewById(R.id.pphn);
age=findViewById(R.id.page);
bgp=findViewById(R.id.pbgp);
type=findViewById(R.id.ptyp);
pfpim=findViewById(R.id.upfp);
       DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
   databaseReference.addValueEventListener(new ValueEventListener() {
       @Override
       public void onDataChange(@NonNull DataSnapshot snapshot) {
           if(snapshot.exists())
           {   phn.setText(snapshot.child("Phone").getValue().toString());
               type.setText(snapshot.child("Type").getValue().toString());
               name.setText(snapshot.child("name").getValue().toString());
               email.setText(snapshot.child("email").getValue().toString());
               age.setText(snapshot.child("age").getValue().toString());
               bgp.setText(snapshot.child("bgp").getValue().toString());
               if(snapshot.hasChild("profilepictureurl"))
               {
               Glide.with(getApplicationContext()).load(snapshot.child("profilepictureurl").getValue().toString()).into(pfpim);}
               else
               {
                   pfpim.setImageResource(R.drawable.o1);
               }
           }
       }

       @Override
       public void onCancelled(@NonNull DatabaseError error) {

       }
   });

   bkbt.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           Intent i=new Intent(Profile.this,MainActivity.class);
           startActivity(i);
       finish();
       }
   });

   }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       switch (item.getItemId())
       {
           case android.R.id.home:
               finish();
               return true;
           default:return super.onOptionsItemSelected(item);}


    }
}