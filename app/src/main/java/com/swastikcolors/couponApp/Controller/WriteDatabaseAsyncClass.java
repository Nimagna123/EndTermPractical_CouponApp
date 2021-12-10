package com.swastikcolors.couponApp.Controller;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.swastikcolors.couponApp.Common;
import com.swastikcolors.couponApp.Constants;
import com.swastikcolors.couponApp.Utility.ManageJson;
import com.swastikcolors.couponApp.Utility.UploadAndDownload;

import java.util.HashMap;
import dmax.dialog.SpotsDialog;

import static com.swastikcolors.couponApp.Utility.UploadAndDownload.setURLVariables;

public class WriteDatabaseAsyncClass extends AsyncTask<StringBuilder, String, String> {
    protected StringBuilder error;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private AlertDialog alertDialog;
    private String table;
    private String operation;
    private HashMap<String,String> data;
    private HashMap<String,String> filter;
    private boolean showDialog;

    public WriteDatabaseAsyncClass(Context context, String table, String operation,
                                   HashMap<String, String> data, HashMap<String, String> filter,
                                   boolean showDialog){
        if(filter==null){
            filter = new HashMap<>();
        }
        this.context = context;
        this.table = table;
        this.operation = operation;
        this.data  = data;
        this.filter = filter;
        this.showDialog = showDialog;
        if(this.showDialog) {
            alertDialog = new SpotsDialog.Builder().setContext(context).build();
            alertDialog.setMessage("Loading");
        }
        error = new StringBuilder();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(showDialog){
            alertDialog.show();
        }
    }

    @Override
    protected String doInBackground(StringBuilder... stringBuilders) {
        HashMap<String, String> params = new HashMap<>();
        params.put("cuser", Common.cuser);
        params.put("table", table);
        params.put("operation", operation);
        params.put("data", ManageJson.objectToJsonString(data));
        params.put("filter", ManageJson.objectToJsonString(filter));
        final StringBuilder sbParams = setURLVariables(params);
        String result = UploadAndDownload.downloadJSON(Constants.writeDatabase,sbParams);
        if (result.startsWith("Error")) {
            error.append(result);
        }
        return result;
    }

    @Override
    protected void onPostExecute(String data) {
        super.onPostExecute(data);
        if(showDialog){
            alertDialog.cancel();
            if(error.length()>0){
                Toast.makeText(context,error, Toast.LENGTH_LONG).show();
            }
        }
    }
}
