package com.example.gameit.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.gameit.R
import com.example.gameit.models.Partida
import com.squareup.picasso.Picasso

class ActualesAdapter(private val mDataSet: ArrayList<Partida>, var clickAction: (Partida) -> Unit) :
    RecyclerView.Adapter<ActualesAdapter.MainViewHolder>() {

    private var TAG = "ActualesAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_actuales, parent, false)
        return MainViewHolder(v)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val data = mDataSet[position]
        data.let {
            holder.bindItems(it)

            holder.actualesCard.setOnClickListener {
                clickAction(data)

                Log.v(TAG,"CLick en la partida actual")

            }
        }
    }

    override fun getItemCount(): Int {
        return mDataSet.size ?: 0
    }

    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        val actualesCard = v.findViewById(R.id.card) as CardView

        private val v1 = v.findViewById(R.id.actualesImage) as ImageView
        private val v2 = v.findViewById(R.id.actualesName) as TextView
        private val v3 = v.findViewById(R.id.actualesCreator) as TextView
        private val v4 = v.findViewById(R.id.actualesLevel) as TextView
        private val v5 = v.findViewById(R.id.actualesApuesta) as TextView
        private val v6 = v.findViewById(R.id.actualesCodigo) as TextView

        fun bindItems(data: Partida) {
            Picasso.get().load("https://images-na.ssl-images-amazon.com/images/I/51rkz8wallL.jpg")
                .into(v1)
            v2.text = data.nombre
            v3.text = data.creador
            v4.text = data.nivel

            /*  when (data.nivel) {
                "Pro" -> {
                    v4.setTextColor(1)
                }
                "Average" -> {
                    v4.setTextColor(2)
                }
                "Noob" -> {
                    v4.setTextColor(3)
                }
            }*/

            v5.text = "${data.apuesta} monedas"
            v6.text = data.codigo
        }
    }
}