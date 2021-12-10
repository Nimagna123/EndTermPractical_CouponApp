package com.swastikcolors.couponApp.Controller;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.widget.Toast;

import com.swastikcolors.couponApp.Utility.UploadAndDownload;

import java.util.HashMap;
import dmax.dialog.SpotsDialog;

import static com.swastikcolors.couponApp.Utility.UploadAndDownload.setURLVariables;

public class CallUrlAsyncClass extends AsyncTask<StringBuilder, Void, String> {

    public StringBuilder error;
    private Context context;
    public AlertDialog alertDialog;
    private String url;
    public boolean showDialog;

    protected CallUrlAsyncClass(Context context, String url , boolean showDialog){
        this.context = context;
        this.url = url;
        if(showDialog && Looper.myLooper() == Looper.getMainLooper()) {
            alertDialog = new SpotsDialog.Builder().setContext(context).build();
            alertDialog.setMessage("Loading");
            alertDialog.show();
        }
        this.showDialog = showDialog;
        error = new StringBuilder();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(showDialog){

        }
    }

    @Override
    protected String doInBackground(StringBuilder... stringBuilders) {
        HashMap<String, String> params = new HashMap<>();
        final StringBuilder sbParams = setURLVariables(params);
        String result = UploadAndDownload.downloadJSON(url,sbParams);
        if (result.startsWith("Error")) {
            error.append(result);
            return null;
        }
        return result;
    }

    @Override
    protected void onPostExecute(String data) {
        super.onPostExecute(data);
        if(showDialog && alertDialog!=null){
            alertDialog.cancel();
            if(error.length()>0){
                Toast.makeText(context,error.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
