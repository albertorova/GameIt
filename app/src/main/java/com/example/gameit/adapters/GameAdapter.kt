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
import com.example.gameit.models.Game
import com.example.gameit.models.Partida
import com.squareup.picasso.Picasso

class GameAdapter(private val mDataSet: ArrayList<Game>, var clickAction: (Game) -> Unit) :
    RecyclerView.Adapter<GameAdapter.MainViewHolder>() {

    private var TAG = "GameAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        return MainViewHolder(v)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val data = mDataSet[position]
        data.let {
            holder.bindItems(it)

            holder.eventCard.setOnClickListener {
                clickAction(data)

                Log.v(TAG, "CLick en la partida del find")

            }
        }
    }

    override fun getItemCount(): Int {
        return mDataSet.size ?: 0
    }

    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        val eventCard = v.findViewById(R.id.eventCard) as CardView

        private val v1 = v.findViewById(R.id.gamePortada) as ImageView
        private val v2 = v.findViewById(R.id.gameNombre) as TextView

        fun bindItems(data: Game) {
            Picasso.get().load(data.portada).into(v1)
            v2.text = data.nombre
        }
    }
}