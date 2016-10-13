package com.example.iak.currency;

/**
 * Created by iak on 11/10/2016.
 */
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Func {
    public static void showData(final Context context, ArrayList<String[]> result, TableLayout tl ) {
        tl.removeAllViewsInLayout();
        int k = 10000;
        String[] align = new String[result.get(0).length];
        TableRow tr;
        for (int i = 0; i < result.size(); i++) {
            tr = new TableRow(context);
            final TableRow finalTr = tr;
            tr.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    TextView tv=(TextView) finalTr.getChildAt(0);
                    final String quote=tv.getText().toString();
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:

                                    db db=new db(context);

                                    db.del("currency","quote=?", new String[] { quote.toLowerCase()});
                                    finalTr.removeAllViews();
                                    //Do your Yes progress
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //Do your No progress
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder ab = new AlertDialog.Builder(context);
                    ab.setMessage("Are you sure to delete "+quote+" ?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
            });
            tr.setId(k++);
            tr.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            String[] s = result.get(i);
            if (s.length>2) {
                if (s[1].equals("`") || s[2].equals("Total")) {
                    tr.setBackgroundColor(Color.LTGRAY);

                } else {
                    tr.setBackgroundColor(Color.CYAN);
                }
            } else {
                if (i%2==0) {
                    tr.setBackgroundColor(Color.LTGRAY);
                } else {
                    tr.setBackgroundColor(Color.CYAN);
                }
            }


            for (int j = 0; j < s.length; j++) {

                TextView tv = new TextView(context);

                tv.setId(k++);
                tv.setPadding(5, 5, 20, 5);
                if (s.length<3) {
                    tv.setMaxWidth(tl.getWidth()/s.length);

                } else {
                    tv.setMaxWidth(200);
                }

                if (i == 0) {
                    if (s[j].contains("`r")) {
                        s[j] = s[j].replaceAll("`r", "");
                        align[j] = "right";
                    } else {
                        align[j] = "";
                    }
                    tr.setBackgroundColor(Color.GRAY);
                    tv.setTextColor(Color.WHITE);
                } else {

                    tv.setTextColor(Color.BLACK);
                }
                if (align[j].equals("right")) {
                    tv.setGravity(Gravity.END);
                }
                tv.setText(s[j]);
                TableRow.LayoutParams params = new TableRow.LayoutParams();
                tr.addView(tv);

            }
            tl.addView(tr, new TableLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    }
    public static String getResult(String url) {
        URL obj = null;
        StringBuilder builder = new StringBuilder();

        try {
            obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());


            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            wr.flush();
            wr.close();

            String aux = "";
            while ((aux = in.readLine()) != null) {
                builder.append(aux);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
    public static String toDoubleStr(String dbl) {
        try {
            return String.format("%.0f", Double.parseDouble(dbl));
        } catch (Exception e) {
            return "0";
        }
    }
}
