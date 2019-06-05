package com.example.hongseonggi.chatting_client;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Painting extends AppCompatActivity {


    private DrawView drawing;
    private ImageView[] colorbtn;
    private ImageView reset;
    private ImageView save;
    private LinearLayout layout;

    @Override // 다이알로그 띄워준 곳에서 받기

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101){
            if(data != null){
                int color = data.getIntExtra("color",0);
                drawing.setColor(color);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        layout = (LinearLayout)findViewById(R.id.LinearLayout);
        drawing = (DrawView) findViewById(R.id.drawingView);
        reset = (ImageView) findViewById(R.id.reset);
        save = (ImageView) findViewById(R.id.save);

        colorbtn = new ImageView[4];
        colorbtn[0] = (ImageView) findViewById(R.id.colorselect);
        colorbtn[1] = (ImageView) findViewById(R.id.up);
        colorbtn[2] = (ImageView) findViewById(R.id.down);

        final ArrayList<String> array = new ArrayList<String>() ;
        array.add("가");
        array.add("갸");
        array.add("거");
        array.add("겨");
        array.add("교");
        final String[] items = new String[5];

       //final String[] items={array.get(0),array.get(1),array.get(2),array.get(3),array.get(4)};
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawing.reset();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //drawing.save(Painting.this);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Painting.this);
                alertDialogBuilder.setTitle("선택하세요!!");
                alertDialogBuilder.setMessage("그림 or 글자 ?").setCancelable(false)
                        .setPositiveButton("그림",
                                new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                }
                                }).setNegativeButton("글자", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                            for(int i=0;i<5;i++)
                                            {
                                                items[i] = array.get(i);
                                            }
                                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Painting.this);
                                            alertDialogBuilder.setTitle("원하시는 글자를 선택하세요.");
                                            alertDialogBuilder.setItems(items, new DialogInterface.OnClickListener(){
                                                public void onClick(DialogInterface dialog, int id){
                                                    Toast.makeText(getApplicationContext(),items[id]+" 선택했습니다.",Toast.LENGTH_SHORT).show();
                                                }
                                        });
                                            AlertDialog alertDialog2 = alertDialogBuilder.create();
                                            alertDialog2.show();

                                        }
                                });
                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();
                                    }
        });
    }
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.colorselect:
                Intent intent = new Intent(getApplicationContext(),ColorDialog.class);
                startActivityForResult(intent,101);
                break;
            case R.id.up:
                drawing.setLineWith(drawing.getLineWith()+1);
                Toast.makeText(getApplicationContext(),"선 굵기 : "+drawing.getLineWith(),Toast.LENGTH_SHORT).show();
                break;
            case R.id.down:
                if(drawing.getLineWith()>1)
                    drawing.setLineWith(drawing.getLineWith()-1);
                Toast.makeText(getApplicationContext(),"선 굵기 : "+drawing.getLineWith(),Toast.LENGTH_SHORT).show();
        }
    }
}
