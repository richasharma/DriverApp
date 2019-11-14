package com.ekaaksh.driverapp.Activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ekaaksh.driverapp.Model.Beans.LoginBean;
import com.ekaaksh.driverapp.Model.Response.LoginResponse;
import com.ekaaksh.driverapp.Model.Response.TokenResponse;
import com.ekaaksh.driverapp.R;
import com.ekaaksh.driverapp.Rest.ApiClient;
import com.ekaaksh.driverapp.Rest.ApiInterface;
import com.ekaaksh.driverapp.Utils.AppPrefrences;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    EditText edit_email;
    EditText edit_password;
    Button btn_login;
    String Str_Email;
    String Str_Password;

    AVLoadingIndicatorView avi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        find();
    }
    public void find(){
        avi = findViewById(R.id.bar);
        edit_email=findViewById(R.id.email);
        edit_password=findViewById(R.id.password);
        btn_login=findViewById(R.id.email_sign_in_button);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view==btn_login){
            Str_Email = edit_email.getText().toString().trim();
            Str_Password = edit_password.getText().toString().trim();
            if (Str_Email.equals("") || Str_Email.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter valid Email.", Toast.LENGTH_SHORT).show();
            } else if (!Str_Email.matches(emailPattern)) {
                Toast.makeText(LoginActivity.this, "Invalid Email.", Toast.LENGTH_SHORT).show();
            }else if (Str_Password.equals("") || Str_Password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter password.", Toast.LENGTH_SHORT).show();
            } else if (Str_Password.length() < 5 || Str_Password.length() > 15) {
                Toast.makeText(LoginActivity.this, "Password should be 6 to 15 characters.", Toast.LENGTH_SHORT).show();
            }else {
                Login();
            }
        }
    }



//////////////////////////////

    private void Login() {
        avi.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        //  FormBody.Builder builder = ApiClient.createBuilder(new String[]{"email", "password"},
        //        new String[]{"deliveryboy@gmail.com", "123456"});
        FormBody.Builder builder = ApiClient.createBuilder(new String[]
                        {"email","password"},
                new String[]{Str_Email,Str_Password});
        Call<LoginResponse> call = apiInterface.Login(builder.build());
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response){
                // stopLoadingDialog();
                avi.setVisibility(View.GONE);
                Log.d("11111","11111");

                if (response.isSuccessful())
                {
                    try {
                        if(response.isSuccessful() && response.body().getInfo()!=null){

                            if(response.body().getInfo().getIsactive().equals("1")) { //driver is active or not
                                if(response.body().getInfo().getAttendance().equals("1")){
                                    Toast.makeText(getApplicationContext(),"You are already login on another device.",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    LoginBean loginInfo = response.body().getInfo();
                                    AppPrefrences.SaveUserdetail(LoginActivity.this, loginInfo);
                                    UpdateToken(response.body().getInfo().getId());
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }

                            }
                            else
                                Toast.makeText(getApplicationContext(),"Not Active, Please contact to Admin.",Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                //onApiFailure(call, t);
                avi.setVisibility(View.GONE);
                Log.d("0000","0000");
                Toast.makeText(LoginActivity.this, "Invalid Credentials. Please try again.", Toast.LENGTH_LONG).show();
            }
        });

    }
    private void UpdateToken(String id) {
        SharedPreferences prefs = getSharedPreferences("NEW_TOKEN", MODE_PRIVATE);
        String token = prefs.getString("token", "");
        Log.e("token",""+token);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        FormBody.Builder builder = ApiClient.createBuilder(new String[]
                        {"user_id","token","user_type","device_type"},
                new String[]{id,token,"0","android"}); // user Type '0' for delivery boy
        Call<TokenResponse> call = apiInterface.UpdateToken(builder.build());
        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response){
                if (response.isSuccessful())
                {
                    try {
                        if(response.body()!=null && response.body().getData()!=null && response.body().getData().getSuccess().equals("1")){
                            Log.e("toast",""+ response.body().getData().getMessage());
                           Toast.makeText(getApplicationContext(),""+response.body().getData().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                //onApiFailure(call, t);
                Log.d("0000","0000");
            }
        });
    }
    public void onApiFailure(Call<ResponseBody> call, Throwable t) {
        avi.setVisibility(View.GONE);

        if ((t instanceof ApiClient.NoConnectivityException))
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Please try later", Toast.LENGTH_SHORT).show();
    }

}

