package com.example.wlgusdn.ourhealth

import android.app.Activity
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.amazonaws.amplify.generated.graphql.*
import com.amazonaws.mobile.client.AWSMobileClient
import com.apollographql.apollo.exception.ApolloException
import kotlinx.android.synthetic.main.foodinsert.*
import type.*

class FoodPopup : Activity()
{

    var tv : TextView?=null
    var bu_yes : Button?=null
    var bu_no : Button?=null
    var iv : ImageView?=null

    var list : ArrayList<FoodDataInput>?= ArrayList<FoodDataInput>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.popup_food)


        tv = findViewById(R.id.food_text)
        iv = findViewById(R.id.food_image)
        iv!!.setImageBitmap(MainActivity.bm)

        bu_yes = findViewById(R.id.food_yes)

        bu_no = findViewById(R.id.food_no)



        tv!!.text = intent.getStringExtra("food")+"\n이 음식이 맞나요?"

        bu_yes!!.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                var str : List<String> = intent.getStringExtra("food").split("\"")
                for(aa in 0.. str.size-1)
                {
                    Log.d("asdasd",aa.toString() + "/"+str[aa])
                }
                var st = str[2].substring(0,str[2].length-1)
                Log.d("asdasd",st)

                AccountActivity.mAWSAppSyncClient!!.query(GetFoodQuery.builder().id(st).build())
                    .enqueue(object : com.apollographql.apollo.GraphQLCall.Callback<GetFoodQuery.Data>() {
                        override fun onResponse(response: com.apollographql.apollo.api.Response<GetFoodQuery.Data>) {




                            runOnUiThread {
                                    var listt = response.data()!!.food!!.food()

                                    for(ii in listt)
                                    {
                                        Log.d("TLqkf",ii.id()+"/"+ii.cal())
                                    }

                                    listt=ArrayList(listt)
                                    llist = ArrayList<FoodDataInput>()
                                    for(data in listt)
                                    {
                                        val dd = FoodDataInput.builder()
                                            .id(data.id())
                                            .cal(data.cal())
                                            .pro(data.pro())
                                            .car(data.car())
                                            .fat(data.fat())
                                            .build()
                                        Log.d("asdas",dd.id()+"/"+dd.cal().toString()+"/"+dd.car().toString()+"/"+dd.pro().toString()+"/"+dd.fat().toString())

                                        llist!!.add(dd)
                                    }

                                Log.d("asdasd","123")
                                val intent = Intent(this@FoodPopup,FoodListActivity::class.java)

                                startActivityForResult(intent,0)
                            }

                        }

                        override fun onFailure(e: ApolloException) {

                            Log.d("searchget", "Fail")
                        }


                    }
                    )





            }
        })

        bu_no!!.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {



            }
        })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==0)
        {
            if(resultCode== RESULT_OK)
            {
                finish()
            }
        }

    }

    companion object
    {
        var llist  : ArrayList<FoodDataInput>?=null
    }

}