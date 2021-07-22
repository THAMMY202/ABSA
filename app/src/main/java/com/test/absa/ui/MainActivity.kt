package com.test.absa.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.EditText
import androidx.appcompat.widget.SearchView
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

        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(name: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                displayArrayList.clear()
                if (newText.isNotEmpty() && newText.isNotBlank()) {
                    val search = newText.toLowerCase()
                    countriesArrayList.forEach {

                        if (it.name.toLowerCase().contains(search)) {
                            Log.d("TAG",it.name)
                            displayArrayList.add(it)
                        }
                    }
                } else {
                    displayArrayList.addAll(countriesArrayList)
                }
                country_recyclerView.adapter = CountryNameAdapter(baseContext, displayArrayList)
                country_recyclerView.adapter?.notifyDataSetChanged()
                return true
                }
        })
    }

    private fun getCountiesData() {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(Constants.BASE_URL).build()

        val service = retrofit.create(ICountryService::class.java)

        var response: Observable<List<Country>> = service.getAllCountries()
        Log.d("TAG",response.toString())

        response.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe {
            layoutManager = LinearLayoutManager(this)
            country_recyclerView.layoutManager = layoutManager
            country_recyclerView.adapter = CountryNameAdapter(this, it)

            fun addData() {
                for (item in it) {
                    countriesArrayList.add(item)
                    Log.d("TAG",it.toString())
                }
            }
            addData()
        }
    }
}