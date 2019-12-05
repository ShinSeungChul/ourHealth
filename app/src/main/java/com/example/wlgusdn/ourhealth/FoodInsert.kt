package com.example.wlgusdn.ourhealth

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.amazonaws.amplify.generated.graphql.*
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.config.AWSConfiguration
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient
import com.amazonaws.mobileconnectors.appsync.sigv4.CognitoUserPoolsAuthProvider
import com.apollographql.apollo.GraphQLCall
import com.apollographql.apollo.exception.ApolloException
import kotlinx.android.synthetic.main.foodinsert.*
import type.*
import java.lang.Exception

class FoodInsert : AppCompatActivity()
{
    var list : ArrayList<FoodDataInput>?= ArrayList<FoodDataInput>()

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

    val UpdatemutateCallback = object : GraphQLCall.Callback<UpdateFoodMutation.Data>() {


        override fun onResponse(response: com.apollographql.apollo.api.Response<UpdateFoodMutation.Data>) {

            runOnUiThread {

            }
        }



        override fun onFailure(e: ApolloException) {
            runOnUiThread {
                Log.e("", "Failed to perform AddPetMutation", e)

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

                AccountActivity.mAWSAppSyncClient!!.query(GetFoodQuery.builder().id(name).build())
                    .enqueue(object : com.apollographql.apollo.GraphQLCall.Callback<GetFoodQuery.Data>() {
                        override fun onResponse(response: com.apollographql.apollo.api.Response<GetFoodQuery.Data>) {




                            runOnUiThread {


                                if(response.data()?.food?.id()==null)
                                {




                                    val sub :String = et_sub!!.text.toString()
                                    val kcal = et_kcal!!.text.toString().toInt()
                                    val pro = et_pro!!.text.toString().toInt()
                                    val car = et_car!!.text.toString().toInt()
                                    val fat = et_fat!!.text.toString().toInt()

                                    val dd = FoodDataInput.builder()
                                        .id(sub)
                                        .cal(kcal)
                                        .pro(pro)
                                        .car(car)
                                        .fat(fat)
                                        .build()
                                    Log.d("wlgusdn111",dd.toString())
                                    list!!.add(dd)

                                    val input = CreateFoodInput.builder()
                                        .id(name)
                                        .food(list!!)
                                        .build()



                                    val addFoodMutation = CreateFoodMutation.builder()
                                        .input(input)
                                        .build()

                                    AccountActivity.mAWSAppSyncClient!!.mutate(addFoodMutation).enqueue(CreatemutateCallback)


                                }

                                else
                                {

                                    val name = et_name!!.text.toString()
                                    val sub :String = et_sub!!.text.toString()
                                    val kcal = et_kcal!!.text.toString().toInt()
                                    val pro = et_pro!!.text.toString().toInt()
                                    val car = et_car!!.text.toString().toInt()
                                    val fat = et_fat!!.text.toString().toInt()




                                    var listt = response.data()!!.food!!.food()
                                    Log.d("wlgusdn111",listt.toString())




                                    listt=ArrayList(listt)



                                    listt.add(GetFoodQuery.Food(listt[0].__typename(),sub,kcal,car,pro,fat))


                                    val llist : ArrayList<FoodDataInput> = ArrayList<FoodDataInput>()
                                    for(data in listt)
                                    {



                                        val dd = FoodDataInput.builder()
                                            .id(data.id())
                                            .cal(data.cal())
                                            .pro(data.pro())
                                            .car(data.car())
                                            .fat(data.fat())
                                            .build()

                                        llist!!.add(
                                            dd
                                        )



                                    }

                                    val input = UpdateFoodInput.builder()
                                        .id(name!!)
                                        .food(llist)
                                        .build()



                                    val addFoodMutation = UpdateFoodMutation.builder()
                                        .input(input)
                                        .build()

                                    AccountActivity.mAWSAppSyncClient!!.mutate(addFoodMutation).enqueue(UpdatemutateCallback)



                                }




                            }

                        }

                        override fun onFailure(e: ApolloException) {

                            Log.d("searchget", "Fail")
                        }


                    }
                    )









    }

}