package com.example.iak.news;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private TableLayout tl;
    private ProgressDialog dialog;
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        return networkInfo != null && networkInfo.isConnected();
    }
    public String convertDate(String dateStr) {
        String result="";
        result=dateStr.substring(8,10)+"-"+dateStr.substring(5,7)+"-"+dateStr.substring(0,4);
        result+=" "+dateStr.substring(11,13)+":"+dateStr.substring(14,16);
        return result;
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tl=(TableLayout) findViewById(R.id.tbl);
        String[] s;
        ArrayList<String[]> result=new ArrayList<>();
        tl.setColumnCollapsed(2,true);
        s=new String[2];
        s[0]="News";
        s[1]="Link";

        result.add(s);
        while (!isNetworkAvailable()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        String url="http://news-iskandartio.rhcloud.com/services.php?type=refresh";
        String url_result=Func.getResult(url);
        try {
            JSONArray arr= new JSONArray(url_result);
            for (int i=0;i<arr.length();i++){
                JSONObject o=arr.getJSONObject(i);
                s=new String[2];

                s[0]= convertDate(o.getString("news_date"))+"<br>"+o.getString("title");
                s[1]=o.getString("link");
                result.add(s);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        dialog=new ProgressDialog(this);
        Func.showData(this, result, tl, dialog);
    }

    protected void onPostExecute(String str) {
        //Update your UI here.... Get value from doInBackground ....

    }
}
