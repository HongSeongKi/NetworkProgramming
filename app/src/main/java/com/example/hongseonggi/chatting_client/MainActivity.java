package com.example.hongseonggi.chatting_client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button chatBut = (Button)findViewById(R.id.chat);
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
