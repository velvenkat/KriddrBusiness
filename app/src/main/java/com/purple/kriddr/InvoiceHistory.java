package com.purple.kriddr;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.purple.kriddr.controller.AppController;
import com.purple.kriddr.iface.FragmentCallInterface;
import com.purple.kriddr.iface.InterfaceActionBarUtil;
import com.purple.kriddr.iface.InterfaceUserModel;
import com.purple.kriddr.model.InvoiceDetailsInfoModel;
import com.purple.kriddr.model.InvoiceModelGraph;
import com.purple.kriddr.model.InvoiceViewListModel;
import com.purple.kriddr.model.PetDetailsModel;
import com.purple.kriddr.model.UserModel;
import com.purple.kriddr.util.ActionBarUtil;
import com.purple.kriddr.util.GenFragmentCall_Main;
import com.purple.kriddr.util.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by pf-05 on 3/19/2018.
 */

public class InvoiceHistory extends Fragment implements OnChartGestureListener, OnChartValueSelectedListener {

    ActionBarUtil actionBarUtilObj;
    GenFragmentCall_Main fragmentCall_mainObj;
    View rootView;
    UserModel userModel;
    TabLayout tabLayout;
    private LineChart mchart;
    Context context;
    String key = "";
    JSONObject graphJsonObject;
    InvoiceModelGraph invoiceModelGraph;
    ArrayList<String> xVals;
    ArrayList<Entry> yVals;
    int Y_VAL_MAX=0;
    TextView week_value,month_value,year_value,total_val,revenue_week_value,revenue_month_value,revenue_year_value,revenue_total_val;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.invoice_graph, container, false);

        mchart = (LineChart) rootView.findViewById(R.id.linechart);
        mchart.setOnChartGestureListener(this);
        xVals = new ArrayList<String>();
        yVals = new ArrayList<Entry>();
        mchart.setOnChartValueSelectedListener(this);
        mchart.setDrawGridBackground(false);

        mchart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mchart.getXAxis().setDrawGridLines(false);




        mchart.setDescription(null);


        mchart.setTouchEnabled(true);


        week_value = (TextView)rootView.findViewById(R.id.week_value);
        month_value = (TextView)rootView.findViewById(R.id.month_value);
        year_value = (TextView)rootView.findViewById(R.id.year_value);
        total_val = (TextView)rootView.findViewById(R.id.total_val);

        revenue_week_value = (TextView)rootView.findViewById(R.id.revenue_week_value);
        revenue_month_value = (TextView)rootView.findViewById(R.id.revenue_month_value);
        revenue_year_value = (TextView)rootView.findViewById(R.id.revenue_year_value);
        revenue_total_val = (TextView)rootView.findViewById(R.id.revenue_total_val);



        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);

        key = "day";


        setupTabIcons();

        actionBarUtilObj.setActionBarVisible();

        actionBarUtilObj.getImgBack().setVisibility(View.INVISIBLE);

        actionBarUtilObj.getImgSettings().setVisibility(View.GONE);

        actionBarUtilObj.getImgSettings().setImageResource(R.drawable.search);

        actionBarUtilObj.setTitle("STATS");
        tabLayout.getTabAt(0).select();


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int Pos = tab.getPosition();
                switch (Pos) {
                    case 0:
                        key = "day";
                        break;
                    case 1:
                        key = "month";
                        break;
                    case 2:
                        key = "year";
                        break;
                }


                if (NetworkConnection.isOnline(getActivity())) {
                    invoiceHistoryDetails(getResources().getString(R.string.url_reference) + "invoice_week_month_year.php");
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        if (NetworkConnection.isOnline(getActivity())) {
            invoiceHistoryDetails(getResources().getString(R.string.url_reference) + "invoice_week_month_year.php");
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }



        return rootView;
    }


    public void invoiceHistoryDetails(String url) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //progress.hide();
                Log.d("INVOICELSTRES", "INVOICELSTRES" + s);

                xVals = new ArrayList<String>();
                yVals = new ArrayList<Entry>();
                Y_VAL_MAX=0;

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");


                    for (int index = 0; index < jsonArray.length(); index++) {
                        invoiceModelGraph = new InvoiceModelGraph();
                        graphJsonObject = jsonArray.getJSONObject(index);

                        invoiceModelGraph.setXaxis(graphJsonObject.getString("xaxis"));
                        invoiceModelGraph.setYaxis(graphJsonObject.getString("yaxis"));

                        String xAxis=graphJsonObject.getString("xaxis");


                        if(!xAxis.equalsIgnoreCase("Empty") )
                        {
                            int yAxis= graphJsonObject.getInt("yaxis");
                            xVals.add(xAxis);
                            yVals.add(new Entry(Float.valueOf(yAxis), index));
                            if(yAxis>Y_VAL_MAX)
                                Y_VAL_MAX=yAxis;

                            setData();

                        }
                        else
                        {
                            mchart.clear();
                        }




                    }


                    JSONObject basic = jsonObject.getJSONObject("sent_invoice_total");



                    String wee_amount = "", month_amount = "", year_amount= "", total= "", rev_amount_week = "" , rev_amount_month = "", rev_amount_year = "", rev_amount_total = "";


                    wee_amount = basic.getString("wee_amount");
                    month_amount = basic.getString("month_amount");
                    year_amount = basic.getString("year_amount");
                    total = basic.getString("total");

                    rev_amount_week = basic.getString("wee_amount_rv");
                    rev_amount_month = basic.getString("month_amount_rv");
                    rev_amount_year = basic.getString("year_amount_rv");
                    rev_amount_total = basic.getString("total_rv");




                    Log.d("RESPAMT",""+wee_amount + "MO AMT"+month_amount + "YER AMT"+year_amount + "TOTALAM"+total);

                    if(wee_amount.equalsIgnoreCase("null"))
                    {
                        week_value.setText("$"+"0");
                    }
                    else
                    {
                        week_value.setText("$"+wee_amount);
                    }

                    if(month_amount.equalsIgnoreCase("null"))
                    {
                        month_value.setText("$"+"0");
                    }
                    else
                    {
                        month_value.setText("$"+month_amount);

                    }

                    if(year_amount.equalsIgnoreCase("null"))
                    {
                        year_value.setText("$"+"0");
                    }
                    else
                    {
                        year_value.setText("$"+year_amount);
                    }

                    if(total.equalsIgnoreCase("null"))
                    {
                        total_val.setText("$"+"0");
                    }
                    else
                    {
                        total_val.setText("$"+total);
                    }

                    if(rev_amount_week.equalsIgnoreCase("null"))
                    {
                        revenue_week_value.setText("$"+"0");
                    }
                    else
                    {
                        revenue_week_value.setText("$"+rev_amount_week);
                    }

                    if(rev_amount_month.equalsIgnoreCase("null"))
                    {
                        revenue_month_value.setText("$"+"0");
                    }
                    else
                    {
                        revenue_month_value.setText("$"+rev_amount_month);
                    }

                    if(rev_amount_year.equalsIgnoreCase("null"))
                    {
                        revenue_year_value.setText("$"+"0");
                    }
                    else
                    {
                        revenue_year_value.setText("$"+rev_amount_year);
                    }

                    if(rev_amount_total.equalsIgnoreCase("null"))
                    {
                        revenue_total_val.setText("$"+"0");
                    }
                    else
                    {
                        revenue_total_val.setText("$"+rev_amount_total);
                    }




                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // progress.hide();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.d("USERID", "USERID" + userModel.getId() + "KERDS " + key);
               params.put("user_id", userModel.getId());

               // params.put("user_id","35");
                params.put("key", key);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request);

    }


    private void setupTabIcons() {

        View tabOne = (View) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_btn_graph, null);
        TextView txtTab1 = (TextView) tabOne.findViewById(R.id.tab);
        txtTab1.setText("WEEK");
        TabLayout.Tab tab = tabLayout.newTab();
        tabLayout.addTab(tab);
        tab.setCustomView(tabOne);
        tabOne.setBackgroundResource(R.drawable.rounded_cornor_tab);


        View tabTwo = (View) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_btn_graph, null);
        TextView txtTab2 = (TextView) tabTwo.findViewById(R.id.tab);
        txtTab2.setText("MONTH");
        TabLayout.Tab tab1 = tabLayout.newTab();
        tabLayout.addTab(tab1);
        tab1.setCustomView(tabTwo);
        tabTwo.setBackgroundResource(R.drawable.rounded_cornor_tab_normal);


        View tabThree = (View) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_btn_graph, null);
        TextView txtTab3 = (TextView) tabThree.findViewById(R.id.tab);
        txtTab3.setText("YEAR");
        TabLayout.Tab tab2 = tabLayout.newTab();
        tabLayout.addTab(tab2);
        tab2.setCustomView(tabThree);
        tabThree.setBackgroundResource(R.drawable.rounded_cornor_tab_right);

    }


    private ArrayList<String> setXAxisValues() {


        return xVals;
    }

    private ArrayList<Entry> setYAxisValues() {


        return yVals;
    }

    private void setData() {



        YAxis leftAxis = mchart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines

        leftAxis.setAxisMaxValue(Y_VAL_MAX+100);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setLabelCount(yVals.size(), true);

        leftAxis.setDrawZeroLine(false);


        leftAxis.setEnabled(true);
        mchart.getAxisRight().setEnabled(false);
        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawLimitLinesBehindData(true);



        mchart.animateX(2500, Easing.EasingOption.EaseInOutQuart);

        //  dont forget to refresh the drawing


        mchart.invalidate();


        LineDataSet set1;

        // create a dataset and give it a type
        set1 = new LineDataSet(yVals, " ");

        set1.setFillAlpha(110);
        set1.setDrawValues(false);

        set1.setDrawHighlightIndicators(false);
        set1.setFillDrawable(getResources().getDrawable(R.drawable.line_fill));


        set1.setLineWidth(1f);

        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);


        // set data
        mchart.setData(data);



    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        InterfaceUserModel interfaceUserModel;

        if (context instanceof FragmentCallInterface) {
            FragmentCallInterface callInterface = (FragmentCallInterface) context;
            fragmentCall_mainObj = callInterface.Get_GenFragCallMainObj();
        }
        if (context instanceof InterfaceActionBarUtil) {
            actionBarUtilObj = ((InterfaceActionBarUtil) context).getActionBarUtilObj();

        }

        if (context instanceof InterfaceUserModel) {
            interfaceUserModel = (InterfaceUserModel) context;
            userModel = interfaceUserModel.getUserModel();

        }
    }


    @Override
    public void onPause() {
        super.onPause();
        // LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);

    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getContext())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "START, x: " + me.getX() + ", y: " + me.getY());
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "END, lastGesture: " + lastPerformedGesture);

        // un-highlight values after the gesture is finished and no single-tap
        if (lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
            mchart.highlightValues(null); // or highlightTouch(null) for callback to onNothingSelected(...)
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i("LongPress", "Chart longpressed.");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
    }


    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }


    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }

}
