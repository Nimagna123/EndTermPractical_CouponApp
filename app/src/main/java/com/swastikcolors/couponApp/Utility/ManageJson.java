package com.swastikcolors.couponApp.Utility;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ManageJson {

    public static String objectToJsonString(Object object) {
        if(object == null) {
            return "";
        }
        else {
            Gson gson =new Gson();
            return gson.toJson(object);
        }
    }

    public static Object jsonStringToObject(String jsonString, Class c) {
        Gson gson =new Gson();
        return gson.fromJson(jsonString, c);
    }

    public static Object[] jsonFileToObjectList(File file,Class c){
        try {
            Gson gson = new Gson();
            FileReader fileReader = new FileReader(file);
            Object[] objects = (Object[]) gson.fromJson(fileReader, c);
            fileReader.close();
            return objects;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Object jsonFileToObject(String filePath,Class c){
        try {
            Gson gson = new Gson();
            FileReader fileReader = new FileReader(filePath);
            Object object = gson.fromJson(fileReader, c);
            fileReader.close();
            return object;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Object getDataFromJSONObject(JsonObject jsonObject,String path)
    {
        try {
            if(jsonObject==null){
                return null;
            }
            String[] pathArray = path.split("/");
            for (int i = 0; i < pathArray.length - 1; i++) {
                if(!jsonObject.has(pathArray[i])){
                    return null;
                }
                jsonObject = (JsonObject) jsonObject.get(pathArray[i]);
            }
            JsonElement jsonElement;
            if (pathArray.length > 0) {
                if(!jsonObject.has(pathArray[pathArray.length - 1])){
                    return null;
                }
                jsonElement =  jsonObject.get(pathArray[pathArray.length - 1]);
            } else
                jsonElement =  jsonObject;
            if(jsonElement.isJsonPrimitive()){
                JsonPrimitive primitive = jsonElement.getAsJsonPrimitive();
                if(primitive.isString()){
                    return primitive.getAsString();
                }
                else if(primitive.isNumber()){
                    return primitive.getAsNumber();
                }
                else if(primitive.isBoolean()){
                    return primitive.getAsBoolean();
                }
                else{
                    return primitive;
                }
            }
            else{
                return  jsonElement.getAsJsonObject();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveDataToJSONObject(JsonObject jsonObject,String path,Object value)
    {
        String[] pathArray = path.split("/");
        for(int i=0;i<pathArray.length-1;i++){
            if(jsonObject.has(pathArray[i]))
                jsonObject = (JsonObject)jsonObject.get(pathArray[i]);
            else {
                jsonObject.add(pathArray[i], new JsonObject());
                jsonObject = (JsonObject)jsonObject.get(pathArray[i]);
            }
        }
        if(pathArray.length>0){
            if(value instanceof String)
                jsonObject.addProperty(pathArray[pathArray.length-1], (String) value);
            else if(value instanceof Number)
                jsonObject.addProperty(pathArray[pathArray.length-1], (Number) value);
            else if(value instanceof Boolean)
                jsonObject.addProperty(pathArray[pathArray.length-1], (Boolean) value);
            else if(value instanceof Character)
                jsonObject.addProperty(pathArray[pathArray.length-1], (Character) value);
            else if(value instanceof JsonObject)
                jsonObject.add(pathArray[pathArray.length-1], (JsonObject)value);
        }
    }

    public static void deleteDataFromJSONObject(JsonObject jsonObject,String path)
    {
        String[] pathArray = path.split("/");
        for(int i=0;i<pathArray.length-1;i++){
            jsonObject = (JsonObject)jsonObject.get(pathArray[i]);
        }
        if(pathArray.length>0)
            jsonObject.remove(pathArray[pathArray.length-1]);
    }

    public static boolean saveJSONObjectToFile(JsonObject jsonObject, String filePath){
        try {
            File file = new File(filePath);
            FileWriter fileWriter = new FileWriter(file);
            Gson gson = new Gson();
            gson.toJson(jsonObject,fileWriter);
            fileWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static String readLocalFile(String filePath) throws FileNotFoundException
    {
        String st = "";
        File file = new File(filePath);
        Scanner sc = new Scanner(file);
        while(sc.hasNextLine()){
            st=st+sc.nextLine()+"\n";
        }
        sc.close();
        return st;
    }

}
