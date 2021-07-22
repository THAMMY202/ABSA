package com.test.absa.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.test.absa.R
import com.test.absa.model.Country
import kotlinx.android.synthetic.main.country_layout.view.*


class CountryNameAdapter(private val context: Context, var CountryList: List<Country>) :
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

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        var name = CountryList[position].name

        holder.view.name?.text = CountryList[position].name
        holder.view.capital?.text = concatenate(context.getString(R.string.capital_key)," ",CountryList[position].capital)

        GlideToVectorYou
            .init()
            .with(holder.view.context)
            .load(Uri.parse(CountryList[position].flag), holder.view.flag_imageView);

        holder?.name = name
    }


    class CustomViewHolder(var view: View, var name: String? = null) :
        RecyclerView.ViewHolder(view) {
        companion object {
            var country_name = "Country name"
        }

        init {
            view.setOnClickListener {
            }
        }
    }

    fun concatenate(vararg string: String?): String {
        var sb = StringBuilder()
        string.forEach { sb.append(it) }
        return sb.toString()
    }
}