package com.example.administrator.final_project_kotlin

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast

import kotlin.collections.ArrayList

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var myData: ArrayList<Info> = ArrayList()
    private var mLayoutManager: LinearLayoutManager = LinearLayoutManager(this)
    private var mAdapter: MainActivityRecyclerViewAdapter = MainActivityRecyclerViewAdapter(myData)
    //Image tempImage = new Image();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        //Log.e("TAG", ((Integer) myData.size()).toString());
        my_recyclerview.layoutManager = mLayoutManager
        my_recyclerview.setHasFixedSize(true)
        mAdapter.setOnItemClickListener(object : MainActivityRecyclerViewAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent()
                intent.setClass(this@MainActivity, NoteContextActivity::class.java)
                startActivity(intent)
            }
        })
        mAdapter.setOnItemLongClickListener(object : MainActivityRecyclerViewAdapter.OnItemLongClickListener{
            override fun onItemLongClick(view: View, position: Int)
                    = Toast.makeText(applicationContext, "Long click", Toast.LENGTH_SHORT).show()

        })
        my_recyclerview.adapter = mAdapter
    }

    fun init() {
        myData = ArrayList()
        val tempInfo = Info("aaa", "bbb", "ccc", "ddd")
        for (i in 0..4) {
            myData.add(tempInfo)
        }
    }

}
