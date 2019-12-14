package com.example.wlgusdn.ourhealth

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import com.amazonaws.amplify.generated.graphql.CreateAccountMutation
import com.amazonaws.amplify.generated.graphql.GetAccountQuery
import com.amazonaws.amplify.generated.graphql.GetDateQuery
import com.amazonaws.amplify.generated.graphql.UpdateAccountMutation
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.config.AWSConfiguration
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient
import com.amazonaws.mobileconnectors.appsync.sigv4.CognitoUserPoolsAuthProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.apollographql.apollo.GraphQLCall
import com.apollographql.apollo.exception.ApolloException
import type.CreateAccountInput
import type.DayInput
import type.FoodDataInput
import java.lang.Exception
import java.text.SimpleDateFormat

class AccountActivity : AppCompatActivity()
{
    var et_id : EditText? = null
    var et_height : EditText? = null
    var et_weight : EditText? = null
    var et_momentum : EditText? = null
    var et_age : EditText? = null
    var sex : String? = null
    var ra1 : RadioButton?=null
    var ra2 : RadioButton?=null
    var tv : TextView? = null
    var bu : Button?=null
    var mo : Int?=null
    var cal : Int?=null
    var car : Int?=null
    var pro : Int?=null
    var fat : Int?=null
    var str : String?= null
    val singleton : HealthData_Singleton = HealthData_Singleton.getInstance()
    var insert : Button?=null

