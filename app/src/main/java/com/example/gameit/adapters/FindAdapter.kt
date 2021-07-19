package com.example.gameit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gameit.R
import com.example.gameit.models.Partida
import com.squareup.picasso.Picasso

class FindAdapter(private val mDataSet: ArrayList<Partida>) :
    RecyclerView.Adapter<FindAdapter.MainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_find, parent, false)
        return MainViewHolder(v)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val data = mDataSet.get(position)
        data.let {
            holder.bindItems(it)
        }
    }

    override fun getItemCount(): Int {
        return mDataSet.size ?: 0
    }

    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private val v1 = v.findViewById(R.id.findImage) as ImageView
        private val v2 = v.findViewById(R.id.findName) as TextView
        private val v3 = v.findViewById(R.id.findCreator) as TextView
        private val v4 = v.findViewById(R.id.findLevel) as TextView
        private val v5 = v.findViewById(R.id.findApuesta) as TextView

        fun bindItems(data: Partida) {
            Picasso.get().load("https://images-na.ssl-images-amazon.com/images/I/51rkz8wallL.jpg")
                .into(v1)
            v2.text = data.nombre
            v3.text = data.creador
            v4.text = data.nivel

            when (data.nivel) {
                "Alto" -> {
                    v4.setTextColor(1)
                }
                "Medio" -> {
                    v4.setTextColor(2)
                }
                "Bajo" -> {
                    v4.setTextColor(3)
                }
            }

            v5.text = "${data.apuesta} monedas"

        }
    }
}
