package com.example.wlgusdn.ourhealth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import java.io.FileInputStream

class Popupselect : Activity()
{

    lateinit var ca:Button
    lateinit var ga : Button
    val REQUEST_FOOD_CAPTURE = 1
    val REQUEST_FOOD_GALLERY = 2
    val REQUEST_TEXT_CAPTURE = 3
    val REQUEST_TEXT_GALLERY = 4
    private val REQUEST_PERMISSION = 101

    var photoPath : String?=null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.popup_intentselect)

        ca = findViewById(R.id.select_camera)
        ga = findViewById(R.id.select_gallery)
        var intent = intent
        if(intent.getStringExtra("select").equals("Food")) {
            ca.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {


                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, REQUEST_FOOD_CAPTURE)


                }
            })

            ga.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {


                    val intent = Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, REQUEST_FOOD_GALLERY);


                }
            })
        }
        else
        {
            ca.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {


                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, REQUEST_TEXT_CAPTURE)


                }
            })

            ga.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {


                    val intent = Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, REQUEST_TEXT_GALLERY);


                }
            })
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode)
        {
            REQUEST_FOOD_CAPTURE->{
                Log.d("camera","request food capture...")
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


                var intent : Intent = Intent()

                intent.putExtra("path",photoPath)

                setResult(RESULT_OK,intent)
                finish()
            }
            REQUEST_FOOD_GALLERY->{

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


                var intent : Intent = Intent()

                intent.putExtra("path",photoPath)

                setResult(RESULT_OK,intent)
                finish()



            }
            REQUEST_TEXT_CAPTURE->{
                Log.d("camera","request menu capture...")
                var intent : Intent = Intent()

                intent.putExtra("path",photoPath)

                setResult(RESULT_OK,intent)
                finish()
            }
            REQUEST_TEXT_GALLERY->{

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




                var intent : Intent = Intent()

                intent.putExtra("path",photoPath)

                setResult(RESULT_OK,intent)
                finish()

            }
            else->{Log.d("camera","request else...")}

        }
    }


    fun getResizedBitmap( image:Bitmap,  newHeight:Int,  newWidth:Int):Bitmap {
        var width = image.width
        var height = image.height

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


    fun decodeUri( selectedImage: Uri) : Bitmap {
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