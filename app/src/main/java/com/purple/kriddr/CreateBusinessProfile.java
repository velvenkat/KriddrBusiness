package com.purple.kriddr;

import android.app.Activity;
import android.app.AlertDialog;

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
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.purple.kriddr.controller.AppController;
import com.purple.kriddr.iface.InterfaceActionBarUtil;
import com.purple.kriddr.model.UserModel;
import com.purple.kriddr.util.ActionBarUtil;
import com.purple.kriddr.util.NetworkConnection;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

/**
 * Created by pf-05 on 1/31/2018.
 */

public class CreateBusinessProfile extends Fragment implements View.OnClickListener {


    View rootView;
    private EditText business, mobile, address;
    private Button create_profile;
    String business_Name, mobile_Number, address_Value, user_id;
    ImageView add_Photo;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    // private Resources mResources;
    Uri picUri;
    UserModel userModel;
    int ScreenFromVal;
    RelativeLayout rl_email_contr;
    List<UserModel> feedslist;
    EditText edt_email;
    String image = "";
    ActionBarUtil actionBarUtilObj;
    public static final int CROP_IMAGE_ACTIVITY_REQUEST_CODE = 203;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.create_bussiness_profile, container, false);

        add_Photo = (ImageView) rootView.findViewById(R.id.add_photo);
        business = (EditText) rootView.findViewById(R.id.business_name);
        mobile = (EditText) rootView.findViewById(R.id.mobile);
        edt_email = (EditText) rootView.findViewById(R.id.edt_email);
        rl_email_contr = (RelativeLayout) rootView.findViewById(R.id.rl_email_contr);

        mobile.addTextChangedListener(new PhoneNumberFormattingTextWatcher("US"));
        address = (EditText) rootView.findViewById(R.id.address);
        create_profile = (Button) rootView.findViewById(R.id.create_profile);

        actionBarUtilObj.setActionBarVisible();

        actionBarUtilObj.getEditText().setVisibility(View.INVISIBLE);

        business.requestFocus();


        actionBarUtilObj.getImgBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.Screens.EDIT_PROFILE.ordinal() == ScreenFromVal) {
                    Intent in = new Intent(getActivity(), KridderNavigationActivity.class);
                    in.putExtra(KridderNavigationActivity.USER_MODEL_TAG, userModel);
                    in.putExtra(MainActivity.SCREEN_FROM_TAG, MainActivity.Screens.UPDATE_PROFILE.ordinal());
                    getActivity().finish();
                    startActivity(in);
                } else {
                    alert_Back(getActivity());
                }
            }
        });


        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            //Toast.makeText(this, "Permission checking", Toast.LENGTH_SHORT).show();
            checkPermission();
        }


        if (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA)) { // asks primission to use the devices camera

            //what ever you want to do ..

        } else {
            requestWritePermission(getActivity());
        }

        Bundle bundle = getArguments();

        try {
            userModel = bundle.getParcelable(KridderNavigationActivity.USER_MODEL_TAG);
            ScreenFromVal = bundle.getInt(MainActivity.SCREEN_FROM_TAG);

            if (ScreenFromVal == MainActivity.Screens.EDIT_PROFILE.ordinal()) {
                //((TextView) actionBar.getCustomView().findViewById(R.id.textBarTitle)).setText("EDIT PROFILE");
                actionBarUtilObj.setTitle("EDIT PROFILE");
                create_profile.setText("UPDATE PROFILE");
                rl_email_contr.setVisibility(View.VISIBLE);
                edt_email.setText(userModel.getEmail());
                Log.d("PAGCALL", "PAGCALL" + userModel.getBusiness_name() + "USERID VAL " + userModel.getId());
                userModel = bundle.getParcelable(KridderNavigationActivity.USER_MODEL_TAG);
                business.setText(userModel.getBusiness_name());
                mobile.setText(userModel.getBusiness_phone());
                address.setText(userModel.getBusiness_address());

                Glide.with(getActivity()).load(userModel.getLogo_url()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).transform(new CircleTransform(getActivity())).into(add_Photo);



            } else {
                rl_email_contr.setVisibility(View.GONE);
                actionBarUtilObj.setTitle("CREATE BUSINESS PROFILE");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        Log.e("HHH", "HHHMMM");

        add_Photo.setOnClickListener(this);

        create_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateData();

            }
        });
        //handle_BackKey();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Fragment mContent;
        if (savedInstanceState != null) {
            mContent = getActivity().getSupportFragmentManager().getFragment(savedInstanceState, "CRT_Busns_STATE");
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.frame_layout, mContent, "crt_busns_scrn");


            fragmentTransaction.addToBackStack("crt_busns_scrn");

            fragmentTransaction.commit();

        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getActivity().getSupportFragmentManager().putFragment(outState, "CRT_Busns_STATE", this);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InterfaceActionBarUtil) {
            actionBarUtilObj = ((InterfaceActionBarUtil) context).getActionBarUtilObj();
        }
    }

    public void onResume() {
        super.onResume();
        //    business.requestFocus();


        business.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    business.clearFocus();
                }
                return false;
            }
        });
        address.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    address.clearFocus();
                }
                return false;
            }
        });
        mobile.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    mobile.clearFocus();
                }
                return false;
            }
        });
        handle_BackKey();
    }

    public void handle_BackKey() {
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && (event.getAction() == KeyEvent.ACTION_UP)) {
                    if (ScreenFromVal == MainActivity.Screens.EDIT_PROFILE.ordinal()) {
                        Intent in = new Intent(getActivity(), KridderNavigationActivity.class);
                        in.putExtra(KridderNavigationActivity.USER_MODEL_TAG, userModel);
                        in.putExtra(MainActivity.SCREEN_FROM_TAG, MainActivity.Screens.UPDATE_PROFILE.ordinal());
                        getActivity().finish();
                        startActivity(in);
                    } else
                        alert_Back(getActivity());
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 123: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Peform your task here if any
                } else {

                    checkPermission();
                }
                return;
            }
        }
    }


    public void alert_Back(Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(context.getResources().getString(R.string.do_you_want_exit))
                .setCancelable(false)
                .setNegativeButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        getActivity().finish();
                    }
                })
                .setPositiveButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

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
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            {
                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == RESULT_OK) {
                        ((ImageView) getActivity().findViewById(R.id.add_photo)).setImageURI(result.getUri());

                        picUri = result.getUri();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picUri);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                        drawable.setCircular(true);
                        add_Photo.setImageDrawable(drawable);


                        // Toast.makeText(getActivity(), "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Toast.makeText(getActivity(), "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
                    }


                }
                if (requestCode == PICK_IMAGE_REQUEST) {
                    if (getPickImageResultUri(data) != null) {
                        picUri = getPickImageResultUri(data);

                        CropImage.activity(picUri)
                                .setGuidelines(CropImageView.Guidelines.OFF)
                                .setActivityTitle("My Crop")
                                .setCropShape(CropImageView.CropShape.OVAL)
                                .setCropMenuCropButtonTitle("Done")
                                .setMinCropResultSize(400, 400)
                                .setMaxCropResultSize(1500, 1500)
                                .setAspectRatio(1, 1)
                                .start(getContext(), this);


                    }
                }
            }

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


    private void validateData() {
        boolean isFieldSet = true;
        business_Name = business.getText().toString().trim();
        mobile_Number = mobile.getText().toString().trim();
        mobile_Number = mobile_Number.replaceAll("[^0-9]", "").trim();
        mobile_Number = mobile_Number.trim();
        String EMailVal = "";
        address_Value = address.getText().toString().trim();
        if (business_Name.equals("") || business_Name.isEmpty()) {
            isFieldSet = false;
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.enter_business), Toast.LENGTH_SHORT).show();
        } else if (mobile_Number.isEmpty() || mobile_Number.equals("")) {
            isFieldSet = false;
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.enter_mobile), Toast.LENGTH_SHORT).show();
        } else if (address_Value.equals("") || address_Value.isEmpty()) {
            isFieldSet = false;
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.enter_address), Toast.LENGTH_SHORT).show();
        } else if (ScreenFromVal == MainActivity.Screens.EDIT_PROFILE.ordinal()) {
            EMailVal = edt_email.getText().toString();
            if (EMailVal.trim().equalsIgnoreCase("")) {
                isFieldSet = false;
                Toast.makeText(getActivity(), getResources().getString(R.string.enter_email_val), Toast.LENGTH_SHORT).show();

            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(EMailVal).matches()) {
                isFieldSet = false;
                Toast.makeText(getActivity(), getResources().getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
            } else if (bitmap == null) {
                image = "old";
            } else {

                if (bitmap != null) {
                    image = getStringImage(bitmap);
                }
            }


        } else {
            if (bitmap == null) {
                Toast.makeText(getActivity(), getResources().getString(R.string.choose_image), Toast.LENGTH_SHORT).show();
                isFieldSet = false;
                //  isImageSet=false;
            }

            if (bitmap != null) {
                image = getStringImage(bitmap);
            }
        }

        if (isFieldSet) {
            if (NetworkConnection.isOnline(getActivity())) {
                if (ScreenFromVal == MainActivity.Screens.EDIT_PROFILE.ordinal()) {
                    createProfile(getResources().getString(R.string.url_reference) + "business_details_edit.php");
                } else {
                    createProfile(getResources().getString(R.string.url_reference) + "business_creation.php");
                }

            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            }
        } else {
            handle_BackKey();

        }


    }

    private void createProfile(String url) {
        final MyProgressDialog myProgressDialog=new MyProgressDialog(getActivity());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.d("PROFILERES", "PROFILERES" + s);
                myProgressDialog.hide();
                try {
                    UserModel flower = new UserModel();
                    JSONObject jsonObj = new JSONObject(s);

                    flower.setId(jsonObj.getString("user_id"));
                    flower.setName(jsonObj.getString("user_name"));
                    flower.setMobile(jsonObj.getString("mobile"));
                    flower.setEmail(jsonObj.getString("email"));
                    flower.setStatus(jsonObj.getString("status"));
                    flower.setBusiness_status(jsonObj.getString("business"));
                    flower.setBusiness_id(jsonObj.getString("business_id"));
                    flower.setLogo_url(jsonObj.getString("logo"));
                    flower.setBusiness_name(jsonObj.getString("business_name"));
                    flower.setBusiness_phone(jsonObj.getString("phone"));
                    flower.setBusiness_address(jsonObj.getString("address"));


                    if (flower.getBusiness_status().equals("created")) {

                        dbhelp entry = new dbhelp(getActivity());
                        entry.open();
                        entry.deleteTable();
                        entry.createuser(flower.getId(), flower.getName(), flower.getEmail(), flower.getMobile(), flower.getBusiness_status(), flower.getStatus(), flower.getBusiness_id(), flower.getBusiness_name(), flower.getLogo_url(), flower.getBusiness_phone(), flower.getBusiness_address());
                        entry.close();

                        Intent intent = new Intent(getActivity(), KridderNavigationActivity.class);
                        if (ScreenFromVal == MainActivity.Screens.EDIT_PROFILE.ordinal()) {
                            intent.putExtra(MainActivity.SCREEN_FROM_TAG, MainActivity.Screens.UPDATE_PROFILE.ordinal());
                        } else {
                            intent.putExtra(MainActivity.SCREEN_FROM_TAG, MainActivity.Screens.CREATE_PROFILE.ordinal());
                        }
                        intent.putExtra(KridderNavigationActivity.USER_MODEL_TAG, flower);
                        ((Activity) getActivity()).finish();
                        getActivity().startActivity(intent);


                    } else if (flower.getStatus().equals("Exist")) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.business_already_create), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
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
            protected Response<String> parseNetworkResponse(NetworkResponse response) {

                return super.parseNetworkResponse(response);
            }


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();


                Gson gson = new Gson();

                Log.d("CREBUSRES", "CREBUSRES" + userModel.getId() + "NAM" + business_Name + "PH " + mobile_Number + "ADD " + address_Value + "IMG " + image + "BUSID " + userModel.getBusiness_id());

                params.put("user_id", userModel.getId());
                params.put("name", business_Name);
                params.put("phone", mobile_Number);
                params.put("address", address_Value);
                if (ScreenFromVal == MainActivity.Screens.EDIT_PROFILE.ordinal()) {

                    params.put("business_id", userModel.getBusiness_id());
                    params.put("email", edt_email.getText().toString().trim());
                }
                params.put("image", image);
                return checkParams(params);
            }

            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }


        };
        myProgressDialog.show();
        AppController.getInstance().addToRequestQueue(request);

    }

    private void showFileChooser() {

        startActivityForResult(getPickImageChooserIntent(), PICK_IMAGE_REQUEST);


    }


    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getActivity().getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
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

        if (v == add_Photo) {
            showFileChooser();
        }

    }
}
