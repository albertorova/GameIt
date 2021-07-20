package com.example.gameit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gameit.R
import com.example.gameit.models.Mensaje
import com.example.gameit.models.Partida

class ChatAdapter(private val mDataSet: ArrayList<Mensaje>) :
    RecyclerView.Adapter<ChatAdapter.MainViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return MainViewHolder(v)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val data = mDataSet[position]
        data.let {
            holder.bindItems(it)

        }
    }

    override fun getItemCount(): Int {
        return mDataSet.size ?: 0
    }

    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private val v1 = v.findViewById(R.id.chatName) as TextView
        private val v2 = v.findViewById(R.id.chatMessage) as TextView
        private val v3 = v.findViewById(R.id.chatDate) as TextView

        fun bindItems(data: Mensaje) {
            v1.text = data.usuario
            v2.text = data.mensaje
            v3.text = data.fecha
        }
    }
}