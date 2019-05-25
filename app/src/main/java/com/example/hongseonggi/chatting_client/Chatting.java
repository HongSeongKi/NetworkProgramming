package com.example.hongseonggi.chatting_client;

import android.content.Intent;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

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

    private DrawView drawing;
     ListView listView;
    ArrayList<String> Data ;
    ImageView button;  //확인
    EditText editText;
    TextView textView;
    ImageView paint;// 그림
    ChattingAdapter adapter;
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

        paint = (ImageView) findViewById(R.id.paint); //
        button = (ImageView) findViewById(R.id.button); //확인버튼
        editText = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(R.id.listView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = editText.getText().toString();
                System.out.println(str);
                if (str == null || str.equals("")) {
                } else {
                    String content = editText.getText().toString();
                    adapter.addItem(new Useritem("성기",content));
                }
            }
        });



        paint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Painting.class);
                startActivity(intent);
            }
        });

        adapter = new ChattingAdapter();
        adapter.addItem(new Useritem("희원","나는 갓이다잉"));
        adapter.addItem(new Useritem("지혜","내가 더 갓이다잉"));
        adapter.addItem(new Useritem("성기","너희 둘이가 짱이다잉"));
        listView.setAdapter(adapter);
    }

    public class ChattingAdapter extends BaseAdapter {
        ArrayList<Useritem> items = new ArrayList<Useritem>();

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void addItem(Useritem item) {
            items.add(item);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            UseritemView view = new UseritemView(getApplicationContext());
            Useritem item = items.get(position);

            view.setName(item.getName());
            view.setContents(item.getContents());
            return view;

        }
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
