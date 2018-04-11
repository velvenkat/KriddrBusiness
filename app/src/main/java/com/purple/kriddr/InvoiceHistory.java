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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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

public class InvoiceHistory extends Fragment implements OnChartGestureListener, OnChartValueSelectedListener, LocationListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    ActionBarUtil actionBarUtilObj;
    GenFragmentCall_Main fragmentCall_mainObj;
    View rootView;
    UserModel userModel;
    TabLayout tabLayout;
    private LineChart mchart;
    private GoogleMap googleMapObj;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Marker mCurrLocationMarker;
    Context context;
    Location mLastLocation;
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

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


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
                     //xVals.add("0");
//
//                    xVals = new ArrayList<String>();
//                    yVals = new ArrayList<Entry>();

                    for (int index = 0; index < jsonArray.length(); index++) {
                        invoiceModelGraph = new InvoiceModelGraph();
                        graphJsonObject = jsonArray.getJSONObject(index);

                        invoiceModelGraph.setXaxis(graphJsonObject.getString("xaxis"));
                        invoiceModelGraph.setYaxis(graphJsonObject.getString("yaxis"));

                        String xAxis=graphJsonObject.getString("xaxis");


                        if(!xAxis.equalsIgnoreCase("Empty") )
                        {
                            int yAxis=graphJsonObject.getInt("yaxis");
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
                    // add data


                    //JSONObject jsonObject1 = new JSONObject("sent_invoice_total");

                    JSONObject basic = jsonObject.getJSONObject("sent_invoice_total");


                    Log.d("KGDFASDF","KGDFASDF"+basic);


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

        View tabOne = (View) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_btn, null);
        TextView txtTab1 = (TextView) tabOne.findViewById(R.id.tab);
        txtTab1.setText("WEEK");
        TabLayout.Tab tab = tabLayout.newTab();
        tabLayout.addTab(tab);
        tab.setCustomView(tabOne);
        tabOne.setBackgroundResource(R.drawable.rounded_cornor_tab);


        View tabTwo = (View) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_btn, null);
        TextView txtTab2 = (TextView) tabTwo.findViewById(R.id.tab);
        txtTab2.setText("MONTH");
        TabLayout.Tab tab1 = tabLayout.newTab();
        tabLayout.addTab(tab1);
        tab1.setCustomView(tabTwo);
        tabTwo.setBackgroundResource(R.drawable.rounded_cornor_tab_normal);
        /*tabTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                key = "month";

            }
        });
*/

        View tabThree = (View) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_btn, null);
        TextView txtTab3 = (TextView) tabThree.findViewById(R.id.tab);
        txtTab3.setText("YEAR");
        TabLayout.Tab tab2 = tabLayout.newTab();
        tabLayout.addTab(tab2);
        tab2.setCustomView(tabThree);
        tabThree.setBackgroundResource(R.drawable.rounded_cornor_tab_right);
