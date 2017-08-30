package com.example.akshay_agrawal.nssattendance;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by Akshay_agrawal on 21-07-2017.
 */

public class markAttendance extends AsyncTask<String, Void, Void> {
    private Context context;
    ProgressDialog dialog;
    String result = "Got no result..";
    String id, day,sheet;

    public markAttendance(Context context) {
        this.context = context;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setTitle("Hey Wait Please...");
        dialog.setMessage("Marking Attendance..");
        dialog.show();
    }

    @Override
    protected Void doInBackground(String... params) {
        id = params[0];
        day = params[1];
        sheet = params[2];
        JSONObject jsonObject = Controller.insertData(id, day, sheet);
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
        if(result.equals("id not found")){
            AlertDialog diaBox = AskOption();
            diaBox.show();
        }
        else
        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
    }
    private android.support.v7.app.AlertDialog AskOption()
    {
        android.support.v7.app.AlertDialog myQuittingDialogBox =new android.support.v7.app.AlertDialog.Builder(context)
                .setTitle("ID not exist")
                .setMessage("Do you want to add this ID?")
                .setIcon(R.drawable.info)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intent = new Intent(context,InsertStudent.class).putExtra("date",sheet);
                        intent.putExtra("id",id);
                        context.startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;

    }
}
