package com.example.farmfresh.Activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.farmfresh.Adapters.OrderAdapter
import com.example.farmfresh.Model.Order
import com.example.farmfresh.Model.OrderList
import com.example.farmfresh.R

class PreviousOrdersActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_previous)

        val orderListObj = intent.getSerializableExtra("orderListObj") as OrderList
        Log.d("PreviousActivity","${orderListObj.orderList}")

        val recycleView: RecyclerView = findViewById(R.id.previous_recycleview)
        recycleView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false) as RecyclerView.LayoutManager?
        val adapter = OrderAdapter(
            this,
            orderListObj.orderList as MutableList<Order>
        )
        recycleView.adapter = adapter
    }
}