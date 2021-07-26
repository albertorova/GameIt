package com.example.gameit.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.gameit.R
import com.example.gameit.databinding.ItemGameBinding
import com.example.gameit.models.Game
import com.squareup.picasso.Picasso

class GameAdapter(private val mDataSet: ArrayList<Game>, var clickAction: (Game) -> Unit) :
    RecyclerView.Adapter<GameAdapter.MainViewHolder>() {

    private var posicion: String = ""
    private var TAG = "GameAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        return MainViewHolder(v)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val data = mDataSet[position]
        data.let {
            holder.bindItems(it)

            holder.b.eventCard.setOnClickListener {
                clickAction(data)

                Log.v(TAG, "CLick en la partida del find")

                posicion = data.nombre.toString()

                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return mDataSet.size
    }

    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        val b = ItemGameBinding.bind(v)

        fun bindItems(data: Game) {

            Picasso.get().load(data.portada).into(b.gamePortada)
            b.gameNombre.text = data.nombre

            if (data.nombre == posicion) {

                b.eventCard.alpha = 0.5F

            } else {

                b.eventCard.alpha = 1F
            }
        }
    }
}