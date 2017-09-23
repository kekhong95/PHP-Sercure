package com.example.kekho.myapplication.activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.kekho.myapplication.R;
import com.example.kekho.myapplication.connections.Request;
import com.example.kekho.myapplication.encryto.CriptoKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    EditText edtUser,edtPass,edtPhone,edtClass,edtName,edtGender;
    RelativeLayout bntSubmit;
    RequestQueue queue;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(Request.STRING_RESPONSE_SERVER_SIGN_UP)){
                try {
                    JSONObject object = new JSONObject(intent.getExtras().getString(Request.STRING_RESPONSE_SERVER_SIGN_UP));
                    if (!object.getBoolean("error"))
                        return;
                } catch (JSONException e) {
                }
                Toast.makeText(context, "Submit thất bại !", Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sign_up);
        initView();
        initEvent();
        resignListener();
    }

    private void resignListener() {
        registerReceiver(receiver,new IntentFilter(Request.STRING_RESPONSE_SERVER_SIGN_UP));
    }

    private void initEvent() {
        bntSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> map = new HashMap<String, String>();
                JSONObject object = new JSONObject();
                try {
                    object.put("user",edtUser.getText().toString().trim());
                    object.put("pass",edtUser.getText().toString().trim());
                    object.put("phone",edtUser.getText().toString().trim());
                    object.put("class",edtUser.getText().toString().trim());
                    object.put("name",edtUser.getText().toString().trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CriptoKey key = new CriptoKey(SignUpActivity.this);
                byte[] data = key.encrytion(object.toString().getBytes());
                map.put("data", Base64.encodeToString(data,Base64.DEFAULT));
                Request request = new Request(getString(R.string.app_post_public_sign_up),"POST",map,SignUpActivity.this,Request.STRING_RESPONSE_SERVER_SIGN_UP);
                queue.add(request.getRequest());

            }
        });
    }

    private void initView() {
        edtUser     = (EditText) findViewById(R.id.edt_user_name);
        edtPass     = (EditText) findViewById(R.id.edt_pass_word);
        edtPhone    = (EditText) findViewById(R.id.edt_phone);
        edtClass    = (EditText) findViewById(R.id.edt_class);
        edtName     = (EditText) findViewById(R.id.edt_name);
        bntSubmit   = (RelativeLayout) findViewById(R.id.bnt_submit);
        edtGender   = (EditText) findViewById(R.id.edt_gender);
        queue       = Volley.newRequestQueue(this);
    }
}
