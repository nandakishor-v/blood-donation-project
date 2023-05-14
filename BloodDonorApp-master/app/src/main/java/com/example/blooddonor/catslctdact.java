package com.example.blooddonor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.example.blooddonor.Adapter.UserAdapter;
import com.example.blooddonor.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class catslctdact extends AppCompatActivity {
private Toolbar toolbar;
private RecyclerView recyclerView;
private List<User>userList;
private UserAdapter userAdapter;
private String title="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catslctdact);
    toolbar=findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    recyclerView=findViewById(R.id.rcyvw);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        userList=new ArrayList<>();
        userAdapter=new UserAdapter(catslctdact.this,userList);
        recyclerView.setAdapter(userAdapter);

        if(getIntent().getExtras()!=null)
        {
            title=getIntent().getStringExtra("group");
            if(title.equals("Compatible With Me"))
            {
                getCompatUsers();
                getSupportActionBar().setTitle("Compatible With Me");
            }
            else{
            getSupportActionBar().setTitle("Blood Group "+title);
            readUsers();}
        }
    }

    private void getCompatUsers() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String result;
                String type=snapshot.child("Type").getValue().toString();
                if(type.equals("Donor"))
                {result="Receiver";

                }
                else
                    result="donor";
                String bgp=snapshot.child("bgp").getValue().toString();
                DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("users");
                Query query=reference.orderByChild("search").equalTo(result+bgp);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userList.clear();
                        for(DataSnapshot dataSnapshot:snapshot.getChildren())
                        {User user=dataSnapshot.getValue(User.class);
                            userList.add(user);

                        }
                        userAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readUsers() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
           String result;
           String type=snapshot.child("Type").getValue().toString();
            if(type.equals("Donor"))
            {result="Receiver";

            }
            else
            result="donor";
            DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("users");
            Query query=reference.orderByChild("search").equalTo(result+title);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
               userList.clear();
               for(DataSnapshot dataSnapshot:snapshot.getChildren())
               {User user=dataSnapshot.getValue(User.class);
                   userList.add(user);

               }
               userAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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