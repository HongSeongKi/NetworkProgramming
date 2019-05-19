package com.example.kvlks.chatting_test;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;


public class MainActivity extends AppCompatActivity {


    private Socket socket;

    private BufferedReader networkReader;
    private BufferedWriter networkWriter;

    private String ip = "192.168.0.7"; // IP
    private int port = 9100; // PORT번호

    private String globalLine;
    EditText et;
    TextView tv;
    Button send_btn;
    Button file_send_btn;
    Button file_recv_btn;
    ImageView myImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et = (EditText) findViewById(R.id.EditText01);
        send_btn = (Button) findViewById(R.id.Button01);
        file_recv_btn = (Button) findViewById(R.id.Button02);
        file_send_btn = (Button) findViewById(R.id.button03);
        tv = (TextView) findViewById(R.id.chatting);

        send_btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (et.getText().toString() != null || !et.getText().toString().equals("")) {
                    PrintWriter out = new PrintWriter(networkWriter, true);
                    String return_msg = et.getText().toString();
                    out.println(return_msg);
                    et.setText("");
                }
            }
        });

        file_recv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrintWriter out = new PrintWriter(networkWriter, true);
                out.println("image_send_server_to_client@stoc_image.jpg");

            }
        });

        file_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrintWriter out = new PrintWriter(networkWriter, true);
                File file = new File(Environment.getExternalStorageDirectory() + "/ctos_image.jpg");
                long file_length = file.length();
                out.println("image_send_client_to_server@ctos_image.jpg@" + String.valueOf(file_length));

                tv.setText(tv.getText() + "파일 송신 요청 : " + String.valueOf(file_length) + "\n");

                try {
                    FileInputStream fis = new FileInputStream(file);
                    DataInputStream dis = new DataInputStream(fis);
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                    int len;
                    int total_len = 0;
                    int size = 1024;
                    byte[] data = new byte[size];
                    while ((len = dis.read(data)) != -1) {
                        total_len += len;
                        dos.write(data);
                    }
                    dos.flush();

                    tv.setText(tv.getText() + "파일 송신 완료 : " + String.valueOf(total_len) + "\n");
                }catch (Exception e) { }
            }
        });

        connect_and_check.start();

        AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);

        ad.setTitle("닉네임 입력");
        final EditText d_et = new EditText(MainActivity.this);
        ad.setView(d_et);
        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PrintWriter out = new PrintWriter(networkWriter, true);
                String return_msg = d_et.getText().toString();
                out.println(return_msg);
            }
        });

        ad.show();
    }

    Thread connect_and_check = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                setSocket(ip, port);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String line = "";
            myImg = (ImageView) findViewById(R.id.imageView);


            while (true) {
                try {
                    line = networkReader.readLine().toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                globalLine = line;
                if(globalLine.split("@")[0].equals("image_send_server_to_client"))
                {
                    String file_path = Environment.getExternalStorageDirectory() + "/" + globalLine.split("@")[1];

                    //File dir = new File(dirPath);
                    //if(!dir.exists()){
                    //    dir.mkdir();
                    //}

                    File file = new File(file_path);
                    FileOutputStream output = null;

                    byte[] buf = new byte[1024];
                    int total_size = 0;
                    int max_size = Integer.parseInt(globalLine.split("@")[2]);
                    int recv_size = 0;
                    try {
                        output = new FileOutputStream(file);

                        while ((recv_size = socket.getInputStream().read(buf)) > 0) {

                            output.write(buf);
                            output.flush();

                            total_size += recv_size;
                            if (total_size >= max_size) {
                                output.close();
                                break;
                            }

                        }
                    }
                    catch (IOException e) { e.printStackTrace(); }

                    final int inner_size = total_size;
                    final int inner_max = max_size;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText(tv.getText() + "이미지 수신 완료: " + String.valueOf(inner_size) + "max: " + String.valueOf(inner_max)  + "\n");
                            Uri uri = Uri.parse("file:///" + Environment.getExternalStorageDirectory() + "/stoc_image.jpg");
                            myImg.setImageURI(uri);
                        }
                    });

                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText(tv.getText() + globalLine + "\n");
                        }
                    });
                }
            }
        }
    });


    public void setSocket(String ip, int port) throws IOException {

        try {
            socket = new Socket(ip, port);
            networkWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            networkReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            MyGlobals.getInstance().setNetworkReader(networkReader);
            MyGlobals.getInstance().setNetworkWriter(networkWriter);

        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }

    }
}