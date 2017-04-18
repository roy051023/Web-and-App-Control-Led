package com.example.roy.test2;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ValueActivity extends AppCompatActivity {

    String data, name, pw, db, dataTime;
    Button btn4;
    TextView textView3;
    ConnectionClass connectionClass;
    ResultSet rs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_value);

        btn4 = (Button)findViewById(R.id.btn4);
        textView3 = (TextView)findViewById(R.id.textView3);


        Intent intent = this.getIntent();
        data = intent.getStringExtra("rs");
        name = intent.getStringExtra("name");
        pw = intent.getStringExtra("pw");
        db = intent.getStringExtra("db");

        String value[] = data.split("\n");
        String str = "";
        for(int i=2;i<value.length;i++){
            str += value[i] + "\n";
        }
        textView3.setTextSize(25);
        textView3.setText(str);


        btn4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setClass(ValueActivity.this, chooseActivity.class);
                intent.putExtra("rs", data);
                intent.putExtra("name", name);
                intent.putExtra("pw", pw);
                intent.putExtra("db", db);
                startActivity(intent);
                ValueActivity.this.finish();
            }
        });
    }

}
