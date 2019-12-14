package com.example.wlgusdn.ourhealth

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.widget.*
import com.amazonaws.amplify.generated.graphql.*
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.s3.transferutility.TransferService
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.PutObjectRequest
import com.apollographql.apollo.GraphQLCall
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.exception.ApolloException
import com.example.wlgusdn.ourhealth.CircularProgressBar
import com.example.wlgusdn.ourhealth.HistoryActivity
import com.example.wlgusdn.ourhealth.R.string.ok
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.api.client.json.Json
import com.google.api.client.json.JsonParser
import com.google.gson.JsonObject
import org.json.JSONObject
import type.*
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.random.Random

class MainActivity : AppCompatActivity()
{
    val url = " https://1228u0kujc.execute-api.ap-northeast-2.amazonaws.com/post/hello"

    lateinit var myInfoBtn : ImageButton
    lateinit var foodBtn : ImageButton
    lateinit var menuBtn : ImageButton
    lateinit var checkmyBtn : Button
    lateinit var saveBtn :Button
    lateinit var progress : CircularProgressBar
    val REQUEST_FOOD_CAPTURE = 1
    val REQUEST_FOOD_GALLERY = 2
    val REQUEST_TEXT_CAPTURE = 3
    val REQUEST_TEXT_GALLERY = 4
    var i=1
    var strr : String? = null
    private val REQUEST_PERMISSION = 101
    lateinit var mykcal : TextView
    lateinit var dailyKcal : TextView
    var Datesave : Button?=null
    var Foodsearch : Button?=null
    var photoPath : String?=null;
    val singleton : HealthData_Singleton = HealthData_Singleton.getInstance()
    var s3 : AmazonS3Client?= null
    var transferUtility : TransferUtility?=null
    var list : ArrayList<DayInput>?= ArrayList<DayInput>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



       val CreatemutateCallback  = object : GraphQLCall.Callback<CreateDateMutation.Data>() {


            override fun onResponse(response: com.apollographql.apollo.api.Response<CreateDateMutation.Data>) {

                runOnUiThread {

                    Log.d("checkkkk",response.toString())

                }
            }



            override fun onFailure(e: ApolloException) {
                runOnUiThread {
                    Log.d("checkkkk",e.toString())
                }
            }
        }
        val UpdatemutateCallback = object : GraphQLCall.Callback<UpdateDateMutation.Data>() {


            override fun onResponse(response: com.apollographql.apollo.api.Response<UpdateDateMutation.Data>) {

                runOnUiThread {

                }
            }



            override fun onFailure(e: ApolloException) {
                runOnUiThread {
                    Log.e("", "Failed to perform AddPetMutation", e)

                }
            }
        }

        Foodsearch = findViewById(R.id.button6)




        myInfoBtn= findViewById(R.id.todaybtn)
        foodBtn = findViewById(R.id.food)
        menuBtn = findViewById(R.id.menu)
        checkmyBtn = findViewById(R.id.checkmyavg)
        progress = findViewById(R.id.progress)
        dailyKcal = findViewById(R.id.maxtxt)
        mykcal = findViewById(R.id.kcaltxt)
        setProgressText()
        val chart = findViewById<BarChart>(R.id.brchart)
        val entries = ArrayList<BarEntry>()
        for(x in 0..24)
        {
            val randomValues =  Random.nextInt(0, 800)
            if(x== 9 || x== 13 || x==18 || x== 23)
                entries.add(BarEntry(x.toFloat(), randomValues.toFloat()))


        }


        val set = BarDataSet(entries, "BarDataSet")
        set.setColor(Color.parseColor("#FF8EFF7F"))
        val data = BarData(set)
        data.barWidth = 0.6f // set custom bar width
        data.setValueTextSize(10f)

        chart.data = data
        //chart.setBackgroundColor(Color.LTGRAY)
        val xAxis = chart.xAxis
        val leftAxis = chart.axisLeft
        val rightAxis = chart.axisRight
        xAxis.axisLineWidth = 3f
        xAxis.textSize = 10f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setAxisMinValue(0f)
        xAxis.setAxisMaxValue(24f)
        xAxis.setLabelCount(5,true)
        leftAxis.setAxisMinValue(0f)
        leftAxis.setAxisMaxValue(2400f)
        leftAxis.setDrawAxisLine(false)
        leftAxis.setLabelCount(5,true)
        //leftAxis.isEnabled = false
        leftAxis.setDrawGridLines(false)
        rightAxis.isEnabled = false
        leftAxis.isEnabled = false
        xAxis.setDrawGridLines(false)
        chart.setFitBars(true) // make the x-axis fit exactly all bars
        chart.description.isEnabled = false
        chart.legend.isEnabled = false
        chart.invalidate() // refresh




