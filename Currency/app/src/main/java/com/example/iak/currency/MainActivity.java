package com.example.iak.currency;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TableLayout tl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tl=(TableLayout) findViewById(R.id.tbl);

    }

    public void add_click(View v) {
        db db =new db(this);
        db.execMe("delete from currency where quote='rmbidr'");

        ArrayList<String> params=new ArrayList<>();
        String quote=((EditText) findViewById(R.id.quote)).getText().toString();
        params.add(quote);
        quote=db.select_single("select quote from currency where quote=?", params);
        if (quote.equals("")) {
            db.execMe("insert into currency(quote) values(?)", params);
        }
        refresh_click(v);
    }
    public void refresh_click(View v) {
        String[] s;
        ArrayList<String[]> result=new ArrayList<>();
        db db= new db(this);
        while (!isNetworkAvailable()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (isNetworkAvailable()) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }

        Cursor c=db.query("select quote from currency");
        c.moveToFirst();
        result.clear();
        s=new String[5];
        s[0]="Quote";
        s[1]="Bloomberg`r";
        s[2]="Google`r";
        s[3]="Yahoo`r";
        s[4]="BCA`r";
        result.add(s);
        String u="";
        while (!c.isAfterLast()) {
            s = new String[5];
            s[0] = c.getString(0).toUpperCase();
            u+="&quote[]="+c.getString(0).toUpperCase();
            result.add(s);
            c.moveToNext();
        }

        String url="http://currency-iskandartio.rhcloud.com/services.php?"+u;
        String url_result=Func.getResult(url);
        try {
            JSONObject o= new JSONObject(url_result);
            String bloomberg[]=o.getString("bloomberg").split(",");
            String google[]=(o.getString("google")+" ").split(",");
            String yahoo[]=o.getString("yahoo").split(",");
            String bca[]=o.getString("bca").split(",");
            for(int i=1;i<result.size();i++) {
                s= result.get(i);
                s[1]=Func.toDoubleStr(bloomberg[i-1]);
                s[2]=Func.toDoubleStr(google[i-1]);
                s[3]=Func.toDoubleStr(yahoo[i-1]);
                s[4]=Func.toDoubleStr(bca[i-1]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Func.showData(this, result, tl);
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