/*

        tabThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                key = "year";
            }
        });
*/



       /* tabLayout.add(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("DAFDASF","DAFDASF");
                Toast.makeText(getActivity(),"This is onTabSelected",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

               // Log.d("RESERE","RESERE");

              //  Toast.makeText(getActivity(),"This is onTabUnSelected",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d("ONTABSERE","ONTABSERE");


                Toast.makeText(getActivity(),"This is onTabReSelected",Toast.LENGTH_SHORT).show();
            }
        });

*/
        Log.d("KERYVARLDDA", "KERYVARLDDA");

    }


    private ArrayList<String> setXAxisValues() {
//
//        xVals.add("Fri");
//        xVals.add("Sat");
//        xVals.add("M0n");
//        xVals.add("Tues");
//        xVals.add("G");

        return xVals;
    }

    private ArrayList<Entry> setYAxisValues() {

//        yVals.add(new Entry(60, 0));
//        yVals.add(new Entry(48, 1));
//        yVals.add(new Entry(42.5f, 2));
//        yVals.add(new Entry(52, 3));
//        yVals.add(new Entry(68.9f, 4));

        return yVals;
    }

    private void setData() {



        YAxis leftAxis = mchart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        //    leftAxis.addLimitLine(upper_limit);
        //      leftAxis.addLimitLine(lower_limit);
        leftAxis.setAxisMaxValue(Y_VAL_MAX+100);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setLabelCount(yVals.size(), true);
        //leftAxis.setYOffset(20f);

        //  leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);

      /*  // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);*/
        leftAxis.setEnabled(true);
        mchart.getAxisRight().setEnabled(false);
        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawLimitLinesBehindData(true);


        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        mchart.animateX(2500, Easing.EasingOption.EaseInOutQuart);

        //  dont forget to refresh the drawing


        mchart.invalidate();


        LineDataSet set1;

        // create a dataset and give it a type
        set1 = new LineDataSet(yVals, " ");

        set1.setFillAlpha(110);
        set1.setDrawValues(false);

        set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        //   set1.enableDashedLine(10f, 5f, 0f);
        // set1.enableDashedHighlightLine(10f, 5f, 0f);
        // set1.setColor(Color.RED);
        // set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        // set1.setCircleRadius(3f);
        //  set1.setDrawCircleHole(false);
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
            //  Toast.makeText(getActivity(),"USRMDOELDID"+userModel.getId(),Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    public void onPause() {
        super.onPause();
        // LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);

    }


    public void StoreLocation(double latitude, double longitude) {

        SharedPreferences sharedPref = getActivity().getSharedPreferences("locationpref", 0);
        SharedPreferences.Editor sharedEdite = sharedPref.edit();
        sharedEdite.putFloat("latitude", (float) latitude);
        sharedEdite.putFloat("longitute", (float) longitude);
        sharedEdite.commit();
    }


    private boolean isGooglePlayServicesAvailable() {
        int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);

        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), status, 0).show();
            return false;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMapObj = googleMap;
        googleMapObj.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        googleMapObj.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

             /*   StoreLocation(latLng.latitude,  latLng.longitude);

                Log.d("MAPLATLONG",""+latLng.latitude + "DLAFD"+latLng.longitude);



                mCurrLocationMarker.setPosition(latLng);
                googleMapObj.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMapObj.animateCamera(CameraUpdateFactory.zoomTo(15));*/
                Location loc = new Location("");
                loc.setLatitude(latLng.latitude);
                loc.setLongitude(latLng.longitude);
                onLocationChanged(loc);
            }
        });
        // Setting a custom info window adapter for the google map
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker arg0) {

                // Getting view from the layout file info_window_layout
                View v = getLayoutInflater().inflate(R.layout.map_info_view, null);

                // Getting the position from the marker

                LatLng latLng = arg0.getPosition();

                // Getting reference to the TextView to set latitude
                TextView tvLat = (TextView) v.findViewById(R.id.txtInfo);


                // Setting the latitude
                tvLat.setText(arg0.getTitle());


                // Returning the view containing InfoWindow contents
                return v;

            }
        });
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                googleMapObj.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            googleMapObj.setMyLocationEnabled(true);
        }

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
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

    /* @Override
     public boolean onKey(View v, int keyCode, KeyEvent event) {

         if(KeyEvent.KEYCODE_BACK==keyCode){
             if (mGoogleApiClient != null) {
                 LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
             }
           //  locationManager.removeUpdates(this);
             return true;
         }

         return false;
     }
 */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        googleMapObj.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getContext(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));


        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        String address = "", city = "", state = "", country = "", postalCode = "", knownName = "", locationName = "";


        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(String.valueOf(location.getLatitude())), Double.parseDouble(String.valueOf(location.getLongitude())), 1);


            for (int index = 0; index < addresses.size(); index++) {
                address = addresses.get(index).getAddressLine(index); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                city = addresses.get(index).getLocality();
                state = addresses.get(index).getAdminArea();
//                country = addresses.get(index).getCountryName();
                postalCode = addresses.get(index).getPostalCode();
                knownName = addresses.get(index).getFeatureName();
                locationName = address;
                markerOptions.title(locationName);
                mCurrLocationMarker = googleMapObj.addMarker(markerOptions);
                mCurrLocationMarker.showInfoWindow();
                mCurrLocationMarker.setZIndex(100);
                Log.d("MAPADDR", "MAPADDR" + address + "Ci " + city + "St " + state + "Cou " + country + "POc " + postalCode);
            }


        } catch (IOException e) {
            mCurrLocationMarker = googleMapObj.addMarker(markerOptions);
            e.printStackTrace();
        }


        StoreLocation(latLng.latitude, latLng.longitude);
        //move map camera
        googleMapObj.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

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
