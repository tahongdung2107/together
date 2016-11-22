package com.example.dung.togetherfinal11.Interface;
import org.json.JSONObject;


public interface ModelManagerListener {
    public void onSuccess(JSONObject response);
    public void onError(String error);

}
