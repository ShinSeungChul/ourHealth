package com.example.wlgusdn.ourhealth;


import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.*;

import com.amazonaws.amplify.generated.graphql.GetAccountQuery;
import com.amazonaws.amplify.generated.graphql.GetFoodQuery;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CaptureResult extends AppCompatActivity {

    private final String TAG = "CloudVisionExample";
    static final int REQUEST_GALLERY_IMAGE = 100;
    static final int REQUEST_CODE_PICK_ACCOUNT = 101;
    static final int REQUEST_ACCOUNT_AUTHORIZATION = 102;
    static final int REQUEST_PERMISSIONS = 13;
    private AWSAppSyncClient mAWSAppSyncClient = null;
    private static String accessToken;
   // private ImageView selectedImage;
    //private TextView labelResults;
    //private TextView textResults;
    private Account mAccount;
    private ProgressDialog mProgressDialog;
    private  HealthData_Singleton singleton = HealthData_Singleton.getInstance();
    private ArrayList<MenuList> menudata = new ArrayList<MenuList>();

    private ListView listview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_capture_result);
       // menudata = new ArrayList<MenuList>();

        listview = findViewById(R.id.list_menu);




        mProgressDialog = new ProgressDialog(this);

        //Button selectImageButton = findViewById(R.id.select_image_button);
        //selectedImage = findViewById(R.id.selected_image);
        //labelResults = findViewById(R.id.tv_label_results);
        //textResults = findViewById(R.id.tv_texts_results);
        //launchImagePicker();

        ActivityCompat.requestPermissions(CaptureResult.this,
                new String[]{Manifest.permission.GET_ACCOUNTS},
                REQUEST_PERMISSIONS);
        /*selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(CaptureResult.this,
                        new String[]{Manifest.permission.GET_ACCOUNTS},
                        REQUEST_PERMISSIONS);
            }
        });*/
    }

    private void launchImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an image"),
                REQUEST_GALLERY_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getAuthToken();
                } else {
                    Toast.makeText(CaptureResult.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY_IMAGE && resultCode == RESULT_OK && data != null) {
            performCloudVisionRequest(data.getData());
        } else if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
            if (resultCode == RESULT_OK) {
                String email = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                AccountManager am = AccountManager.get(this);
                Account[] accounts = am.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
                for (Account account : accounts) {
                    if (account.name.equals(email)) {
                        mAccount = account;
                        break;
                    }
                }
                getAuthToken();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "No Account Selected", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == REQUEST_ACCOUNT_AUTHORIZATION) {
            if (resultCode == RESULT_OK) {
                Bundle extra = data.getExtras();
                onTokenReceived(extra.getString("authtoken"));
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Authorization Failed", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    public void performCloudVisionRequest(Uri uri) {
        if (uri != null) {
            try {
                Bitmap bitmap = resizeBitmap(
                        MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
                callCloudVision(bitmap);
                //selectedImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void callCloudVision(final Bitmap bitmap) throws IOException {
        mProgressDialog = ProgressDialog.show(this, null,"Scanning image with Vision API...", true);

        new AsyncTask<Object, Void, BatchAnnotateImagesResponse>() {
            @Override
            protected BatchAnnotateImagesResponse doInBackground(Object... params) {
                try {
                    GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    Vision.Builder builder = new Vision.Builder
                            (httpTransport, jsonFactory, credential);
                    Vision vision = builder.build();

                    List<Feature> featureList = new ArrayList<>();
                    Feature labelDetection = new Feature();
                    labelDetection.setType("LABEL_DETECTION");
                    labelDetection.setMaxResults(10);
                    featureList.add(labelDetection);

                    Feature textDetection = new Feature();
                    textDetection.setType("TEXT_DETECTION");
                    textDetection.setMaxResults(10);
                    featureList.add(textDetection);

                    List<AnnotateImageRequest> imageList = new ArrayList<>();
                    AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();
                    Image base64EncodedImage = getBase64EncodedJpeg(bitmap);
                    annotateImageRequest.setImage(base64EncodedImage);
                    annotateImageRequest.setFeatures(featureList);
                    imageList.add(annotateImageRequest);

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                            new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(imageList);

                    Vision.Images.Annotate annotateRequest =
                            vision.images().annotate(batchAnnotateImagesRequest);
                    annotateRequest.setDisableGZipContent(true);
                    Log.d(TAG, "Sending request to Google Cloud");

                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    return response;

                } catch (GoogleJsonResponseException e) {
                    Log.e(TAG, "Request error: " + e.getContent());
                } catch (IOException e) {
                    Log.d(TAG, "Request error: " + e.getMessage());
                }
                return null;
            }

            protected void onPostExecute(BatchAnnotateImagesResponse response) {
                mProgressDialog.dismiss();
                //textResults.setText(getDetectedTexts(response));
                Log.d("capture",getDetectedTexts(response).toString()+"    text is string?");
                String[] words = getDetectedTexts(response).split("\\n+");
                for (int i = 0; i < words.length; i++) {
                    //all of list of detected text... -> split by line .... -> if list include exception text? ->
                    words[i] = words[i].replaceAll("[^\\w]", "");
                    if(words[i].contains("null") || words[i].contains("0"))//exception text : price, 원산지, title(가게이름 , 음식종류 , ...)
                    {

                    }
                    else
                    {

                        if(words[i].contains("멸치국수"))
                        {
                            Log.d("list",words[i]+"   replace..");
                            GetDB("멸치국수");
                        }
                        if(words[i].contains("콩국수"))
                        {
                            Log.d("list",words[i]+"   replace..");
                            GetDB("콩국수");
                        }
                        if(words[i].contains("고기국수"))
                        {
                            Log.d("list",words[i]+"   replace..");
                            GetDB("고기국수");
                        }
                        if(words[i].contains("비빔국수"))
                        {
                            GetDB("비빔국수");
                        }
                    }


                }

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        Log.d("tag",menudata.size()+"  last size...");
                        //Toast.makeText(getApplicationContext(),menudata.size(),Toast.LENGTH_LONG).show();
                        MenuAdapter adapter = new MenuAdapter(menudata);

                        adapter.notifyDataSetChanged();
                        listview.setAdapter(adapter);
                        listview.setOnItemClickListener(
                                new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        MenuList item = (MenuList) adapter.getItem(position);
                                        //Toast.makeText(getApplicationContext(),adapter.getCount(),Toast.LENGTH_LONG).show();
                                        Log.d("item",item.getFoodname()+ "position!");
                                        Intent intent = new Intent(getApplication(),MenuPopup.class);
                                        intent.putExtra("menuname",item.getFoodname());
                                        intent.putExtra("kcal",item.getKcal());
                                        intent.putExtra("car",item.getCar());
                                        intent.putExtra("pro",item.getPro());
                                        intent.putExtra("fat",item.getFat());
                                        startActivity(intent);
                                    }
                                }
                        );

                        Log.d("tag","  last size... notified");
                        //labelResults.setText(getDetectedLabels(response));
                    }

                }, 5000);
                //menudata.add(new MenuList("멸치국수",""));
                //MenuAdapter adapter = new MenuAdapter(menudata);

                //adapter.notifyDataSetChanged();




            }

        }.execute();
    }

    private String getDetectedLabels(BatchAnnotateImagesResponse response){
        StringBuilder message = new StringBuilder("");
        List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
        if (labels != null) {
            for (EntityAnnotation label : labels) {
                message.append(String.format(Locale.getDefault(), "%.3f: %s",
                        label.getScore(), label.getDescription()));
                message.append("\n");
            }
        } else {
            message.append("nothing\n");
        }

        return message.toString();
    }

    private String getDetectedTexts(BatchAnnotateImagesResponse response){
        StringBuilder message = new StringBuilder("");
        List<EntityAnnotation> texts = response.getResponses().get(0)
                .getTextAnnotations();
        if (texts != null) {
            for (EntityAnnotation text : texts) {
                message.append(String.format(Locale.getDefault(), "%s: %s",
                        text.getLocale(), text.getDescription()));
                message.append("\n");
            }
        } else {
            message.append("nothing\n");
        }

        return message.toString();
    }

    public Bitmap resizeBitmap(Bitmap bitmap) {

        int maxDimension = 1024;
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    public Image getBase64EncodedJpeg(Bitmap bitmap) {
        Image image = new Image();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        image.encodeContent(imageBytes);
        return image;
    }

    private void pickUserAccount() {
        String[] accountTypes = new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }

    private void getAuthToken() {
        String SCOPE = "oauth2:https://www.googleapis.com/auth/cloud-platform";
        if (mAccount == null) {
            pickUserAccount();
        } else {
            new GetOAuthToken(CaptureResult.this, mAccount, SCOPE, REQUEST_ACCOUNT_AUTHORIZATION)
                    .execute();
        }
    }

    public void onTokenReceived(String token){
        accessToken = token;
        launchImagePicker();
    }

    public void GetDB(String foodname)
    {
        Log.d("tag",foodname+"...foodname?");
        singleton.mClient.query(GetFoodQuery.builder().id(foodname)
                .build())
                .enqueue(new com.apollographql.apollo.GraphQLCall.Callback<GetFoodQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<GetFoodQuery.Data> response) {
                        Log.d("checkkk",response.data().getFood().food().size()+ "foodname?><");
                        Log.d("checkkk",response.data().getFood().food().get(0).id()+ "foodname?><");
                        Log.d("checkkk",response.data().getFood().id()+ "foodname?><");
                        if (response.data().getFood().food().get(0) != null) {
                            Log.d("checkkk", "찾음"+response.data().getFood().food().get(0).id().toString());
                            menudata.add(new MenuList(response.data().getFood().id().toString(),""
                            ,response.data().getFood().food().get(0).cal().toString()
                            ,response.data().getFood().food().get(0).car().toString()
                            ,response.data().getFood().food().get(0).pro().toString()
                            ,response.data().getFood().food().get(0).fat().toString()));//여기다가 칼로리 탄단지도 넣어야함 디비에서불러온거
                        }
                        Log.d("tag",menudata.size()+"  size...");
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                    }
                });


    }
}