package com.example.wlgusdn.ourhealth

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.amazonaws.amplify.generated.graphql.CreateAccountMutation
import com.amazonaws.amplify.generated.graphql.CreateFoodMutation
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.config.AWSConfiguration
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient
import com.amazonaws.mobileconnectors.appsync.sigv4.CognitoUserPoolsAuthProvider
import com.apollographql.apollo.GraphQLCall
import com.apollographql.apollo.exception.ApolloException
import kotlinx.android.synthetic.main.foodinsert.*
import type.CreateAccountInput
import type.CreateFoodInput
import java.lang.Exception

class FoodInsert : AppCompatActivity()
{

    private val CreatemutateCallback  = object : GraphQLCall.Callback<CreateFoodMutation.Data>() {


        override fun onResponse(response: com.apollographql.apollo.api.Response<CreateFoodMutation.Data>) {

            runOnUiThread {
                Toast.makeText(this@FoodInsert, "Added pet", Toast.LENGTH_SHORT).show()

            }
        }



        override fun onFailure(e: ApolloException) {
            runOnUiThread {
                Log.e("", "Failed to perform AddPetMutation", e)
                Toast.makeText(this@FoodInsert, "Failed to add pet", Toast.LENGTH_SHORT).show()

            }
        }
    }

    var sa : Button?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.foodinsert)

        sa = findViewById(R.id.save)
        sa!!.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {

                save()

            }
        })




        AccountActivity.mAWSAppSyncClient = AWSAppSyncClient.builder()
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


    }


    private fun save() {
        val name = et_name!!.text.toString()
        val sub = et_sub!!.text.toString()
        val kcal = et_kcal!!.text.toString().toInt()
        val pro = et_pro!!.text.toString().toInt()
        val car = et_car!!.text.toString().toInt()
        val fat = et_fat!!.text.toString().toInt()

        val input = CreateFoodInput.builder()
            .id(sub)
            .name(name!!)
            .cal(kcal!!)
            .pro(pro)
            .car(car)
            .fat(fat)
            .build()

        val addFoodMutation = CreateFoodMutation.builder()
            .input(input)
            .build()

        AccountActivity.mAWSAppSyncClient!!.mutate(addFoodMutation).enqueue(CreatemutateCallback)



    }

}