package com.example.blooddonor.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.blooddonor.Model.User;
import com.example.blooddonor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{
    private Context context;
    private List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_displayed_layout,parent,false);
return new ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
final User user=userList.get(position);
    holder.type.setText(user.getType());
    if(user.getType().equals("Receiver"))
    {holder.emlnw.setVisibility(View.GONE);}
    holder.phn.setText(user.getPhone());
    holder.useml.setText(user.getEmail());
    holder.usnm.setText(user.getName());
    holder.useml.setText(user.getEmail());
    holder.bgp.setText(user.getBgp());
        Glide.with(context).load(user.getProfilepictureurl()).into(holder.usrpfimg);
 final String nameofreceiver=user.getName();
 holder.emlnw.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View view) {
         new AlertDialog.Builder(context).setTitle("Send E-mail")
                 .setMessage("Send email to "+user.getName()+" ?")
                 .setCancelable(false)
                 .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {
                         DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference()
                                 .child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                         databaseReference.addValueEventListener(new ValueEventListener() {
                             @Override
                             public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String sendername=snapshot.child("name").getValue().toString();
                            String sendeml=snapshot.child("email").getValue().toString();
                            String age=snapshot.child("age").getValue().toString();
                            String bgp=snapshot.child("bgp").getValue().toString();
                            String phn=snapshot.child("Phone").getValue().toString();
                            String receml=user.getEmail();
                            String recnm=user.getName();
                            String cont="Hello "+recnm+" , We have received a request for Blood Donation for Your Compatible Blood Group.\n\n"
                                    +"Below are the mentioned Details of the user :\n\n"
                                    +"Name     :  "+sendername+"\n"
                                    +"E-mail   :  "+sendeml+"\n"
                                    +"Phone    :  "+phn+"\n"
                                    +"Blood Gp :  "+bgp+"\n"
                                    +"Age      :  "+age+"\n"
                                    +"\nWe hope you are able to reach out to them soon.\n\nThank You!\nBlood Welfare Club";
                                 Intent emailIntent = new Intent(Intent.ACTION_SEND);

                                 emailIntent.setData(Uri.parse("mailto:"));
                                 emailIntent.setType("text/plain");
                                 emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{receml});
                                 emailIntent.putExtra(Intent.EXTRA_CC, "");
                                 emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Blood Donation Request");
                                 emailIntent.putExtra(Intent.EXTRA_TEXT, cont);

                                 try {
                                     context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                                     ((Activity)context).finish();
                                     Log.i("Finished sending email...", "");
                                 } catch (android.content.ActivityNotFoundException ex) {
                                     Toast.makeText(view.getContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
                                 }

                             }

                             @Override
                             public void onCancelled(@NonNull DatabaseError error) {

                             }
                         });
                     }
                 })
                 .setNegativeButton("No",null)
                 .show();
     }
 });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView usrpfimg;
        public TextView type,usnm,useml,phn,bgp;
        public Button emlnw;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        usrpfimg=itemView.findViewById(R.id.userprfimg);
        type=itemView.findViewById(R.id.usptyp);
            usnm=itemView.findViewById(R.id.uspname);
            phn=itemView.findViewById(R.id.uspphn);
            bgp=itemView.findViewById(R.id.uspbgp);
            useml=itemView.findViewById(R.id.uspemail);
            emlnw=itemView.findViewById(R.id.usemlnw);

        }
    }
}