    private val CreatemutateCallback  = object : GraphQLCall.Callback<CreateAccountMutation.Data>() {


        override fun onResponse(response: com.apollographql.apollo.api.Response<CreateAccountMutation.Data>) {

            runOnUiThread {
                Toast.makeText(this@AccountActivity, "Added pet", Toast.LENGTH_SHORT).show()
                this@AccountActivity.finish()
            }
        }



        override fun onFailure(e: ApolloException) {
            runOnUiThread {
                Log.e("", "Failed to perform AddPetMutation", e)
                Toast.makeText(this@AccountActivity, "Failed to add pet", Toast.LENGTH_SHORT).show()
                this@AccountActivity.finish()
            }
        }
    }
    private val UpdatemutateCallback = object : GraphQLCall.Callback<UpdateAccountMutation.Data>() {


        override fun onResponse(response: com.apollographql.apollo.api.Response<UpdateAccountMutation.Data>) {

            runOnUiThread {
                Toast.makeText(this@AccountActivity, "Added pet", Toast.LENGTH_SHORT).show()
                this@AccountActivity.finish()
            }
        }



        override fun onFailure(e: ApolloException) {
            runOnUiThread {
                Log.e("", "Failed to perform AddPetMutation", e)
                Toast.makeText(this@AccountActivity, "Failed to add pet", Toast.LENGTH_SHORT).show()
                this@AccountActivity.finish()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        insert = findViewById(R.id.button5)

        insert!!.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {

                val intent = Intent(this@AccountActivity,FoodInsert::class.java)

                startActivity(intent)

            }
        })

        Log.d("checkkk",AWSMobileClient.getInstance().toString())
        mAWSAppSyncClient = AWSAppSyncClient.builder()
                .context(applicationContext)
                .awsConfiguration(AWSConfiguration(applicationContext))
                .cognitoUserPoolsAuthProvider(CognitoUserPoolsAuthProvider {
                    try {
                        Log.e("APPSYNC_ERROR", "heelloo")


                        return@CognitoUserPoolsAuthProvider AWSMobileClient.getInstance().tokens.idToken.tokenString

                    } catch (e: Exception) {
                        Log.e("APPSYNC_ERROR", e.localizedMessage)
                        return@CognitoUserPoolsAuthProvider e.localizedMessage
                    }
                }).build()

        singleton.mClient = mAWSAppSyncClient



    if(AWSMobileClient.getInstance().identityId!=null) {

        mAWSAppSyncClient!!.query(GetAccountQuery.builder().id(AWSMobileClient.getInstance().identityId/*"멸치국수"*/).build())
            .enqueue(object : com.apollographql.apollo.GraphQLCall.Callback<GetAccountQuery.Data>() {
                override fun onResponse(response: com.apollographql.apollo.api.Response<GetAccountQuery.Data>) {


                    runOnUiThread {
                        Log.d("checkkk",response.data()!!.account.toString())
                        if (response.data()!!.account != null) {
                            singleton.clientData.kcal = response.data()!!.account!!.cal()
                            singleton.clientData.id = response.data()!!.account!!.id()
                            singleton.clientData.name = response.data()!!.account!!.name()
                            singleton.clientData.height = response.data()!!.account!!.height()
                            singleton.clientData.weight = response.data()!!.account!!.weight()
                            singleton.clientData.car = response.data()!!.account!!.car()
                            singleton.clientData.pro = response.data()!!.account!!.pro()
                            singleton.clientData.fat = response.data()!!.account!!.fat()

                            Log.d("checkkk", "singleton user     "+singleton.clientData.id)

                            //여기서 clientdata.id 로 date data 받아와야함


                            //Log.d("checkkk", "찾음"+response.data()!!.account!!.toString())
                            //Log.d("checkkk", "찾음"+response.data()!!.account!!.cal().toString())
                            // Log.d("checkkk", "찾음"+response.data()!!.account!!.toString())
                            //  Log.d("checkkk", "찾음"+response.data()!!.account!!.toString())
                            Log.d("checkkk", "찾음"+response.data()!!.account!!.toString())



                        }

                    }

                }




                override fun onFailure(e: ApolloException) {

                    Log.d("searchget", "Fail")
                }


            }
            )

        mAWSAppSyncClient!!.query(GetDateQuery.builder().id(AWSMobileClient.getInstance().identityId/*"멸치국수"*/).build())
            .enqueue(object : com.apollographql.apollo.GraphQLCall.Callback<GetDateQuery.Data>() {
                override fun onResponse(response: com.apollographql.apollo.api.Response<GetDateQuery.Data>) {


                    runOnUiThread {


                        Log.d("checkkk",response.data()!!.date.toString())
                        if (response.data()!!.date!!.date() != null) {

                            var date_list = response.data()!!.date!!.date()
                            date_list = ArrayList(date_list)
                            llist = ArrayList()
                            //for 문 결과가 다같음 i == 1 일때만 출력됨?
                            for(i in date_list)
                            {
                                var date_aws : String = i.time()!!
                                val data = DayInput.builder()
                                    .id(i.id())
                                    .time(i.time())
                                    .food(i.food())
                                    .kcal(i.kcal())
                                    .pro(i.pro())
                                    .car(i.car())
                                    .fat(i.fat())
                                    .build()

                                val format : SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd")
                                val format2 = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                                var date  = format.parse(data.time())
                                var hour = format2.parse(data.time())

                                Log.d("clock",date.toString()+"///"+ i.food()+"////")


                                    singleton.allFoods.add(
                                    AllFood(
                                        date.toString()
                                        ,hour.toString()
                                        ,i.food()!!
                                        ,i.kcal()!!
                                        ,i.car()!!
                                        ,i.pro()!!
                                        ,i.fat()!!
                                    )

                                )
                                    //Log.d("jeong",singleton.allFoods[i].time+" : time")
                            }







                        }
                        else
                        {
                            Toast.makeText(applicationContext,"no data",Toast.LENGTH_LONG).show()
                        }

                        val intent = Intent(this@AccountActivity, MainActivity::class.java)
                        startActivity(intent)

                    }

                }




                override fun onFailure(e: ApolloException) {

                    Log.d("searchget", "Fail")
                }


            }
            )




    }


        et_id = findViewById(R.id.account_ID)
        et_height = findViewById(R.id.account_Height)
        et_weight = findViewById(R.id.account_Weight)
        et_momentum = findViewById(R.id.account_Momentum)
        et_age = findViewById(R.id.account_Age)
        ra1 = findViewById(R.id.radioButton)
        ra2 = findViewById(R.id.radioButton2)
        tv = findViewById(R.id.textView7)
        bu = findViewById(R.id.button2)


        ra1!!.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {

                if(ra1!!.isSelected)
                {
                    //다이어트
                    sex="woman"

                }
                else
                {
                    sex="man"

                }

            }
        })

        ra2!!.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {

                if(ra2!!.isSelected)
                {
                    sex="man"

                }
                else
                {
                    sex="woman"

                }

            }
        })

        bu!!.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {




                if(sex=="man")
                {
                    var bmr = (66.47 +(13.75*et_weight!!.text.toString().toInt()) + (5 * et_height!!.text.toString().toInt()) - (6.76 * et_age!!.text.toString().toInt())).toFloat()
                    Toast.makeText(this@AccountActivity,et_height!!.text.toString()+"  "+et_age!!.text.toString()+"  "+et_weight!!.text.toString()+"  "+et_momentum!!.text.toString(),Toast.LENGTH_LONG).show()
                    Log.d("checkkk",bmr.toString())
                    when(et_momentum!!.text.toString().toInt())
                    {

                        1->{cal = (bmr*1.4).toInt() }
                        2->{cal = (bmr*1.7).toInt()}
                        3->{cal = (bmr*1.9).toInt()}

                    }
                    Log.d("checkkk",cal.toString())
                }

                else
                {
                    var bmr = (655.1 +(9.56*et_weight!!.text.toString().toInt()) + (1.85 * et_height!!.text.toString().toInt()) - (4.68 * et_age!!.text.toString().toInt())).toFloat()
                    when(et_momentum!!.text.toString().toInt())
                    {
                        1->{cal = (bmr*1.4).toInt() }
                        2->{cal = (bmr*1.6).toInt()}
                        3->{cal = (bmr*1.8).toInt()}
                    }
                }

                pro = (et_weight!!.text.toString().toInt()*1.0).toInt()

                fat = (cal!!.toDouble()/90).toInt()

                car = (cal!! - (pro!!*4 + fat!!*9))





                save()



            }
        })


    }
    private fun save() {
        val id = et_id!!.text.toString()
        val weight = et_weight!!.text.toString()
        val height = et_height!!.text.toString()
        val momentum = et_momentum!!.text.toString()
        val age = et_age!!.text.toString()


        val input = CreateAccountInput.builder()
                .id(AWSMobileClient.getInstance().identityId)
                .name(id)
                .height(height.toInt())
                .weight(weight.toInt())
                .momentum(momentum.toInt())
                .age(age.toInt())
                .cal(cal)
                .car(car)
                .pro(pro)
                .fat(fat)
                .build()

        val addBodyMutation = CreateAccountMutation.builder()
                .input(input)
                .build()

        mAWSAppSyncClient!!.mutate(addBodyMutation).enqueue(CreatemutateCallback)


    }
    companion object
    {
        var mAWSAppSyncClient : AWSAppSyncClient?=null
        var transferUtility : TransferUtility?=null
        var llist  : ArrayList<DayInput>?=null
    }


}