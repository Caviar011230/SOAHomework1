package com.example.homework1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Button mBtnTouristInfo;
    private Button mBtnPostalCode;
    private String httpUrl="http://api.tianapi.com/travel/index?key=c0ca516a1ee7f0dc972cd114c3d85e2e&num=5";
    private String responseData;
    private String[] titles = new String[5];
    private Response response;
    private TextView[] news=new TextView[5];
    public void parseJSONWithJSONObject(String jsonData) {
        try {
            JSONObject jsonObj = new JSONObject(jsonData);
            String tempJSONObj = jsonObj.getString("newslist");
            // 把需要解析的数据传入到 JSONArray 对象中
            JSONArray jsonArray = new JSONArray(tempJSONObj);
            for (int i = 0;i < jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                titles[i] = jsonObject.getString("title");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void getNews(final String path) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();

                    RequestBody body = new FormBody.Builder()
                            .add("key", "c0ca516a1ee7f0dc972cd114c3d85e2e")
                            .add("num", "5")
                            .build();
                    Request request = new Request.Builder()
                            .url(path)
                            .post(body)
                            .build();
                    // 创建Call对象，并调用它的execute()方法发送请求并获取服务器返回的数据
                    response = client.newCall(request).execute();
                    responseData = response.body().string();
                    System.out.println(responseData);
                    parseJSONWithJSONObject(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    protected void onCreate(Bundle savedInstanceState) {
        if(titles[4]==null) {
            getNews(httpUrl);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnTouristInfo = (Button) findViewById(R.id.btn_tourist_info);
        news[0]=(TextView) findViewById(R.id.tv_main_news_1);
        news[1]=(TextView) findViewById(R.id.tv_main_news_2);
        news[2]=(TextView) findViewById(R.id.tv_main_news_3);
        news[3]=(TextView) findViewById(R.id.tv_main_news_4);
        news[4]=(TextView) findViewById(R.id.tv_main_news_5);
        while(titles[4]==null){

        }
        for(int i=1;i<=5;i++){
            news[i-1].setText(i+"."+titles[i-1]);
        }
        mBtnTouristInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,TouristInfoActivity.class);
                startActivity(intent);
            }
        });
        mBtnPostalCode = (Button) findViewById(R.id.btn_postal_code);
        mBtnPostalCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PostalCodeActivity.class);
                startActivity(intent);
            }
        });

    }
}