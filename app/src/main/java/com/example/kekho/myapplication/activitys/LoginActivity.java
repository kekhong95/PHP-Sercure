package com.example.kekho.myapplication.activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.kekho.myapplication.R;
import com.example.kekho.myapplication.connections.Request;
import com.example.kekho.myapplication.encryto.CriptoKey;
import com.example.kekho.myapplication.object.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 100;
    EditText edtUser,edtPass;
    RelativeLayout bntLogin;
    TextView txtForgot,txtSignUp;
    RequestQueue queue;
    User user;
    LoginButton loginButton;
    CallbackManager callbackManager;
    GoogleApiClient mGoogleApiClient;
    BroadcastReceiver recever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
                if (intent.hasExtra(Request.STRING_RESPONSE_SERVER_PUBLIC_KEY))
                {
                    if (!intent.getExtras().getString(Request.STRING_RESPONSE_SERVER_PUBLIC_KEY).equalsIgnoreCase(getString(R.string.app_server_key))){
                        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                        alertDialog.setTitle(getString(R.string.app_alret_network_title));
                        alertDialog.setMessage(getString(R.string.app_alret_network_body));
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                });
                        alertDialog.show();
                    }
                }else if (intent.hasExtra(Request.STRING_RESPONSE_SERVER_LOGIN)){

                    Log.d("Data Server : ",new String(Base64.decode(intent.getExtras().getString(Request.STRING_RESPONSE_SERVER_LOGIN),Base64.DEFAULT)));
                    try {
                        JSONObject object = new JSONObject(intent.getExtras().getString(Request.STRING_RESPONSE_SERVER_LOGIN));
                        if (object.getBoolean("result"))
                            Toast.makeText(context, "Login complete !!!", Toast.LENGTH_LONG).show();
                        else{
                            Toast.makeText(context, "Wrong user or pass !!! "+object.getString("error"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    byte[] data = Base64.decode(intent.getExtras().getString(Request.STRING_RESPONSE_SERVER_LOGIN),Base64.DEFAULT);
//                    CriptoKey criptoKey = new CriptoKey(context);
//                    byte[] key = criptoKey.getMd5(user.getSession());
//                    data = criptoKey.getDecode(data,key);
//                    try {
//                        JSONObject object = new JSONObject(new String(data));
//                        Log.d("Data",object.getString("session"));
//                    } catch (JSONException e) {
//                        Log.d("Error",e.getMessage());
//                    }

                }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initEvent();
        resignListener();
        initFace();

    }

    private void initFace() {
        if(AccessToken.getCurrentAccessToken()!= null)
        LoginManager.getInstance().logOut();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        callbackManager = CallbackManager.Factory.create();
         loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Map<String,String> map = new HashMap<>();
                map.put("user",loginResult.getAccessToken().getUserId());
                map.put("pass",loginResult.getAccessToken().getToken());
                map.put("default","false");
                Log.d("FaceBook : ",loginResult.getAccessToken().getUserId());
                Request request = new Request(getString(R.string.app_post_public_login),"POST",map,LoginActivity.this,Request.STRING_RESPONSE_SERVER_LOGIN);
                queue.add(request.getRequest());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }});
    }

    private void resignListener() {
        registerReceiver(recever,new IntentFilter(Request.STRING_RESPONSE_SERVER));
    }

    private void initEvent() {
        bntLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(edtPass.getText().toString() !=  null && edtUser.getText().toString() !=  null)){
                    Toast.makeText(LoginActivity.this, "Bạn chưa nhập tài khoản hoặc mật khẩu !!!", Toast.LENGTH_LONG).show();
                }else {
                    user = new User(null,null,null,null,null);
                    user.setUser(edtUser.getText().toString().trim());
                    user.setSession(edtPass.getText().toString().trim());
//                    try {
//                        user.setSession((CriptoKey.hash256(edtPass.getText().toString())));
//                    } catch (NoSuchAlgorithmException e) {
//                        e.printStackTrace();
//                    }
//                    Map<String,String> map = new HashMap<String, String>();
//                    map.put("user",user.getUser());
//                    CriptoKey key = new CriptoKey(LoginActivity.this);
//                    JSONObject object = new JSONObject();
//                    try {
//                        object.put("pass",user.getSession());
//                        Log.d("Client : ",user.getSession());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    byte[] data = key.encrytion(object.toString().getBytes());
//                    map.put("data",Base64.encodeToString(data,0,data.length,Base64.DEFAULT));
                    Map<String,String> map = new HashMap<String, String>();
                    map.put("user",user.getUser());
                    map.put("pass",user.getSession());
                    Request request = new Request(getString(R.string.app_post_public_login),"POST",map,LoginActivity.this,Request.STRING_RESPONSE_SERVER_LOGIN);
                    queue.add(request.getRequest());
                }
            }
        });
//        txtSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivityForResult(new Intent(LoginActivity.this,SignUpActivity.class),100);
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            return;
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void initView() {
        edtPass = (EditText) findViewById(R.id.edt_pass_word);
        edtUser = (EditText) findViewById(R.id.edt_user_name);
        bntLogin = (RelativeLayout) findViewById(R.id.bnt_Login);
        queue =     Volley.newRequestQueue(this);
//        txtForgot = (TextView) findViewById(R.id.txtForgot);
//        txtSignUp = (TextView) findViewById(R.id.txtSignUp);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("AAAAAAAAAAAAAA", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Map<String,String> map = new HashMap<>();
            map.put("user",acct.getEmail());
            map.put("pass",acct.getId()+ System.currentTimeMillis() + acct.getEmail());
            map.put("default","false");
            Request request = new Request(getString(R.string.app_post_public_login),"POST",map,this,Request.STRING_RESPONSE_SERVER_LOGIN);
            queue.add(request.getRequest());
        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(this, "False", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
//        Request request = new Request(getString(R.string.app_get_public_key),"GET",null,this,Request.STRING_RESPONSE_SERVER_PUBLIC_KEY);
//        queue.add(request.getRequest());
//        String data = "pVsgi/wu8i0M6J5H0DgE1lXa7yQ2Wb+dImpayM2UcP4=";
//        CriptoKey keys = new CriptoKey(this);
//        String key = "f07c30bce9ae84fbb4f613cbf0f8d04ced20dbe810c26fe8c2aa4d0d1eccb10d";
        try {
//            byte[] keysMd5 = keys.getMd5(key);
//            byte[] values = (Base64.decode(data,Base64.DEFAULT));
//            byte[] fist = new byte[16];
//            Log.d("Size : ", String.valueOf(values.length));
//            for (int i=0;i<16;i++){
//                fist[i] = values[i];
//            }
//            byte[] res = keys.getDecode(fist,keysMd5);
//            Log.d("results Dec: ",new String(res));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Log.d("MD5",Base64.encodeToString(keys.getMd5("thanh1995"),Base64.DEFAULT));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
