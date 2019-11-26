package com.example.wlgusdn.ourhealth

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import com.amazonaws.Response
import com.amazonaws.amplify.generated.graphql.CreateAccountMutation
import com.amazonaws.amplify.generated.graphql.GetAccountQuery
import com.amazonaws.amplify.generated.graphql.ListAccountsQuery
import com.amazonaws.amplify.generated.graphql.UpdateAccountMutation
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.config.AWSConfiguration
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient
import com.amazonaws.mobileconnectors.appsync.sigv4.CognitoUserPoolsAuthProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.services.s3.AmazonS3Client
import com.apollographql.apollo.GraphQLCall
import com.apollographql.apollo.api.Query
import com.apollographql.apollo.exception.ApolloException
import com.example.wlgusdn.ourhealth.MainActivity
import type.CreateAccountInput
import java.lang.Exception
import javax.annotation.Nonnull

class AccountActivity : AppCompatActivity()
{
    var et_id : EditText? = null
    var et_height : EditText? = null
    var et_weight : EditText? = null
    var et_momentum : EditText? = null
    var et_select : EditText? = null
    var select : Int = 0
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



    if(AWSMobileClient.getInstance().identityId!=null) {
        mAWSAppSyncClient!!.query(GetAccountQuery.builder().id(AWSMobileClient.getInstance().identityId).build())
            .enqueue(object : com.apollographql.apollo.GraphQLCall.Callback<GetAccountQuery.Data>() {
                override fun onResponse(response: com.apollographql.apollo.api.Response<GetAccountQuery.Data>) {


                    runOnUiThread(
                        {
                            Log.d("checkkk",response.data()!!.account.toString())
                            if (response.data()!!.account != null) {
                                Log.d("checkkk", "찾음"+response.data()!!.account!!.toString())
                                val intent = Intent(this@AccountActivity, MainActivity::class.java)
                                startActivity(intent)
                            }

                        }
                    )

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
        et_select = findViewById(R.id.account_Select)
        ra1 = findViewById(R.id.radioButton)
        ra2 = findViewById(R.id.radioButton2)
        tv = findViewById(R.id.textView7)
        bu = findViewById(R.id.button2)


        ra1!!.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {

                if(ra1!!.isSelected)
                {
                    //다이어트
                    select=1
                    tv!!.text="목표 체중"
                    et_select!!.hint = "1달 후 달성하고 싶은 체중"
                }
                else
                {
                    select=2
                    tv!!.text="운동 강도"
                    et_select!!.hint = "1:매우약함 2:약함 3:보통 4:강함 5:매우강함"
                }

            }
        })

        ra2!!.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {

                if(ra2!!.isSelected)
                {
                    select=2
                    tv!!.text="운동 강도"
                    et_select!!.hint = "1:매우약함 2:약함 3:보통 4:강함 5:매우강함"

                }
                else
                {
                    select=1
                    tv!!.text="목표 체중"
                    et_select!!.hint = "1달 후 달성하고 싶은 체중"

                }

            }
        })

        bu!!.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {

                when(et_momentum!!.text.toString().toInt())
                {
                    1->{mo=20}
                    2->{mo=25}
                    3->{mo=30}
                    4->{mo=35}
                    5->{mo=40}
                }

                cal = (((et_height!!.text.toString().toInt()-100)*(0.9))*mo!!).toInt()

                if(select==1)
                    pro = (et_weight!!.text.toString().toInt()*1.0).toInt()
                else
                {
                    when(et_select!!.text.toString().toInt())
                    {
                        1->{pro = (et_weight!!.text.toString().toInt()*1.0).toInt()}
                        2->{pro = (et_weight!!.text.toString().toInt()*1.3).toInt()}
                        3->{pro = (et_weight!!.text.toString().toInt()*1.5).toInt()}
                        4->{pro = (et_weight!!.text.toString().toInt()*1.8).toInt()}
                        5->{pro = (et_weight!!.text.toString().toInt()*2.0).toInt()}
                    }
                }

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


        val input = CreateAccountInput.builder()
                .id(AWSMobileClient.getInstance().identityId)
                .name(id)
                .height(height.toInt())
                .weight(weight.toInt())
                .momentum(momentum.toInt())
                .select(select)
                .selvalue(et_select!!.text.toString().toInt())
                .cal(cal)
                .car(car)
                .pro(pro)
                .fat(fat)
                .build()

        val addBodyMutation = CreateAccountMutation.builder()
                .input(input)
                .build()

        mAWSAppSyncClient!!.mutate(addBodyMutation).enqueue(CreatemutateCallback)

        val intent = Intent(this@AccountActivity,MainActivity::class.java)
        startActivity(intent)

    }
    companion object
    {
        var mAWSAppSyncClient : AWSAppSyncClient?=null
        var transferUtility : TransferUtility?=null
    }

}