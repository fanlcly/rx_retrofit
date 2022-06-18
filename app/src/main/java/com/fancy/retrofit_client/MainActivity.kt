package com.fancy.retrofit_client

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.fastjson.JSONObject
import com.fancy.retrofit_client.adapter.SampleAdapter
import com.fancy.retrofit_client.net.Api
import com.fancy.retrofit_client.net.BaseModle
import com.fancy.rxretrofit.HttpClient
import com.fancy.rxretrofit.callback.BaseCallBack
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var sampleAdapter: SampleAdapter
    private var recyclerview: RecyclerView? = null
    private var tvJson: TextView? = null
    private var checkRequestLayoutFlag: Int = 0
    private var mData: MutableList<String?> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnJson = findViewById<Button>(R.id.btn_json)
        val btnParse = findViewById<Button>(R.id.btn_parse)
        tvJson = findViewById(R.id.tv_json)
        recyclerview = findViewById(R.id.recycler_view)
        recyclerview?.layoutManager = LinearLayoutManager(this)
        sampleAdapter = SampleAdapter(mData)
        recyclerview?.adapter = sampleAdapter

        btnJson.setOnClickListener {
            checkRequestLayoutFlag = 0
            checkRequestLayout()
            requestData()
        }

        btnParse.setOnClickListener {
            checkRequestLayoutFlag = 1
            checkRequestLayout()
            requestData()
        }

    }

    private fun requestData() {
        val json = JSONObject()
        json["return"] = "json"
        HttpClient.request(Api.instance.randomImage(json),
            object : BaseCallBack<BaseModle<JSONObject>>(this) {
                override fun onObtain(response: Response<BaseModle<JSONObject>>?) {
                    if (checkRequestLayoutFlag == 0) {
                        tvJson?.text = response?.body().toString()
                    } else {
                        val list = mutableListOf<String?>()
                        list.add(response?.body()?.imgurl)
                        sampleAdapter.replaceData(list)
                    }

                }

                override fun onLose(
                    response: Response<BaseModle<JSONObject>>?, message: String?, failCode: Int
                ) {
                    Toast.makeText(
                        this@MainActivity,
                        response?.body().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
    }

    private fun checkRequestLayout() {
        if (checkRequestLayoutFlag == 0) {
            tvJson?.visibility = View.VISIBLE
            recyclerview?.visibility = View.GONE
        } else {
            tvJson?.visibility = View.GONE
            recyclerview?.visibility = View.VISIBLE
        }
    }
}