package com.example.blooddonor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
private ImageView sym;
private TextView title,slogan;
Animation top,btm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscreen);
    sym=findViewById(R.id.sym);
    title=findViewById(R.id.title);
    slogan=findViewById(R.id.slogan);
    top= AnimationUtils.loadAnimation(this,R.anim.top_anim);
    btm=AnimationUtils.loadAnimation(this,R.anim.bot_anim);
    sym.setAnimation(top);
    slogan.setAnimation(btm);
    title.setAnimation(btm);
    int Splash_Screen=4300;
    new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            Intent i=new Intent(SplashScreen.this,select.class);
            startActivity(i);
            finish();
        }
    },Splash_Screen);
    }
}