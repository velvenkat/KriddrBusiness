package com.purple.kriddr;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.purple.kriddr.adapter.PetClientListAdapter;
import com.purple.kriddr.controller.AppController;
import com.purple.kriddr.iface.FragmentCallInterface;
import com.purple.kriddr.iface.InterfaceActionBarUtil;
import com.purple.kriddr.iface.InterfaceUserModel;
import com.purple.kriddr.model.UserModel;
import com.purple.kriddr.util.ActionBarUtil;
import com.purple.kriddr.util.GenFragmentCall_Main;
import com.purple.kriddr.util.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.os.Build.VERSION_CODES.M;
import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;
import static com.purple.kriddr.controller.AppController.TAG;

/**
 * Created by pf-05 on 2/22/2018.
 */

public class RecordCreationFragment extends Fragment implements View.OnClickListener{


    ImageView add_photo;
    View rootView;
    EditText docu_name;
    Spinner category_value;
    String documents_name = "", documents_id = "";
    ArrayList<String> docIdList = new ArrayList<>();
    ArrayList<String> docNameList = new ArrayList<>();
    ArrayAdapter<String> category_adapter;
    String doc_id = "", doc_name = "";
    final int PIC_CROP = 11;
    Uri picUri;
    Button submit_button;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap selectedBitmap;
    String image = "";
    GenFragmentCall_Main fragmentCall_mainObj;
    ActionBarUtil actionBarUtilObj;
    UserModel userModel;
    String pet_id = "";
    String[] Per_List=new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.add_records,container,false);



        ((KridderNavigationActivity)getActivity()).setNavigationVisibility(false);

        Bundle bundle_args=getArguments();
        if(bundle_args!=null) {
            pet_id = bundle_args.getString("pet_id", null);


        }

        actionBarUtilObj.setActionBarVisible();

        actionBarUtilObj.getImgBack().setVisibility(View.VISIBLE);

        actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
            }
        });

        actionBarUtilObj.setTitle("BACK");

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            //Toast.makeText(this, "Permission checking", Toast.LENGTH_SHORT).show();
            checkPermission();
        }

        if (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA)) { // asks primission to use the devices camera

            //what ever you want to do ..

        } else {
            requestWritePermission(getActivity());
        }


        add_photo = (ImageView)rootView.findViewById(R.id.add_photo);
        docu_name = (EditText)rootView.findViewById(R.id.doc_name);
        category_value = (Spinner)rootView.findViewById(R.id.category_value);

        submit_button = (Button)rootView.findViewById(R.id.submit_button);


        category_adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_layout, docNameList);
        category_adapter.setDropDownViewResource(R.layout.simple_spinner_contact);

        category_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {

                    doc_id = docIdList.get(position).toString();
                    doc_name = docNameList.get(position).toString();
                    Log.v("TYPENAME","TYPENAME"+doc_id + "TYNA"+doc_name);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        if (NetworkConnection.isOnline(getActivity()))
        {
            categoryList(getResources().getString(R.string.url_reference) + "master_data.php");

        }



        add_photo.setOnClickListener(this);

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validateData();



            }
        });



        return rootView;
    }




    public void validateData()
    {

        String docName = docu_name.getText().toString().trim();

        if(docName.isEmpty() || docName.equalsIgnoreCase(""))
        {
            Toast.makeText(getActivity(), "Please enter the document name", Toast.LENGTH_SHORT).show();
        }
        else if (selectedBitmap == null) {
            Toast.makeText(getActivity(), "Please choose the image", Toast.LENGTH_SHORT).show();
        }

        else {
            if (NetworkConnection.isOnline(getActivity())) {
                submitData(getResources().getString(R.string.url_reference) + "pet_documents_creation.php",docName);
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            }
        }


    }





    private void submitData(String url, final String docName)
    {

        final MyProgressDialog myProgressDialog = new MyProgressDialog(getActivity());

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //progress.hide();
                Log.d("RECORDFRAG","RECORDFRAG"+s);

                myProgressDialog.hide();

                try
                {

                    JSONObject jsonObject = new JSONObject(s);
                    String id = jsonObject.getString("id");
                    String result = jsonObject.getString("result");
                    if(result.equalsIgnoreCase("Success"))
                    {

                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(getResources().getString(R.string.record_creation))
                                .setCancelable(false)
                                .setNeutralButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();

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



                image = getStringImage(selectedBitmap);

                Log.d("SERNRES","SERNRES"+userModel.getId() + "PE ID"+pet_id + "NA "+docName + "DOCATID "+doc_id + "IMG "+image);

                Gson gson = new Gson();
                params.put("user_id",userModel.getId());
                params.put("pet_id",pet_id);
                params.put("name",docName);
                params.put("document_category_id",doc_id);
                params.put("image",image);
                return params;
            }

        };

        myProgressDialog.show();
        AppController.getInstance().addToRequestQueue(request);
    }




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


    private void categoryList(String url)
    {
        StringRequest request = new StringRequest(Request.Method.POST,url,new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.d("CATEGORLIST","CATEGORLIST"+s);

               docIdList.clear();
               docNameList.clear();

//
//                typeIdList.add("Select");
//                typeNameList.add("Select");

                try {

                    JSONArray jsonArray = new JSONArray(s);

                    for(int i = 0; i < jsonArray.length();i++)
                    {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        documents_id = obj.getString("documents_id");
                        documents_name = obj.getString("documents_name");

                        docIdList.add(documents_id);
                        docNameList.add(documents_name);
                        category_value.setEnabled(true);

                    }

                    category_value.setAdapter(category_adapter);


//                    for (int i = 0; i < typeIdList.size();i++) {
//
//                        if (gas_type_id.equals(typeIdList.get(i))) {
//                            type_spin.setSelection(i);
//                        }
//                    }




                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("master_data","documents_category");



                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request);
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



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {


            {
                if (getPickImageResultUri(data) != null) {
                    picUri = getPickImageResultUri(data);

                    try {
                        selectedBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picUri);
                        selectedBitmap = rotateImageIfRequired(selectedBitmap, picUri);
                        selectedBitmap = getResizedBitmap(selectedBitmap, 200);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } else {


                    selectedBitmap = (Bitmap) data.getExtras().get("data");

                }

                add_photo.setImageBitmap(selectedBitmap);
            }





//            if(requestCode == PIC_CROP)
//            {
//
//                if (data != null) {
//                    // get the returned data
//
//                    Bundle extras = data.getExtras();
//
//
//                    try {
//                        selectedBitmap = extras.getParcelable("data");
//                        if(selectedBitmap== null)
//                        {
//
//                        }
//
//
//                    }
//                    catch (Exception e)
//                    {
//                        Uri uri = data.getData();
//
//
//                        try {
//                            selectedBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri);
//                        } catch (IOException e1) {
//                            e1.printStackTrace();
//                        }
//                        e.printStackTrace();
//
//                    }
//
//
//
//
//
//                    add_photo.setImageBitmap(selectedBitmap);
//                }
//
//
//

            }





//            if (requestCode == PICK_IMAGE_REQUEST)
//            {
//
//
//                if (getPickImageResultUri(data) != null) {
//                    picUri = getPickImageResultUri(data);
//
//
//                   performCrop(picUri);
//
////                try {
////                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getCfontentResolver(), picUri);
////                    bitmap = rotateImageIfRequired(bitmap, picUri);
////                    bitmap = getResizedBitmap(bitmap, 200);
////
////
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
//
//
//                }
////                else {
////
////
////                    bitmap = (Bitmap) data.getExtras().get("data");
////
////                }
//            }

//            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
//            add_photo.setImageDrawable(drawable);
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


        if (checkPermissionForExternalStorage()||checkPermissionForCamera()){


            Log.v("pickuri",picUri+"");

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







      //  try {
       /*     Intent cropIntent = new Intent("com.android.camera.action.CROP");

            Uri contentUri;
            File file=new File(picUri.getPath());
            if(Build.VERSION.SDK_INT > M){

                //contentUri = FileProvider.getUriForFile(getActivity(), "android3.maxtingapp.provider",file);//package.provider

                //TODO:  Permission..
                   file=new File(picUri.getPath());
                  contentUri = FileProvider.getUriForFile(getActivity(), "android3.maxtingapp.provider", file);//package.provider
                getActivity().getApplicationContext().grantUriPermission("com.android.camera", picUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

                cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            }else{

                contentUri = Uri.fromFile(file);

            }

            cropIntent.setDataAndType(contentUri, "image*//*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 2);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);

            cropIntent.putExtra("return-data", true);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
            startActivityForResult(cropIntent, PIC_CROP);
*//*
        }catch (ActivityNotFoundException a) {
            Log.e("Activity Not Found",""+a.toString());
        }*/



    }


    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }


        return isCamera ? getCaptureImageOutputUri() : data.getData();
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


    private void showFileChooser() {
        //  Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // startActivityForResult(i, PICK_IMAGE_REQUEST);

        startActivityForResult(getPickImageChooserIntent(), PICK_IMAGE_REQUEST);

    }


}
