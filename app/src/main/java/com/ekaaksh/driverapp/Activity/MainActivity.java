package com.ekaaksh.driverapp.Activity;



import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.ekaaksh.driverapp.Fragment.ChangePasswordFragment;
import com.ekaaksh.driverapp.Fragment.ManageRevenueFragment;
import com.ekaaksh.driverapp.Fragment.PreviousOrderFragment;
import com.ekaaksh.driverapp.Fragment.ProfileSettingsFragment;
import com.ekaaksh.driverapp.Fragment.UserFeedbackFragment;
import com.ekaaksh.driverapp.GPS.DirectionsJSONParser;
import com.ekaaksh.driverapp.Model.Response.AcceptOrderResponse;
import com.ekaaksh.driverapp.Model.Response.BaseResponse;
import com.ekaaksh.driverapp.Model.Response.OrderDetailResponse;
import com.ekaaksh.driverapp.R;
import com.ekaaksh.driverapp.Rest.ApiClient;
import com.ekaaksh.driverapp.Rest.ApiInterface;
import com.ekaaksh.driverapp.Utils.AppPrefrences;
import com.ekaaksh.driverapp.Utils.CommonUtils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LocationListener, View.OnClickListener {

    DrawerLayout drawerLayout;
    LinearLayout left_drawer_left;
    Toolbar toolbar;
    Dialog pickUpOrderDialog;
    TextView tvHome, tvProfileSettings, tvChangePassword, tvPreviousOrders, tvManageRevenue, tvUserFeedback,tvLogout;
    LocationManager locationManager;
    Double latitude, longitude;
    private Polyline mPolyline;
    GoogleMap googleMap;
    String UserId;
    private static final int PERMISSION_REQUEST_CODE = 1;
    String userPhoneNumber="";
    Double distanceInKms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        pickUpOrderDialog = new Dialog(this);
        left_drawer_left = (LinearLayout) findViewById(R.id.left_drawer_layout);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvProfileSettings = (TextView) findViewById(R.id.tvProfileSettings);
        tvChangePassword = (TextView) findViewById(R.id.tvChangePassword);
        tvPreviousOrders = (TextView) findViewById(R.id.tvPreviousOrders);
        tvManageRevenue = (TextView) findViewById(R.id.tvManageRevenue);
        tvUserFeedback = (TextView) findViewById(R.id.tvUserFeedback);
        tvLogout = (TextView) findViewById(R.id.tvLogout);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white);



        UserId = AppPrefrences.getSavedUser(MainActivity.this).getId(); // Deliveryboy UserId.
        Log.e("UserId", "" + UserId);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            getLocation();
        }

        //open drawer on navigation button click
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        /********************* firebase messaging push notification ****************/
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MainActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e("newToken", newToken);

            }
        });
        /**********************************************************/
        //navigation Item Clicks
        //tvHome.setOnClickListener(this);
        tvProfileSettings.setOnClickListener(this);
        tvPreviousOrders.setOnClickListener(this);
        tvChangePassword.setOnClickListener(this);
        tvManageRevenue.setOnClickListener(this);
        tvUserFeedback.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
        //setting fragment on home
        getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new PreviousOrderFragment()).commit();


    }

    public void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(left_drawer_left)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
        }


    }

    @Override
    public void onClick(View view) {
        /*if (view == tvHome) {
           if(getFragmentManager().findFragmentById(R.id.fragmentContainer) instanceof PickUpOrderFragment){}   //not open fragment again if already opened.
               else{
                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new PickUpOrderFragment()).addToBackStack(null).commit();
            }
        } else*/
        if (view == tvProfileSettings) {
            if (getFragmentManager().findFragmentById(R.id.fragmentContainer) instanceof ProfileSettingsFragment) {
            } else {
                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new ProfileSettingsFragment()).addToBackStack(null).commit();
            }
        } else if (view == tvChangePassword) {
            if (getFragmentManager().findFragmentById(R.id.fragmentContainer) instanceof ChangePasswordFragment) {
            } else {
                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new ChangePasswordFragment()).addToBackStack(null).commit();
            }
        } else if (view == tvPreviousOrders) {
            if (getFragmentManager().findFragmentById(R.id.fragmentContainer) instanceof PreviousOrderFragment) {
            } else {
                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new PreviousOrderFragment()).addToBackStack(null).commit();
            }
        } else if (view == tvManageRevenue) {
            if (getFragmentManager().findFragmentById(R.id.fragmentContainer) instanceof ManageRevenueFragment) {
            } else {
                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new ManageRevenueFragment()).addToBackStack(null).commit();
            }
        } else if (view == tvUserFeedback) {
            if (getFragmentManager().findFragmentById(R.id.fragmentContainer) instanceof UserFeedbackFragment) {
            } else {
                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new UserFeedbackFragment()).addToBackStack(null).commit();
            }
        }else if (view == tvLogout) {

            new AlertDialog.Builder(this)
                    .setTitle("LOGOUT")
                    .setMessage("Are you sure you want to Logout?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                           LogoutApi();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();

        }


        drawerLayout.closeDrawer(GravityCompat.START);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notification) {
            return true;
        }

        if (id == R.id.action_help) {
            Toast.makeText(MainActivity.this, "Help clicked", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getLocation();
        if (getIntent().getExtras() != null) {
            Log.e("intent", "yes " + getIntent().getExtras());
            Log.e("data", "" + getIntent().getExtras().getString("NOTIFICATION_DATA"));

            if (getIntent().getExtras().getString("NOTIFICATION_DATA") != null) {
                String orderId = getIntent().getExtras().getString("NOTIFICATION_DATA");
                getOrderDetailApi(orderId);

            }
        }
    }

    private void getOrderDetailApi(String orderId) {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        FormBody.Builder builder = ApiClient.createBuilder(new String[]
                        {"order_id"},
                new String[]{"100"});
        if (CommonUtils.isNetworkAvailable(getApplicationContext())) {
            Call<OrderDetailResponse> call = apiInterface.OrderDetail(builder.build());
            call.enqueue(new Callback<OrderDetailResponse>() {
                @Override
                public void onResponse(Call<OrderDetailResponse> call, Response<OrderDetailResponse> response) {

                    Log.d("11111", "11111");
                    if (response.isSuccessful()) {
                        try {
                            if (response.body() != null && response.body().getOrder_details() != null)
                                showPickUpOrderDialog(response.body());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<OrderDetailResponse> call, Throwable t) {
                    //onApiFailure(call, t);
                    Log.d("0000", "0000");
                    Toast.makeText(MainActivity.this, "Something went wrong!.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Please check your Internet Connection.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPickUpOrderDialog(final OrderDetailResponse orderDetailResponse) {

        //toolbar.getMenu().clear();

      /*  getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Pick Up Order?");

        PickUpOrderFragment dialogFragment = new PickUpOrderFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainer, dialogFragment);
        ft.commit();
*/

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        // custom dialog

        pickUpOrderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pickUpOrderDialog.setContentView(R.layout.pickup_order_layout);
        pickUpOrderDialog.setCancelable(false);

        Window window = pickUpOrderDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        // set the custom dialog components - text, image and button
        final TextView tvHeader = (TextView) pickUpOrderDialog.findViewById(R.id.tvHeader);
        TextView tvTimeTaken = (TextView) pickUpOrderDialog.findViewById(R.id.tvTimeTaken);
        TextView tvDistance = (TextView) pickUpOrderDialog.findViewById(R.id.tvDistance);
        TextView tvAmount = (TextView) pickUpOrderDialog.findViewById(R.id.tvAmount);
        ImageView imgAccept = (ImageView) pickUpOrderDialog.findViewById(R.id.imgAccept);
        ImageView imgDecline = (ImageView) pickUpOrderDialog.findViewById(R.id.imgDecline);
        MapView mMapView = (MapView) pickUpOrderDialog.findViewById(R.id.mMapView);

        // calculate distance between driver current loaction and restaurant location

        final Double restaurantLat = Double.valueOf(orderDetailResponse.getOrder_details().getRestaurant_lat());
        final Double restaurantLong = Double.valueOf(orderDetailResponse.getOrder_details().getRestaurant_lng());

        if (latitude != null && longitude != null)
            distanceInKms = CommonUtils.distance(latitude, longitude, restaurantLat, restaurantLong);
        else
            distanceInKms = CommonUtils.distance(26.9148, 75.7937, restaurantLat, restaurantLong);

        Log.e("distanceInKms", "" + distanceInKms);
        if (distanceInKms > 1)
            tvDistance.setText(CommonUtils.RoundOf2Places(distanceInKms) + "Kms");
        else
            tvDistance.setText(CommonUtils.RoundOf2Places(distanceInKms) + "Km");


        tvAmount.setText("₹" + orderDetailResponse.getOrder_details().getOrder_amount());
        tvTimeTaken.setText(orderDetailResponse.getOrder_details().getDelivery_time() + "Mins");


        MapsInitializer.initialize(getApplicationContext());

        mMapView.onCreate(pickUpOrderDialog.onSaveInstanceState());
        mMapView.onResume();

        mMapView.getMapAsync(new OnMapReadyCallback() {
//26.9148, 75.7937

            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng myLocation, restaurantLocation;

                if (latitude != null && longitude != null) {
                    myLocation = new LatLng(latitude, longitude);
                } else
                    myLocation = new LatLng(26.9148, 75.7937);// office lat long
                restaurantLocation = new LatLng(restaurantLat, restaurantLong);
                googleMap.addMarker(new MarkerOptions().position(myLocation).title("Source"));
                googleMap.addMarker(new MarkerOptions().position(restaurantLocation).title("Destination"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
                googleMap.getUiSettings().setZoomControlsEnabled(true);

                //googleMap.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);
                //drawMarker(myLocation);
                CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                        myLocation, 15);
                googleMap.animateCamera(location);
                if (myLocation != null && restaurantLocation != null)
                    drawRoute(myLocation, restaurantLocation);

            }
        });

        imgAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvHeader.setText("Ongoing Order");
                getAcceptOrderApi(orderDetailResponse.getOrder_details().getOrder_id(), "0", "2", orderDetailResponse, pickUpOrderDialog); // 0 is UserType for deliveryboy and 2 is for Acceptance by deliveryboy

            }
        });

        imgDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                pickUpOrderDialog.dismiss();
            }
        });

        pickUpOrderDialog.show();
    }

    private void showConfirmOrderDialog(final OrderDetailResponse orderDetailResponse) {


        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirm_order_popup);
        dialog.setCancelable(false);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        // set the custom dialog components - text, image and button
        Button tvPickOrder = (Button) dialog.findViewById(R.id.tvPickOrder);
        TextView tvAmount = (TextView) dialog.findViewById(R.id.tvAmount);
        TextView tvDistance = (TextView) dialog.findViewById(R.id.tvDistance);
        TextView tvTimeToDeliver = (TextView) dialog.findViewById(R.id.tvTimeToDeliver);
        TextView tvRestaurantName = (TextView) dialog.findViewById(R.id.tvRestaurantName);
        TextView tvRestaurantAddress = (TextView) dialog.findViewById(R.id.tvRestaurantAddress);
        TextView tvCustomerName = (TextView) dialog.findViewById(R.id.tvCustomerName);
        TextView tvCustomerAddress = (TextView) dialog.findViewById(R.id.tvCustomerAddress);

        tvAmount.setText("₹" + orderDetailResponse.getOrder_details().getOrder_amount());
        //tvDistance.setText(""+distanceInKms);
        tvTimeToDeliver.setText(orderDetailResponse.getOrder_details().getDelivery_time() + " mins to Pick");
        tvRestaurantName.setText("" + orderDetailResponse.getOrder_details().getRestaurant_name());
        tvRestaurantAddress.setText("" + orderDetailResponse.getOrder_details().getRestaurant_address());
        tvCustomerName.setText("" + orderDetailResponse.getUser().getFullname());
        tvCustomerAddress.setText("" + orderDetailResponse.getOrder_details().getUser_address());

        if (distanceInKms > 1)
            tvDistance.setText(CommonUtils.RoundOf2Places(distanceInKms) + "Kms");
        else
            tvDistance.setText(CommonUtils.RoundOf2Places(distanceInKms) + "Km");

        tvPickOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAcceptOrderApi(orderDetailResponse.getOrder_details().getOrder_id(), "0", "3", orderDetailResponse, dialog); // 0 is UserType for deliveryboy and 3 is for food picked by deliveryboy

            }
        });

        dialog.show();

    }

    private void showOrderDeliveredDialog(final OrderDetailResponse orderDetailResponse) {

        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.order_delivered_popup);
        dialog.setCancelable(false);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        // set the custom dialog components - text, image and button
        TextView tvOrderDelivered = (TextView) dialog.findViewById(R.id.tvOrderDelivered);
        TextView tvAmount = (TextView) dialog.findViewById(R.id.tvAmount);
        TextView tvDistance = (TextView) dialog.findViewById(R.id.tvDistance);
        TextView tvTimeToDeliver = (TextView) dialog.findViewById(R.id.tvTimeToDeliver);
        TextView tvCustomerName = (TextView) dialog.findViewById(R.id.tvCustomerName);
        TextView tvCustomerAddress = (TextView) dialog.findViewById(R.id.tvCustomerAddress);
        ImageView imgUser = (ImageView) dialog.findViewById(R.id.imgUser);
        ImageView imgCall = (ImageView) dialog.findViewById(R.id.imgCall);

        tvAmount.setText("₹" + orderDetailResponse.getOrder_details().getOrder_amount());
        //tvDistance.setText(""+distanceInKms);
        tvTimeToDeliver.setText(orderDetailResponse.getOrder_details().getDelivery_time() + "mins to deliver");
        tvCustomerName.setText("" + orderDetailResponse.getUser().getFullname());
        tvCustomerAddress.setText("" + orderDetailResponse.getOrder_details().getUser_address());
        //imgUser.setImageResource(Integer.parseInt(orderDetailResponse.getUser().getImage()));

        if (distanceInKms > 1)
            tvDistance.setText(CommonUtils.RoundOf2Places(distanceInKms) + "Kms");
        else
            tvDistance.setText(CommonUtils.RoundOf2Places(distanceInKms) + "Km");


        tvOrderDelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAcceptOrderApi(orderDetailResponse.getOrder_details().getOrder_id(), "0", "4", orderDetailResponse, dialog); // 0 is UserType for deliveryboy and 4 is for order completed by deliveryboy

            }
        });

        imgCall.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
               userPhoneNumber =  orderDetailResponse.getUser().getPhone_no();
                Log.e("userPhoneNumber ", "" + userPhoneNumber);
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CODE);
                    requestForCallPermission();
                }
                else
                    makeCall(userPhoneNumber);
            }
        });

        dialog.show();
    }

    public void requestForCallPermission()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CALL_PHONE))
            makeCall(userPhoneNumber);
        else
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeCall(userPhoneNumber);
                }
                break;
        }
    }

    private void makeCall(String userPhoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + userPhoneNumber));
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED)
            startActivity(callIntent);
        else
            requestForCallPermission();
    }


    private void drawRoute(LatLng myLocation, LatLng restaurantLocation){

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(myLocation, restaurantLocation);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }
    private String getDirectionsUrl(LatLng origin,LatLng dest){
/*// Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Waypoints
        String waypoints = "";
        for(int i=2;i<markerPoints.size();i++){
            LatLng point  = (LatLng) markerPoints.get(i);
            if(i==2)
                waypoints = "waypoints=";
            waypoints += point.latitude + "," + point.longitude + "|";
        }

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+waypoints;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;*/

      /*  String waypoints = "waypoints=optimize:true|"
                + origin.latitude + "," + origin.longitude
                + "|" + "|" +  dest.latitude + ","
                + dest.longitude;
        String OriDest = "origin="+origin.latitude+","+origin.longitude+"&destination="+dest.latitude+","+dest.longitude;

        String sensor = "sensor=false";
        String params = OriDest+"&%20"+waypoints + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params;*/


        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;
        Log.e("1", "origin.latitude"+origin.latitude +" origin.longitude"+origin.longitude);
        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;
        Log.e("2", "dest.latitude"+dest.latitude +" dest.longitude"+dest.longitude);
        // Key
        String key = "key=" + getString(R.string.google_maps_key);
        Log.e("key", ""+key);
        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+key;
        Log.e("parameters", ""+parameters);
        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
        Log.e("url", ""+url);
        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception on download", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("onLocationChanged", "onLocationChanged");
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Toast.makeText(getApplicationContext(), "lat: "+latitude+"long :"+longitude,Toast.LENGTH_SHORT).show();

        if(latitude == location.getLatitude() && longitude == location.getLongitude())
            Toast.makeText(getApplicationContext(), "Same Location",Toast.LENGTH_SHORT).show();
        else
            sendCurrentLatLongApi(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(getApplicationContext(), "onProviderEnabled",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(getApplicationContext(), "plz turn on location.",Toast.LENGTH_SHORT).show();
    }

    /** A class to download data from Google Directions URL */
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("DownloadTask","DownloadTask : " + data);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Directions in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> > {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            Log.e("result", ""+result);
            try {
                // Traversing through all the routes
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(8);
                    lineOptions.color(Color.RED);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                if(mPolyline != null){
                    mPolyline.remove();
                }
                mPolyline = googleMap.addPolyline(lineOptions);

            }else
                Toast.makeText(getApplicationContext(),"No route is found", Toast.LENGTH_SHORT).show();
        }
    }

    private void getAcceptOrderApi(String order_id, String user_type, final String order_status, final OrderDetailResponse orderDetailsResponse, final Dialog dialog) {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        FormBody.Builder builder = ApiClient.createBuilder(new String[]
                        {"order_id","user_id","user_type","order_status"},
                new String[]{order_id,UserId,user_type,order_status});
        if(CommonUtils.isNetworkAvailable(getApplicationContext())) {
            Call<AcceptOrderResponse> call = apiInterface.AcceptOrder(builder.build());
            call.enqueue(new Callback<AcceptOrderResponse>() {
                @Override
                public void onResponse(Call<AcceptOrderResponse> call, Response<AcceptOrderResponse> response) {

                    Log.d("11111", "11111");

                    if (response.isSuccessful() && response.body() != null && response.body().getSuccess().equals("1")) {
                        if (order_status.equals("2")) {
                            Toast.makeText(getApplicationContext(), "Your Order is Accepted successfully.", Toast.LENGTH_SHORT).show();
                            showConfirmOrderDialog(orderDetailsResponse);
                        } else if (order_status.equals("3")) {
                            Toast.makeText(getApplicationContext(), "Your Order is Picked successfully.", Toast.LENGTH_SHORT).show();
                            showOrderDeliveredDialog(orderDetailsResponse);
                            dialog.dismiss();
                        } else if (order_status.equals("4")) {
                            Toast.makeText(getApplicationContext(), "Your Order is Completed successfully.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            pickUpOrderDialog.dismiss();
                            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        }
                    }
                }

                @Override
                public void onFailure(Call<AcceptOrderResponse> call, Throwable t) {
                    //onApiFailure(call, t);

                    Log.d("0000", "0000");
                    Toast.makeText(MainActivity.this, "Something went wrong!.", Toast.LENGTH_SHORT).show();
                }
            });
        }else {

            Toast.makeText(getApplicationContext(), "Please check your Internet Connection.", Toast.LENGTH_SHORT).show();
        }
    }



    private void LogoutApi() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        FormBody.Builder builder = ApiClient.createBuilder(new String[]
                        {"user_id","user_type"},
                new String[]{UserId,"0"});     //  User Type '0' for driver
        if(CommonUtils.isNetworkAvailable(getApplicationContext())) {
            Call<ResponseBody> call = apiInterface.Logout(builder.build());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                        AppPrefrences.clearPrefsdata(getApplicationContext());
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    //onApiFailure(call, t);
                    Toast.makeText(MainActivity.this, "Something went wrong!.", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(getApplicationContext(), "Please check your Internet Connection.", Toast.LENGTH_SHORT).show();
        }
    }



    private void sendCurrentLatLongApi(String latitude, String longitude) {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        FormBody.Builder builder = ApiClient.createBuilder(new String[]
                        {"user_id","lat","lng"},
                new String[]{UserId,latitude,longitude});     //  Current Lat Long
        if(CommonUtils.isNetworkAvailable(getApplicationContext())) {
            Call<BaseResponse> call = apiInterface.SendLatLong(builder.build());
            call.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    if (response.isSuccessful()) {

                        Toast.makeText(MainActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    //onApiFailure(call, t);
                    Toast.makeText(MainActivity.this, "Something went wrong!.", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(getApplicationContext(), "Please check your Internet Connection.", Toast.LENGTH_SHORT).show();
        }
    }
}
