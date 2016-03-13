package com.pankaj.cube26_paymentportal.Activity;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.pankaj.cube26_paymentportal.Entities.GatewayInfo;
import com.pankaj.cube26_paymentportal.Entities.PaymentGateways;
import com.pankaj.cube26_paymentportal.Fragments.GatewayDetailsFragment;
import com.pankaj.cube26_paymentportal.Fragments.LikesFragment;
import com.pankaj.cube26_paymentportal.R;
import com.pankaj.cube26_paymentportal.Utils.AlertMessage;
import com.pankaj.cube26_paymentportal.Utils.Constants;
import com.pankaj.cube26_paymentportal.Utils.SlidingTabLayout;

import java.util.List;

import static com.pankaj.cube26_paymentportal.Fragments.GatewayDetailsFragment.*;

public class GatewayInfoActivity extends AppCompatActivity {

    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;

    PaymentGateways objPaymentGateways;
    String selectedDate;
    View scrolledView;

    SamplePagerAdapter adapter;

    public OnLikeAdd onLikeAddListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gateway_info_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_gatewayInfo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Gateway Info");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        init();
        // BEGIN_INCLUDE (setup_viewpager)
        // Get the ViewPager and set it's PagerAdapter so that it can display
        // items
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new SamplePagerAdapter();
        mViewPager.setAdapter(adapter);
        // END_INCLUDE (setup_viewpager)

        // BEGIN_INCLUDE (setup_slidingtablayout)
        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the
        // ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setCustomTabView(R.layout.tabview_layout,
                R.id.tabview_textview);
        mSlidingTabLayout.setSelectedIndicatorColors(getColorResourceId(R.color.colorPrimary));
        mSlidingTabLayout.setViewPager(mViewPager);
        // END_INCLUDE (setup_slidingtablayout)
    }

    private void init() {
        selectedDate = "";
        scrolledView = null;
        try {
            Intent intent = getIntent();
            objPaymentGateways = (PaymentGateways) intent
                    .getSerializableExtra(Constants.PARAMETER_GATEWAY_INFO);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        onLikeAddListner = new OnLikeAdd() {
//            @Override
//            public void onAdd() {
//                likeAdded();
//            }
//        };
    }

    public void likeAdded(){

    }

    public int getColorResourceId(int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(GatewayInfoActivity.this, id);
        } else {
            return getResources().getColor(id);
        }
    }

    /**
     * The {@link android.support.v4.view.PagerAdapter} used to display pages in
     * this sample. The individual pages are simple and just display two lines
     * of text. The important section of this class is the
     * {@link #getPageTitle(int)} method which controls what is displayed in the
     * {@link SlidingTabLayout}.
     */
    public class SamplePagerAdapter extends PagerAdapter {

        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return 2;
        }

        /**
         * @return true if the value returned from
         *         {@link #instantiateItem(ViewGroup, int)} is the same object
         *         as the {@link View} added to the {@link ViewPager}.
         */
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        // BEGIN_INCLUDE (pageradapter_getpagetitle)
        /**
         * Return the title of the item at {@code position}. This is important
         * as what this method returns is what is displayed in the
         * {@link SlidingTabLayout}.
         * <p>
         * Here we construct one using the position value, but for real
         * application the title should refer to the item's contents.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            String title;
            if (position == 0) {
                title = "Gateway Info";
            } else {
                title = "Likes";
            }
            return title;
        }

        // END_INCLUDE (pageradapter_getpagetitle)

        /**
         * Instantiate the {@link View} which should be displayed at
         * {@code position}. Here we inflate a layout from the apps resources
         * and then change the text view to signify the position.
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = null;
            if (position == 0) {
                view = getLayoutInflater().inflate(
                        R.layout.gateway_details_layout,
                        container, false);
                view = new GatewayDetailsFragment().getFragmentView(new LikesFragment(), view,
                        objPaymentGateways, GatewayInfoActivity.this);
            } else {
                view = getLayoutInflater().inflate(
                        R.layout.likes_layout, container, false);
                view = new LikesFragment().getFragmentView(view,GatewayInfoActivity.this);
            }
            container.addView(view);
            scrolledView = view;
            return view;
        }

        /**
         * Destroy the item from the {@link ViewPager}. In our case this is
         * simply removing the {@link View}.
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public interface OnLikeAdd{
        public void onAdd(List<?> arrayList, Activity activity);
    }
}
