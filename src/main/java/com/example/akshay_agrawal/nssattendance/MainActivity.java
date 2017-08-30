package com.example.akshay_agrawal.nssattendance;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private int mYear, mMonth, mDay;
    TextView dateview;
    EditText student_id;
    private IntentIntegrator qrScan;
    private Button buttonScan, update, set_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonScan = (Button) findViewById(R.id.scan_rollno);
        update = (Button) findViewById(R.id.insert_btn);
        set_date = (Button)findViewById(R.id.change_date) ;
        dateview = (TextView) findViewById(R.id.dateview);
        student_id = (EditText) findViewById(R.id.Student_id);

        qrScan = new IntentIntegrator(this);
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");//formating according to my need
        String date = formatter.format(today);
        setDate(date);

        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrScan.setBeepEnabled(true);
                qrScan.initiateScan();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (InternetConnection.checkConnection(getApplicationContext())) {
                    String id= student_id.getText().toString();
                    String current_date = dateview.getText().toString();
                    if (id.isEmpty())
                        student_id.setError("Can't be empty");
                    else
                        new markAttendance(MainActivity.this).execute(id, current_date.substring(0,2),current_date.substring(current_date.length()-7) );
                } else {
                    Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });
        set_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose_date();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_settings:
                Intent i=new Intent(MainActivity.this,AboutUs.class);
                startActivity(i);
                break;
            default:
                break;
        }

        return true;
    }

    public void choose_date() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        monthOfYear += 1;
                        setDate((dayOfMonth <= 9 ? "0" + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth)) + "/" + (monthOfYear <= 9 ? "0" + String.valueOf(monthOfYear) : String.valueOf(monthOfYear)) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();

    }

    public void setDate(String date) {
        dateview.setText(date);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    student_id.setText((result.getContents()));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}