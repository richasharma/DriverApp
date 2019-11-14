package com.ekaaksh.driverapp.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ekaaksh.driverapp.Adapters.PreviousOrderAdapter;
import com.ekaaksh.driverapp.Model.Beans.PreviousOrderBean;
import com.ekaaksh.driverapp.Model.Response.PreviousOrderResponse;
import com.ekaaksh.driverapp.R;
import com.ekaaksh.driverapp.Rest.ApiClient;
import com.ekaaksh.driverapp.Rest.ApiInterface;
import com.ekaaksh.driverapp.Utils.AppPrefrences;
import com.ekaaksh.driverapp.Utils.CommonUtils;
import com.wang.avi.AVLoadingIndicatorView;

import okhttp3.FormBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFragment extends Fragment {

    View rootView;
    EditText etOldPassword,etNewPassword,etConfirmPassword;
    Button btnChangePassword;
    String OldPassword,UserId;
    AVLoadingIndicatorView avi;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.change_password_layout, container, false);

        OldPassword = AppPrefrences.getSavedUser(getActivity()).getPassword();
        UserId = AppPrefrences.getSavedUser(getActivity()).getId(); // Deliveryboy UserId.

        etOldPassword = (EditText)rootView.findViewById(R.id.etOldPassword);
        etNewPassword = (EditText)rootView.findViewById(R.id.etNewPassword);
        etConfirmPassword = (EditText)rootView.findViewById(R.id.etConfirmPassword);
        btnChangePassword = (Button)rootView.findViewById(R.id.btnChangePassword);
        avi = (AVLoadingIndicatorView)rootView.findViewById(R.id.avi);

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               changePassword();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Change Password");
    }

    private void changePassword() {

        if(etOldPassword.getText().toString().length()==0 || etNewPassword.getText().toString().length()==0 || etConfirmPassword.getText().toString().length()==0 ){
            Toast.makeText(getActivity(), "Please fill password details", Toast.LENGTH_SHORT).show();
        }
        else if(etOldPassword.getText().toString().length()<6 || etNewPassword.getText().toString().length()<6  || etConfirmPassword.getText().toString().length()<6  ){
            Toast.makeText(getActivity(), "Password should be of minimum 6 characters", Toast.LENGTH_SHORT).show();
        }
        else{
            if(!etOldPassword.getText().toString().equals(OldPassword)){
                Toast.makeText(getActivity(), "Old Password is wrong!, Try again.", Toast.LENGTH_SHORT).show();
            }
            else{
               if(!etNewPassword.getText().toString().equals(etConfirmPassword.getText().toString()))
                   Toast.makeText(getActivity(), "Please Confirm Password again!", Toast.LENGTH_SHORT).show();
               else{
                   if(etOldPassword.getText().toString().equals(etNewPassword.getText().toString()))
                       Toast.makeText(getActivity(), "Old and New Passwords are same.", Toast.LENGTH_SHORT).show();
                   else
                       changePasswordApi(etNewPassword.getText().toString());
               }
            }
        }
    }

    private void changePasswordApi(String newPassword) {

        avi.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        FormBody.Builder builder = ApiClient.createBuilder(new String[]
                        {"user_id","user_type","user_pass"},
                new String[]{UserId,"0",newPassword});          // User Type '0' for driver
        if (CommonUtils.isNetworkAvailable(getActivity())) {
            Call<ResponseBody> call = apiInterface.ChangePassword(builder.build());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    avi.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        try {
                            if (response.body() != null) {

                                Toast.makeText(getActivity(), "Password changed successfully.", Toast.LENGTH_SHORT).show();
                                etOldPassword.setText("");
                                etNewPassword.setText("");
                                etConfirmPassword.setText("");
                                etOldPassword.requestFocus();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    avi.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Change Password Something went wrong!.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            avi.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Please check your Internet Connection.", Toast.LENGTH_SHORT).show();
        }
    }
}
