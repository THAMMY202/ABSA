package com.test.absa.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.test.absa.R
import com.test.absa.model.Country
import com.test.absa.ui.CountryDetailsActivity
import kotlinx.android.synthetic.main.country_layout.view.*

class CountryNameAdapter(private val context: Context, private var CountryList: List<Country>) :
        RecyclerView.Adapter<CountryNameAdapter.CustomViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {

        val layoutInflater = LayoutInflater.from(context).inflate(
                R.layout.country_layout,
                parent, false
        )
        return CustomViewHolder(layoutInflater)
    }

    override fun getItemCount(): Int {
        return CountryList.size
    }

    interface ItemClickListener {
        fun onItemClick(country: Country)
        fun onLongClick(country: Country)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        holder.view.name?.text = CountryList[position].name
        holder.view.capital?.text = concatenate(
                context.getString(R.string.capital_key),
                " ",
                CountryList[position].capital
        )

        GlideToVectorYou
                .init()
                .with(holder.view.context)
                .load(Uri.parse(CountryList[position].flag), holder.view.flag_imageView)

        holder?.alpha = CountryList[position].alpha3Code
        holder?.name = CountryList[position].name

    }


    class CustomViewHolder(var view: View, var alpha: String? = null, var name: String? = null) :
            RecyclerView.ViewHolder(view) {
        companion object {
            var alphaCode = "alpha"
            var countryName = "name"
        }

        init {
            view.setOnClickListener {
                val intent = Intent(view.context, CountryDetailsActivity::class.java)
                intent.putExtra(alphaCode, alpha)
                intent.putExtra(countryName, name)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
                view.context.startActivity(intent)
            }
        }
    }
}

fun concatenate(vararg string: String?): String {
    val sb = StringBuilder()
    string.forEach { sb.append(it) }
    return sb.toString()
}
