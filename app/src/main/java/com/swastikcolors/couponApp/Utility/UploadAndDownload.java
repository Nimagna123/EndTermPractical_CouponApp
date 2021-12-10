package com.swastikcolors.couponApp.Utility;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import static com.swastikcolors.couponApp.Utility.ManageJson.readLocalFile;

public class UploadAndDownload {

    public static String downloadJSON(final String urlWebService, StringBuilder sbParams) {
        try {
            URL url = new URL(urlWebService);
            System.out.println(urlWebService);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Charset", "UTF-8");
            con.connect();
            String paramsString = sbParams.toString();
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(paramsString);
            wr.flush();
            wr.close();
            StringBuilder sb = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String json;
            while ((json = bufferedReader.readLine()) != null) {
                sb.append(json).append("\n");
            }
            return sb.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error!!! Could not fetch data from server";
        }
    }

    public static String downloadFileFromServer(final String urlWebService,
                                                 StringBuilder sbParams,String filePath) {
        try {
            File file = new File(filePath);
            URL url = new URL(urlWebService);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.connect();
            String paramsString = sbParams.toString();
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(paramsString);
            wr.flush();
            wr.close();
            Log.i("H",urlWebService+" "+paramsString);
            File parFile = file.getParentFile();
            parFile.mkdirs();
            FileOutputStream fos = new FileOutputStream(file);
            InputStream is = con.getInputStream();

            byte[] buffer = new byte[4096];
            int len1;

            while ((len1 = is.read(buffer)) != -1)
            {
                fos.write(buffer, 0, len1);
            }
            fos.close();
            is.close();
            String data = readLocalFile(filePath);
            System.out.println(data);
            if(data.startsWith("Error")){
                file.delete();
                return data;
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error!!! File could not be downloaded. This might be an internet issue.";
        }
    }

    public static StringBuilder setURLVariables(HashMap<String, String> params) {
        StringBuilder sbParams = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0) {
                    sbParams.append("&");
                }
                try {
                    sbParams.append(key).append("=")
                            .append(URLEncoder.encode(params.get(key), "UTF-8"));
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            i++;
        }
        return sbParams;
    }

    public static String uploadFile(final String urlWebService,
                                    HashMap<String, String> params,String filePath) {
        String charset = "UTF-8";
        try {
            MultipartUtility multipart = new MultipartUtility(urlWebService, charset);
            File file = new File(filePath);
            multipart.addHeaderField("User-Agent", "CodeJava");
            multipart.addHeaderField("Test-Header", "Header-Value");
            for (String key : params.keySet()) {
                multipart.addFormField(key, params.get(key));
            }
            multipart.addFilePart("file", file);
            List<String> response = multipart.finish();
            StringBuilder output= new StringBuilder();
            for (String line : response) {
                System.out.println(line);
                output.append(line).append("\n");
            }
            return output.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error!!! File could not be uploaded.This may be internet issue.";
        }
    }

}
