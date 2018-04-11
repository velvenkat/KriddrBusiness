package com.purple.kriddr;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.purple.kriddr.controller.AppController;
import com.purple.kriddr.iface.FragmentCallInterface;
import com.purple.kriddr.iface.InterfaceActionBarUtil;
import com.purple.kriddr.iface.InterfaceUserModel;
import com.purple.kriddr.model.PetModel;
import com.purple.kriddr.model.UserModel;
import com.purple.kriddr.util.ActionBarUtil;
import com.purple.kriddr.util.GenFragmentCall_Main;
import com.purple.kriddr.util.NetworkConnection;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

/**
 * Created by pf-05 on 2/15/2018.
 */

public class ClientCreationFragment extends Fragment implements View.OnClickListener{

    View rootView;
    UserModel userModel;
    PetModel petModel;
    ActionBarUtil actionBarUtilObj;
    ImageView add_photo;
    EditText petname_value,day_month_year,brand_value,protein_value,servings_value;
    TextView person_name_text,mobile_text,location_text;
    String petNameVal,dayMonthYear,brandVal,proteinVal,servingsVal;
    Button done_button;
    final int PIC_CROP = 11;
    final int PIC_CHOOSE = 12;

    String myFormat = "MM-dd-yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

    DatePickerDialog.OnDateSetListener pickup_date;

    Calendar myCalendar1 = Calendar.getInstance();

    String PickupDate_Str="", upcoming_date = "", phone_number = "";

    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap selectedBitmap;
    Uri picUri;
    String image = "";
    List<PetModel> feedlist;
    GenFragmentCall_Main fragmentCall_mainObj;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        InterfaceUserModel interfaceUserModel;

        if(context instanceof FragmentCallInterface)
        {
            FragmentCallInterface callInterface = (FragmentCallInterface)context;
            fragmentCall_mainObj = callInterface.Get_GenFragCallMainObj();
        }
        if (context instanceof InterfaceActionBarUtil)
        {
            actionBarUtilObj=((InterfaceActionBarUtil)context).getActionBarUtilObj();

        }

        if (context instanceof InterfaceUserModel) {
            interfaceUserModel = (InterfaceUserModel) context;
            userModel = interfaceUserModel.getUserModel();
            //  Toast.makeText(getActivity(),"USRMDOELDID"+userModel.getId(),Toast.LENGTH_SHORT).show();

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.add_client,container,false);

        ((KridderNavigationActivity)getActivity()).setNavigationVisibility(false);

        Bundle bundle = getArguments();

