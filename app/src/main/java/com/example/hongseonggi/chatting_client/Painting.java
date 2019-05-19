package com.example.hongseonggi.chatting_client;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Painting extends AppCompatActivity {


    private DrawView drawing;
    private Button[] colorbtn;
    private Button reset, save;

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

        drawing = (DrawView) findViewById(R.id.drawingView);
        reset = (Button) findViewById(R.id.reset);
        save = (Button) findViewById(R.id.save);

        colorbtn = new Button[4];
        colorbtn[0] = (Button) findViewById(R.id.colorselect);
        colorbtn[1] = (Button) findViewById(R.id.up);
        colorbtn[2] = (Button) findViewById(R.id.down);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawing.reset();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawing.save(Painting.this);
            }
        });
    }
         /*save.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                View rootView = getWindow().getDecorView();

                File screenShot = ScreenShot(rootView);
                if(screenShot!=null){
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(screenShot)));
                }
                Toast.makeText(getApplicationContext(),"저장완료",Toast.LENGTH_LONG).show();
             }
         }); //그림을 서버로 보내기
    }
    public File ScreenShot(View view){
        view.setDrawingCacheEnabled(true);  //화면에 뿌릴때 캐시를 사용하게 한다

        Bitmap screenBitmap = view.getDrawingCache();   //캐시를 비트맵으로 변환

        String filename = "screenshot.png";
        File file = new File(Environment.getExternalStorageDirectory(), filename);  //screenshot.png 파일
        FileOutputStream os = null;
        try{
            os = new FileOutputStream(file);
            screenBitmap.compress(Bitmap.CompressFormat.PNG, 90, os);   //비트맵을 PNG파일로 변환
            os.close();
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }

        view.setDrawingCacheEnabled(false);
        return file;
    }*/



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
                Toast.makeText(getApplicationContext(),"선 굵기 : "+drawing.getLineWith(),Toast.LENGTH_LONG).show();
                break;
            case R.id.down:
                if(drawing.getLineWith()>1)
                    drawing.setLineWith(drawing.getLineWith()-1);
                Toast.makeText(getApplicationContext(),"선 굵기 : "+drawing.getLineWith(),Toast.LENGTH_LONG).show();
        }
    }
}
