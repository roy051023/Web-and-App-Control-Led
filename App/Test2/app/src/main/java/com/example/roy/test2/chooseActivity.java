package com.example.roy.test2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class chooseActivity extends AppCompatActivity {

    Button btnSwitch, btnValue;
    String data, name, pw, db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        btnSwitch = (Button)findViewById(R.id.button2);
        btnValue = (Button)findViewById(R.id.button3);

        Intent intent = this.getIntent();
        data = intent.getStringExtra("rs");
        name = intent.getStringExtra("name");
        pw = intent.getStringExtra("pw");
        db = intent.getStringExtra("db");



        btnSwitch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setClass(chooseActivity.this, ControlActivity.class);
                intent.putExtra("rs", data);
                intent.putExtra("name", name);
                intent.putExtra("pw", pw);
                intent.putExtra("db", db);
                startActivity(intent);
                chooseActivity.this.finish();
            }
        });

        btnValue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setClass(chooseActivity.this, ValueActivity.class);
                intent.putExtra("rs", data);
                intent.putExtra("name", name);
                intent.putExtra("pw", pw);
                intent.putExtra("db", db);
                startActivity(intent);
                chooseActivity.this.finish();
            }
        });
    }
}
