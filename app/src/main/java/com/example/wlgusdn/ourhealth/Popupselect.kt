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
import android.widget.Toast
import android.R.attr.data
import android.icu.text.SimpleDateFormat
import android.os.Environment
import android.support.v4.content.FileProvider
import android.view.MotionEvent
import kotlinx.android.synthetic.main.foodinsert.*
import java.io.*
import java.lang.Exception
import java.net.URI
import java.util.*
import kotlin.random.Random


class Popupselect : Activity()
{

    lateinit var ca: Button
    lateinit var ga : Button
    val REQUEST_FOOD_CAPTURE = 1
    val REQUEST_FOOD_GALLERY = 2
    val REQUEST_TEXT_CAPTURE = 3
    val REQUEST_TEXT_GALLERY = 4
    private val REQUEST_PERMISSION = 101

    var file : File?=null
    var photoPath : String?=null;
    var photoUri : Uri?=null
    var photoFile : File? = null

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }
    public override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event!!.action == MotionEvent.ACTION_OUTSIDE){return false}
        return true;
    }
    public override fun onBackPressed() {
        return;
    }

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


                    val takePictureIntent =Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Ensure that there's a camera activity to handle the intent
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go
                        photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch ( ex : IOException) {
                            // Error occurred while creating the File
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null)
                        {
                            val photoURI = FileProvider.getUriForFile(this@Popupselect,
                            "com.example.wlgusdn.ourhealth.fileprovider",
                            photoFile!!);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePictureIntent,REQUEST_FOOD_CAPTURE);
                        }
                    }




                    /*  val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                   startActivityForResult(intent, REQUEST_FOOD_CAPTURE)
*/

                }
            })

            ga.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {


                    val intent = Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE)
                    startActivityForResult(intent, REQUEST_FOOD_GALLERY)


                }
            })
        }
        else
        {
            ca.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {


                    var photoFile : File? = null

                    val takePictureIntent =Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Ensure that there's a camera activity to handle the intent
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go
                        photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch ( ex : IOException) {
                            // Error occurred while creating the File
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null)
                        {
                            val photoURI = FileProvider.getUriForFile(this@Popupselect,
                                "com.example.wlgusdn.ourhealth.fileprovider",
                                photoFile!!);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePictureIntent,REQUEST_FOOD_CAPTURE);
                        }
                    }


                }
            })

            ga.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {


                    /*val intent = Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, REQUEST_TEXT_GALLERY);*/
                    val intent = Intent(application.applicationContext,CaptureResult::class.java);
                    startActivity(intent)


                }
            })
        }

    }

   fun createImageFile() : File?
   {
    // Create an image file name
   val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date());
    val imageFileName = "JPEG_" + timeStamp + "_";
    val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    val image = File.createTempFile(
            imageFileName,  /* prefix */
    ".jpg",         /* suffix */
    storageDir      /* directory */
    );

    // Save a file: path for use with ACTION_VIEW intents
    photoPath = image.getAbsolutePath();
    return image;
}

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
       val picName = "pic${Random(100000)}.jpg";
        val PATH = Environment.getExternalStorageDirectory().getPath()+ picName;
        val f = File(PATH);
        val yourUri = Uri.fromFile(f);
        return yourUri
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode)
        {

            REQUEST_FOOD_CAPTURE->{
                var temp :File?=null
                var name :String?=null
                if(resultCode== RESULT_OK)
                {
                    var ff = File(photoPath)
                    var bit =getResizedBitmap( MediaStore.Images.Media.getBitmap(contentResolver,Uri.fromFile(ff)),100,100)

                    val sto = cacheDir
                 name = "pic${Random(100000)}.jpg"
                  temp = File(sto,name)
                    try
                    {

                        temp.createNewFile()

                        val out = FileOutputStream(temp)

                        bit.compress(Bitmap.CompressFormat.PNG,100,out)

                        out.close()

                    }
                    catch(e : Exception)
                    {

                    }

                    if(bit!=null)
                    {
                        Toast.makeText(this@Popupselect,"사진생성",Toast.LENGTH_LONG).show()
                    }


                }

                    var intent: Intent = Intent()


                    intent.putExtra("path",cacheDir.toString()+"/"+name)

                    setResult(RESULT_OK, intent)
                    finish()

            }
            REQUEST_FOOD_GALLERY->{


                val selectedImage = data!!.getData();
                var filePathColumn : Array<String> = arrayOf(MediaStore.Images.Media.DATA)
                var cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
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
                if(resultCode== RESULT_OK)
                {
                    var ff = File(photoPath)
                    var bit = MediaStore.Images.Media.getBitmap(contentResolver,Uri.fromFile(ff))
                    if(bit!=null)
                    {
                        Toast.makeText(this@Popupselect,"사진생성",Toast.LENGTH_LONG).show()
                    }
                }

                var intent: Intent = Intent()

                intent.putExtra("path", photoPath)

                setResult(RESULT_OK, intent)
                finish()
            }
            REQUEST_TEXT_GALLERY->{
                Toast.makeText(this,"gallery text", Toast.LENGTH_LONG).show()
                /*val selectedImage = data!!.getData();
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
                finish()*/

            }
            else->{
                Log.d("camera","request else...")}

        }
    }


    fun getResizedBitmap(image: Bitmap, newHeight:Int, newWidth:Int): Bitmap {
        var width = image.width
        var height = image.height
        Log.d("asdaa",width.toString()+"/"+height.toString())

        var scaleWidth = ((newWidth) / width).toFloat();
        var scaleHeight = (( newHeight) / height).toFloat();
        // create a matrix for the manipulation
        var matrix = Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        if (Build.VERSION.SDK_INT <= 19) {
            //matrix.postRotate(90);
        }
        // recreate the new Bitmap
        var ratio = 40.0/width
        var width1 = width * ratio
        var height1 = height * ratio

        var resizedBitmap = Bitmap.createScaledBitmap(image,(height/10).toInt(),(width/10).toInt(),true)
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