package com.example.wlgusdn.ourhealth

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import com.amazonaws.amplify.generated.graphql.*
import com.amazonaws.mobile.client.AWSMobileClient
import com.apollographql.apollo.GraphQLCall
import com.apollographql.apollo.exception.ApolloException
import com.example.wlgusdn.ourhealth.FoodPopup.Companion.llist
import kotlinx.android.synthetic.main.foodinsert.*
import type.*
import java.util.*
import kotlin.collections.ArrayList

class FoodListActivity : AppCompatActivity
{
    var list : ListView?=null
    var data : ArrayList<FoodDataInput> = ArrayList<FoodDataInput>()
    var daydata : ArrayList<DayInput> = ArrayList<DayInput>()


    constructor()

    private val CreatemutateCallback  = object : GraphQLCall.Callback<CreateDateMutation.Data>() {


        override fun onResponse(response: com.apollographql.apollo.api.Response<CreateDateMutation.Data>) {

            runOnUiThread {
                Toast.makeText(this@FoodListActivity, "Added pet", Toast.LENGTH_SHORT).show()
                this@FoodListActivity.finish()
            }
        }



        override fun onFailure(e: ApolloException) {
            runOnUiThread {
                Log.e("", "Failed to perform AddPetMutation", e)
                Toast.makeText(this@FoodListActivity, "Failed to add pet", Toast.LENGTH_SHORT).show()
                this@FoodListActivity.finish()
            }
        }
    }

    private val UpdatemutateCallback = object : GraphQLCall.Callback<UpdateDateMutation.Data>() {


        override fun onResponse(response: com.apollographql.apollo.api.Response<UpdateDateMutation.Data>) {

            runOnUiThread {
                Toast.makeText(this@FoodListActivity, "Added pet", Toast.LENGTH_SHORT).show()
                this@FoodListActivity.finish()
            }
        }



        override fun onFailure(e: ApolloException) {
            runOnUiThread {
                Log.e("", "Failed to perform AddPetMutation", e)
                Toast.makeText(this@FoodListActivity, "Failed to add pet", Toast.LENGTH_SHORT).show()
                this@FoodListActivity.finish()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_selectfood)

        list = findViewById(R.id.select_list)
        Log.d("asdasdd",FoodPopup.llist!!.size.toString())


        var adap = FoodListAdapter(FoodPopup.llist!!)
        adap.notifyDataSetChanged()
        Log.d("asdasdd","11")
        list!!.adapter=adap
        adap.notifyDataSetChanged()
        Log.d("asdasdd","22")
        setListViewHeightBasedOnChildren(list!!)


        list!!.setOnItemClickListener(object: AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                val now = System.currentTimeMillis()



                val today = Date(now)
                var strdate: String? = null

                var format1: SimpleDateFormat? = null
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                {
                    format1 = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")

                    strdate = format1.format(today)


                }

                AccountActivity.mAWSAppSyncClient!!.query(GetDateQuery.builder().id(AWSMobileClient.getInstance().identityId).build())
                    .enqueue(object : com.apollographql.apollo.GraphQLCall.Callback<GetDateQuery.Data>() {
                        override fun onResponse(response: com.apollographql.apollo.api.Response<GetDateQuery.Data>) {




                            runOnUiThread {


                                if(response.data()?.date?.id()==null)
                                {




                                    val day_name = llist!![position].id()
                                    val day_kcal = llist!![position].cal()
                                    val day_pro = llist!![position].pro()
                                    val day_car = llist!![position].car()
                                    val day_fat =llist!![position].fat()


                                    //클릭한 음식의 데이터를 받아와 DayInput으로 만들고 Date에 넣는다
                                    val dd =DayInput.builder()
                                        .id(AWSMobileClient.getInstance().identityId)
                                        .time(strdate)
                                        .food(day_name)
                                        .kcal(day_kcal)
                                        .pro(day_pro)
                                        .car(day_car)
                                        .fat(day_fat)
                                        .build()

                                    Log.d("wlgusdn111",dd.toString())
                                    daydata!!.add(dd)

                                    val input = CreateDateInput.builder()
                                        .id(AWSMobileClient.getInstance().identityId)
                                        .date(daydata!!)
                                        .build()



                                    val addDateMutation = CreateDateMutation.builder()
                                        .input(input)
                                        .build()

                                    AccountActivity.mAWSAppSyncClient!!.mutate(addDateMutation).enqueue(CreatemutateCallback)


                                }

                                else
                                {

                                    val day_name = llist!![position].id()
                                    val day_kcal = llist!![position].cal()
                                    val day_pro = llist!![position].pro()
                                    val day_car = llist!![position].car()
                                    val day_fat = llist!![position].fat()




                                    var listt = response.data()!!.date!!.date()
                                    Log.d("wlgusdn111",listt.toString())




                                    listt=ArrayList(listt)



                                    listt.add(GetDateQuery.Date(listt[0].__typename(),AWSMobileClient.getInstance().identityId,strdate,day_name,day_kcal,day_pro,day_car,day_fat))


                                    val llist : ArrayList<DayInput> = ArrayList<DayInput>()
                                    for(data in listt)
                                    {



                                        val dd = DayInput.builder()
                                            .id(data.id())
                                            .food(data.food())
                                            .time(data.time())
                                            .kcal(data.kcal())
                                            .pro(data.pro())
                                            .car(data.car())
                                            .fat(data.fat())
                                            .build()

                                        llist!!.add(
                                            dd
                                        )



                                    }

                                    val input = UpdateDateInput.builder()
                                        .id(AWSMobileClient.getInstance().identityId)
                                        .date(llist)
                                        .build()



                                    val addDateMutation = UpdateDateMutation.builder()
                                        .input(input)
                                        .build()

                                    AccountActivity.mAWSAppSyncClient!!.mutate(addDateMutation).enqueue(UpdatemutateCallback)



                                }




                            }

                        }

                        override fun onFailure(e: ApolloException) {

                            Log.d("searchget", "Fail")
                        }


                    }
                    )
                finish()
            }
        })


    }

    private fun save() {



    }

    fun setListViewHeightBasedOnChildren(listView: ListView) {
        val listAdapter = listView.adapter
            ?: // pre-condition
            return

        var totalHeight = 0
        val desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.width, View.MeasureSpec.AT_MOST)

        for (i in 0 until listAdapter.count) {
            val listItem = listAdapter.getView(i, null, listView)
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
            totalHeight += listItem.measuredHeight
        }

        val params = listView.layoutParams
        params.height = totalHeight + listView.dividerHeight * (listAdapter.count - 1)
        listView.layoutParams = params
        listView.requestLayout()
    }
}