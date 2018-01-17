package com.example.administrator.final_project_kotlin

import android.app.AlarmManager
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import java.util.ArrayList

import kotlinx.android.synthetic.main.note_content.*

class NoteContextActivity : AppCompatActivity() {
    private val mLayoutManager: LinearLayoutManager? = null
    private val mAdapter: MainActivityRecyclerViewAdapter? = null
    var myData: ArrayList<Info>? = null
    internal var am: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    //Image tempImage = new Image();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_content)
    }

}
