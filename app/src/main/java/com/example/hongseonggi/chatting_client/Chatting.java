package com.example.hongseonggi.chatting_client;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Chatting extends AppCompatActivity {

    private Handler mHandler;
    private Socket socket;

    private BufferedReader networkReader;
    private BufferedWriter networkWriter;

    private String line;
    private String ip= "192.168.35.17";
    private int port = 9100;
    private static final int PICK_FROM_ALBUM =1;
    String globalLine;
    String myNickName;
    private File tempFile;

    private DrawView drawing;
    ListView listView;
    ImageView camera;
    ArrayList<String> Data ;
    ImageView ok;  //확인
    EditText editText;
    TextView textView;
    ImageView paint;// 그림
    ChattingAdapter adapter;
    Uri photoUri;
    String imageString=null;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_FROM_ALBUM)
        {
            photoUri = data.getData();

            Cursor cursor = null;
            try {

                /*
                 *  Uri 스키마를
                 *  content:/// 에서 file:/// 로  변경한다.
                 */
                String[] proj = { MediaStore.Images.Media.DATA };

                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            String str = tempFile.toString();
            System.out.println(str);
            //ArrayList<String> list = new ArrayList<String>();
            imageString = str.split("/0/")[1];
            System.out.println(imageString);


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        tedPermission();
        mHandler = new Handler();



        camera = (ImageView)findViewById(R.id.camera);//카메라(사진)
        paint = (ImageView) findViewById(R.id.paint); //그림
        ok = (ImageView) findViewById(R.id.ok); //확인버튼
        editText = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(R.id.listView);

        connect_and_check.start();

        AlertDialog.Builder ad = new AlertDialog.Builder(Chatting.this);

        ad.setTitle("닉네임 입력");
        final EditText d_et = new EditText(Chatting.this);
        ad.setView(d_et);
        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PrintWriter out = new PrintWriter(networkWriter, true);
                String return_msg = d_et.getText().toString();
                myNickName = d_et.getText().toString();
                out.println(return_msg);
            }
        });

        ad.show();

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);

            }
        });



        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString() != null && !editText.getText().toString().equals("")) {
                    PrintWriter out = new PrintWriter(networkWriter, true);
                    out.println(myNickName + "@normal_chatting@" + editText.getText().toString());
                    editText.setText("");
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
            adapter = new ChattingAdapter();

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
                    File file = new File(file_path);
                    FileOutputStream output = null;

                    byte[] buf = new byte[1024];
                    int total_size = 0;
                    int max_size = Integer.parseInt(globalLine.split("@")[2]);
                    int recv_size = 0;
                    try {
                        output = new FileOutputStream(file);
                        InputStream is = socket.getInputStream();
                        DataInputStream dis = new DataInputStream(is);

                        while ((recv_size = dis.read(buf)) != -1) {
                            total_size += recv_size;output.write(buf, 0, recv_size);
                            output.flush();
                            if (total_size >= max_size) {
                                output.close();
                                break;
                            }
                        }
                    }
                    catch (IOException e) { e.printStackTrace(); }


                }
                else if (globalLine.split("@")[1].equals("normal_chatting"))
                { //리스트뷰 채팅 추가하는 코드
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Useritem item = new Useritem( globalLine.split("@")[0],null,globalLine.split("@")[2]);
                            if(globalLine.split("@")[0].equals("h"))
                            {
                                item.setResId(R.drawable.camera2);
                            }
                            else if(globalLine.split("@")[0].equals("s"))
                            {
                                item.setResId(R.drawable.up);
                            }
                            else if(globalLine.split("@")[0].equals("j"))
                            {
                                item.setResId(R.drawable.down);
                            }
                            else {
                                item.setResId(-1);
                            }
                            adapter.addItem(item);
                            listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            listView.setSelection(adapter.getCount() - 1);
                        }
                    });
                }
            }
        }
    });

    public void tedPermission(){
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }


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
            UseritemView view =null;
            if(convertView == null)
            {
                view = new UseritemView(getApplicationContext());
            }else{
                view = (UseritemView)convertView;
            }
            Useritem item = items.get(position);

            view.setImage(item.getResId());
            view.setName(item.getName());
            view.setContents(item.getContents());
            view.setUri(item.getUri());
            return view;
        }
    }

}