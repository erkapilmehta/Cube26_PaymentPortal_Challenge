package com.pankaj.cube26_paymentportal.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.pankaj.cube26_paymentportal.Activity.GatewayInfoActivity;
import com.pankaj.cube26_paymentportal.Entities.GatewayInfo;
import com.pankaj.cube26_paymentportal.Entities.PaymentGateways;
import com.pankaj.cube26_paymentportal.R;
import com.pankaj.cube26_paymentportal.Utils.AlertMessage;
import com.pankaj.cube26_paymentportal.Utils.ConnectionDetector;
import com.pankaj.cube26_paymentportal.Utils.Constants;
import com.pankaj.cube26_paymentportal.Utils.ReadWriteJsonFileUtils;
import com.pankaj.cube26_paymentportal.Utils.TextAwesome;
import com.pankaj.cube26_paymentportal.adapters.ListBaseAdapter;
import com.pankaj.cube26_paymentportal.webservices.RestWebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class GatewaysListFragment extends Fragment {

    ListView listView;
    TextView apiHitsTextView;
    TextView totalGatewayTextView;
    Button nameButton;
    Button ratingButton;
    Button listAllButton;
    EditText searchEditText;


    ArrayList<PaymentGateways> paymentGatewaysArrayList;
    List<PaymentGateways> tempList = new ArrayList<PaymentGateways>();

    ListBaseAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        init();
        View rootView = inflater.inflate(R.layout.fragment_payment_list, container,
                false);
        findViews(rootView);
        listeners();
        hideKeypad();
        setDefaultValues();
        getList();
        getApiHits();
        return rootView;
    }

    private void init() {
        paymentGatewaysArrayList = new ArrayList<PaymentGateways>();
        adapter = new ListBaseAdapter(getActivity(),paymentGatewaysArrayList);
    }

    private void findViews(View rootView) {
        listView = (ListView) rootView.findViewById(R.id.listview_listgateways);
        apiHitsTextView = (TextView) rootView
                .findViewById(R.id.textView_listGateways_apiHit);
        totalGatewayTextView = (TextView) rootView
                .findViewById(R.id.textView_listGateways_totalGateways);
        nameButton = (Button) rootView
                .findViewById(R.id.button_listGateways_price);
        ratingButton = (Button) rootView
                .findViewById(R.id.button_listGateways_rating);
        searchEditText = (EditText) rootView.findViewById(R.id.editText_search);
        listAllButton = (Button) rootView
                .findViewById(R.id.button_listGateways_listAll);
    }

    private void listeners() {
        listAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addList(paymentGatewaysArrayList);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                PaymentGateways objGateways = (PaymentGateways) parent.getAdapter().getItem(position);
                Intent intent = new Intent(getActivity(), GatewayInfoActivity.class);
                intent.putExtra(Constants.PARAMETER_GATEWAY_INFO, objGateways);
                getActivity().startActivity(intent);
            }
        });

        nameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter != null) {
                    ArrayList<PaymentGateways> arrayList = (ArrayList<PaymentGateways>) adapter
                            .getList();
                    Collections.sort(arrayList, byName());
                    adapter.addList(arrayList);
                }
            }
        });

        ratingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter != null) {
                    ArrayList<PaymentGateways> arrayList = (ArrayList<PaymentGateways>) adapter
                            .getList();
                    Collections.sort(arrayList, byRating());
                    adapter.addList(arrayList);
                }
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                tempList = new ArrayList<PaymentGateways>();
                tempList.clear();
            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                if (adapter != null) {
                    if (s.length() > 1) {
                        if (adapter.getList() != null) {
                            ArrayList<PaymentGateways> arrayList = (ArrayList<PaymentGateways>) adapter
                                    .getList();
                            for (int i = 0; i < arrayList.size(); i++) {
                                string = string.toLowerCase();
                                String name = arrayList.get(i).getName()
                                        .toLowerCase();
                                String currency = arrayList.get(i)
                                        .getCurrencies().toLowerCase();
                                if (name.startsWith(string)) {
                                    tempList.add(arrayList.get(i));
                                } else if (currency.contains(string)) {
                                    tempList.add(arrayList.get(i));
                                }
                            }
                            adapter.addList(tempList);
                            totalGatewayTextView.setText(String.format(
                                    getActivity().getResources().getString(
                                            R.string.total_gateways),
                                    tempList.size()));
                        }
                    } else {
                        adapter.addList(paymentGatewaysArrayList);
                        totalGatewayTextView.setText(String.format(getActivity()
                                        .getResources().getString(R.string.total_gateways),
                                paymentGatewaysArrayList.size()));
                    }
                }
            }
        });
    }

    private Comparator<PaymentGateways> byName() {
        return new Comparator<PaymentGateways>() {
            @Override
            public int compare(PaymentGateways lhs, PaymentGateways rhs) {
                String lhsString = lhs.getName();
                String rhsString = rhs.getName();
                return lhsString.compareTo(rhsString);
            }
        };
    }

    private Comparator<PaymentGateways> byRating() {
        return new Comparator<PaymentGateways>() {
            @Override
            public int compare(PaymentGateways lhs, PaymentGateways rhs) {
                return rhs.getRating().compareTo(lhs.getRating());
            }
        };
    }

    private void getList() {
        boolean showLoading = true;

        // for checking data in file if exist
        String data = new ReadWriteJsonFileUtils(getActivity())
                .readJsonFileData(Constants.PARAMETER_JSON_FILE_GATEWAY_INFO);
        // if there is data then assign to arraylist
        if (data != null) {
            GatewayInfo objGatewayInfo = new Gson().fromJson(data,
                    GatewayInfo.class);
            paymentGatewaysArrayList = new ArrayList<PaymentGateways>(Arrays.asList(objGatewayInfo
                    .getPayment_gateways()));
            if (paymentGatewaysArrayList.size() > 0) {
                setAdapter();
                totalGatewayTextView.setText(String.format(getActivity()
                                .getResources().getString(R.string.total_gateways),
                        paymentGatewaysArrayList.size()));
            }
            showLoading = false;
        }

        if (!new ConnectionDetector(getActivity()).isConnectedToInternet()) {
            new AlertMessage(getActivity())
                    .showTostMessage(Constants.NO_Internet);
            setDefaultValues();
            return;
        }

        callAPIParcelInfo(showLoading);
    }

    private void callAPIParcelInfo(final boolean showLoading) {
        new RestWebService(getActivity()) {
            public void onSuccess(String data) {
                if (showLoading) {
                    GatewayInfo objGatewayInfo = new Gson().fromJson(data,
                            GatewayInfo.class);
                    paymentGatewaysArrayList = new ArrayList<PaymentGateways>(
                            Arrays.asList(objGatewayInfo.getPayment_gateways()));
                    new ReadWriteJsonFileUtils(getActivity())
                            .createJsonFileData(
                                    Constants.PARAMETER_JSON_FILE_GATEWAY_INFO,
                                    data);
                    setAdapter();
                    totalGatewayTextView.setText(String.format(getActivity()
                                    .getResources().getString(R.string.total_gateways),
                            paymentGatewaysArrayList.size()));
                } else {
                    new ReadWriteJsonFileUtils(getActivity())
                            .createJsonFileData(
                                    Constants.PARAMETER_JSON_FILE_GATEWAY_INFO,
                                    data);
                }
            };

            public void onError(VolleyError error) {
                Log.d("Error", error.toString());
            };
        }.serviceCall(Constants.API_GET_GATEWAY_LIST, "", showLoading);
    }

    private void getApiHits() {
        if (!new ConnectionDetector(getActivity()).isConnectedToInternet()) {
            return;
        }

        new RestWebService(getActivity()) {
            public void onSuccess(String data) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(data);
                    String apiHits = jsonObject
                            .getString(Constants.PARAMETER_API_HITS);
                    apiHitsTextView.setText(String.format(getActivity()
                                    .getResources().getString(R.string.api_hit),
                            apiHits));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };

            public void onError(VolleyError error) {
                Log.d("Error", error.toString());
            };
        }.serviceCall(Constants.API_GET_HIT_COUNT, "", false); // false for not
        // showing
        // loading
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void setAdapter() {
        adapter = new ListBaseAdapter(getActivity(), paymentGatewaysArrayList) {

            @Override
            public View setViews(int position, View convertView, ViewGroup parent, LayoutInflater inflater, List arrayList) {
                ViewHolder holder = new ViewHolder();
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.list_item_layout, parent,
                            false);
                    holder = new ViewHolder();
                    holder.nameTextView = (TextView) convertView
                            .findViewById(R.id.textview_name);
                    holder.priceTextView = (TextView) convertView
                            .findViewById(R.id.textview_price);
                    holder.ratingBar = (RatingBar) convertView
                            .findViewById(R.id.ratingBar);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                Object object = arrayList.get(position);
                if (object instanceof PaymentGateways) {
                    PaymentGateways obj = (PaymentGateways) object;
                    holder.nameTextView.setText(obj.getName());

                    try {
                        holder.ratingBar.setRating(Float.parseFloat(obj
                                .getRating()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                return convertView;
            }

        };
        listView.setAdapter(adapter);
    }

    public static class ViewHolder {
        public TextView nameTextView;
        public TextView priceTextView;
        public RatingBar ratingBar;
        public LinearLayout ratingLinearLayout;
        public TextAwesome textAwesome;
    }

    private void hideKeypad() {
        View view = getActivity().getCurrentFocus();

        InputMethodManager inputManager = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view instanceof EditText) {
            inputManager.hideSoftInputFromWindow(getActivity()
                            .getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void setDefaultValues() {
        apiHitsTextView.setText(String.format(getActivity().getResources()
                .getString(R.string.api_hit), "N/A"));
        totalGatewayTextView.setText(String.format(getActivity().getResources()
                .getString(R.string.total_gateways), "N/A"));
    }


}
