package com.work.hsinwei.my0802_1_internet_img;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Handler handler;
    ImageView img;
    Bitmap bmp; //讀取圖片用
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler =new Handler();
        img =(ImageView)findViewById(R.id.imageView);
        tv=(TextView)findViewById(R.id.textView);
        //msg
        handler= new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                double s = (double)msg.obj;
                tv.setText("progress:"+s);
            }
        };
    }
    public void click1(View v)
    {
         new Thread()
        {

            @Override
            public void run ()
            {

                String strurl = "http://www.pcschool.tv/images/inside_logo.gif";
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte b[]=new byte[64];  //設定讀取byte大小
                try {
                    URL url = new URL(strurl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();   //網路連接到指定 URL
                    conn.setRequestMethod("GET");   //要取得網頁 xml 內容(原始碼)
                    conn.connect(); //啟動連線
                    Log.d("img","conected");
                    InputStream is = conn.getInputStream(); //IS是Java API
                    //msg
                    double totalLength =conn.getContentLength();
                    double sum=0;

                    int readSize=0;
                    while ((readSize=is.read(b))>0)
                    {
                        bos.write(b,0,readSize);
                        //msg
                        sum += readSize;
                        Message message = handler.obtainMessage(1,sum/totalLength*100);
                        handler.sendMessage(message);
                    }
                    byte[] result=bos.toByteArray();
                    bmp = BitmapFactory.decodeByteArray(result,0,result.length);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            img.setImageBitmap(bmp);
                        }
                    });
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
