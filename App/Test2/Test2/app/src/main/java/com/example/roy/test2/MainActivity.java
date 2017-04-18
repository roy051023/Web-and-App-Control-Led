package com.example.roy.test2;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    ConnectionClass connectionClass;

    Button login;
    EditText username,password, database;
    ProgressBar progressBar;
    String name, pw, db;

    ResultSet rs1, rs2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        connectionClass = new ConnectionClass();

        login = (Button) findViewById(R.id.button);
        username = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);
        database = (EditText) findViewById(R.id.editText3);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                name = username.getText().toString();
                pw = password.getText().toString();
                db = database.getText().toString();
                DoLogin  doLogin = new DoLogin();
                doLogin.execute("");
            }
        });




    }

    public class DoLogin extends AsyncTask<String,String,String> {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            progressBar.setVisibility(View.GONE);

            if(isSuccess) {
                Toast.makeText(MainActivity.this, "Login successfull",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, chooseActivity.class);
                intent.putExtra("rs", z);
                intent.putExtra("name", name);
                intent.putExtra("pw", pw);
                intent.putExtra("db", db);
                startActivity(intent);
                MainActivity.this.finish();
            }
            else
                Toast.makeText(MainActivity.this,r, Toast.LENGTH_SHORT).show();

        }

        @Override
        protected String doInBackground(String... params) {
            if(name.equals(""))
                z = "Please enter User Id and Password";
            else
            {
                try {
                    Connection con = connectionClass.CONN(name, pw, db);
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {
                        String query1 = "select * from switch";
                        //String query2 = "select * from light ORDER BY 'time' ASC";
                        String query2 = "select * from light ORDER BY 'time' ASC";
                        Statement stmt = con.createStatement();
                        Statement stmt1 = con.createStatement();
                        rs1 = stmt.executeQuery(query1);
                        rs2 = stmt1.executeQuery(query2);

                        if(!rs1.wasNull())
                        {
                            z = "Login successfull\n";
                            while (rs1.next())
                               z += rs1.getString("status") + "\n";
                            while (rs2.next())
                                z += rs2.getString("time") + " : " + rs2.getString("data") + "\n";
                            isSuccess=true;
                        }
                        else
                        {
                            z = "Invalid Credentials";
                            isSuccess = false;
                        }

                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    z = "Exceptions";
                }
            }
            return z;
        }
    }
}
