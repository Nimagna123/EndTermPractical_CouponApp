package com.swastikcolors.couponApp;

public class Constants {
    public static final String department = "couponApp";
    public static final String rootUrl = "http://192.168.29.71:8080/swastikColorWebapi";
    //public static final String rootUrl = "http://157.245.102.117:8080/swastikColorWebapi";
    public static final String login = rootUrl+"/cuLogin";
    public static final String signUp = rootUrl+"/cuSignUp";
    public static final String readDatabase = rootUrl+"/bCReadDatabase";
    public static final String writeDatabase = rootUrl+"/bCWriteDatabase";
    public static final String downloadFile = rootUrl+"/bCDownloadFile";

    public static String loginNotAllowed = "Error!!! Wrong Details";
}
