package com.pankaj.cube26_paymentportal.Fragments;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.pankaj.cube26_paymentportal.Activity.GatewayInfoActivity;
import com.pankaj.cube26_paymentportal.Entities.Likes;
import com.pankaj.cube26_paymentportal.Entities.PaymentGateways;
import com.pankaj.cube26_paymentportal.PaymentPortalApplication;
import com.pankaj.cube26_paymentportal.R;
import com.pankaj.cube26_paymentportal.Utils.AlertMessage;
import com.pankaj.cube26_paymentportal.database.DatabaseHelper;

import java.util.ArrayList;

/**
 * Created by Pankaj on 13/03/16.
 */
public class GatewayDetailsFragment {

    Activity activity;

    PaymentGateways objPaymentGateways;

    TextView ratingTextView, nameTextView, transactionFeeTextView, brandinTextView, supportedCurenciesTextView,
            descriptionTextView;

    Button shareButton, likeButton, downloadButton;

    RatingBar ratingBar;

    NetworkImageView imageView;

    int totalCount = 0;
    GatewayInfoActivity.SamplePagerAdapter adapter;
    GatewayInfoActivity.OnLikeAdd onLikeAddListner;

    public View getFragmentView(GatewayInfoActivity.OnLikeAdd onLikeAddListner, View rootView, PaymentGateways objPaymentGateways, Activity activity) {
        this.objPaymentGateways = objPaymentGateways;
        this.activity = activity;
        this.onLikeAddListner = onLikeAddListner;

        findView(rootView);
        setViewsValues();
        listeners();
        return rootView;
    }

    private void findView(View rootView) {
        imageView = (NetworkImageView) rootView
                .findViewById(R.id.imageView);
        nameTextView = (TextView) rootView
                .findViewById(R.id.textview_gatewayInfo_name);
        transactionFeeTextView = (TextView) rootView
                .findViewById(R.id.textView_gatewayInfo_transactionFee);
        brandinTextView = (TextView) rootView
                .findViewById(R.id.textView_gatewayInfo_branding);
        supportedCurenciesTextView = (TextView) rootView
                .findViewById(R.id.textView_gatewayInfo_supportedCurrencies);
        ratingTextView = (TextView) rootView
                .findViewById(R.id.textView_gatewayInfo_rating);
        descriptionTextView = (TextView) rootView.findViewById(R.id.textView_gatewayInfo_description);
        ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar_gatewayInfo);
        shareButton = (Button) rootView.findViewById(R.id.button_gatewayInfo_share);
        likeButton = (Button) rootView.findViewById(R.id.button_gatewayInfo_like);
        downloadButton = (Button) rootView.findViewById(R.id.button_gatewayInfo_document);
    }

    private void setViewsValues() {
        try {
            imageView.setImageUrl(objPaymentGateways.getImage(), PaymentPortalApplication
                    .getInstance().getImageLoader());
            nameTextView.setText(objPaymentGateways.getName());
            transactionFeeTextView.setText(String.format(activity.getResources()
                    .getString(R.string.transaction_fee), objPaymentGateways.getTransaction_fees()));
            ratingTextView.setText(String.format(activity.getResources()
                    .getString(R.string.rating_star), objPaymentGateways.getRating()));

            if(objPaymentGateways.getBranding().equalsIgnoreCase("0")) {
                brandinTextView.setText(String.format(activity.getResources()
                        .getString(R.string.branding), activity.getResources()
                        .getString(R.string.branding_no)));
            }else{
                brandinTextView.setText(String.format(activity.getResources()
                        .getString(R.string.branding), activity.getResources()
                        .getString(R.string.branding_yes)));
            }

            supportedCurenciesTextView.setText(String.format(activity.getResources()
                    .getString(R.string.supported_currencies), objPaymentGateways.getCurrencies()));
            descriptionTextView.setText(objPaymentGateways.getDescription());
            ratingBar.setRating(Float.parseFloat(objPaymentGateways.getRating()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listeners() {

        shareButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey checkout this payment portal app.");
                sendIntent.setType("text/plain");
                activity.startActivity(sendIntent);
            }
        });

        likeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatabaseHelper dbHelper = new DatabaseHelper(activity);
                ArrayList<Likes> likesArrayList = dbHelper.getAllLikesArrayList();
                if (likesArrayList.size() > 0) {
                    for (Likes obj : likesArrayList) {
                        if(obj.getName().equalsIgnoreCase(objPaymentGateways.getName())){
                            totalCount = Integer.parseInt(obj.getTotalCount());
                            Log.d("Count=====>", totalCount+"");
                            break;
                        }
                    }
                }
                dbHelper.close();

                if(totalCount <= 0) {
                    dbHelper = new DatabaseHelper(activity);
                    Likes obj = new Likes();
                    obj.setName(objPaymentGateways.getName());
                    totalCount++;
                    obj.setTotalCount(totalCount + "");
                    dbHelper.addLike(obj);
                    dbHelper.close();
                } else{
                    dbHelper = new DatabaseHelper(activity);
                    Likes obj = new Likes();
                    obj.setName(objPaymentGateways.getName());
                    totalCount++;
                    obj.setTotalCount(totalCount + "");
                    dbHelper.updateLike(obj);
                    dbHelper.close();
                }

                dbHelper = new DatabaseHelper((GatewayInfoActivity)activity);
                likesArrayList = dbHelper.getAllLikesArrayList();
                dbHelper.close();

                onLikeAddListner.onAdd(likesArrayList, activity);
                new AlertMessage(activity).showTostMessage(R.string.like_added);
            }
        });

        downloadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                startDownload(objPaymentGateways.getHow_to_document());
            }
        });
    }

    public void startDownload(String url) {
        new AlertMessage(activity).showTostMessage(R.string.downloading_wait);
        DownloadManager mManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request mRqRequest = new DownloadManager.Request(
                Uri.parse(url));
        mRqRequest.setDescription("Downloading...");
        long idDownLoad=mManager.enqueue(mRqRequest);
    }


}
