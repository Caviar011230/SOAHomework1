package com.example.homework1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TouristInfoActivity extends AppCompatActivity {
    private boolean end=false;
    private boolean get=false;
    private Button mBtnBack;
    private String provinceSelected;
    private String citySelected;
    public String provincePath="https://restapi.amap.com/v3/config/district?substrict=1&key=efc3347eb9e730e347f3874cc6da92a1&output=xml";
    public String cityPath="https://restapi.amap.com/v3/config/district?key=efc3347eb9e730e347f3874cc6da92a1&output=xml&keywords=";
    private Button mBtnSearch;
    private TextView test;
    private String responseData;
    private Response response;
    private String responseData2;
    private Response response2;
    private Spinner provinceSpinner;
    private Spinner citySpinner;
    private ArrayList<String> provinceList = new ArrayList<>();
    private ArrayList<String> cityList = new ArrayList<>();
    //private ArrayAdapter<String> pro_adapter;
    private void parseXMLWithPullProvince(String xmlData) {
        try {
            //xmlData = xmlData.substring(39);
            //获取一个XmlPullParserFactory实例对象
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            //通过XmlPullParserFactory实例对象获得一个XmlPullParser对象
            XmlPullParser xmlPullParser = factory.newPullParser();
            //将XML数据设置进去
            xmlPullParser.setInput(new StringReader(xmlData));
            //通过getEventType()得到当前的解析事件
            int eventType = xmlPullParser.getEventType();
            provinceList.add("请选择省份");
            //开始解析，如果当前的解析事件不等于XmlPullParser.END_DOCUMENT，则表示解析还没完成
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    //开始解析某个节点
                    case XmlPullParser.START_TAG:{
                        if ("name".equals(nodeName)) {
                            String tempProvince=xmlPullParser.nextText();
                            if(!tempProvince.equals("中华人民共和国")){
                                provinceList.add(tempProvince);
                            }
                        }

                        break;
                    }
                    //完成解析某个节点
                    case XmlPullParser.END_TAG:{
                        if ("app".equals(nodeName)) {
                            Log.d("MainActivity",provinceList.get(0));

                        }
                        break;
                    }
                    default:
                        break;
                }
                eventType = xmlPullParser.next();

            }
            try{

            } catch (Exception e2){
                e2.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getProvince(final String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    test = (TextView) findViewById(R.id.test);
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(path)
                            .build();
                    // 创建Call对象，并调用它的execute()方法发送请求并获取服务器返回的数据
                    response = client.newCall(request).execute();
                    responseData = response.body().string();
                    System.out.println(responseData);
                    parseXMLWithPullProvince(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void parseXMLWithPullCity(String xmlData) {
        try {
            //xmlData = xmlData.substring(39);
            //获取一个XmlPullParserFactory实例对象
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            //通过XmlPullParserFactory实例对象获得一个XmlPullParser对象
            XmlPullParser xmlPullParser = factory.newPullParser();
            //将XML数据设置进去
            xmlPullParser.setInput(new StringReader(xmlData));
            //通过getEventType()得到当前的解析事件
            int eventType = xmlPullParser.getEventType();
            //开始解析，如果当前的解析事件不等于XmlPullParser.END_DOCUMENT，则表示解析还没完成
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    //开始解析某个节点
                    case XmlPullParser.START_TAG:{
                        if ("name".equals(nodeName)) {
                            String tempCity=xmlPullParser.nextText();
                            if(!tempCity.equals(provinceSelected)){
                                cityList.add(tempCity);
                            }
                        }

                        break;
                    }
                    //完成解析某个节点
                    case XmlPullParser.END_TAG:{
                        if ("app".equals(nodeName)) {
                            Log.d("MainActivity",cityList.get(0));

                        }
                        break;
                    }
                    default:
                        break;
                }
                eventType = xmlPullParser.next();

            }
            try{

            } catch (Exception e2){
                e2.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getCity(final String path){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(path+provinceSelected)
                            .build();
                    // 创建Call对象，并调用它的execute()方法发送请求并获取服务器返回的数据
                    response2 = client.newCall(request).execute();
                    responseData2 = response2.body().string();
                    System.out.println(responseData2);
                    parseXMLWithPullCity(responseData2);
                    end=true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_info);
        getProvince(provincePath);
        try {
            Thread.sleep(1500);
        } catch (Exception e) {

        }
        while (provinceList.isEmpty()) {

        }
        mBtnBack = (Button) findViewById(R.id.btn_tourist_info_back);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        provinceSpinner = (Spinner) findViewById(R.id.sp_tourist_info_1);
        ArrayAdapter<String> pro_adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, provinceList);
        pro_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provinceSpinner.setAdapter(pro_adapter);
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                end = false;
                provinceSelected = provinceList.get(position);
                if (!provinceSelected.equals("请选择省份")) {
                    cityList.clear();
                    getCity(cityPath);
                    try{
                        Thread.sleep(1200);
                    } catch(Exception e){

                    }
                    while (!end) {

                    }


                    ArrayAdapter ci_adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, cityList);
                    ci_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(ci_adapter);
                    ci_adapter.notifyDataSetChanged();

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        citySpinner = (Spinner) findViewById(R.id.sp_tourist_info_2);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                citySelected = cityList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mBtnSearch = (Button) findViewById(R.id.btn_tourist_info_search);
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TouristInfoActivity.this, TouristInfoResActivity.class);
                intent.putExtra("province", provinceSelected);
                intent.putExtra("city", citySelected);
                startActivity(intent);
                finish();
            }
        });
    }
}