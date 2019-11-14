package com.ekaaksh.driverapp.Rest;

import android.database.Observable;

import com.ekaaksh.driverapp.Model.Response.AcceptOrderResponse;
import com.ekaaksh.driverapp.Model.Response.BaseResponse;
import com.ekaaksh.driverapp.Model.Response.GetProfileResponse;
import com.ekaaksh.driverapp.Model.Response.LoginResponse;
import com.ekaaksh.driverapp.Model.Response.OrderDetailResponse;
import com.ekaaksh.driverapp.Model.Response.PreviousOrderResponse;
import com.ekaaksh.driverapp.Model.Response.ReviewsResponse;
import com.ekaaksh.driverapp.Model.Response.TokenResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {
    // @POST("driverlogin.php?")
    @POST("restaurant_deliveryboylogin.php?")
    Call<LoginResponse> Login(@Body RequestBody requestBody);

    @POST("token.php?")
    Call<TokenResponse> UpdateToken(@Body RequestBody requestBody);

    @POST("order_details.php?")
    Call<OrderDetailResponse> OrderDetail(@Body RequestBody requestBody);

    @POST("order_complete.php?")
    Call<AcceptOrderResponse> AcceptOrder(@Body RequestBody requestBody);

    @POST("deliveryboy_allorder.php?")
    Call<PreviousOrderResponse> PreviousOrder(@Body RequestBody requestBody);

    @POST("deliveryboy_review.php?")
    Call<ReviewsResponse> Reviews(@Body RequestBody requestBody);

    @POST("deliveryboy_profile.php?")
    Call<GetProfileResponse> GetProfileDetails(@Body RequestBody requestBody);

    @POST("logout.php?")
    Call<ResponseBody> Logout(@Body RequestBody requestBody);

    @POST("change_password.php?")
    Call<ResponseBody> ChangePassword(@Body RequestBody requestBody);

    @Multipart
    @POST("user_profileupdate.php?")
    Call<ResponseBody> uploadFile(@Part MultipartBody.Part file, @Part("user_id") RequestBody requestBody);


    @POST("deliveryboy_location.php?")
    Call<BaseResponse> SendLatLong(@Body RequestBody requestBody);

   /* @POST("update-profile")
    Call<ResponseBody> Update_profile(@Body RequestBody requestBody);

    @POST("update-password")
    Call<ResponseBody> Update_password(@Body RequestBody requestBody);

    @POST("users-redeam")
    Call<ResponseBody> Users_redeam(@Body RequestBody requestBody);

    @POST("users-redeam-history")
    Call<ResponseBody> Users_Redeam_History(@Body RequestBody requestBody);

    @POST("groupusers")
    Call<ResponseBody> Groupusers(@Body RequestBody requestBody);

    @POST("addgroup")
    Call<ResponseBody> Addgroup(@Body RequestBody requestBody);*/

}