        val credentialsProvider = BasicAWSCredentials("AKIASZWJLFUTLG3KSE4T","TdSE5o42See3cTE/m7OU2HS2kkp62mNrrNL0P4MK")

        /*CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
    getApplicationContext(),
    "ap-northeast-2:3e9aa6ca-613b-48c7-8659-d7ab8b6f1ac5", // 자격 증명 풀 ID
    Regions.AP_NORTHEAST_2 // 리전
);*/




        s3=AmazonS3Client(credentialsProvider);
        s3!!.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
        s3!!.setEndpoint("s3.ap-northeast-2.amazonaws.com");
        transferUtility = TransferUtility(s3,applicationContext)
        getApplicationContext().startService(Intent(getApplicationContext(), TransferService::class.java))



        myInfoBtn.setOnClickListener {
            val intent = Intent(this,HistoryActivity::class.java)
            startActivity(intent)
        }
        foodBtn.setOnClickListener {
            if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this as Activity, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),REQUEST_PERMISSION)
            }
            else
            {
                val intent = Intent(this@MainActivity,Popupselect::class.java)
                intent.putExtra("select","Food")
                startActivityForResult(intent,1)
            }
        }

        menuBtn.setOnClickListener {
            if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this as Activity, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),REQUEST_PERMISSION)
            }
            else
            {
                val intent = Intent(this@MainActivity,Popupselect::class.java)
                intent.putExtra("select","Text")
                startActivityForResult(intent,2)
            }
        }

        checkmyBtn.setOnClickListener {
            //val intent = Intent(this,CaptureResult::class.java)
            val intent = Intent(this,BMR_Popup::class.java)
            startActivity(intent)  }








    }
    fun saves3(str : String)
    {
        Thread(object : Runnable {
            override fun run() {
                try {


                    val arrstr = str.split("/")
                    for(i in 0..arrstr.size-1)
                    {
                        if(i==arrstr.size-1)
                        {
                            strr = arrstr[i]
                        }
                    }
                    Log.d("asdasd",str)




                    val observer = transferUtility!!.upload(

                        "sagemaker-forourhealth",     /* 업로드 할 버킷 이름 */
                        strr,    /* 버킷에 저장할 파일의 이름 */
                        File(str)/* 버킷에 저장할 파일  */
                    )


                    val notification = JSONObject()
                    notification.put("key1", "wlgusdnzzz")
                    notification.put("food",strr)




                    val Url = URL(url)
                    val conn = Url.openConnection() as HttpURLConnection
                    var sb = StringBuilder()
                    conn.requestMethod = "POST"
                    conn.doOutput = true
                    conn.doInput = true


                    conn.setRequestProperty("Accept", "application/json")
                    conn.setRequestProperty("Content-type", "application/json")
                    val os = conn.outputStream
                    os.write(notification.toString().toByteArray(charset("utf-8")))
                    os.flush()


                    var br =  BufferedReader( InputStreamReader(conn.getInputStream()))


                    for(line in br.readLine())
                    {
                        sb.append(line)
                    }
                    var JsonParser = com.google.gson.JsonParser()
                    var jObject =JsonParser.parse(sb.toString().trim()) as JsonObject

                    br.close()
                    conn.disconnect()

                    Log.d("asdasd",jObject.get("foodname").toString())
                    //{"statusCode": 200, "body": "\"wlgusdnzzz\""}

                    val intent = Intent(this@MainActivity,FoodPopup::class.java)
                    intent.putExtra("food",jObject.get("foodname").toString())
                    startActivity(intent)




                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }).start()


    }

    fun setProgressText()
    {
        mykcal.text = singleton.eatkcal.toString() +"(-" + singleton.kcal + ")"
        dailyKcal.text = "/" + singleton.daily + "kcal"
        progress.setProgress(singleton.Progress())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            1->{
                photoPath=data!!.getStringExtra("path")
                var matrix = Matrix();
                val bmp = BitmapFactory.decodeStream(FileInputStream(photoPath), null, null)
              bm = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);




                saves3(photoPath!!)
            }
            2->{
               photoPath= data!!.getStringExtra("path")
                var matrix = Matrix();
                val bmp = BitmapFactory.decodeStream(FileInputStream(photoPath), null, null)
                bm = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);




                saves3(photoPath!!)




            }

        }
    }


    companion object
    {
        var bm : Bitmap?=null
    }
}
