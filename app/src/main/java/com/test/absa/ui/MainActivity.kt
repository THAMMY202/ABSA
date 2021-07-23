package com.test.absa.ui

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.test.absa.R
import com.test.absa.adapter.CountryNameAdapter
import com.test.absa.model.*
import com.test.absa.utils.Constants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.schedulers.IoScheduler
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity(){

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private var countriesArrayList: MutableList<Country> = ArrayList()
    private var displayArrayList: MutableList<Country> = ArrayList()
    private lateinit var dialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dialog = ProgressDialog.show(this, getString(R.string.downloading), getString(R.string.loading), true)

        if (isNetworkAvailbale()) {
            this.getCountiesData()
        } else {
            internet_Text.visibility = View.VISIBLE
        }

        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener, CountryNameAdapter.ItemClickListener {

            override fun onQueryTextSubmit(name: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                displayArrayList.clear()
                if (newText.isNotEmpty() && newText.isNotBlank()) {
                    val search = newText.toLowerCase()
                    countriesArrayList.forEach {

                        if (it.name.toLowerCase().contains(search)) {
                            Log.d("TAG", it.name)
                            displayArrayList.add(it)
                        }
                    }
                } else {
                    displayArrayList.addAll(countriesArrayList)
                }
                country_recyclerView.adapter = CountryNameAdapter( baseContext, displayArrayList)
                country_recyclerView.adapter?.notifyDataSetChanged()
                return true
            }

            override fun onItemClick(country: Country) {

            }

            override fun onLongClick(country: Country) {
            }
        })
    }

    private fun getCountiesData() {

        dialog.show()

        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constants.BASE_URL).build()

        val service = retrofit.create(ICountryService::class.java)

        var response: Observable<List<Country>> = service.getAllCountries()
        Log.d("TAG", response.toString())

        response.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe {
            layoutManager = LinearLayoutManager(this)
            country_recyclerView.layoutManager = layoutManager
            country_recyclerView.adapter = CountryNameAdapter(this, it)

            dialog.hide()

            fun addData() {
                for (item in it) {
                    countriesArrayList.add(item)
                    Log.d("TAG", it.toString())
                }
            }
            addData()
        }
    }

    private fun isNetworkAvailbale(): Boolean {
        return try {
            val conManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val internetInfo = conManager.activeNetworkInfo
            internetInfo != null && internetInfo.isConnected
        } catch (e: Exception) {
            Log.d("TAG", e.toString())
            false
        }
    }
}