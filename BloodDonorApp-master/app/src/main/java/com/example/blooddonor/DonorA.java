package com.example.blooddonor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DonorA extends AppCompatActivity {
private TextView bt;
private Button recei;
private CircleImageView pfp;
private TextInputEditText regfn,rage,rphn,reml,rpass;
private Spinner rbgp;
private Button rbt;
private Uri resurl;
private ProgressDialog ldr;
private FirebaseAuth mauth;
private DatabaseReference usdbrf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor);
    recei=findViewById(R.id.recei);
    bt=findViewById(R.id.LogB);
    bt.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i=new Intent(DonorA.this,login.class);
            startActivity(i);
        }

    });
    pfp=findViewById(R.id.profile_image);
    regfn=findViewById(R.id.regfn);
    rage=findViewById(R.id.age);
    rphn=findViewById(R.id.phn);
    reml=findViewById(R.id.eml);
    rpass=findViewById(R.id.pass);
    rbt=findViewById(R.id.recei);
    rbgp=findViewById(R.id.bgp);
    ldr=new ProgressDialog(this);
    mauth=FirebaseAuth.getInstance();
    recei.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final String email=reml.getText().toString().trim();
            final String pass=rpass.getText().toString().trim();
            final String fname=regfn.getText().toString().trim();
            final String age=rage.getText().toString().trim();
            final String phn=rphn.getText().toString().trim();
            final String bgp=rbgp.getSelectedItem().toString();
            if(TextUtils.isEmpty(email)) {
reml.setError("Email is Empty!");
return;}
            if(TextUtils.isEmpty(pass)) {
                rpass.setError("Password is Empty!");
                return;}
            if(TextUtils.isEmpty(fname)) {
                regfn.setError("Full Name is Empty!");
                return;}
            if(TextUtils.isEmpty(age)) {
                rage.setError("Age is Empty!");
                return;}
            if(TextUtils.isEmpty(phn)) {
                rphn.setError("Phone is Empty!");
                return;}
            if(bgp.equals("Select Your Blood Group"))
            {
                Toast.makeText(DonorA.this,"Select Your Blood Group",Toast.LENGTH_SHORT).show();
                return ;
            }
            else{
                ldr.setMessage("Registering  ..");
                ldr.setCanceledOnTouchOutside(false);
                ldr.show();
                mauth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {String error=task.getException().toString();
                            Toast.makeText(DonorA.this,"error"+error,Toast.LENGTH_SHORT).show();

                        }
                        else{ String cusid=mauth.getCurrentUser().getUid();
                            usdbrf= FirebaseDatabase.getInstance().getReference().child("users").child(cusid);
                            HashMap userinfo=new HashMap();
                            userinfo.put("id",cusid);
                            userinfo.put("name",fname);
                            userinfo.put("email",email);
                            userinfo.put("age",age);
                            userinfo.put("Phone",phn);
                            userinfo.put("bgp",bgp);
                            userinfo.put("Type","Donor");
                            userinfo.put("search","donor"+bgp);
                            usdbrf.updateChildren(userinfo).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                               if(task.isSuccessful())
                               {
                                   Toast.makeText(DonorA.this,"Data Added",Toast.LENGTH_SHORT).show();
                               }
                                   else {

                                   Toast.makeText(DonorA.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                               }
                                   finish();
                                   ldr.dismiss();
                                }
                            });
                            if(resurl !=null)
                            {final StorageReference flpth= FirebaseStorage.getInstance().getReference().child("profile images").child(cusid);
                                Bitmap bitmap=null;
                                try{bitmap= MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(),resurl);
                                }
                                catch (IOException e)
                                {e.printStackTrace();}
                                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG,20,byteArrayOutputStream);
                                byte[] data=byteArrayOutputStream.toByteArray();
                                UploadTask uploadTask=flpth.putBytes(data);
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(DonorA.this,"Image Upload Failed",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        if(taskSnapshot.getMetadata()!=null && taskSnapshot.getMetadata().getReference()!=null)
                                        {
                                            Task<Uri>result=taskSnapshot.getStorage().getDownloadUrl();
                                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    String imageurl=uri.toString();
                                                    Map newImagemap=new HashMap();
                                                    newImagemap.put("profilepictureurl",imageurl);
                                                    usdbrf.updateChildren(newImagemap).addOnCompleteListener(new OnCompleteListener() {
                                                        @Override
                                                        public void onComplete(@NonNull Task task) {
                                                            if(task.isSuccessful())
                                                            {Toast.makeText(DonorA.this,"Image Url added to database",Toast.LENGTH_SHORT).show();

                                                            }
                                                            else{
                                                                Toast.makeText(DonorA.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                    finish();
                                                }
                                            });
                                        }
                                    }
                                });
                                Intent i=new Intent(DonorA.this,MainActivity.class);
                                startActivity(i);
                                finish();
                                ldr.dismiss();
                            }


                        }

                    }
                });
            }
        }
    });
    pfp.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i=new Intent(Intent.ACTION_PICK);
            getIntent().setType("image/jpeg");
            startActivityForResult(i,1);

        }
    });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
if(requestCode==1 && resultCode==RESULT_OK && data != null)
{resurl=data.getData();
    pfp.setImageURI(resurl);
    }
}}