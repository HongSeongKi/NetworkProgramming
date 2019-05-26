package com.example.hongseonggi.chatting_client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView ima = (ImageView)findViewById(R.id.imageView);

        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(ima);
        Glide.with(this).load(R.drawable.main).into(imageViewTarget);
        ImageView chatBut = (ImageView)findViewById(R.id.chat);
      //  Button paint = (Button)findViewById(R.id.paint);

        chatBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Chatting.class);
                startActivity(intent);
            }
        });
    }
}
