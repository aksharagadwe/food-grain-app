package com.example.farmfresh

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.Serializable


class LoadingActivity : AppCompatActivity() {
//    private val categoryList:List<String> = listOf<String>("Exotic_Fruits","Exotic_Vegetables","Foodgrains","Fruits","Vegetables")
//    private val itemSubList:List<String> = listOf<String>("Available Quantity","Description","Image","Price","Size")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_page)

        val token = getSharedPreferences("UserSharedPreferences", Context.MODE_PRIVATE)
        val emailHash = token.getString("EMAILHASH", "")

        if (emailHash == "") {
            Log.d("LoadingActivity", "Starting LoginActivity")
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }

        if (emailHash != "") {
            Log.d("LoadingActivity", "Fetching data from database")
            val refFeatured = FirebaseDatabase.getInstance().getReference("/combined_items")
            refFeatured
                .orderByKey()
                .limitToFirst(10)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.d("LoadingActivity", "Error in Fetching all items data : ${p0}")
                    return
                }
                override fun onDataChange(p0: DataSnapshot) {
                    Log.d("LoadingActivity", "${p0}")
//                    val finalHash = HashMap<String, List<HashMap<String,String>>>()
//                    for (name in categoryList){
//                        val tempList = mutableListOf<HashMap<String,String>>()
//                        for(itemName in p0.child(name).children){
//                            val tempHashMap = HashMap<String,String>()
//                            tempHashMap.put("Name",itemName.key.toString())
//                            for (subItem in itemName.children){
//                                tempHashMap.put(subItem.key.toString(), subItem.value.toString())
//                            }
//                            Log.d("LoadingActivity","${tempHashMap}")
//                            tempList.add(tempHashMap)
//                        }
//                        Log.d("LoadingActivity","${name} added in finalHash : size = ${tempList.size}")
//                        finalHash.put(name,tempList)
//                    }
//
//                    Log.d("LoadingActivity","finalHash Created: Creating dataObj")

                    val finalList = mutableListOf<HashMap<String, String>>()
                    for (itemName in p0.children) {
                            val tempHashMap = HashMap<String, String>()
                            tempHashMap.put("Name", itemName.key.toString())
                            for (subItem in itemName.children) {
                                tempHashMap.put(subItem.key.toString(), subItem.value.toString())
                            }
                            Log.d("LoadingActivity", "${tempHashMap}")
                            finalList.add(tempHashMap)
                        }
                    Log.d("LoadingActivity","finalList Created: ${finalList}")


                    val featureRef = FirebaseDatabase.getInstance().getReference("/featured")
                    featureRef.addValueEventListener(object : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {
                            Log.d("LoadingActivity","Failed to retrieve feature list")
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            Log.d("LoadingActivity", "Successfully Fetched featured data")

                            val featureList = mutableListOf<String>()
                            for (name in p0.children) {
//                                Log.d("LoadingActivity", "Image Location : ${name.value.toString()}")
                                featureList.add(name.value.toString())
                            }
                            Log.d("LoadingActivity","FeatureList Created")


                            val totalRef = FirebaseDatabase.getInstance().getReference("/total_items")
                            totalRef.addValueEventListener(object : ValueEventListener{
                                override fun onCancelled(p0: DatabaseError) {
                                    Log.d("LoadingActivity","Failed to Retrieve total items")
                                }
                                override fun onDataChange(p0: DataSnapshot) {

                                    Log.d("LoadingActivity","Successfully to Retrieve total items")
                                    val totalHashMap = HashMap<String,String>()
                                    for( name in p0.children){
                                        totalHashMap.put(name.key.toString(), name.value.toString())
                                    }

                                    Log.d("LoadingActivity","Creating dataObj")
                                    val dataObj = InitData(featureList, finalList, totalHashMap)
                                    val indexIntent = Intent(this@LoadingActivity, IndexActivity::class.java)
                                    indexIntent.putExtra("dataObj", dataObj)
                                    Log.d("LoadingActivity", "User Logged In : Starting IndexActivity")
                                    startActivity(indexIntent)
                                    finish()
                                }
                            })
                        }
                    })
                }
            })
        }
    }
}


class InitData(val featureList: List<String>, val finalList: List<HashMap<String,String>>, totalHashMap: HashMap<String, String>) : Serializable