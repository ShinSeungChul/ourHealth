package com.example.wlgusdn.ourhealth

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Button

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.*
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import java.io.FileInputStream


class StorageActivity : AppCompatActivity()
{

    private val TAG = "wlgusdnzzz"
    var bu1 : Button? = null
    private val RESULT_LOAD_IMAGE = 1;
    var photoPath : String?=null;
    var s3 : AmazonS3Client?= null
    var transferUtility : TransferUtility?=null
    var iv : ImageView? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage)

        iv = findViewById(R.id.iv1)
        // Amazon Cognito 인증 공급자를 초기화합니다

        val credentialsProvider = BasicAWSCredentials("AKIAIGB7RAEJUZJ3WZXA","0oSOy7nmfVolL/1Yrl29dlR+z5OfMk0iFyxt6Vgg")

        /*CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
    getApplicationContext(),
    "ap-northeast-2:3e9aa6ca-613b-48c7-8659-d7ab8b6f1ac5", // 자격 증명 풀 ID
    Regions.AP_NORTHEAST_2 // 리전
);*/
        s3=AmazonS3Client(credentialsProvider);
        s3!!.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
        s3!!.setEndpoint("s3.ap-northeast-2.amazonaws.com");
        transferUtility = TransferUtility(s3,applicationContext)
        getApplicationContext().startService(Intent(getApplicationContext(),TransferService::class.java))

        bu1= findViewById(R.id.btn_add_photo)
        bu1!!.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                Log.d(TAG,"11111")

                choosePhoto()

            }
        })




        val bu : Button = findViewById(R.id.storage_finish)
        bu.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {

                gogo()

            }
        })




    }



    fun gogo()
    {
        val observer = transferUtility!!.upload(

                "sagemaker-forourhealth",     /* 업로드 할 버킷 이름 */
                photoPath,    /* 버킷에 저장할 파일의 이름 */
                File(photoPath)/* 버킷에 저장할 파일  */
        )
    }

    fun choosePhoto() {
        Log.d(TAG,"22222")
        val intent = Intent(Intent.ACTION_PICK);
        Log.d(TAG,"33333")
        Log.d(TAG,"44444")


        Log.d(TAG, "Gallery Intent");

        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, 1);



    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==1 && resultCode == RESULT_OK) {
            val selectedImage = data!!.getData();
            var filePathColumn : Array<String> = arrayOf(MediaStore.Images.Media.DATA)
            var cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val picturePath = cursor.getString(column_index);

            cursor.close();
            // String picturePath contains the path of selected Image
            photoPath = picturePath;
            Log.d(TAG,selectedImage.toString())
            var matrix = Matrix();
            val bmp = BitmapFactory.decodeStream(FileInputStream(photoPath), null, null)
            var bm = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);

            iv!!.setImageBitmap(bm)



        }
    }
    fun getResizedBitmap( image:Bitmap,  newHeight:Int,  newWidth:Int):Bitmap {
        var width = image.width
        var height = image.height
        Log.d(TAG,width.toString()+height.toString())
        var scaleWidth = ((newWidth) / width).toFloat();
        var scaleHeight = (( newHeight) / height).toFloat();
        // create a matrix for the manipulation
        var matrix = Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        if (Build.VERSION.SDK_INT <= 19) {
            //matrix.postRotate(90);
        }
        // recreate the new Bitmap

        var resizedBitmap = Bitmap.createBitmap(image, 1, 1, width-2, height-2,
                matrix, false);
        return resizedBitmap;
    }


    fun decodeUri( selectedImage:Uri) : Bitmap {
        var o = BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(
                this.getContentResolver().openInputStream(selectedImage), null, o);

        val REQUIRED_SIZE = 100;

        var width_tmp = o.outWidth
        var height_tmp = o.outHeight;
        var scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }



        var o2 = BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(
                this.getContentResolver().openInputStream(selectedImage), null, o2);
    }



}