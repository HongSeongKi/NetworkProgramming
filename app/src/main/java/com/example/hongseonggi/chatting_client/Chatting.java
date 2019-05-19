package com.example.hongseonggi.chatting_client;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Chatting extends AppCompatActivity {

    private Handler mHandler;
    private Socket socket;

    private BufferedReader networkReader;
    private BufferedWriter networkWriter;

    private String line;
    private String ip="127.0.0.1";
    private int port = 8888;

    private Runnable show = new Runnable(){
        public void run(){
            Toast.makeText(getApplicationContext(),"받은 문장 : "+line,Toast.LENGTH_LONG).show();
        }
    };

    private Thread printThread = new Thread(){
        public void run(){
            try{
                String str;
                while(true)
                {
                    str = networkReader.readLine();
                    line = str;
                    mHandler.post(show);
                }
            }catch (Exception e){

            }
        }
    };

   /* protected void onStop(){
        super.onStop();
        try{
            socket.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }*/

    Button button;
    EditText editText;
    TextView textView;
    Button paint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        mHandler = new Handler();
       /* try{
         //   setSocket(ip,port);
        }catch(IOException e1){
            e1.printStackTrace();
        }*/

        printThread.start();
        paint = (Button)findViewById(R.id.paint);
        button = (Button)findViewById(R.id.button);
        editText = (EditText)findViewById(R.id.editText);
        textView = (TextView)findViewById(R.id.textView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = editText.getText().toString();
                System.out.println(str);
                if(str ==null || str.equals(""))
                {
                }
                else{
                    textView.setText(textView.getText().toString()+"\n"+str);
                }
            }
        });

        paint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Painting.class);
                startActivity(intent);
            }
        });

    }

    /*public void setSocket(String ip,int port) throws IOException{
        try{
            socket = new Socket(ip,port);
            networkWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            networkReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch(IOException e){
            System.out.println(e);
            e.printStackTrace();
        }
    }*/
}
