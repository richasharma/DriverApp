package com.ekaaksh.driverapp.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.ekaaksh.driverapp.Model.Response.GetProfileResponse;
import com.ekaaksh.driverapp.R;
import com.ekaaksh.driverapp.Rest.ApiClient;
import com.ekaaksh.driverapp.Rest.ApiInterface;
import com.ekaaksh.driverapp.Utils.AppPrefrences;
import com.ekaaksh.driverapp.Utils.CommonUtils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileSettingsFragment extends Fragment {

    View rootView;
    TextView tvDriverName,tvDriverPhone,tvDriverEmail,tvDriverAddress,tvVehicleNumber,tvVehicleType,tvTotalDeliveries,tvTotalRevenue;
    CircularImageView imgDriverPicture;
    ImageView ivUpdateImage;
    String UserId;
    AVLoadingIndicatorView avi;

    private static final String IMAGE_DIRECTORY = "/Foodona";
    private int GALLERY = 1, CAMERA = 2;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.driver_profile_layout, container, false);

        requestMultiplePermissions();

        UserId = AppPrefrences.getSavedUser(getActivity()).getId(); // Deliveryboy UserId.
        Log.e("UserId", ""+UserId);

        tvDriverName = (TextView)rootView.findViewById(R.id.tvDriverName);
        tvDriverPhone = (TextView)rootView.findViewById(R.id.tvDriverPhone);
        tvDriverEmail = (TextView)rootView.findViewById(R.id.tvDriverEmail);
        tvDriverAddress = (TextView)rootView.findViewById(R.id.tvDriverAddress);
        tvVehicleNumber = (TextView)rootView.findViewById(R.id.tvVehicleNumber);
        tvVehicleType = (TextView)rootView.findViewById(R.id.tvVehicleType);
        tvTotalDeliveries = (TextView)rootView.findViewById(R.id.tvTotalDeliveries);
        tvTotalRevenue = (TextView)rootView.findViewById(R.id.tvTotalRevenue);
        imgDriverPicture = (CircularImageView)rootView.findViewById(R.id.imgDriverPicture);
        ivUpdateImage = (ImageView)rootView.findViewById(R.id.ivUpdateImage);
        avi = (AVLoadingIndicatorView) rootView.findViewById(R.id.avi);

        getProfileDetailsApi();

        ivUpdateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });

        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Profile Settings");
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imgDriverPicture.setImageBitmap(thumbnail);
            saveImage(thumbnail);
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());

            uploadToServer(f.getAbsolutePath(),myBitmap);  // uploading umage to server

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void  requestMultiplePermissions(){
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getActivity(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getActivity(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }



    private void getProfileDetailsApi() {

        avi.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        FormBody.Builder builder = ApiClient.createBuilder(new String[]
                        {"deliverboy_id"},
                new String[]{UserId});
        if (CommonUtils.isNetworkAvailable(getActivity())) {
            Call<GetProfileResponse> call = apiInterface.GetProfileDetails(builder.build());
            call.enqueue(new Callback<GetProfileResponse>() {
                @Override
                public void onResponse(Call<GetProfileResponse> call, Response<GetProfileResponse> response) {

                    avi.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        try {
                            if (response.body() != null) {
                                tvDriverName.setText(""+response.body().getName());
                                tvDriverPhone.setText(""+response.body().getPhone());
                                tvDriverEmail.setText(""+response.body().getEmail());
                                tvDriverAddress.setText(""+response.body().getAddress());
                                tvVehicleNumber.setText(""+response.body().getVehicle_no());
                                tvVehicleType.setText(""+response.body().getVehicle_type());
                                tvTotalDeliveries.setText(""+response.body().getComplete_order()+" deliveries");
                                tvTotalRevenue.setText("₹"+response.body().getRevenue()+ " earned");

                                String imageName = ""+response.body().getImage();
                                String imageUrl = "https://www.ekaakshgroup.in/FoodDeliverySystem/food/"+imageName;

                                Picasso.with(getActivity()).load(imageUrl).fit().centerCrop()
                                       .placeholder(R.drawable.profile)
                                       .error(R.drawable.profile)
                                       .into(imgDriverPicture);

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetProfileResponse> call, Throwable t) {
                    avi.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Profile Something went wrong!.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            avi.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Please check your Internet Connection.", Toast.LENGTH_SHORT).show();
        }
    }


    private void uploadToServer(String filePath, final Bitmap myBitmap) {

        avi.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        //Create a file object using file path
        File file = new File(filePath);
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);

        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);

        //Create request body with text description and text media type
        RequestBody userId = RequestBody.create(MediaType.parse("user_id"), UserId);

        Call<ResponseBody> call = apiInterface.uploadFile(part, userId);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()){
                    avi.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Your profile image has been updated successfully. ", Toast.LENGTH_SHORT).show();
                    imgDriverPicture.setImageBitmap(myBitmap);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                avi.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Update Profile Something went wrong!.", Toast.LENGTH_SHORT).show();
            }
        });
    }
 }
