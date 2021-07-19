package com.example.gameit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gameit.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*

class FindAdapter(private val mDataSet: List<String>?) :

    RecyclerView.Adapter<FindAdapter.MainViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_find, parent, false)
        return MainViewHolder(v)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val data = mDataSet?.get(position)
        data?.let {
            holder.bindItems(it)
        }
    }

    override fun getItemCount(): Int {
        return mDataSet?.size ?: 0
    }

    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private val v1 = v.findViewById(R.id.findImage) as ImageView
        private val v2 = v.findViewById(R.id.findName) as TextView

        fun bindItems(data: String) {
            Picasso.get().load("https://images-na.ssl-images-amazon.com/images/I/51rkz8wallL.jpg").into(v1)
            v2.text = data
        }
    }
}
