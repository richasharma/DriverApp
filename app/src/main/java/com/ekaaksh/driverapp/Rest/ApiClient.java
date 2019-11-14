package com.ekaaksh.driverapp.Rest;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final String CLIENT_BASE_URL = "";
    public static final String TEMPR_BASE_URL = "http://ekaakshgroup.in/api/account/";
    public static final String TEMP_BASE_URL = "https://www.ekaakshgroup.in/FoodDeliverySystem/food/api/";

    private static String TEMP_FILE_NAME = "Hajj";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(80, TimeUnit.SECONDS)
                    .readTimeout(80, TimeUnit.SECONDS)
                    .writeTimeout(80, TimeUnit.SECONDS)
                    .addInterceptor(new LoginIntercepter())
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(TEMP_BASE_URL)
                    // .baseUrl(TEMP_BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit;
    }

    public static Retrofit getClientservice(String baseUrl) {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public static FormBody.Builder createBuilder(String[] paramsName, String[] paramsValue) {
        FormBody.Builder builder = new FormBody.Builder();

        for (int i = 0; i < paramsName.length; i++) {
            Log.e("values", "createBuilder: " + paramsName[i] + " : " + paramsValue[i]);
            builder.add(paramsName[i], paramsValue[i]);
        }

        return builder;
    }

    public static MultipartBody.Builder createmultipartbuilder(String[] parmsName, String[] parmsValue, String[] formName, List<String> imageArray) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (int i = 0; i < parmsName.length; i++) {
            builder.addFormDataPart(parmsName[i], parmsValue[i]);
            Log.e("kuch", "createBuilder: " + parmsName[i] + parmsValue[i]);
        }
        if (imageArray.size() > 0) {
            for (int i = 0; i < imageArray.size(); i++) {
                File f = new File(imageArray.get(i));
                if (f.exists()) {
                    builder.addFormDataPart(formName[0], TEMP_FILE_NAME + i + ".png", RequestBody.create(MediaType.parse("image/png"), f));
                }
            }
        }
        return builder;
    }

    public static class LoginIntercepter implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long t1 = System.nanoTime();
            Log.e("OkHttp", String.format("--> Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));

            try {
                Buffer requestBuffer = new Buffer();

                Log.e("OkHttp", requestBuffer.readUtf8().replace("=", ":").replace("&", "\n"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            Log.e("OkHttp", String.format("<-- Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            MediaType contentType = response.body().contentType();
            String content = response.body().string();
            Log.e("OkHttp", content);

            ResponseBody wrappedBody = ResponseBody.create(contentType, content);
            return response.newBuilder().body(wrappedBody).build();
        }
    }


    public static class NoConnectivityException extends IOException {

        @Override
        public String getMessage() {
            return "No network available, please check your WiFi or Data connection";
        }
    }


}

