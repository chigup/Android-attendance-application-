package com.example.akshay_agrawal.nssattendance;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class InsertStudent extends AppCompatActivity {
    Intent intent;
    EditText id, name;
    Button insert;
    String s_id, s_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_student);
        id = (EditText)findViewById(R.id.input_roll_no);
        name = (EditText)findViewById(R.id.input_student_name);
        intent = getIntent();
        id.setText(intent.getStringExtra("id"));
        insert = (Button)findViewById(R.id.proceed);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = getIntent();
                s_id = id.getText().toString();
                s_name = name.getText().toString();
                Date today = Calendar.getInstance().getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");//formating according to my need
                String date = formatter.format(today);
                if (s_id.isEmpty()){
                    id.setError("Can't be empty");
                }
                else if(s_name.isEmpty()){
                    name.setError("Can't be empty");
                }
                else{
                    new insertStudent().execute(s_id,s_name,date.substring(date.length()-7));
                    //Log.i(Controller.TAG, "testing"+ s_id+ " " +s_name+ " "+ date);
                }
            }
        });
    }

    class insertStudent extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog;
        String result = "Got some Error..";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(InsertStudent.this);
            dialog.setTitle("Hey Wait Please...");
            dialog.setMessage("Adding Student..");
            dialog.show();
        }
        @Nullable
        @Override
        protected Void doInBackground(String... params) {
            String id = params[0];
            String name = params[1];
            String sheet = params[2];
            JSONObject jsonObject = Controller.updateData(id,name,sheet);
            Log.i(Controller.TAG, "Json obj ");
            try {
                if (jsonObject != null) {
                    result = jsonObject.getString("result");
                }
            } catch (JSONException je) {
                Log.i(Controller.TAG, "" + je.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
    }
}