        try
        {

            phone_number = bundle.getString("mobile_number");
            petModel = bundle.getParcelable("pet_parent");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        add_photo = (ImageView)rootView.findViewById(R.id.add_photo);
        petname_value = (EditText)rootView.findViewById(R.id.petname_value);
        day_month_year = (EditText)rootView.findViewById(R.id.day_month_year);
        brand_value = (EditText)rootView.findViewById(R.id.brand_value);
        protein_value = (EditText)rootView.findViewById(R.id.protein_value);
        servings_value = (EditText)rootView.findViewById(R.id.servings_value);



        person_name_text = (TextView)rootView.findViewById(R.id.person_name_text);
        mobile_text = (TextView)rootView.findViewById(R.id.mobile_text);
        location_text = (TextView)rootView.findViewById(R.id.location_text);

        person_name_text.setText(petModel.getOwner_name());
        mobile_text.setText(petModel.getMobile());
        location_text.setText(petModel.getAddress());

        done_button = (Button)rootView.findViewById(R.id.done_button);


        actionBarUtilObj.setActionBarVisible();

        actionBarUtilObj.getImgBack().setVisibility(View.VISIBLE);

        actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
            }
        });

        actionBarUtilObj.getImgSettings().setVisibility(View.GONE);



        actionBarUtilObj.setTitle("Back");


        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            //Toast.makeText(this, "Permission checking", Toast.LENGTH_SHORT).show();
            checkPermission();
        }


        if (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA)) { // asks primission to use the devices camera

            //what ever you want to do ..

        } else {
            requestWritePermission(getActivity());
        }


        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validateData();
            }
        });



        pickup_date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar1.set(Calendar.YEAR, year);
                myCalendar1.set(Calendar.MONTH, monthOfYear);
                myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Log.v("date", monthOfYear + "");




                updatePickupDate();

            }

        };


        final DatePickerDialog datePickerDialog=  new DatePickerDialog(getActivity(), pickup_date, myCalendar1
                .get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                myCalendar1.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());


        day_month_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               datePickerDialog.show();


                updatePickupDate();
            }
        });



        add_photo.setOnClickListener(this);


        return rootView;
    }


    private static void requestWritePermission(final Context context) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(context)
                    .setMessage("This app needs permission to use The phone Camera in order to activate the Scanner")
                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.CAMERA}, 1);
                        }
                    }).show();

        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.CAMERA}, 1);
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {//Can add more as per requirement

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    123);

        } else {

        }
    }


    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public boolean checkPermissionForExternalStorage(){
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){
            Log.v("permission","grantedyes");

            return true;
        } else {
            Log.v("permission","grantedno");
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            return false;
        }
    }

    public boolean checkPermissionForCamera(){
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED){
            Log.v("permission","grantedyes");

            return true;
        } else {
            Log.v("permission","grantedno");
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);

            return false;
        }
    }




    private void performCrop(Uri picUri) {

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }


        if (checkPermissionForExternalStorage()||checkPermissionForCamera())
        {
            try {
                Intent cropIntent = new Intent("com.android.camera.action.CROP");
                // indicate image type and Uri
                cropIntent.setDataAndType(picUri, "image/*");
                // set crop properties here
                cropIntent.putExtra("crop", true);
                // indicate aspect of desired crop
                cropIntent.putExtra("aspectX", 1);
                cropIntent.putExtra("aspectY", 1);
                // indicate output X and Y
                cropIntent.putExtra("outputX", 128);
                cropIntent.putExtra("outputY", 128);
                // retrieve data on return
                cropIntent.putExtra("return-data", true);
                cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                // start the activity - we handle returning in onActivityResult
                startActivityForResult(cropIntent, PIC_CROP);
            }
            // respond to users whose devices do not support the crop action
            catch (ActivityNotFoundException anfe) {
                // display an error message
                String errorMessage = "Whoops - your device doesn't support the crop action!";
                Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == PIC_CROP)
            {

                if (data != null) {
                    // get the returned data


                    Bundle extras = data.getExtras();


                    try {
                        selectedBitmap = extras.getParcelable("data");
                        if(selectedBitmap== null)
                        {

                        }


                    }
                    catch (Exception e)
                    {

                        // This solution is work for A1 plus mobile. We are write the below code for A1 mobile previously bitmap is getting null.
                        Uri uri = data.getData();


                        try {
                            selectedBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        e.printStackTrace();

                    }





                    add_photo.setImageBitmap(selectedBitmap);
                }
            }





        if (requestCode == PICK_IMAGE_REQUEST)
        {


                if (getPickImageResultUri(data) != null) {
                    picUri = getPickImageResultUri(data);


                    performCrop(picUri);

//                try {
//                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picUri);
//                    bitmap = rotateImageIfRequired(bitmap, picUri);
//                    bitmap = getResizedBitmap(bitmap, 200);
//
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }


                }
//                else {
//
//
//                    bitmap = (Bitmap) data.getExtras().get("data");
//
//                }
            }

//            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
//            drawable.setCircular(true);
//            add_photo.setImageDrawable(drawable);
        }


    }

    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }


        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }


    private void showFileChooser() {
        //  Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // startActivityForResult(i, PICK_IMAGE_REQUEST);

        startActivityForResult(getPickImageChooserIntent(), PICK_IMAGE_REQUEST);

    }


    public Intent getPickImageChooserIntent() {

        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getActivity().getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // the main intent is the last in the list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }


    @Override
    public void onClick(View v) {

        if (v == add_photo) {
            showFileChooser();
        }

    }


    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getActivity().getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }





    public void validateData() {

        petNameVal = petname_value.getText().toString().trim();
        dayMonthYear = day_month_year.getText().toString().trim();
        brandVal = brand_value.getText().toString().trim();
        proteinVal = protein_value.getText().toString().trim();
        servingsVal = servings_value.getText().toString().trim();



        if (petNameVal.isEmpty() || petNameVal.equals("")) {
            Toast.makeText(getActivity(), getResources().getString(R.string.enter_pet_name), Toast.LENGTH_SHORT).show();
        }
//        else if (daymonthyear.isEmpty() || daymonthyear.equals("")) {
//            Toast.makeText(getActivity(), getResources().getString(R.string.enter_dob), Toast.LENGTH_SHORT).show();
//        }
//        else if (brandVal.isEmpty() || brandVal.equals("")) {
//            Toast.makeText(getActivity(), getResources().getString(R.string.enter_brand), Toast.LENGTH_SHORT).show();
//        }
//        else if (proteinVal.isEmpty() || proteinVal.equals("")) {
//            Toast.makeText(getActivity(), getResources().getString(R.string.enter_protein), Toast.LENGTH_SHORT).show();
//        }
//        else if (servingVal.isEmpty() || servingVal.equals("")) {
//            Toast.makeText(getActivity(), getResources().getString(R.string.enter_servings), Toast.LENGTH_SHORT).show();
//        }
//        else if (nameVal.isEmpty() || nameVal.equals("")) {
//            Toast.makeText(getActivity(), getResources().getString(R.string.enter_servings), Toast.LENGTH_SHORT).show();
//
//        }
//        else if (mobileVal.isEmpty() || mobileVal.equals("")) {
//            Toast.makeText(getActivity(), getResources().getString(R.string.enter_number), Toast.LENGTH_SHORT).show();
//        }
//        else if (addressVal.isEmpty() || addressVal.equals("")) {
//            Toast.makeText(getActivity(), getResources().getString(R.string.enter_address), Toast.LENGTH_SHORT).show();
//
//        }

        else {
            if (NetworkConnection.isOnline(getActivity())) {
                submitData(getResources().getString(R.string.url_reference) + "pet_creation.php");
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            }
        }

    }



    private void submitData(String url)
    {

        final MyProgressDialog myProgressDialog = new MyProgressDialog(getActivity());

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //progress.hide();
                Log.d("CLEINTDONRES","CLEINTDONRES"+s);

                myProgressDialog.hide();

                try
                {

                    JSONObject jsonObject = new JSONObject(s);
                    String id = jsonObject.getString("id");
                    String result = jsonObject.getString("result");
                    if(result.equalsIgnoreCase("Success"))
                    {

                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(getResources().getString(R.string.client_creation))
                                .setCancelable(false)
                                .setNeutralButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
//
//                                        Bundle bundle = new Bundle();
//                                        bundle.putString("mobile_number",phone_number);
//                                        bundle.putParcelable("pet_parent",petModel);
//                                        fragmentCall_mainObj.Fragment_call(new PetClientListFragment(),"clntback",bundle);

                                        ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();



                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();


                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // progress.hide();

                myProgressDialog.hide();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                if (selectedBitmap == null) {
                    image = "";
                } else {

                    if (selectedBitmap != null) {
                        image = getStringImage(selectedBitmap);
                    }
                }


                Log.d("SENTTOSERVE",""+userModel.getId() + "BRNA "+brandVal + "PROTEN "+proteinVal + "PROSE "+servingsVal);
                Log.d("UPCIMDS","UPCIMDS"+upcoming_date + "IMG "+image);

                Gson gson = new Gson();
                params.put("user_id",userModel.getId());
                params.put("owner_id",petModel.getOwwner_id());
                params.put("pet_name",petNameVal);
                params.put("dob",upcoming_date);
                params.put("brand",brandVal);
                params.put("protein",proteinVal);
                params.put("portion_size",servingsVal);
                params.put("image",image);
                return params;
            }

        };

        myProgressDialog.show();
        AppController.getInstance().addToRequestQueue(request);
    }







    public void updatePickupDate() {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));

        if (myCalendar1.getTime().after(cal.getTime())) {
            Toast.makeText(getActivity(), "Please select valid date", Toast.LENGTH_SHORT).show();
        }


        day_month_year.setText(sdf.format(myCalendar1.getTime()));

        PickupDate_Str=sdf.format(myCalendar1.getTime());
        String pick_date = sdf.format(myCalendar1.getTime()) + "";




        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date dateobj = new Date();
        upcoming_date = df.format(myCalendar1.getTime());





//        if (myCalendar1.getTime().before(cal.getTime())) {
//            Toast.makeText(getActivity(), "Please select valid date", Toast.LENGTH_SHORT).show();
//        } else {
//
//
//
//
//
//
//        }





    }
}
