package com.pankaj.cube26_paymentportal.Fragments;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.pankaj.cube26_paymentportal.Activity.GatewayInfoActivity;
import com.pankaj.cube26_paymentportal.Entities.GatewayInfo;
import com.pankaj.cube26_paymentportal.Entities.Likes;
import com.pankaj.cube26_paymentportal.Entities.PaymentGateways;
import com.pankaj.cube26_paymentportal.R;
import com.pankaj.cube26_paymentportal.Utils.TextAwesome;
import com.pankaj.cube26_paymentportal.adapters.ListBaseAdapter;
import com.pankaj.cube26_paymentportal.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pankaj on 13/03/16.
 */
public class LikesFragment implements GatewayInfoActivity.OnLikeAdd{

    Activity activity;

    ListView listView;
    TextView textView;

    ListBaseAdapter adapter;

    public View getFragmentView(View rootView, Activity activity) {
        this.activity = activity;

        findView(rootView);

        DatabaseHelper dbHelper = new DatabaseHelper((GatewayInfoActivity)activity);
        ArrayList<Likes> likesArrayList = dbHelper.getAllLikesArrayList();

        dbHelper.close();

        setAdapter(likesArrayList);
        return rootView;
    }

    private void findView(View view) {
        listView = (ListView) view.findViewById(R.id.listView_likes);
        textView = (TextView) view.findViewById(R.id.textView_likes);
    }

    private void setAdapter(List<?> likesArrayList) {

        adapter = new ListBaseAdapter(activity, likesArrayList) {
            @Override
            public View setViews(int position, View convertView, ViewGroup parent, LayoutInflater inflater, List arrayList) {
                GatewaysListFragment.ViewHolder holder = new GatewaysListFragment.ViewHolder();
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.list_item_layout, parent,
                            false);
                    holder = new GatewaysListFragment.ViewHolder();
                    holder.nameTextView = (TextView) convertView
                            .findViewById(R.id.textview_name);
                    holder.priceTextView = (TextView) convertView
                            .findViewById(R.id.textview_price);
                    holder.ratingBar = (RatingBar) convertView
                            .findViewById(R.id.ratingBar);
                    holder.ratingLinearLayout = (LinearLayout) convertView
                            .findViewById(R.id.layout_rating);
                    holder.textAwesome = (TextAwesome)convertView
                            .findViewById(R.id.textAwesome);
                    convertView.setTag(holder);
                } else {
                    holder = (GatewaysListFragment.ViewHolder) convertView.getTag();
                }

                holder.ratingLinearLayout.setVisibility(View.GONE);
                holder.textAwesome.setVisibility(View.GONE);

                Object object = arrayList.get(position);
                if (object instanceof Likes) {
                    Likes obj = (Likes) object;
                    holder.nameTextView.setText(obj.getName());

                    holder.priceTextView.setText(String.format(activity.getResources().getString(R.string.total_likes),obj.getTotalCount()));
                }

                return convertView;
            }
        };
        listView.setAdapter(adapter);
        listView.setEmptyView(textView);
    }

    @Override
    public void onAdd(List<?> arrayList, Activity activity) {
        this.activity = activity;
        listView = (ListView)((GatewayInfoActivity)activity).findViewById(R.id.listView_likes);
        setAdapter(arrayList);
    }
}
