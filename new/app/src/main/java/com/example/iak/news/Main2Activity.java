package com.example.iak.news;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class Main2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        TextView myTextView=(TextView) findViewById(R.id.tv2);
        Bundle extras = getIntent().getExtras();
        String url="http://news-iskandartio.rhcloud.com/services.php?type=link&link="+extras.get("text").toString();

        String url_result=Func.getResult(url);
        myTextView.setText(Html.fromHtml(url_result));
    }

}
