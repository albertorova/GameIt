package com.example.gameit.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.gameit.R
import com.example.gameit.databinding.ItemHistorialBinding
import com.example.gameit.models.Partida
import com.squareup.picasso.Picasso

class HistorialAdapter(
    private val mDataSet: ArrayList<Partida>
) :
    RecyclerView.Adapter<HistorialAdapter.MainViewHolder>() {

    var context: Context? = null

    private var TAG = "HistorialAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_historial, parent, false)
        return MainViewHolder(v)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        val data = mDataSet[position]

        data.let {
            holder.bindItems(it)

        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    override fun getItemCount(): Int {
        return mDataSet.size ?: 0
    }

    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        val b = ItemHistorialBinding.bind(v)

        fun bindItems(data: Partida) {

            Picasso.get().load("https://images-na.ssl-images-amazon.com/images/I/51rkz8wallL.jpg")
                .into(b.historialImage)
            b.historialName.text = data.nombre
            b.historialCreator.text = data.creador
            b.historialLevel.text = data.nivel

            when (data.nivel) {
                "PRO" -> {
                    b.historialLevel.setTextColor(ContextCompat.getColor(context!!, R.color.red))
                }
                "AVERAGE" -> {
                    b.historialLevel.setTextColor(ContextCompat.getColor(context!!, R.color.yellow))
                }
                "NOOB" -> {
                    b.historialLevel.setTextColor(
                        ContextCompat.getColor(
                            context!!,
                            R.color.blueblue
                        )
                    )
                }
            }

            if (data.isVictory == true) {
                b.victory.isVisible = true
                b.historialApuesta.setTextColor(ContextCompat.getColor(context!!, R.color.green))
                b.historialApuesta.text = "+ ${data.apuesta} \uD83D\uDC8E"

            } else {
                b.defeat.isVisible = true
                b.historialApuesta.setTextColor(ContextCompat.getColor(context!!, R.color.red))
                b.historialApuesta.text = "- ${data.apuesta} \uD83D\uDC8E"
            }

        }
    }
}