package com.example.farmfresh

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_toolbar.*

class ProductActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_itemlist)

        val subDataObj = intent.getSerializableExtra("subDataObj") as SubData
        Log.d("ProductActivity","${subDataObj.itemList}")

        val type = intent.getStringExtra("type")
        Log.d("ProductActivity", "$type")

        val db = CartDatabase(this)
        val cartList = db.readData()
        Log.d("ProductActivity","$cartList")


        val recyclerView:RecyclerView = findViewById(R.id.recycleview)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false) as RecyclerView.LayoutManager?
        val adapter= ProductAdapter(subDataObj.itemList, cartList, type)
        recyclerView.adapter = adapter
    }
}