package com.example.blooddonor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.blooddonor.Adapter.UserAdapter;
import com.example.blooddonor.Model.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
private DrawerLayout drawerLayout;
private Toolbar toolbar;
private NavigationView navw;
private CircleImageView nav_pf_image;
private TextView n_fn,n_em,n_bg,n_ty;
private DatabaseReference usdbrf;
private RecyclerView recyclerView;
private ProgressBar progressBar;
private List<User> userList;
private UserAdapter userAdapter;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
drawerLayout=findViewById(R.id.drawl);
toolbar=findViewById(R.id.toolbr);
setSupportActionBar(toolbar);
getSupportActionBar().setTitle("Blood Donation App");
navw=findViewById(R.id.navw);

ActionBarDrawerToggle toggle=new ActionBarDrawerToggle
        (MainActivity.this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
drawerLayout.addDrawerListener(toggle);
toggle.syncState();
navw.setNavigationItemSelectedListener(this);
    progressBar=findViewById(R.id.prsbr);
    recyclerView=findViewById(R.id.rcyvw);
    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
    linearLayoutManager.setReverseLayout(true);
    linearLayoutManager.setStackFromEnd(true);
    recyclerView.setLayoutManager(linearLayoutManager);
    userList=new ArrayList<>();
    userAdapter=new UserAdapter(this,userList);
    recyclerView.setAdapter(userAdapter);
    DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
            .child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    ref.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            String type = snapshot.child("Type").getValue().toString();
            if (type.equals("Donor"))
            {
                readReciptents();
            }
            else{readDonors();}
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
nav_pf_image=navw.getHeaderView(0).findViewById(R.id.nav_pfp);
n_fn= navw.getHeaderView(0).findViewById(R.id.nav_unm);
        n_em= navw.getHeaderView(0).findViewById(R.id.nav_ueml);
        n_bg= navw.getHeaderView(0).findViewById(R.id.nav_ubgp);
        n_ty= navw.getHeaderView(0).findViewById(R.id.nav_utyp);
        usdbrf= FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    usdbrf.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists())
            {
                String name=snapshot.child("name").getValue().toString();
                n_fn.setText(name);
                String email=snapshot.child("email").getValue().toString();
                n_em.setText(email);
                String bgp=snapshot.child("bgp").getValue().toString();
                n_bg.setText(bgp);
                String type=snapshot.child("Type").getValue().toString();
                n_ty.setText(type);
                if(snapshot.hasChild("profilepictureurl"))
                {String pfurl=snapshot.child("profilepictureurl").getValue().toString();
                Glide.with(getApplicationContext()).load(pfurl).into(nav_pf_image);}
                else{
                    nav_pf_image.setImageResource(R.drawable.o1);
                }
           }
        else
                n_fn.setText("Error");
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    });
    }

    private void readDonors() {DatabaseReference reference=FirebaseDatabase.getInstance().getReference()
            .child("users");
        Query query=reference.orderByChild("Type").equalTo("Donor");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    User user=dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
                userAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                if(userList.isEmpty())
                {
                    Toast.makeText(MainActivity.this,"No Donors",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readReciptents() {DatabaseReference reference=FirebaseDatabase.getInstance().getReference()
            .child("users");
        Query query=reference.orderByChild("Type").equalTo("Receiver");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
           for(DataSnapshot dataSnapshot:snapshot.getChildren()){
               User user=dataSnapshot.getValue(User.class);
               userList.add(user);
           }
            userAdapter.notifyDataSetChanged();
           progressBar.setVisibility(View.GONE);
            if(userList.isEmpty())
            {
                Toast.makeText(MainActivity.this,"No Recipients",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
       switch (item.getItemId())
       {
           case R.id.navupfp:
               Intent i =new Intent(MainActivity.this,Profile.class);
               startActivity(i);
               break;
           case R.id.navlogo:
               FirebaseAuth.getInstance().signOut();
               Intent j=new Intent(MainActivity.this,select.class);
               startActivity(j);
               break;
           case R.id.compt:
               Intent i9=new Intent(MainActivity.this,catslctdact.class);
               i9.putExtra("group","Compatible With Me");
               startActivity(i9);
               break;

           case R.id.ap:
               Intent i1=new Intent(MainActivity.this,catslctdact.class);
               i1.putExtra("group","A+");
               startActivity(i1);
               break;
           case R.id.am:
               Intent i2=new Intent(MainActivity.this,catslctdact.class);
               i2.putExtra("group","A-");
               startActivity(i2);
               break;
           case R.id.op:
               Intent i3=new Intent(MainActivity.this,catslctdact.class);
               i3.putExtra("group","O+");
               startActivity(i3);
               break;
           case R.id.om:
               Intent i4=new Intent(MainActivity.this,catslctdact.class);
               i4.putExtra("group","O-");
               startActivity(i4);
               break;
           case R.id.bp:
               Intent i5=new Intent(MainActivity.this,catslctdact.class);
               i5.putExtra("group","B+");
               startActivity(i5);
               break;
           case R.id.bm:
               Intent i6=new Intent(MainActivity.this,catslctdact.class);
               i6.putExtra("group","B-");
               startActivity(i6);
               break;
           case R.id.abp:
               Intent i7=new Intent(MainActivity.this,catslctdact.class);
               i7.putExtra("group","AB+");
               startActivity(i7);
               break;
           case R.id.abm:
               Intent i8=new Intent(MainActivity.this,catslctdact.class);
               i8.putExtra("group","AB-");
               startActivity(i8);
               break;
           case R.id.about:
               Intent i10=new Intent(MainActivity.this,contact.class);
               startActivity(i10);
               break;
           case R.id.share:
               Intent i11=new Intent(MainActivity.this,Share.class);
               startActivity(i11);
               break;

       }
       drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}