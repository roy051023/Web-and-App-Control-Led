package com.example.roy.test2;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ControlActivity extends AppCompatActivity {
    ImageView led;
    Button btn1, btn2, btn3;
    String data, name, pw, db;
    ConnectionClass connectionClass;
    ResultSet rsOn, rsOff;
    String value[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        connectionClass = new ConnectionClass();

        led = (ImageView)findViewById(R.id.imageView3);
        btn3 = (Button)findViewById(R.id.btn3);
        btn2 = (Button)findViewById(R.id.btn2);
        btn1 = (Button)findViewById(R.id.btn1);

        Intent intent = this.getIntent();
        data = intent.getStringExtra("rs");
        name = intent.getStringExtra("name");
        pw = intent.getStringExtra("pw");
        db = intent.getStringExtra("db");

        value = data.split("\n");
        if(value[1].equals("on"))
            led.setImageResource(R.drawable.on);
        else
            led.setImageResource(R.drawable.off);

        btn3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setClass(ControlActivity.this, chooseActivity.class);
                intent.putExtra("rs", data);
                intent.putExtra("name", name);
                intent.putExtra("pw", pw);
                intent.putExtra("db", db);
                startActivity(intent);
                ControlActivity.this.finish();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DoOff doOff = new DoOff();
                doOff.execute("");
            }
        });

        btn1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DoOn doOn = new DoOn();
                doOn.execute("");
            }
        });
    }


    public class DoOn extends AsyncTask<String,String,String> {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String r) {

            if(isSuccess) {
                //Toast.makeText(ControlActivity.this,"LED on",Toast.LENGTH_SHORT).show();
                led.setImageResource(R.drawable.on);
                value[1] = "on";
                String str = "";
                for(int i = 0;i < value.length;i++){
                    str += value[i] + "\n";
                }
                data = str;

            }
            else
                Toast.makeText(ControlActivity.this,r, Toast.LENGTH_SHORT).show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Connection con = connectionClass.CONN(name, pw, db);
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else{
                    String query = "select * from switch";
                    Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    rsOn = stmt.executeQuery(query);
                    rsOn.last();
                    rsOn.updateString("status","on");
                    rsOn.updateRow();
                    isSuccess=true;
                }

            }catch (Exception ex){
                isSuccess = false;
                z = ex.getMessage();

            }
            return z;
        }
    }


    public class DoOff extends AsyncTask<String,String,String> {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String r) {

            if(isSuccess) {
                //Toast.makeText(ControlActivity.this,"LED off",Toast.LENGTH_SHORT).show();
                led.setImageResource(R.drawable.off);
                value[1] = "off";
                String str = "";
                for(int i = 0;i < value.length;i++){
                    str += value[i] + "\n";
                }
                data = str;
            }
            else
                Toast.makeText(ControlActivity.this,r, Toast.LENGTH_SHORT).show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Connection con = connectionClass.CONN(name, pw, db);
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else{
                    String query = "select * from switch";
                    Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    rsOff = stmt.executeQuery(query);
                    rsOff.last();
                    rsOff.updateString("status","off");
                    rsOff.updateRow();
                    isSuccess=true;
                }

            }catch (Exception ex){
                isSuccess = false;
                z = ex.getMessage();

            }
            return z;
        }
    }

}
