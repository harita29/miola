package com.example.android.sfwhf1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

public class CopyRight extends AppCompatActivity {
    private View viewToAnimate;
    private static int TIME_OUT = 5000;
    private ImageView logo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy_right);

        logo = (ImageView) findViewById(R.id.logo);
        fadeIn();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent welcome = new Intent(CopyRight.this, Welcom.class);
                startActivity(welcome);
                finish();
            }
        },TIME_OUT);

    }

    public void fadeIn(){
        // Animation out = AnimationUtils.makeOutAnimation(this, true);
        //viewToAnimate.startAnimation(out);
        Animation img = new AlphaAnimation(0, 1.0f);
        img.setDuration(3000);
        img.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                logo.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                logo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        logo.startAnimation(img);
    }
}
