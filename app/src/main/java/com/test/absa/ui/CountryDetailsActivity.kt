package com.test.absa.ui

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.google.gson.GsonBuilder
import com.test.absa.R
import com.test.absa.adapter.CountryNameAdapter
import com.test.absa.model.Country
import com.test.absa.model.ICountryService_detail
import com.test.absa.utils.Constants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_country_details.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class CountryDetailsActivity : AppCompatActivity() {

    private lateinit var dialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_details)

        dialog = ProgressDialog.show(this, "", getString(R.string.loading), true)

        var alphaCode = intent.getStringExtra(CountryNameAdapter.CustomViewHolder.alphaCode)
        var countryName = intent.getStringExtra(CountryNameAdapter.CustomViewHolder.countryName)

        supportActionBar?.title = countryName

        if (isNetworkAvailbale()) {

            var retrofit = Retrofit.Builder().addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().create()
                )
            )
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constants.BASE_URL+"alpha/").build()

            var service = retrofit.create(ICountryService_detail::class.java)
            var response: Observable<Country> = service.getCountryByAlpha(alphaCode.toString())

            response.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    renderCountryData(it)
                }
        } else {
            val myToast = Toast.makeText(
                applicationContext,
                getString(R.string.no_internet_error),
                Toast.LENGTH_SHORT
            )
            myToast.setGravity(Gravity.LEFT, 200, 200)
            myToast.show()
        }
    }

    private fun renderCountryData(country: Country) {

        dialog.show()
        var languages: String = ""
        var currencies: String = ""

        country.languages.forEach {
            languages = "\n" + it.nativeName + "\n"
        }

        country.currencies.forEach {
            currencies =
                "\n" + "code :  ${it.code}" + "\n" + "name :  ${it.name}" + "\n" + "symbol :  ${it.symbol}"
        }

        GlideToVectorYou
            .init()
            .with(this)
            .load(Uri.parse(country.flag), country_flag)

        country_name.text = country.name
        country_location.text = "Country in " + country.subregion
        country_capital.text = "Capital: " + country.capital
        country_currency.text = "Currency: " + currencies
        country_native_language.text = "Native Language(s) : " + languages
        country_polulation.text = "Population: " + country.population.toString()

        dialog.hide()

        Log.d("TAG-Country", country.toString())
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