package com.example.kekho.myapplication.connections;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

/**
 * Created by kekho on 9/18/2017.
 */

public class Request implements Response.Listener<String>,Response.ErrorListener  {
    public static final String STRING_RESPONSE_SERVER = "STRING_RESPONSE_SERVER";
    public static final String STRING_RESPONSE_SERVER_LOGIN = "STRING_RESPONSE_SERVER_LOGIN";
    public static final String STRING_RESPONSE_SERVER_PUBLIC_KEY = "STRING_RESPONSE_PUBLIC_KEY";
    public static final String STRING_RESPONSE_SERVER_SIGN_UP = "STRING_RESPONSE_SIGN_UP";
    String response;
    String url;
    Map<String, String> map;
    Context context;
    String method;
    String action;
    public Request(String url,String method, Map<String, String> map,Context context,String action) {
        this.url = url;
        this.map = map;
        this.context = context;
        this.method = method;
        this.action = action;
    }
    public StringRequest getRequest(){
        StringRequest request;
        if (method == "POST")
            request = new StringRequest(com.android.volley.Request.Method.POST,url,this,this){
            @Override
            protected Map<String, String> getParams()
            {
                return Request.this.map;
            }

        };
        else{
            request = new StringRequest(com.android.volley.Request.Method.GET,url,this,this);
        }
        return request;
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        this.response = error.getMessage();
    }

    @Override
    public void onResponse(String response) {
        this.response =  response;
        Log.d("AAAAAAAAAAAAAAAAAAAA",response);
        Intent intent = new Intent(STRING_RESPONSE_SERVER);
        intent.putExtra(action,response);
        context.sendBroadcast(intent);
    }


}
