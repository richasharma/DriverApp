package com.ekaaksh.driverapp.Fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.ekaaksh.driverapp.R;

public class PickUpOrderFragment extends DialogFragment implements View.OnClickListener{

    View rootView;
    ImageView imgAccept, imgDecline;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.pickup_order_layout, container, false);

        imgAccept = (ImageView)rootView.findViewById(R.id.imgAccept);
        imgDecline = (ImageView)rootView.findViewById(R.id.imgDecline);

        imgAccept.setOnClickListener(this);
        imgDecline.setOnClickListener(this);

        return rootView;

    }


    @Override
    public void onClick(View view) {

        if(view == imgAccept){
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Ongoing Order");
            showConfirmOrderDialog();
        }
        else  if(view == imgDecline){
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Ongoing Order");
            dismiss();
        }

    }

    private void showConfirmOrderDialog() {


        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
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
        //TextView tvConfirmOrder = (TextView) dialog.findViewById(R.id.tvConfirmOrder);


       /* tvConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showOrderDeliveredDialog();
            }
        });*/

        dialog.show();

    }

    private void showOrderDeliveredDialog() {

        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
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
        ImageView imgCall = (ImageView) dialog.findViewById(R.id.imgCall);

        tvOrderDelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //dismissing pickUpOrder dialog fragment
                Fragment prev = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
                if (prev != null) {
                    DialogFragment df = (DialogFragment) prev;
                    df.dismiss();
                }
            }
        });

        imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        dialog.show();
    }
}
