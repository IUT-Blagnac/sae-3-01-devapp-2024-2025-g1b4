package org.javafxapp.tools;

import java.io.File;
import java.io.FileWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.*;

public class JsonInteract {

    public static String

    private JSONObject appData;

    private List<String> alRoomData;

    public JsonInteract(){
        try {
            String content = Files.readString(Paths.get("./appData.json"));
            this.appData=new JSONObject(content);
            JSONArray jsArray=this.appData.getJSONArray("roomNames");
            this.alRoomData=new ArrayList<>(jsArray.toList().stream().map(Object::toString).toList());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public List<String> getRoomList(){return this.alRoomData;}


    public void addRoomToList(String roomName){
        this.alRoomData.add(roomName);
    }

    public void properClose(){
        this.appData.put("roomNames",this.alRoomData);

        try {
            Files.writeString(Paths.get("./appData.json"),this.appData.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

//    public boolean set(String pathToData, Object elt){
//        JSONObject jsOb=this.appData;
//        Object temp=null;
//            for (String str : pathToData.split("\\.")) {
//                temp = jsOb.get(str);
//                if (temp instanceof JSONObject)
//                    jsOb = (JSONObject) temp;
//            }
//
//    }

    public Object get(String pathToData){

        return this.getThroughArray(pathToData.split("\\."));
    }

    public Object getThroughArray(String[] paths){
        JSONObject jsOb=this.appData;
        Object temp=null;
        for(String str:paths){
            temp=jsOb.get(str);
            if(temp instanceof JSONObject)
                jsOb=(JSONObject) temp;
            else
                return temp;
        }

        return jsOb;
    }

    public static void main(String[] args) {
        JsonInteract jsInt=new JsonInteract();
        System.out.println(jsInt.get("data.test"));
    }
}
