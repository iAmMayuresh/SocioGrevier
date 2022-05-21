package com.mayuresh08.sociogrevier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 4000;
    TextView name,madeby;
    ImageView image;
    Animation topAnim,botAnim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        botAnim=AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        image=findViewById(R.id.imageView);
        name=findViewById(R.id.textView);
        madeby=findViewById(R.id.textView2);

        image.setAnimation(topAnim);
        name.setAnimation(botAnim);
        madeby.setAnimation(botAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(MainActivity.this,login.class);
                startActivity(intent);
                finish();


            }
        },SPLASH_SCREEN);

    }
}
