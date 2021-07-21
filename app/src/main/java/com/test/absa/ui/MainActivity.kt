package com.test.absa.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.absa.R
import com.test.absa.model.*
import com.google.gson.GsonBuilder
import com.test.absa.adapter.CountryNameAdapter
import com.test.absa.utils.Constants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.schedulers.IoScheduler
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private var countriesArrayList: MutableList<Country> = ArrayList()
    private var displayArrayList: MutableList<Country> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.getCountiesData()
    }

    private fun getCountiesData() {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(Constants.BASE_URL).build()

        val service = retrofit.create(ICountryService::class.java)

        var response: Observable<List<Country>> = service.getAllCountries()

        response.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe {
            layoutManager = LinearLayoutManager(this)
            country_recyclerView.layoutManager = layoutManager
            country_recyclerView.adapter = CountryNameAdapter(this, it)

            fun addData() {
                for (item in it) {
                    countriesArrayList.add(item)
                }
            }
            addData()
        }
    }

}